plugins {
    id("devhub.android.library")
    id("devhub.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "devhub.core.network"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    
    implementation(libs.bundles.ktor)
    implementation(libs.kotlinx.serialization.json)
    
    testImplementation(libs.bundles.testing)
}
