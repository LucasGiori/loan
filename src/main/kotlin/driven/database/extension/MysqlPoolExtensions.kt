package driven.database.extension

import io.vertx.kotlin.coroutines.await
import io.vertx.mysqlclient.MySQLPool
import io.vertx.sqlclient.SqlConnection
import io.vertx.sqlclient.Transaction

suspend fun MySQLPool.withTransactionCustom(closure: suspend (SqlConnection) -> Unit) {
    val connection: SqlConnection = connection.await()
    val transaction: Transaction = connection.begin().await()

    try {
        closure(connection)
        transaction.commit().await()
    } catch (e: Exception) {
        transaction.rollback().await()
        throw e
    } finally {
        connection.close()
    }
}