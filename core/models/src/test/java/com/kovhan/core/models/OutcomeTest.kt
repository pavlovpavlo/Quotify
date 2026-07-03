package com.kovhan.core.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Outcome")
class OutcomeTest {

    private val success: Outcome<Int, String> = Outcome.Success(42)
    private val failure: Outcome<Int, String> = Outcome.Failure("boom")

    @Nested
    @DisplayName("isSuccess")
    inner class IsSuccess {

        @Test
        @DisplayName("is true for Success")
        fun trueForSuccess() {
            assertTrue(success.isSuccess)
        }

        @Test
        @DisplayName("is false for Failure")
        fun falseForFailure() {
            assertFalse(failure.isSuccess)
        }
    }

    @Nested
    @DisplayName("onSuccess")
    inner class OnSuccess {

        @Test
        @DisplayName("runs the action with the data for Success")
        fun runsForSuccess() {
            var captured: Int? = null

            success.onSuccess { captured = it }

            assertEquals(42, captured)
        }

        @Test
        @DisplayName("does not run the action for Failure")
        fun skipsForFailure() {
            var called = false

            failure.onSuccess { called = true }

            assertFalse(called)
        }

        @Test
        @DisplayName("returns the same instance for chaining")
        fun returnsSameInstance() {
            assertSame(success, success.onSuccess { })
        }
    }

    @Nested
    @DisplayName("onFailure")
    inner class OnFailure {

        @Test
        @DisplayName("runs the action with the error for Failure")
        fun runsForFailure() {
            var captured: String? = null

            failure.onFailure { captured = it }

            assertEquals("boom", captured)
        }

        @Test
        @DisplayName("does not run the action for Success")
        fun skipsForSuccess() {
            var called = false

            success.onFailure { called = true }

            assertFalse(called)
        }

        @Test
        @DisplayName("returns the same instance for chaining")
        fun returnsSameInstance() {
            assertSame(failure, failure.onFailure { })
        }
    }

    @Nested
    @DisplayName("chaining onSuccess and onFailure")
    inner class Chaining {

        @Test
        @DisplayName("only the matching branch runs")
        fun onlyMatchingBranchRuns() {
            var successValue: Int? = null
            var failureValue: String? = null

            success
                .onSuccess { successValue = it }
                .onFailure { failureValue = it }

            assertEquals(42, successValue)
            assertNull(failureValue)
        }
    }
}
