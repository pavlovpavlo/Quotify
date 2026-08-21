# Експорт відповідей опитувань

Відповіді лежать у Firestore-колекції `survey_responses` (клієнт лише пише, читати може
тільки бекенд). Цей скрипт вивантажує їх у два JSON-файли, зручні для аналізу через AI.

## Запуск

```bash
cd tools/survey-export
npm install
node export.mjs ../../secrets/firebase-sa.json            # усі опитування
node export.mjs ../../secrets/firebase-sa.json push_interest_v1   # лише одне
```

Перший аргумент — ключ сервісного акаунта Firebase (Project settings → Service accounts →
Generate new private key). Тримати його поза git. `OUT_DIR=/шлях` змінює теку виводу.

## Що виходить

**`survey-responses.raw.json`** — кожна відповідь окремо: `uid`, `surveyId`, `language`,
`appVersion`, `createdAt` і масив `answers`, де поруч з `optionIds` лежать `optionTexts`
і текст питання. Тобто файл читається без доступу до Remote Config.

**`survey-responses.summary.json`** — зведення: по кожному питанню кількість відповідей,
розподіл варіантів (`count` + `share`) і всі коментарі списком. Саме цей файл варто
кидати в AI з питанням на кшталт «що люди просять найчастіше».

## Схема документа `survey_responses`

| Поле | Тип | Опис |
|---|---|---|
| `uid` | string | автор відповіді |
| `surveyId` | string | id опитування з Remote Config |
| `completed` | bool | дійшов до кінця |
| `language` | string | мова, якою бачив питання |
| `appVersion` | string | версія застосунку |
| `createdAt` | timestamp | серверний час запису |
| `answers[]` | array | `questionId`, `questionTitle`, `optionIds[]`, `optionTexts[]`, `inputs{optionId: текст}` |
