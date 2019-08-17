package com.example.pokedex.util

import android.os.Looper

class ThreadIdentifier private constructor() {

    companion object {
        @JvmStatic
        fun isUiThread(): Boolean {
            return Looper.getMainLooper().thread == Thread.currentThread()
        }
    }

}