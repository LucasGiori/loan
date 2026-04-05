package application.domain.aggregate

import application.domain.TestFixtures
import application.domain.models.Profile
import application.domain.models.Version
import application.domain.models.aggregate.Loan
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LoanInitTest {

    @Test
    fun `init deve retornar LoanInitializedEvent com loanId correto`() {
        val loanId = TestFixtures.loanId()
        val event = Loan.init(loanId, TestFixtures.customer())
        assertEquals(loanId, event.loanId)
    }

    @Test
    fun `init deve definir versao inicial como 1`() {
        val event = Loan.init(TestFixtures.loanId(), TestFixtures.customer())
        assertEquals(Version.INITIAL, event.version)
    }

    @Test
    fun `init deve preservar o customer no evento`() {
        val customer = TestFixtures.customer(profile = Profile.SELF_EMPLOYED, income = 3000.0)
        val event = Loan.init(TestFixtures.loanId(), customer)
        assertEquals(customer, event.customer)
    }
}
