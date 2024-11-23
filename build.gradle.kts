plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.hibernate:hibernate-core:5.6.3.Final")
    implementation("org.postgresql:postgresql:42.2.23")
    implementation("log4j:log4j:1.2.17")
    implementation("com.github.javafaker:javafaker:1.0.2")
    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
}

tasks.test {
    useJUnitPlatform()
}
