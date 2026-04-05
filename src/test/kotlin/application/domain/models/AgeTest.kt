package application.domain.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AgeTest {

    @Test
    fun `deve aceitar idade minima de 18`() {
        val age = Age(18)
        assertEquals(18, age.value)
    }

    @Test
    fun `deve aceitar idade acima de 18`() {
        val age = Age(30)
        assertEquals(30, age.value)
    }

    @Test
    fun `deve lancar excecao para idade 17`() {
        assertThrows<IllegalArgumentException> {
            Age(17)
        }
    }

    @Test
    fun `deve lancar excecao para idade zero`() {
        assertThrows<IllegalArgumentException> {
            Age(0)
        }
    }

    @Test
    fun `deve lancar excecao para idade negativa`() {
        assertThrows<IllegalArgumentException> {
            Age(-1)
        }
    }
}
