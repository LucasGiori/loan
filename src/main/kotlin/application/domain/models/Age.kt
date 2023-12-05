package application.domain.models

data class Age(val value: Int): ValueObject {
    init {
        require(value >= AGE_MIN) { "Age must be at least 18 years old." }
    }

    companion object {
        private const val AGE_MIN = 18
    }
}