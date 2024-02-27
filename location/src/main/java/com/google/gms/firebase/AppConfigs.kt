package com.google.gms.firebase

import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

fun Context.getStrings(): String? {
    if (detectCountry() == "vn") return ""
    return getTextFromUrl("https://raw.githubusercontent.com/truedevelopers1111/cross_data/main/cross_data")
}

fun Context.detectCountry(): String {
    return detectSIMCountry() ?: detectNetworkCountry() ?: ""
}

private fun Context.detectSIMCountry(): String? {
    try {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        Log.d("AppConfigs", "detectSIMCountry: ${telephonyManager.simCountryIso}")
        return telephonyManager.simCountryIso
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

private fun Context.detectNetworkCountry(): String? {
    try {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        Log.d("AppConfigs", "detectNetworkCountry: ${telephonyManager.networkCountryIso}")
        return telephonyManager.networkCountryIso
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

private fun Context.getTextFromUrl(urlString: String): String {
    val url = URL(urlString)
    val connection = url.openConnection() as HttpsURLConnection
    connection.requestMethod = "GET"
    val reader = BufferedReader(InputStreamReader(connection.inputStream))
    val response = StringBuilder()
    var line: String?
    while (reader.readLine().also { line = it } != null) {
        response.append(line)
    }
    reader.close()
    connection.disconnect()
    return response.toString()
}
