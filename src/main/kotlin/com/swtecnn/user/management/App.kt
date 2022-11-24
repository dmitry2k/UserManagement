package com.swtecnn.user.management

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.swtecnn.user.management.database.LiquibaseMigrator
import io.dropwizard.Application
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.migrations.MigrationsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

class App : Application<ApplicationConfig>() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            App().run(*args)
        }
    }

    override fun getName(): String = "user-management"

    override fun initialize(bootstrap: Bootstrap<ApplicationConfig>) {
        bootstrap.objectMapper.registerModule(KotlinModule())
        bootstrap.addBundle(
            object : MigrationsBundle<ApplicationConfig>() {
                override fun getDataSourceFactory(configuration: ApplicationConfig): DataSourceFactory {
                    return configuration.database
                }
            }
        )
    }

    override fun run(configuration: ApplicationConfig, environment: Environment) {
        LiquibaseMigrator.migrate(configuration.database.build(environment.metrics(), "migrations"))
    }
}