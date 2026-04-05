package application.domain.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProfileTest {

    @Test
    fun `from deve retornar EMPLOYEE`() {
        assertEquals(Profile.EMPLOYEE, Profile.from("EMPLOYEE"))
    }

    @Test
    fun `from deve retornar PUBLIC_SERVER`() {
        assertEquals(Profile.PUBLIC_SERVER, Profile.from("PUBLIC_SERVER"))
    }

    @Test
    fun `from deve retornar RETIREE`() {
        assertEquals(Profile.RETIREE, Profile.from("RETIREE"))
    }

    @Test
    fun `from deve retornar SELF_EMPLOYED`() {
        assertEquals(Profile.SELF_EMPLOYED, Profile.from("SELF_EMPLOYED"))
    }

    @Test
    fun `from deve retornar PENSIONER`() {
        assertEquals(Profile.PENSIONER, Profile.from("PENSIONER"))
    }

    @Test
    fun `from deve lancar IllegalArgumentException para valor invalido`() {
        assertThrows<IllegalArgumentException> {
            Profile.from("INVALID")
        }
    }
}
