package dev.yashh.devhub.core.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Abstraction for coroutine dispatchers to allow easy testing by swapping out the real implementations.
 */
interface DispatcherProvider {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
}
