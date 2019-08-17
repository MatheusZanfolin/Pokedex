package com.zanfolin.pokedex.base.util

import android.os.Looper

class ThreadIdentifier private constructor() {

    companion object {
        @JvmStatic
        fun isUiThread(): Boolean {
            return Looper.getMainLooper().thread == Thread.currentThread()
        }
    }

}