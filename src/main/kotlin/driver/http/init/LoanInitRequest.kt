package driver.http.init

import application.commands.LoanInitCommand
import application.domain.models.*
import driver.Request

data class LoanInitRequest(val id: String, val customer: CustomerRequest): Request {
    override fun toCommand() = LoanInitCommand(
            LoanId(UUIDv4(id)),
            customer.toCustomer()
        )
}

data class CustomerRequest(
    val name: String,
    val document: String,
    val age: Int,
    val location: String,
    val profile: String,
    val income: Double
) {
    fun toCustomer() = Customer(
        name = Name(name),
        document = Document(document),
        age = Age(age),
        location = location,
        profile = Profile.from(profile),
        income = Income.from(income)
    )
}