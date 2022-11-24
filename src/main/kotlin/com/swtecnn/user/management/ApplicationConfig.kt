package com.swtecnn.user.management

import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory

class ApplicationConfig(
    val database: DataSourceFactory,
) : Configuration()