plugins {
    id("devhub.android.library")
}

android {
    namespace = "devhub.core.model"
}

dependencies {
    implementation(libs.kotlinx.datetime)
}
