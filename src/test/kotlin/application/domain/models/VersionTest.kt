package application.domain.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VersionTest {

    @Test
    fun `INITIAL deve ser versao 1`() {
        assertEquals(1, Version.INITIAL.value)
    }

    @Test
    fun `next deve incrementar a versao`() {
        assertEquals(2, Version(1).next().value)
    }

    @Test
    fun `next deve ser imutavel`() {
        val version = Version(1)
        version.next()
        assertEquals(1, version.value)
    }

    @Test
    fun `previous deve decrementar a versao`() {
        assertEquals(2, Version(3).previous().value)
    }

    @Test
    fun `next encadeado deve acumular incrementos`() {
        assertEquals(3, Version.INITIAL.next().next().value)
    }
}
