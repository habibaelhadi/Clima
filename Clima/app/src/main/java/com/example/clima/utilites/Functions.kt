package com.example.clima.utilites

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat
import com.example.clima.R
import com.example.clima.composable.alarms.broadcastrecievers.AlarmBroadcastReceiver
import com.example.clima.model.ForeCast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun displayDate(date: Long, pattern: String): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(date * 1000)
}

fun ForeCast.dailyForecasts(): Map<Int, List<ForeCast.ForecastWeather>> {
    val forecastMap = mutableMapOf<Int, MutableList<ForeCast.ForecastWeather>>()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    for (item in list) {
        val dateKey = dateFormat.format(Date(item.dt * 1000L)).replace("-", "").toInt()
        if (forecastMap[dateKey] == null) {
            forecastMap[dateKey] = mutableListOf()
        }
        forecastMap[dateKey]?.add(item)
    }
    return forecastMap.mapValues { it.value.take(8) }
}

fun Color.Companion.fromHex(hex: String): Color =
    Color(android.graphics.Color.parseColor(hex))

fun checkForInternet(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        @Suppress("DEPRECATION") val networkInfo =
            connectivityManager.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        return networkInfo.isConnected
    }
}

fun setLocale(context: Context, language: String) {
    val locale = getLanguageCode(language)

    Locale.setDefault(locale)

    val config = context.resources.configuration
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit()
        .putString("app_language", language)
        .putBoolean("skip_splash", true)
        .apply()
}

fun getLanguageCode(language: String) : Locale{
    return when (language) {
        "English" -> Locale("en")
        "العربية" -> Locale("ar")
        "Türkiye" -> Locale("tr")
        else -> Locale.getDefault()
    }
}

fun getUnitCode(temp : String) : String{
    return when (temp) {
        "Celsius (°C)" -> "metric"
        "Kelvin (°K)" -> "standard"
        "Fahrenheit (°F)" -> "imperial"
        "درجة مئوية (°س)" -> "metric"
        "كلفن (°ك)" -> "standard"
        "فهرنهايت (°ف)" -> "imperial"
        "Santigrat (°C)" -> "metric"
        "Kelvin (°K)" -> "standard"
        "Fahrenheit (°F)" -> "imperial"
        else -> "metric"
    }
}

fun Context.getTempUnitSymbol(unit: String): String {
    return when (unit) {
        "standard" -> getString(R.string.kelvin_k)
        "metric" -> getString(R.string.celsius_c)
        "imperial" -> getString(R.string.fahrenheit_f)
        else -> getString(R.string.celsius_c)
    }
}

fun getTempUnit(temp : String):String{
    return when (temp) {
        "Celsius (°C)" -> "°C"
        "Kelvin (°K)" -> "°K"
        "Fahrenheit (°F)" -> "°F"
        "درجة مئوية (°س)" -> "°س"
        "كلفن (°ك)" -> "°ك"
        "فهرنهايت (°ف)" -> "°ف"
        "Santigrat (°C)" -> "°C"
        "Kelvin (°K)" -> "°K"
        "Fahrenheit (°F)" -> "°F"
        else -> "°C"
    }
}

fun convertToArabicNumbers(number: String): String {
    val arabicDigits = arrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
    return number.map { if (it.isDigit()) arabicDigits[it.digitToInt()] else it }.joinToString("")
}

fun formatNumberBasedOnLanguage(number: String): String {
    val language = Locale.getDefault().language
    return if (language == "ar") convertToArabicNumbers(number) else number
}

fun formatTemperatureUnitBasedOnLanguage(unit: String): String {
    val language = Locale.getDefault().language
    if (language == "ar") {
        return when (unit) {
            "°C" -> "°س"
            "°F" -> "°ف"
            "°K" -> "°ك"
            else -> "°س"
        }
    }
    return unit
}


fun Context.getWindSpeedUnitSymbol(unit: String): String {
    return when (unit) {
        "Meters per second (m/s)" -> getString(R.string.meter_sec)
        "Meters per second (m/s)" -> getString(R.string.meter_sec)
        "Miles per hour (m/h)" -> getString(R.string.mile_hour)
        else -> getString(R.string.meter_sec)
    }
}

