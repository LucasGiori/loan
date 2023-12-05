package application.domain.models

import java.util.UUID

data class UUIDv4(val value: String) {
    init {
        val uuid = UUID.fromString(value)

        require(uuid.version() == VERSION) { "Invalid UUID version" }
    }

    companion object {
        private const val VERSION: Int = 4
    }

    override fun toString() = value
}