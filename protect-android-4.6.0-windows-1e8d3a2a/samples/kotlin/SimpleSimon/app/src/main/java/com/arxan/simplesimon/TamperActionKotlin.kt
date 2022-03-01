package com.arxan.simplesimon

import android.content.Context
import android.util.Log

public class TamperActionKotlin {
    fun resourceVerificationNonTamperAction() {
        TamperAction.makeToast("RVG: Tampering not detected")
    }

    fun resourceVerificationTamperAction() {
        TamperAction.makeToast("RVG: Tampering detected")
    }

    fun checksumNonTamperAction(context: Context) {
        // Use Context object for more intricate reactions, like deleting files or accessing application info
        Log.d("SimpleSimon", "Current package name from reaction: " + context.packageName)
        TamperAction.makeToast("CSG: Checksum tampering not detected")
    }

    fun checksumTamperAction() {
        TamperAction.makeToast("CSG: Checksum tampering detected")
    }
}