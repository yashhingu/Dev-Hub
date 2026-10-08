package dev.yashh.devhub.core.common.result

/**
 * A discriminated union that encapsulates a successful outcome with a value of type [T]
 * or a failure with an arbitrary [DataError] of type [E].
 */
sealed interface DataResult<out T, out E: DataError> {
    data class Success<T>(val data: T) : DataResult<T, Nothing>
    data class Failure<E: DataError>(val error: E) : DataResult<Nothing, E>
}

// NOTE: These extensions are incredibly useful for chaining operations cleanly

inline fun <T, E: DataError, R> DataResult<T, E>.map(transform: (T) -> R): DataResult<R, E> =
    when (this) {
        is DataResult.Success -> DataResult.Success(transform(data))
        is DataResult.Failure -> this
    }

inline fun <T, E: DataError, R> DataResult<T, E>.flatMap(transform: (T) -> DataResult<R, E>): DataResult<R, E> =
    when (this) {
        is DataResult.Success -> transform(data)
        is DataResult.Failure -> this
    }

inline fun <T, E: DataError> DataResult<T, E>.onSuccess(action: (T) -> Unit): DataResult<T, E> {
    if (this is DataResult.Success) action(data)
    return this
}

inline fun <T, E: DataError> DataResult<T, E>.onFailure(action: (E) -> Unit): DataResult<T, E> {
    if (this is DataResult.Failure) action(error)
    return this
}

fun <T, E: DataError> DataResult<T, E>.getOrNull(): T? =
    when (this) {
        is DataResult.Success -> data
        is DataResult.Failure -> null
    }

fun <T, E: DataError> DataResult<T, E>.getOrDefault(defaultValue: T): T =
    when (this) {
        is DataResult.Success -> data
        is DataResult.Failure -> defaultValue
    }
