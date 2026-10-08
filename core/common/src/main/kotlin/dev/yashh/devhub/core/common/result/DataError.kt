package dev.yashh.devhub.core.common.result

/**
 * Root marker interface for all domain errors.
 * Ensures we only ever throw or pass around strongly-typed errors we actually handle.
 */
sealed interface DataError {
    
    sealed interface NetworkError : DataError {
        data object NoInternet : NetworkError
        data object Timeout : NetworkError
        data class ServerError(val code: Int) : NetworkError
        data class Unknown(val message: String?) : NetworkError
    }
    
    sealed interface LocalError : DataError {
        data class ReadError(val message: String?) : LocalError
        data class WriteError(val message: String?) : LocalError
        data object NotFound : LocalError
    }
}
