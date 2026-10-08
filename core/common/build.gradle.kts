plugins {
    id("devhub.android.library")
    id("devhub.android.hilt")
}

android {
    namespace = "devhub.core.common"
}

dependencies {
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.timber)
    
    // Test dependencies
    testImplementation(libs.bundles.testing)
}
