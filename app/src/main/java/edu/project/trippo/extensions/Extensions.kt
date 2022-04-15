package edu.project.trippo.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.util.TypedValue
import android.widget.EditText
import edu.project.trippo.model.Conformity
import edu.project.trippo.model.DestinationDTO
import edu.project.trippo.model.Preferences
import edu.project.trippo.model.Status

fun Int.toPx(context: Context): Int =
    toPxF(context).toInt()

fun Int.toPxF(context: Context) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    )

fun EditText.isEmailValid(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this.text.trim().toString()).matches()
}

fun Context.isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}

fun DestinationDTO.getConformity(preferences: Preferences): Conformity {
    var cnt = 0
    return Conformity(
        weather = if (averageTemp[preferences.month] >= preferences.temperature?.first!! && averageTemp[preferences.month] <= preferences.temperature?.second!!) {
            cnt++
            Status.YES
        } else Status.NO,
        budget = if (hotels.threeStar > preferences.hotelBudget!!)
            Status.NO
        else {
            cnt++
            Status.YES
        },
        sea = if (preferences.isSeaExist == seaExist) {
            cnt++
            Status.YES
        } else Status.NO,
        popularity = if (preferences.isPopular == isPopular) {
            cnt++
            Status.YES
        } else Status.NO,
        conformityCount = cnt
    )
}