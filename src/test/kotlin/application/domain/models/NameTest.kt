package application.domain.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class NameTest {

    @Test
    fun `deve aceitar nome com 70 caracteres`() {
        val name70 = "a".repeat(70)
        val name = Name(name70)
        assertEquals(name70, name.value)
    }

    @Test
    fun `deve aceitar nome comum`() {
        val name = Name("Lucas Giori")
        assertEquals("Lucas Giori", name.value)
    }

    @Test
    fun `deve lancar excecao para nome em branco`() {
        assertThrows<IllegalArgumentException> {
            Name("   ")
        }
    }

    @Test
    fun `deve lancar excecao para nome vazio`() {
        assertThrows<IllegalArgumentException> {
            Name("")
        }
    }

    @Test
    fun `deve lancar excecao para nome com mais de 70 caracteres`() {
        assertThrows<IllegalArgumentException> {
            Name("a".repeat(71))
        }
    }

    @Test
    fun `from deve criar Name a partir de String`() {
        val name = Name.from("Lucas")
        assertEquals("Lucas", name.value)
    }
}
