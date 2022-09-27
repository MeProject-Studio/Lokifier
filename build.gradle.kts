plugins {
    java
}

allprojects {
    group = "ru.meproject"
    version = "1.0.0-SNAPSHOT-3"

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
}

subprojects {
    apply(plugin = "java")
}