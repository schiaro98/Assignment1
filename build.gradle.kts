plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    java
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    // Use JCenter for resolving dependencies.
    jcenter()
}

dependencies {
    implementation("com.intellij:forms_rt:7.0.3")
    implementation("org.junit.jupiter:junit-jupiter:5.4.2")
    // Use JUnit test framework.
    testImplementation("junit:junit:4.13")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:29.0-jre")

    // https://mvnrepository.com/artifact/org.apache.pdfbox/pdfbox-app
    implementation("org.apache.pdfbox:pdfbox-app:2.0.22")
}

application {
    // Define the main class for the application.
    mainClass.set("Main")
}

tasks.named<JavaExec>("run") {
    args("res/pdf", "res/ignored/ignore.txt", "10")
}

task("runJPF", JavaExec::class) {
    main = "MainJPF"
    classpath = sourceSets["main"].runtimeClasspath
    doLast {
        exec {
            commandLine = listOf("java", "-jar", "jpf-core/build/RunJPF.jar", "+classpath=build/classes/java/main", main)
        }
    }
}
