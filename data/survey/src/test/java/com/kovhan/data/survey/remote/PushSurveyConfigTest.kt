package com.kovhan.data.survey.remote

import com.kovhan.core.models.survey.SurveyQuestionType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/** The payload that is actually published in Remote Config. */
@DisplayName("push_interest_v2 config")
class PushSurveyConfigTest {

    private val parser = SurveyConfigParser()

    private val raw: String = requireNotNull(
        javaClass.classLoader?.getResource("push_interest_v2.json")?.readText(),
    )

    @Test
    @DisplayName("parses into one survey with all four questions")
    fun parsesWholeSurvey() {
        val surveys = parser.parse(raw, "uk")

        assertEquals(listOf("push_interest_v2"), surveys.map { it.id })
        assertEquals(
            listOf("interest", "content", "timing", "blocker"),
            surveys.first().questions.map { it.id },
        )
    }

    @Test
    @DisplayName("keeps the invite copy in Ukrainian")
    fun resolvesInvite() {
        val invite = parser.parse(raw, "uk").first().invite

        assertEquals("Чи потрібні Quotify сповіщення?", invite?.title)
        assertTrue(invite?.reward?.contains("фон") == true)
    }

    @Test
    @DisplayName("carries the flags each question was configured with")
    fun keepsQuestionFlags() {
        val questions = parser.parse(raw, "uk").first().questions.associateBy { it.id }

        assertEquals(SurveyQuestionType.SINGLE, questions.getValue("interest").type)
        assertFalse(questions.getValue("interest").skippable)
        assertEquals(SurveyQuestionType.MULTI, questions.getValue("content").type)
        assertTrue(questions.getValue("timing").skippable)
    }

    @Test
    @DisplayName("attaches the extra field to the options that asked for one")
    fun keepsOptionInputs() {
        val questions = parser.parse(raw, "uk").first().questions.associateBy { it.id }

        val never = questions.getValue("interest").options.last()
        assertEquals("Чому ні?", never.input?.label)
        assertFalse(never.input?.required == true)

        val other = questions.getValue("content").options.last()
        assertTrue(other.input?.required == true)
        assertEquals("Що саме?", other.input?.label)
    }
}
