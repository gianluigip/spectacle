package io.gianluigip.spectacle.dsl.assertions

import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class JavaTimeAssertionsExtensionsTest {

    private val now = Instant.now()
    private val zonedNow = ZonedDateTime.ofInstant(now, ZoneId.systemDefault())

    @Test
    fun `shouldBeCloseTo should assert closer ZonedDateTime`() {
        zonedNow shouldBeCloseTo ZonedDateTime.now()
    }

    @Test
    fun `shouldBeCloseTo should fail when not close enough ZonedDateTime`() =
        runAndCatch {
            zonedNow shouldBeCloseTo zonedNow.minusSeconds(1)
        }.message shouldStartWith "Expected '$now' to be closer to '${now.minusSeconds(1)}' but the difference was bigger than 100 milli seconds"

    @Test
    fun `shouldBeCloseTo should assert closer Instant`() {
        now shouldBeCloseTo Instant.now()
    }

    @Test
    fun `shouldBeCloseTo should fail when not close enough Instant`() =
        runAndCatch {
            now shouldBeCloseTo now.minusSeconds(1)
        }.message shouldStartWith "Expected '$now' to be closer to '${now.minusSeconds(1)}' but the difference was bigger than 100 milli seconds"

}