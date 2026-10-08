package dev.yashh.devhub.core.common.extensions

import dev.yashh.devhub.core.common.result.DataError
import dev.yashh.devhub.core.common.result.DataResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException

// NOTE: converting exceptions to DataResult here ensures we catch crashes early.

/**
 * Converts a Flow of type [T] into a Flow of [DataResult].
 * Any unexpected exceptions are caught and wrapped as Unknown NetworkError/LocalError.
 */
fun <T> Flow<T>.asResult(): Flow<DataResult<T, DataError>> {
    return this
        .map<T, DataResult<T, DataError>> { DataResult.Success(it) }
        .catch { e ->
            val error = when (e) {
                is IOException -> DataError.NetworkError.NoInternet
                else -> DataError.NetworkError.Unknown(e.message)
            }
            emit(DataResult.Failure(error))
        }
}

/**
 * Emits the first item and then drops any subsequent items emitted within the [windowDurationMillis] period.
 */
fun <T> Flow<T>.throttleFirst(windowDurationMillis: Long): Flow<T> = flow {
    var lastEmissionTime = 0L
    collect { upstream ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastEmissionTime > windowDurationMillis) {
            lastEmissionTime = currentTime
            emit(upstream)
        }
    }
}

/**
 * Retries the flow with an exponential backoff strategy on failure.
 */
fun <T> Flow<T>.retryWithExponentialBackoff(
    maxRetries: Int = 3,
    initialDelayMillis: Long = 1000,
    maxDelayMillis: Long = 10000,
    factor: Double = 2.0
): Flow<T> = flow {
    var currentDelay = initialDelayMillis
    var attempt = 0
    var success = false

    while (!success) {
        try {
            collect { value ->
                emit(value)
            }
            success = true
        } catch (e: Exception) {
            attempt++
            if (attempt > maxRetries) {
                throw e
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMillis)
        }
    }
}
