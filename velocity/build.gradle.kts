repositories {
    maven("https://papermc.io/repo/repository/maven-public")
}

dependencies {
    implementation("com.velocitypowered:velocity-api:3.1.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")
    implementation(project(":common"))
}
