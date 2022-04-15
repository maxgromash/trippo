package edu.project.trippo.repository

import edu.project.trippo.api.Api
import edu.project.trippo.api.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException

abstract class BaseRepository(private val api: Api) {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Request<T> =
        //Переходим корутину в параллельный поток
        withContext(Dispatchers.IO) {
            try {
                Request.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        val mesJson = throwable.response()?.errorBody()?.string()
                        var message = "Some problems"
                        mesJson?.let {
                            message = JSONObject(mesJson).getString("message")
                        }
                        Request.Failure(false, throwable.code(), message)
                    }
                    else -> {
                        Request.Failure(true, null, "Ошибка подключения")
                    }
                }
            }
        }
}
