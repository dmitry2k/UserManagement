import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    application
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
    runtimeOnly("com.h2database:h2:1.4.200")


    // Test dependencies:
    testImplementation("io.dropwizard:dropwizard-testing:$dropwizardVersion")

    testImplementation(platform("org.junit:junit-bom:5.4.0"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit-pioneer:junit-pioneer:1.1.0")

    testImplementation(platform("io.strikt:strikt-bom:0.28.0"))
    testImplementation("io.strikt:strikt-core")
    testImplementation("io.strikt:strikt-java-time")

    testImplementation("io.mockk:mockk:1.9.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("com.swtecnn.user.management.App")
}