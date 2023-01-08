import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream

plugins {
    kotlin("jvm") version "1.8.0"
    application
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
        apiVersion = "1.8"
        languageVersion = "1.8"
    }
}

group = "org.swtecnn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val dropwizardVersion = "2.0.28"
val jdbiVersion = "3.25.0"

dependencies {
    // Implementation dependencies:
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("io.dropwizard:dropwizard-core:$dropwizardVersion")
    implementation("io.dropwizard:dropwizard-jdbi3:$dropwizardVersion")
    implementation("io.dropwizard:dropwizard-migrations:$dropwizardVersion")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.5")

    implementation("org.jdbi:jdbi3-core:$jdbiVersion")
    implementation("org.jdbi:jdbi3-kotlin:$jdbiVersion")
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:$jdbiVersion")

    implementation("com.smoketurner:dropwizard-swagger:2.0.0-1")

    implementation("org.kodein.di:kodein-di-generic-jvm:6.1.0")


    // Runtime dependencies:
    runtimeOnly("mysql:mysql-connector-java:8.0.26")


    // Test dependencies:
    testImplementation("io.dropwizard:dropwizard-testing:$dropwizardVersion")

    testImplementation(platform("org.junit:junit-bom:5.4.0"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit-pioneer:junit-pioneer:1.1.0")

    testImplementation("io.strikt:strikt-core:0.34.1")

    testImplementation("io.mockk:mockk:1.9.3")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.swtecnn.user.management.App")
}

val mysqlDevPort = "8801"
val mysqlDevName = "user-management-mysql-playground"
val mysqlDevUser = "root"
val mysqlDevPassword = "venturegain"

tasks {

    val startMysqlDevContainer by registering {
        group = "MySQL"
        description = "Start a MySQL Docker container."

        doLast {
            // Start the container
            exec {
                commandLine(
                    "docker", "run",
                    "--publish", "$mysqlDevPort:3306",
                    "--name", mysqlDevName,
                    "--detach",
                    "--env", "MYSQL_ROOT_PASSWORD=$mysqlDevPassword",
                    "--health-cmd", "mysql -u $mysqlDevUser --password=$mysqlDevPassword --execute 'show databases;'",
                    "--health-interval", "1s",
                    "--health-retries", "20",
                    "mysql:8"
                )
            }

            // Wait for it to be ready (and get the associated IPs)
            var dockerPsOutput: String
            do {
                dockerPsOutput = ByteArrayOutputStream().use { stdout ->
                    exec {
                        commandLine(
                            "docker",
                            "ps",
                            "--filter",
                            "name=$mysqlDevName",
                            "--filter",
                            "health=healthy",
                            "--format",
                            "{{.Ports}}"
                        )

                        standardOutput = stdout
                        isIgnoreExitValue = true
                    }

                    stdout.toString(Charsets.UTF_8)
                }.trim()
            } while (dockerPsOutput.isEmpty())

            logger.lifecycle("Docker container $mysqlDevName started, running, and healthy!")
            logger.lifecycle("Port mappings:")
            logger.lifecycle("  HOST:HOST_PORT -> CONTAINER_PORT")
            logger.lifecycle("  localhost:$mysqlDevPort -> 3306")

            logger.lifecycle(
                """
            MySQL Dev Container is ready to use!

            Try executing sql commands on the container with the following command:

                docker exec -it $mysqlDevName mysql -u $mysqlDevUser -p

            Make sure to use '$mysqlDevPassword' as the password.

            When you're done with the database, or want to stop it to clear its
            state and start a new one, run:

                ./gradlew stopMysqlDevContainer
                """.trimIndent()
            )
        }
    }

    val stopMysqlDevContainer by registering {
        group = "MySQL"
        description = "Stop container started with startMysqlDevContainer."
        mustRunAfter(startMysqlDevContainer)
        doLast {
            exec { commandLine("docker", "kill", mysqlDevName) }
            exec { commandLine("docker", "rm", mysqlDevName) }
        }
    }

}