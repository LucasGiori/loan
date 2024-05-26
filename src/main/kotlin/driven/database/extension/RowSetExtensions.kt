package driven.database.extension

import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet

fun RowSet<Row>.requireRowCountGreaterThan(threshold: Int): RowSet<Row> {
    if (this.rowCount() <= threshold) {
        throw IllegalStateException("Number of affected rows is less than or equal to $threshold")
    }

    return this
}