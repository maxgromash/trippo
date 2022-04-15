package edu.project.trippo.api

import edu.project.trippo.model.City
import edu.project.trippo.model.Corona
import edu.project.trippo.model.Hotel
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

const val HOTEL_API_KEY = "o3thPZ2NQKfFhBIAK4glCx9ry38N7O30"
const val CORONA_API_KEY = "C6B6AAD9FA351CFBDFA77BDDA8C5D"
const val CITY_API_KET = "CZokTwr3WvvPyrnMfpGsYLJ2vSFZ75QT"
const val WEATHER_API_KET = "3B44D641F9AC6"

interface Api {

    @GET("hotels/info")
    suspend fun getHotelPrices(
        @Field("cityCode") id: String,
        @Field("infoType") type: String
    ): Hotel

    @GET("corona/status/byCountryCode")
    suspend fun getCoronaStatus(
        @Query("countryCode") id: String
    ): Corona

    @GET("cityInfo/getGeneralInfo")
    suspend fun getCityInfo(
        @Query("cityCodeCode") id: String
    ): City

    @GET("cityInfo/getSights")
    suspend fun getCitySights(
        @Query("cityCode") id: String
    ): List<City.Sight>

    @GET("data/month/average")
    suspend fun getWeather(
        @Query("code") id: String
    ): String

}