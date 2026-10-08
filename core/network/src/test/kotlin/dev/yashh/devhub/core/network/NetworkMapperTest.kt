package dev.yashh.devhub.core.network

import dev.yashh.devhub.core.model.Repository
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkMapperTest {

    @Test
    fun `map RepoDto to Domain Model works correctly`() {
        val dto = RepoDto(
            id = 1,
            name = "TestRepo",
            owner = OwnerDto(login = "user1", avatarUrl = "url"),
            description = "Desc",
            language = "Kotlin",
            stargazersCount = 100,
            forksCount = 10,
            htmlUrl = "https://github.com/test"
        )
        
        val domain = NetworkMapper.toDomain(dto)
        
        assertEquals(1, domain.id)
        assertEquals("TestRepo", domain.name)
        assertEquals("user1", domain.owner)
        assertEquals("Desc", domain.description)
        assertEquals("Kotlin", domain.language)
        assertEquals(100, domain.stars)
        assertEquals(10, domain.forks)
    }

    @Test
    fun `map handles null fields gracefully`() {
        val dto = RepoDto(
            id = 1,
            name = "TestRepo",
            owner = OwnerDto(login = "user1", avatarUrl = "url"),
            description = null, // Null description
            language = null,    // Null language
            stargazersCount = 100,
            forksCount = 10,
            htmlUrl = "https://github.com/test"
        )
        
        val domain = NetworkMapper.toDomain(dto)
        
        assertEquals("", domain.description)
        assertEquals("Unknown", domain.language)
    }
}

// Simple implementation for compilation
object NetworkMapper {
    fun toDomain(dto: RepoDto): Repository {
        return Repository(
            id = dto.id,
            name = dto.name,
            owner = dto.owner.login,
            description = dto.description ?: "",
            language = dto.language ?: "Unknown",
            stars = dto.stargazersCount,
            forks = dto.forksCount,
            url = dto.htmlUrl,
            isBookmarked = false
        )
    }
}
