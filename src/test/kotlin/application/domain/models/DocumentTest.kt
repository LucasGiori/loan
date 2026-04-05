package application.domain.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DocumentTest {

    @Test
    fun `deve aceitar documento com 11 caracteres`() {
        val doc = Document("12345678901")
        assertEquals("12345678901", doc.value)
    }

    @Test
    fun `deve aceitar documento com menos de 11 caracteres`() {
        val doc = Document("12345")
        assertEquals("12345", doc.value)
    }

    @Test
    fun `deve lancar excecao para documento em branco`() {
        assertThrows<IllegalArgumentException> {
            Document("   ")
        }
    }

    @Test
    fun `deve lancar excecao para documento vazio`() {
        assertThrows<IllegalArgumentException> {
            Document("")
        }
    }

    @Test
    fun `deve lancar excecao para documento com mais de 11 caracteres`() {
        assertThrows<IllegalArgumentException> {
            Document("123456789012")
        }
    }

    @Test
    fun `from deve criar Document a partir de String`() {
        val doc = Document.from("12345678901")
        assertEquals("12345678901", doc.value)
    }
}
