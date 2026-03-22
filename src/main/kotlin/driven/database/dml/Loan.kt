package driven.database.dml

class Loan {
    companion object {
        const val INSERT_INITIAL_AGGREGATE = "INSERT INTO loan (id, customer, status, version) VALUES (?, ?, ?, ?);"
        const val FIND_BY_ID = "SELECT id, customer, status, version, proposals, created_at, updated_at FROM loan WHERE id = ?;"
        const val FIND_BY_VERSION = "SELECT id, customer, status, version, proposals, created_at, updated_at FROM loan WHERE id = ? AND version = ?;"
        const val UPDATE_AGGREGATE = "UPDATE loan SET status = ?, version = ?, proposals = ? WHERE id = ? AND version = ?"
        const val UPDATE_STATUS_ONLY = "UPDATE loan SET status = ?, version = ? WHERE id = ? AND version = ?"
    }
}