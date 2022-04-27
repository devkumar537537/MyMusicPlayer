package com.myexample.mymusicplayer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat

object Utils {
    val PREV="Previous"
    val NEXT  ="Next"
    val PAUSE="pause"
    val PLAY="play"
    val EXIT ="exit"
    fun isPermissionGranted(context: Context?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val readExtStorage = ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            readExtStorage == PackageManager.PERMISSION_GRANTED
        }
    }
}