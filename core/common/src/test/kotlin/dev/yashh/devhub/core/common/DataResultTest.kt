package dev.yashh.devhub.core.common

import dev.yashh.devhub.core.common.result.DataError
import dev.yashh.devhub.core.common.result.DataResult
import dev.yashh.devhub.core.common.result.getOrDefault
import dev.yashh.devhub.core.common.result.map
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class DataResultTest {
    @Test fun successMapsItsValue() {
        val result: DataResult<Int, DataError> = DataResult.Success(4)
        assertEquals(8, result.map { it * 2 }.getOrDefault(0))
    }

    @Test fun failurePreservesItsError() {
        val error = DataError.NetworkError.NoInternet
        val result: DataResult<Int, DataError> = DataResult.Failure(error)
        assertSame(error, (result.map { it * 2 } as DataResult.Failure).error)
        assertEquals(7, result.getOrDefault(7))
    }
}
