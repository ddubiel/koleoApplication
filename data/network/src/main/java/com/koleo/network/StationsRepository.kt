package com.koleo.network

import com.koleo.network.model.Station
import com.koleo.network.model.StationKeyword
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StationsRepository @Inject constructor(
    private val koleoApi: KoleoApi
) {
    suspend fun getStations(): NetworkResult<List<Station>?> {
        runCatching {
            val response = koleoApi.getStations()
            return if (response.isSuccessful) {
                NetworkResult.Success(response.body())
            } else {
                NetworkResult.Failure(
                    when (response.code()) {
                        400 -> NetworkResultFailureReason.BadRequest(Exception(response.message()))
                        401 -> NetworkResultFailureReason.Unauthorized(Exception(response.message()))
                        403 -> NetworkResultFailureReason.Forbidden(Exception(response.message()))
                        404 -> NetworkResultFailureReason.Unavailable(Exception(response.message()))
                        500 -> NetworkResultFailureReason.InternalError(Exception(response.message()))
                        else -> NetworkResultFailureReason.OtherError(Exception(response.message()))

                    }
                )
            }
        }
            .getOrElse {
                return NetworkResult.Failure(NetworkResultFailureReason.OtherError(Exception(it)))
            }
    }
    suspend fun getStationsKeywords(): NetworkResult<List<StationKeyword>?> {
        runCatching {
            val response = koleoApi.getStationsKeywords()
            return if (response.isSuccessful) {
                NetworkResult.Success(response.body())
            } else {
                NetworkResult.Failure(
                    when (response.code()) {
                        400 -> NetworkResultFailureReason.BadRequest(Exception(response.message()))
                        401 -> NetworkResultFailureReason.Unauthorized(Exception(response.message()))
                        403 -> NetworkResultFailureReason.Forbidden(Exception(response.message()))
                        404 -> NetworkResultFailureReason.Unavailable(Exception(response.message()))
                        500 -> NetworkResultFailureReason.InternalError(Exception(response.message()))
                        else -> NetworkResultFailureReason.OtherError(Exception(response.message()))

                    }
                )
            }
        }
            .getOrElse {
                return NetworkResult.Failure(NetworkResultFailureReason.OtherError(Exception(it)))
            }
    }
}


sealed class NetworkResult<ResponseT> {
    data class Success<ResponseT>(val value: ResponseT) : NetworkResult<ResponseT>()
    data class Failure<ResponseT>(val failureReason: NetworkResultFailureReason) :
        NetworkResult<ResponseT>()
}

sealed class NetworkResultFailureReason(open val error: Throwable? = null) {
    data class Unauthorized(override val error: Throwable? = null) :
        NetworkResultFailureReason(error)

    data class Unavailable(override val error: Throwable? = null) :
        NetworkResultFailureReason(error)

    data class InternalError(override val error: Throwable? = null) :
        NetworkResultFailureReason(error)

    data class Forbidden(override val error: Throwable? = null) : NetworkResultFailureReason(error)
    data class BadRequest(override val error: Throwable? = null) : NetworkResultFailureReason(error)
    data class NotFound(override val error: Throwable? = null) : NetworkResultFailureReason(error)
    data class OtherError(override val error: Throwable? = null) : NetworkResultFailureReason(error)
}

suspend inline fun <reified T> NetworkResult<T>.onSuccess(
    crossinline block: suspend (T) -> Unit,
): NetworkResult<T> = apply { if (this is NetworkResult.Success) block(this.value) }

suspend inline fun <reified T> NetworkResult<T>.onError(
    crossinline block: suspend (NetworkResultFailureReason) -> Unit,
): NetworkResult<T> = apply { if (this is NetworkResult.Failure) block(this.failureReason) }