package dev.yashh.devhub.core.database.mapper

import dev.yashh.devhub.core.database.entity.BookmarkTagEntity
import dev.yashh.devhub.core.database.entity.BookmarkWithTags
import dev.yashh.devhub.core.database.entity.RepositoryEntity
import dev.yashh.devhub.core.model.BookmarkTag
import dev.yashh.devhub.core.model.Owner
import dev.yashh.devhub.core.model.Repository
import kotlinx.collections.immutable.toImmutableList

fun RepositoryEntity.toDomain(): Repository = Repository(
    id = id,
    name = name,
    fullName = fullName,
    description = description,
    owner = Owner(
        id = ownerId,
        login = ownerLogin,
        avatarUrl = ownerAvatarUrl,
        htmlUrl = ownerHtmlUrl
    ),
    stargazersCount = stargazersCount,
    forksCount = forksCount,
    language = language,
    languageColor = languageColor,
    openIssuesCount = openIssuesCount,
    topics = topics.toImmutableList(),
    htmlUrl = htmlUrl,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isBookmarked = isBookmarked
)

fun Repository.toEntity(): RepositoryEntity = RepositoryEntity(
    id = id,
    name = name,
    fullName = fullName,
    description = description,
    ownerId = owner.id,
    ownerLogin = owner.login,
    ownerAvatarUrl = owner.avatarUrl,
    ownerHtmlUrl = owner.htmlUrl,
    stargazersCount = stargazersCount,
    forksCount = forksCount,
    language = language,
    languageColor = languageColor,
    openIssuesCount = openIssuesCount,
    topics = topics.toList(),
    htmlUrl = htmlUrl,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isBookmarked = isBookmarked
)

fun BookmarkTagEntity.toDomain(): BookmarkTag = BookmarkTag(
    id = id,
    name = name,
    color = colorArgb
)

fun BookmarkWithTags.toDomainRepository(): Repository {
    // The repository entity in this relation correctly reflects the domain state 
    // when we attach its tags if needed, but for now we just return the repo.
    // NOTE: If tags need to be in Repository, we'd add it to the model.
    return repository.toDomain().copy(isBookmarked = true)
}
