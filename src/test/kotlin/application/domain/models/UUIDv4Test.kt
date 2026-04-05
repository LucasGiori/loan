package application.domain.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class UUIDv4Test {

    @Test
    fun `deve aceitar UUID versao 4 valido`() {
        val uuid = UUID.randomUUID().toString()
        val uuidv4 = UUIDv4(uuid)
        assertEquals(uuid, uuidv4.value)
    }

    @Test
    fun `deve lancar excecao para UUID versao 1`() {
        val uuidV1 = "550e8400-e29b-11d4-a716-446655440000"
        assertThrows<IllegalArgumentException> {
            UUIDv4(uuidV1)
        }
    }

    @Test
    fun `deve lancar excecao para UUID versao 3`() {
        val uuidV3 = UUID.nameUUIDFromBytes("test".toByteArray()).toString()
        assertThrows<IllegalArgumentException> {
            UUIDv4(uuidV3)
        }
    }

    @Test
    fun `randomUUID deve gerar UUID versao 4`() {
        val uuidv4 = UUIDv4.randomUUID()
        assertNotNull(uuidv4)
        assertEquals(4, UUID.fromString(uuidv4.value).version())
    }

    @Test
    fun `randomUUID deve gerar valores unicos a cada chamada`() {
        val uuid1 = UUIDv4.randomUUID()
        val uuid2 = UUIDv4.randomUUID()
        assert(uuid1.value != uuid2.value)
    }
}
