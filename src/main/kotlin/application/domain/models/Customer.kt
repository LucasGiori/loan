package application.domain.models

data class Customer(
    val name: Name,
    val document: Document,
    val age: Age,
    val location: String,
    val profile: Profile,
    val income: Income
) : ValueObject

fun Customer.toJson(): String {
    val locationJson = location.let { "\"location\": \"$it\"" }
    val profileJson = profile.let { "\"profile\": \"${it.value}\"" }
    val incomeJson = income.let { "\"income\": ${it.value}" }

    return "{ \"name\": \"${name.value}\", \"document\": \"${document.value}\", \"age\": ${age.value}, $locationJson, $profileJson, $incomeJson }"
}