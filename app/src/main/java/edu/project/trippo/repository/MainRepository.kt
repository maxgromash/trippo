package edu.project.trippo.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import edu.project.trippo.R
import edu.project.trippo.api.Api
import edu.project.trippo.model.*
import javax.inject.Inject

class MainRepository @Inject constructor(val api: Api) : BaseRepository(api) {

    private var data: MutableList<DestinationDTO>

    init {
        data = getData()
    }

    fun getSavedPrefs(): Preferences {
        val pref = Preferences()

        val user = Firebase.auth.currentUser
        val url = user?.photoUrl ?: return pref
        val prefs = url.toString().split("&")[0]
        val list = prefs.split("-")

        val temp1 = list[0].toDouble()
        val temp2 = list[1].toDouble()
        pref.temperature = Pair(temp1, temp2)

        val hotel = list[2].toInt()
        pref.hotelBudget = hotel

        val sea = list[3].toBoolean()
        pref.isSeaExist = sea

        val popular = list[4].toBoolean()
        pref.isPopular = popular

        val month = list[5].toInt()
        pref.month = month

        return pref
    }

    fun getStarred(): List<Long> {
        val pref = listOf<Long>()
        val user = Firebase.auth.currentUser
        val url = user?.photoUrl ?: return pref
        val starred = url.toString().split("&")[1]
        if (starred != "") {
            val list = starred.split("_")
            return list.map { it.toLong() }
        } else
            return pref
    }

    fun savePrefs(preferences: Preferences) {

        val user = Firebase.auth.currentUser
        val url = user?.photoUrl
        val saved = url?.toString() ?: "&"

        val prefs =
            "${preferences.temperature?.first}-${preferences.temperature?.second}-${preferences.hotelBudget}-${preferences.isSeaExist}-${preferences.isPopular}-${preferences.month}"

        val two = saved.split("&")

        val newSaved = prefs + "&" + two[1]
        val profileUpdates = userProfileChangeRequest {
            photoUri = Uri.parse(newSaved)
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }
    }

    fun saveStarred(list: List<DestinationVO>) {
        val user = Firebase.auth.currentUser
        val url = user?.photoUrl
        val saved = url?.toString() ?: "&"
        val two = saved.split("&")

        val listS = list.map { it.id }.joinToString("_")
        val newSaved = two[0] + "&" + listS

        val profileUpdates = userProfileChangeRequest {
            photoUri = Uri.parse(newSaved)
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }
    }


    suspend fun getAllData() {

        for (city in data) {

            val hotels = getHotelPrices(city.city)
            val corona = getCoronaData(city.country)
            val city = getCityData(city.city)
            val weather = getWeatherData(city.city)

            val mappedData = mutableListOf<DestinationDTO>()
            hotels.forEachIndexed { index, hotel ->
                val mappedCity = DestinationDTO(
                    city = city,
                    country = country,
                    cardImageLink = city[index].image,
                    averageTemp = weather,
                    description = city[index].description,
                    timeDiff = city[index].timeDiff,
                    corona = corona[index],
                    hotels = hotel,
                    engLevel = city[index].englishRate
                )
                mappedData.add(mappedCity)
            }

            data.addAll(mappedData)
        }
    }

    suspend fun getHotelPrices(city: String): List<Hotel> {
        return safeApiCall {
            api.getHotelPrices(city, "averagePrices")
        }
    }

    suspend fun getCoronaData(country: String): List<Corona> {
        return safeApiCall {
            api.getCoronaStatus(country)
        }
    }

    suspend fun getCityData(city: String): List<City> {
        return safeApiCall {
            api.getCityInfo(city)
        }
    }

    suspend fun getWeatherData(city: String): List<Int> {
        return safeApiCall {
            api.getWeather(city)
        }
    }

    fun getViaPrefsData(preferences: Preferences): List<DestinationVO> {
        return data.map { it.map(preferences) }
    }

    fun getQuestions() = listOf(
        Quiz(
            "What is your expected temperature? ",
            R.raw.quiz1,
            0,
            "Over 20",
            "Between 10 and 20",
            "Between 0 and 10",
            "less than 0"
        ),
        Quiz(
            "What is your budget per night in a hotel??",
            R.raw.quiz2,
            1,
            "Not more than 20$",
            "Not more than 50$",
            "Not more than 100$",
            "Up to 200"
        ),
        Quiz(
            "Should there be a sea or an ocean in this place?",
            R.raw.quiz3,
            2,
            "Yes",
            "No"
        ),
        Quiz(
            "Is the place supposed to be popular with tourists??",
            R.raw.quiz4,
            3,
            "Popular",
            "Something new"
        )
    )
}