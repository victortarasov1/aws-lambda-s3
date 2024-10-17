plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("software.amazon.awssdk:s3:2.28.22")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.amazonaws:aws-lambda-java-core:1.2.3")
    implementation("com.amazonaws:aws-lambda-java-events:3.14.0")


}

tasks.test {
    useJUnitPlatform()
}


tasks.register<Zip>("buildZip") {
    archiveBaseName.set("springBootServerless")

    from(tasks.compileJava)
    from(tasks.processResources)

    into("lib") {
        from(configurations.runtimeClasspath)
    }
}

tasks.named("build") {
    dependsOn("buildZip")
}
