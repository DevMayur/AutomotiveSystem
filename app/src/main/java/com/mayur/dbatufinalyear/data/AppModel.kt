package app.dbatufinalyear.data

import android.os.UserHandle

data class AppModel(
    val appLabel: String,
    val appPackage: String,
    val user: UserHandle
)