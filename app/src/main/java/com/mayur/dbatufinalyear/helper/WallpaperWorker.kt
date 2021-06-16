package com.mayur.dbatufinalyear.helper

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mayur.dbatufinalyear.data.Constants
import com.mayur.dbatufinalyear.data.Prefs
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.*

class WallpaperWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {

        val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        if (date == Prefs(applicationContext).wallpaperUpdatedDay) Result.success()

        var wallType = Constants.WALL_TYPE_DARK
        if (Prefs(applicationContext).themeColor == Constants.THEME_MODE_LIGHT)
            wallType = Constants.WALL_TYPE_LIGHT

        val wallpaperUrl = getTodaysWallpaper(wallType)

        val success = setWallpaper(
            applicationContext,
            wallpaperUrl
        )

        if (success) {
            updateWallpaperPrefs(wallpaperUrl, date)
            Result.success()
        } else Result.retry()
    }

    private fun updateWallpaperPrefs(url: String, date: String) {
        Prefs(applicationContext).dailyWallpaperUrl = url
        Prefs(applicationContext).wallpaperUpdatedDay = date
    }
}
