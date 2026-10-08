plugins {
    id("devhub.android.library")
    id("devhub.android.compose")
}

android {
    namespace = "devhub.core.designsystem"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.network.ktor)
    implementation(libs.kotlinx.collections.immutable)
}
