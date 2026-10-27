plugins {
    id("java")
    id("java-common-conventions")
}

group = "dev.olaren.coffee_biscuit.generator"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jspecify:jspecify:1.0.0")
}
