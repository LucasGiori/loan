package application.domain.models

data class Name(val value: String): ValueObject {
    init {
        require(value.isNotBlank()) { "Name cannot be blank" }
        require(value.isNotEmpty()) { "Name cannot be empty"}
        require(value.length <= 70) { "Name cannot be longer than 70 characters" }
    }

    companion object {
        fun from(value: String) = Name(value)
    }
}