fun updateUnitsBasedOnLanguage(context: Context, sharedPreferences: SharedPreferences, language: String) {
    val newTempUnits: List<String>
    val newWindUnits: List<String>

    when (language) {
        "English" -> {
            newTempUnits = listOf(
                context.getString(R.string.celsius_c),   // Celsius (°C)
                context.getString(R.string.fahrenheit_f), // Fahrenheit (°F)
                context.getString(R.string.kelvin_k)      // Kelvin (°K)
            )
            newWindUnits = listOf(
                context.getString(R.string.miles_per_hour_m_h), // Miles per hour (m/h)
                context.getString(R.string.meters_per_second_m_s) // Meters per second (m/s)
            )
        }

        "العربية" -> {
            newTempUnits = listOf(
                context.getString(R.string.celsius_c),   // درجة مئوية (°س)
                context.getString(R.string.fahrenheit_f), // فهرنهايت (°ف)
                context.getString(R.string.kelvin_k)      // كلفن (°ك)
            )
            newWindUnits = listOf(
                context.getString(R.string.miles_per_hour_m_h), // أميال في الساعة (م/س)
                context.getString(R.string.meters_per_second_m_s) // أمتار في الثانية (م/ث)
            )
        }

        "Türkiye" -> {
            newTempUnits = listOf(
                context.getString(R.string.celsius_c),   // Santigrat (°C)
                context.getString(R.string.fahrenheit_f), // Fahrenheit (°F)
                context.getString(R.string.kelvin_k)      // Kelvin (°K)
            )
            newWindUnits = listOf(
                context.getString(R.string.miles_per_hour_m_h), // Mil/saat (m/h)
                context.getString(R.string.meters_per_second_m_s) // Metre/saniye (m/s)
            )
        }

        else -> {
            // Default to English if language is not recognized
            newTempUnits = listOf(
                context.getString(R.string.celsius_c),
                context.getString(R.string.fahrenheit_f),
                context.getString(R.string.kelvin_k)
            )
            newWindUnits = listOf(
                context.getString(R.string.miles_per_hour_m_h),
                context.getString(R.string.meters_per_second_m_s)
            )
        }
    }

    val currentTemp = sharedPreferences.getString("app_temp", newTempUnits[0])
    val currentWind = sharedPreferences.getString("app_wind", newWindUnits[0])

    // If the saved unit exists in the previous language set, replace it with the new one
    val updatedTempUnit = when (currentTemp) {
        context.getString(R.string.celsius_c) -> newTempUnits[0]
        context.getString(R.string.fahrenheit_f) -> newTempUnits[1]
        context.getString(R.string.kelvin_k) -> newTempUnits[2]
        else -> newTempUnits[0] // Default to Celsius
    }

    val updatedWindUnit = when (currentWind) {
        context.getString(R.string.miles_per_hour_m_h) -> newWindUnits[0]
        context.getString(R.string.meters_per_second_m_s) -> newWindUnits[1]
        else -> newWindUnits[0] // Default to Miles per hour
    }

    // Save the updated localized units
    sharedPreferences.edit().putString("app_temp", updatedTempUnit).apply()
    sharedPreferences.edit().putString("app_wind", updatedWindUnit).apply()
}

fun createNotification(
    context: Context,
    alarmId: Int,
    weatherDescription: String,
) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "ALARM_CHANNEL",
            "Alarm Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for alarm notifications"
        }
        notificationManager.createNotificationChannel(channel)
    }

    val deleteIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
        putExtra("ALARM_ID", alarmId)
        putExtra("ALARM_ACTION", "STOP")
        action = "DELETE"
    }
    val deletePendingIntent = PendingIntent.getBroadcast(
        context,
        alarmId,
        deleteIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val cancelIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
        putExtra("ALARM_ID", alarmId)
        putExtra("ALARM_ACTION", "STOP")
    }
    val cancelPendingIntent = PendingIntent.getBroadcast(
        context,
        alarmId,
        cancelIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val openIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
        putExtra("ALARM_ID", alarmId)
        putExtra("ALARM_ACTION", "OPEN")
    }
    var newAlarmId = alarmId+10
    Log.i("TAG", "createNotification: $newAlarmId")
    val openPendingIntent = PendingIntent.getBroadcast(
        context,
        newAlarmId,
        openIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(context, "ALARM_CHANNEL")
        .setContentTitle("Alarm: Weather awaits!")
        .setContentText("Current weather: $weatherDescription")
        .setSmallIcon(R.drawable.shower_rain)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setAutoCancel(true)
        .addAction(
            R.drawable.broken_clouds,
            "Cancel",
            cancelPendingIntent
        )
        .addAction(
            R.drawable.broken_clouds,
            "Open",
            openPendingIntent
        )
        .setDeleteIntent(deletePendingIntent)
        .build()

    notificationManager.notify(alarmId, notification)
}

fun playAudio(context: Context) {
    MyMediaPlayer.playAudio(context)
}

fun String.isValidTimeFormat(): Boolean {
    return this.matches(Regex("^[0-9].*"))
}

fun parseTimeToMillis(timeString: String): Long {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault()) // "3:25" should be "03:25"
    val date = sdf.parse(timeString) ?: return 0L

    val calendar = Calendar.getInstance()
    calendar.time = date

    // Get the current date and set the extracted time
    val now = Calendar.getInstance()
    calendar.set(Calendar.YEAR, now.get(Calendar.YEAR))
    calendar.set(Calendar.MONTH, now.get(Calendar.MONTH))
    calendar.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH))

    return calendar.timeInMillis
}