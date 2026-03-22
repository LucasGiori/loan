package application.domain.models.service

import application.domain.models.*

class LoanAvailabilityService {
    companion object {
        fun getAvailableLoanTypes(customer: Customer, loanRulesMap: Map<LoanType, LoanRule>): Map<LoanType, LoanRule> {

            return loanRulesMap.filter { (_, rule) -> rule.availableTo().contains(element = customer.profile) }
        }
    }
}