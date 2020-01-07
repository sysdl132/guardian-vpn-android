package org.mozilla.firefox.vpn.util

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

class StringResource {
    var id: Int? = null
    var str: String? = null
    var formatArgs: Array<out String>? = null

    constructor(str: String) {
        this.str = str
    }

    constructor(id: Int) {
        this.id = id
    }

    constructor(ftlId: Int, vararg strings: String) {
        this.id = ftlId
        this.formatArgs = strings
    }

    fun resolve(context: Context): String? {
        formatArgs?.let { return context.getString(this.id!!, *it) }
        str?.let { return it }
        id?.let { return context.getString(it) }

        return null
    }
}

fun Context.color(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun Context.colorStateList(@ColorRes id: Int): ColorStateList? {
    return ContextCompat.getColorStateList(this, id)
}
