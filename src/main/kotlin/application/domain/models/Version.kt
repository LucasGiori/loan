package application.domain.models

data class Version(val value: Int) : ValueObject {
    companion object {
        val INITIAL = Version(1)
    }

    fun next() = Version(value + 1)

    fun previous() = Version(value - 1)
}