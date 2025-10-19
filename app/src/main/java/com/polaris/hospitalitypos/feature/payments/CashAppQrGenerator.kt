package com.polaris.hospitalitypos.feature.payments

import android.graphics.Bitmap
import android.graphics.Color
import javax.inject.Inject
import kotlin.math.min

class CashAppQrGenerator @Inject constructor() {
    fun generate(handle: String): Bitmap {
        val size = 512
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val hash = handle.hashCode()
        for (x in 0 until size) {
            for (y in 0 until size) {
                val toggle = ((x xor y xor hash) and 0x1) == 0
                bitmap.setPixel(x, y, if (toggle) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }
}
