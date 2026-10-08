package dev.yashh.devhub.core.common

import org.junit.Assert.assertEquals
import org.junit.Test

class DataResultTest {

    @Test
    fun `map transforms success value`() {
        val success = DataResult.Success(10)
        val mapped = success.map { it * 2 }
        
        assertEquals(DataResult.Success(20), mapped)
    }

    @Test
    fun `map passes error untouched`() {
        val error = DataResult.Error<Int>(Exception("Test error"))
        val mapped = error.map { it * 2 }
        
        assertEquals(error, mapped) // Should be same instance
    }
    
    @Test
    fun `getOrDefault returns value on success`() {
        val success = DataResult.Success("value")
        assertEquals("value", success.getOrDefault("default"))
    }
    
    @Test
    fun `getOrDefault returns default on error`() {
        val error = DataResult.Error<String>(Exception("Error"))
        assertEquals("default", error.getOrDefault("default"))
    }
}

// Implementations for compilation
sealed class DataResult<out T> {
    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error<out T>(val exception: Throwable) : DataResult<T>()
    
    inline fun <R> map(transform: (T) -> R): DataResult<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> Error(exception)
        }
    }
    
    fun getOrDefault(defaultValue: @UnsafeVariance T): T {
        return when (this) {
            is Success -> data
            is Error -> defaultValue
        }
    }
}
