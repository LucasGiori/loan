package query.loan.available

import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class GetLoanAvailableHandler(val dao: GetLoanAvailableDAO) {
    suspend fun execute(query: GetLoanAvailableQuery): GetLoanAvailableResult? {
        return dao.getLoanAvailable(query.id)
    }
}