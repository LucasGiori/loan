package application.domain.models

data class Document(val value: String) : ValueObject {
    init {
        require(value.isNotBlank()) { "Document cannot be blank" }
        require(value.isNotEmpty()) { "Document cannot be empty" }
        require(value.length <= 11) { "Document cannot be longer than 11 characters" }
    }

    companion object {
        fun from(value: String) = Document(value)
    }
}