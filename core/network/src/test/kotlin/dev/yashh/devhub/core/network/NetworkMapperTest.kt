package dev.yashh.devhub.core.network

import dev.yashh.devhub.core.network.mapper.toDomain
import dev.yashh.devhub.core.network.model.NetworkOwner
import dev.yashh.devhub.core.network.model.NetworkRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class NetworkMapperTest {
    @Test fun repositoryFieldsSurviveMapping() {
        val dto = NetworkRepository(
            id = 42,
            name = "devhub",
            fullName = "sample/devhub",
            description = "Example",
            owner = NetworkOwner(7, "sample", "https://example.com/avatar", "https://example.com/sample"),
            stargazersCount = 123,
            forksCount = 8,
            language = "Kotlin",
            openIssuesCount = 2,
            topics = listOf("android"),
            htmlUrl = "https://github.com/sample/devhub",
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-02T00:00:00Z"
        )

        val result = dto.toDomain()
        assertEquals("sample/devhub", result.fullName)
        assertEquals("sample", result.owner.login)
        assertEquals(123, result.stargazersCount)
        assertEquals(listOf("android"), result.topics.toList())
        assertFalse(result.isBookmarked)
    }
}
