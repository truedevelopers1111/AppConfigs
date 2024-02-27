package com.google.gms.firebase

import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class AppConfigs {

    companion object {
        const val TAG = "AppConfigs"
    }

    private fun getDetectedCountry(context: Context): String {
        return detectSIMCountry(context)
            ?: detectNetworkCountry(context)
            ?: ""
    }

    private fun detectSIMCountry(context: Context): String? {
        try {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            Log.d(TAG, "detectSIMCountry: ${telephonyManager.simCountryIso}")
            return telephonyManager.simCountryIso
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun detectNetworkCountry(context: Context): String? {
        try {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            Log.d(TAG, "detectNetworkCountry: ${telephonyManager.networkCountryIso}")
            return telephonyManager.networkCountryIso
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getStrings(context: Context): String? {
        if (getDetectedCountry(context) == "vn") return ""
        return getTextFromUrl("https://raw.githubusercontent.com/truedevelopers1111/cross_data/main/cross_data")
    }

    fun getTextFromUrl(urlString: String): String {
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
}