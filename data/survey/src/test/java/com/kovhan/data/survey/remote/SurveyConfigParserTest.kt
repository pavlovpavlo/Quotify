package com.kovhan.data.survey.remote

import com.kovhan.core.models.survey.SurveyQuestionType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("SurveyConfigParser")
class SurveyConfigParserTest {

    private val parser = SurveyConfigParser()

    @Nested
    @DisplayName("valid config")
    inner class Valid {

        @Test
        @DisplayName("resolves copy for the requested language")
        fun resolvesLanguage() {
            val surveys = parser.parse(CONFIG, "uk")

            assertEquals(1, surveys.size)
            assertEquals("Чого бракує?", surveys.first().questions.first().title)
            assertEquals("Синхронізація", surveys.first().questions.first().options.first().text)
        }

        @Test
        @DisplayName("falls back to English for a missing locale")
        fun fallsBackToEnglish() {
            val surveys = parser.parse(CONFIG, "de")

            assertEquals("What is missing?", surveys.first().questions.first().title)
        }

        @Test
        @DisplayName("keeps the extra input attached to its option")
        fun keepsOptionInput() {
            val options = parser.parse(CONFIG, "en").first().questions.first().options

            assertNull(options.first().input)
            assertEquals(true, options.last().input?.required)
            assertEquals("Tell us more", options.last().input?.label)
        }

        @Test
        @DisplayName("reads multiSelect and skippable flags")
        fun readsFlags() {
            val question = parser.parse(CONFIG, "en").first().questions.last()

            assertEquals(SurveyQuestionType.MULTI, question.type)
            assertEquals(false, question.skippable)
        }

        @Test
        @DisplayName("accepts a bare string instead of a locale map")
        fun acceptsBareString() {
            val raw = """
                {"surveys":[{"id":"s","questions":[
                  {"id":"q","title":"Plain title","options":[{"id":"o","text":"Plain option"}]}
                ]}]}
            """.trimIndent()

            val question = parser.parse(raw, "uk").first().questions.first()

            assertEquals("Plain title", question.title)
            assertEquals("Plain option", question.options.first().text)
        }
    }

    @Nested
    @DisplayName("broken config")
    inner class Broken {

        @Test
        @DisplayName("returns nothing for malformed json")
        fun malformedJson() {
            assertTrue(parser.parse("{not json", "en").isEmpty())
        }

        @Test
        @DisplayName("returns nothing for a blank payload")
        fun blankPayload() {
            assertTrue(parser.parse("", "en").isEmpty())
        }

        @Test
        @DisplayName("drops surveys without an id or questions")
        fun dropsIncompleteSurveys() {
            val raw = """
                {"surveys":[
                  {"questions":[{"id":"q","title":{"en":"t"},"options":[{"id":"o","text":{"en":"o"}}]}]},
                  {"id":"empty","questions":[]},
                  {"id":"kept","questions":[{"id":"q","title":{"en":"t"},"options":[{"id":"o","text":{"en":"o"}}]}]}
                ]}
            """.trimIndent()

            val surveys = parser.parse(raw, "en")

            assertEquals(listOf("kept"), surveys.map { it.id })
        }

        @Test
        @DisplayName("drops questions whose options are unusable")
        fun dropsQuestionsWithoutOptions() {
            val raw = """
                {"surveys":[{"id":"s","questions":[
                  {"id":"bad","title":{"en":"t"},"options":[{"text":{"en":"no id"}}]},
                  {"id":"good","title":{"en":"t"},"options":[{"id":"o","text":{"en":"o"}}]}
                ]}]}
            """.trimIndent()

            val questions = parser.parse(raw, "en").first().questions

            assertEquals(listOf("good"), questions.map { it.id })
        }

        @Test
        @DisplayName("still reads a config whose schema version is bumped by mistake")
        fun parsesNewerSchema() {
            val raw = """
                {"schemaVersion":99,"surveys":[{"id":"s","questions":[
                  {"id":"q","title":{"en":"t"},"options":[{"id":"o","text":{"en":"o"}}]}
                ]}]}
            """.trimIndent()

            assertEquals(listOf("s"), parser.parse(raw, "en").map { it.id })
        }
    }

    private companion object {
        val CONFIG = """
            {
              "schemaVersion": 1,
              "surveys": [
                {
                  "id": "quotify_v1",
                  "enabled": true,
                  "invite": {
                    "title": {"en": "Help us", "uk": "Допоможіть"},
                    "body": {"en": "Few questions", "uk": "Кілька питань"}
                  },
                  "reward": {"id": "widget-bg-aurora", "kind": "widget_background"},
                  "questions": [
                    {
                      "id": "missing",
                      "title": {"en": "What is missing?", "uk": "Чого бракує?"},
                      "description": {"en": "Pick one", "uk": "Оберіть один"},
                      "options": [
                        {"id": "sync", "text": {"en": "Sync", "uk": "Синхронізація"}},
                        {
                          "id": "other",
                          "text": {"en": "Something else", "uk": "Інше"},
                          "input": {
                            "required": true,
                            "label": {"en": "Tell us more", "uk": "Розкажіть більше"},
                            "placeholder": {"en": "...", "uk": "..."}
                          }
                        }
                      ]
                    },
                    {
                      "id": "value",
                      "title": {"en": "What do you value?", "uk": "Що цінуєте?"},
                      "multiSelect": true,
                      "skippable": false,
                      "options": [
                        {"id": "widget", "text": {"en": "Widget", "uk": "Віджет"}}
                      ]
                    }
                  ]
                }
              ]
            }
        """.trimIndent()
    }
}
