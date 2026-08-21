import { readFileSync, writeFileSync } from 'node:fs';
import { cert, initializeApp } from 'firebase-admin/app';
import { getFirestore } from 'firebase-admin/firestore';

const keyPath = process.argv[2] ?? process.env.GOOGLE_APPLICATION_CREDENTIALS;
if (!keyPath) {
  console.error('Usage: node export.mjs <path-to-service-account.json> [surveyId]');
  process.exit(1);
}

const surveyFilter = process.argv[3] ?? null;
const outDir = process.env.OUT_DIR ?? '.';

initializeApp({ credential: cert(JSON.parse(readFileSync(keyPath, 'utf8'))) });
const db = getFirestore();

let query = db.collection('survey_responses');
if (surveyFilter) query = query.where('surveyId', '==', surveyFilter);

const snapshot = await query.orderBy('createdAt', 'asc').get();

const responses = snapshot.docs.map((doc) => {
  const data = doc.data();
  return {
    id: doc.id,
    uid: data.uid ?? null,
    surveyId: data.surveyId ?? null,
    completed: data.completed ?? null,
    language: data.language ?? null,
    appVersion: data.appVersion ?? null,
    createdAt: data.createdAt?.toDate?.().toISOString() ?? null,
    answers: (data.answers ?? []).map((answer) => ({
      questionId: answer.questionId ?? null,
      questionTitle: answer.questionTitle ?? null,
      optionIds: answer.optionIds ?? [],
      optionTexts: answer.optionTexts ?? [],
      comments: answer.inputs ?? {},
    })),
  };
});

// Grouped the way a question is actually read: how many people picked each
// option, and every free-text comment underneath it.
const summary = {};
for (const response of responses) {
  const survey = (summary[response.surveyId] ??= {
    surveyId: response.surveyId,
    responses: 0,
    completedResponses: 0,
    languages: {},
    questions: {},
  });

  survey.responses += 1;
  if (response.completed) survey.completedResponses += 1;
  if (response.language) survey.languages[response.language] = (survey.languages[response.language] ?? 0) + 1;

  for (const answer of response.answers) {
    const question = (survey.questions[answer.questionId] ??= {
      questionId: answer.questionId,
      title: answer.questionTitle,
      answered: 0,
      options: {},
      comments: [],
    });

    question.answered += 1;
    answer.optionIds.forEach((optionId, index) => {
      const option = (question.options[optionId] ??= {
        optionId,
        text: answer.optionTexts[index] ?? null,
        count: 0,
        share: 0,
      });
      option.count += 1;
    });

    for (const [optionId, comment] of Object.entries(answer.comments)) {
      if (comment?.trim()) question.comments.push({ optionId, comment: comment.trim() });
    }
  }
}

for (const survey of Object.values(summary)) {
  for (const question of Object.values(survey.questions)) {
    for (const option of Object.values(question.options)) {
      option.share = question.answered ? Number((option.count / question.answered).toFixed(3)) : 0;
    }
    question.options = Object.values(question.options).sort((a, b) => b.count - a.count);
  }
  survey.questions = Object.values(survey.questions);
}

const exportedAt = new Date().toISOString();
writeFileSync(`${outDir}/survey-responses.raw.json`, JSON.stringify({ exportedAt, responses }, null, 2), 'utf8');
writeFileSync(`${outDir}/survey-responses.summary.json`, JSON.stringify({ exportedAt, surveys: Object.values(summary) }, null, 2), 'utf8');

console.log(`Exported ${responses.length} responses`);
console.log(`  ${outDir}/survey-responses.raw.json`);
console.log(`  ${outDir}/survey-responses.summary.json`);
