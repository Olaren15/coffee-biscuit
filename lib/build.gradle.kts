plugins {
    id("java-library")
    id("java-common-conventions")
}

group = "dev.olaren.coffee_biscuit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("org.jspecify:jspecify:1.0.0")
    implementation("org.apache.commons:commons-collections4:4.6.0")

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}