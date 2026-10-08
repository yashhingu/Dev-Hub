package dev.yashh.devhub.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkReadme(
    val name: String,
    val path: String,
    val sha: String,
    val size: Int,
    val url: String,
    val html_url: String,
    val git_url: String,
    val download_url: String?,
    val type: String,
    val content: String,
    val encoding: String
)
