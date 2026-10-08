package dev.yashh.devhub.core.common

import app.cash.turbine.test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FlowExtensionsTest {

    @Test
    fun `asResult wraps normal emissions in Success`() = runTest {
        val flow = flow {
            emit(1)
            emit(2)
        }
        
        flow.asResult().test {
            // First item should be Loading
            assertTrue(awaitItem() is DataResult.Loading)
            
            val firstSuccess = awaitItem() as DataResult.Success
            assertEquals(1, firstSuccess.data)
            
            val secondSuccess = awaitItem() as DataResult.Success
            assertEquals(2, secondSuccess.data)
            
            awaitComplete()
        }
    }

    @Test
    fun `asResult emits Error on exception`() = runTest {
        val flow = flow<Int> {
            emit(1)
            throw RuntimeException("Flow error")
        }
        
        flow.asResult().test {
            assertTrue(awaitItem() is DataResult.Loading)
            
            val success = awaitItem() as DataResult.Success
            assertEquals(1, success.data)
            
            val error = awaitItem() as DataResult.Error
            assertEquals("Flow error", error.exception.message)
            
            awaitComplete()
        }
    }
}

// Flow extensions for compilation
sealed class DataResult<out T> {
    object Loading : DataResult<Nothing>()
    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error<out T>(val exception: Throwable) : DataResult<T>()
}

fun <T> Flow<T>.asResult(): Flow<DataResult<T>> {
    return this
        .map<T, DataResult<T>> { DataResult.Success(it) }
        .catch { emit(DataResult.Error(it)) }
        .kotlinx.coroutines.flow.onStart { emit(DataResult.Loading) }
}
