package application.domain.models

enum class Profile(val value: String) {
    EMPLOYEE("EMPLOYEE"),
    PUBLIC_SERVER("PUBLIC_SERVER"),
    RETIREE("RETIREE"),
    SELF_EMPLOYED("SELF_EMPLOYED"),
    PENSIONER("PENSIONER");

    companion object {
        fun from (value: String) = entries
            .firstOrNull { it.value == value } ?: throw IllegalArgumentException("${Profile::class.java.simpleName} enum does not contains $value.")
    }
}