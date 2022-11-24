package com.swtecnn.user.management.database

import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import javax.sql.DataSource

object LiquibaseMigrator {

    private const val NO_CONTEXT_MODE = ""

    fun migrate(dataSource: DataSource) {
        dataSource.connection.use {
            val migrator = Liquibase("migrations.xml", ClassLoaderResourceAccessor(), JdbcConnection(it))
            migrator.update(NO_CONTEXT_MODE)
        }
    }

}