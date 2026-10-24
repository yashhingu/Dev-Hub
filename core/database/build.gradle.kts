plugins {
    id("devhub.android.library")
    id("devhub.android.hilt")
    alias(libs.plugins.ksp)
}

android {
    namespace = "devhub.core.database"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    
    implementation(libs.datastore.preferences)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serialization.json)
    
    testImplementation(libs.bundles.testing)
}
