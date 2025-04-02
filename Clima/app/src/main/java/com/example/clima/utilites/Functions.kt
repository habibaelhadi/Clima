package com.example.clima.utilites

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat
import com.example.clima.R
import com.example.clima.composable.alarms.broadcastrecievers.AlarmBroadcastReceiver
import com.example.clima.model.ForeCast
import com.example.clima.repo.WeatherRepo
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
    val locale = when (language) {
        context.getString(R.string.system_default) -> {
            // Use system default locale
            Resources.getSystem().configuration.locales.get(0)
        }
        else -> {
            // Use the selected language
            getLanguageCode(language)
        }
    }

    Locale.setDefault(locale)

    val config = context.resources.configuration
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    // Save the selected language (could be "System Default")
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit()
        .putString("app_language", language)
        .putBoolean("skip_splash", true)
        .apply()
}


fun getLanguageCode(language: String): Locale {
    return when (language) {
        "English" -> Locale("en")
        "العربية" -> Locale("ar")
        "Türkiye" -> Locale("tr")
        else -> Resources.getSystem().configuration.locales.get(0)
    }
}

fun getUnitCode(temp: String): String {
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


fun getTempUnit(temp: String): String {
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

fun Context.getWindSpeedUnitSymbol(unit: String): String {
    return when (unit) {
        "Meters per second (m/s)" -> getString(R.string.meter_sec)
        "Meters per second (m/s)" -> getString(R.string.meter_sec)
        "Miles per hour (m/h)" -> getString(R.string.mile_hour)
        else -> getString(R.string.meter_sec)
    }
}

fun updateUnitsBasedOnLanguage(context: Context, repo: WeatherRepo, language: String) {

    val systemLanguage = Resources.getSystem().configuration.locales.get(0) // Get system language (e.g., "ar" or "en")
    val effectiveLanguage = when {
        language == "العربية" || systemLanguage.toLanguageTag().startsWith("ar") -> "العربية"
        language == "English" || systemLanguage.toLanguageTag().startsWith("en") -> "English"
        else -> language
    }

    val newTempUnits: List<String>
    val newWindUnits: List<String>

    when (effectiveLanguage) {
        "English" -> {
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

        "العربية" -> {
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

        "Türkiye" -> {
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

        else -> {

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

    val currentTemp = repo.getTemperatureUnit()
    val currentWind = repo.getWindSpeedUnit()

    val updatedTempUnit = when (currentTemp) {
        context.getString(R.string.celsius_c) -> newTempUnits[0]
        context.getString(R.string.fahrenheit_f) -> newTempUnits[1]
        context.getString(R.string.kelvin_k) -> newTempUnits[2]
        else -> newTempUnits[0]
    }

    val updatedWindUnit = when (currentWind) {
        context.getString(R.string.miles_per_hour_m_h) -> newWindUnits[0]
        context.getString(R.string.meters_per_second_m_s) -> newWindUnits[1]
        else -> newWindUnits[0]
    }

    repo.setTemperatureUnit(updatedTempUnit)
    repo.setWindSpeedUnit(updatedWindUnit)
}

fun updateLocationBasedOnLanguage(context: Context, repo: WeatherRepo, language: String) {
    val newLocationValues: List<String>

    val systemLanguage = Resources.getSystem().configuration.locales.get(0) // Get system language (e.g., "ar" or "en")
    val effectiveLanguage = when {
        language == "العربية" || systemLanguage.toLanguageTag().startsWith("ar") -> "العربية"
        language == "English" || systemLanguage.toLanguageTag().startsWith("en") -> "English"
        else -> language
    }

    when (effectiveLanguage) {
        "English" -> {
            newLocationValues = listOf(
                context.getString(R.string.gps),
                context.getString(R.string.map)
            )
        }

        "العربية" -> {
            newLocationValues = listOf(
                context.getString(R.string.gps),
                context.getString(R.string.map)
            )
        }

        "Türkiye" -> {
            newLocationValues = listOf(
                context.getString(R.string.gps),
                context.getString(R.string.map)
            )
        }

        else -> {

            newLocationValues = listOf(
                context.getString(R.string.gps),
                context.getString(R.string.map)
            )
        }
    }

    val currentLocation = repo.getLocationSource()

    val updatedLocation = when (currentLocation) {
        context.getString(R.string.gps) -> newLocationValues[0]
        context.getString(R.string.map) -> newLocationValues[1]
        else -> newLocationValues[0]
    }

    repo.setLocationSource(updatedLocation)
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
    var newAlarmId = alarmId + 10
    val openPendingIntent = PendingIntent.getBroadcast(
        context,
        newAlarmId,
        openIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(context, "ALARM_CHANNEL")
        .setContentTitle(context.getString(R.string.alarm_weather_awaits))
        .setContentText(context.getString(R.string.current_weather, weatherDescription))
        .setSmallIcon(R.drawable.shower_rain)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setAutoCancel(true)
        .addAction(
            R.drawable.broken_clouds,
            context.getString(R.string.close),
            cancelPendingIntent
        )
        .addAction(
            R.drawable.broken_clouds,
            context.getString(R.string.open),
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