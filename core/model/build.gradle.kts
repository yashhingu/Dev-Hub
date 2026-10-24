plugins {
    id("devhub.android.library")
}

android {
    namespace = "devhub.core.model"
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)
    implementation(libs.kotlinx.collections.immutable)
}
