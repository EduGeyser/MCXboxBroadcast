plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(libs.shadow)
    implementation(libs.indra.git)
    implementation(libs.blossom)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
