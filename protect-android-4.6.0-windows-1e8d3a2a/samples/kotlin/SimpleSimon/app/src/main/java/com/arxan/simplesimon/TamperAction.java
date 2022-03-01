package com.arxan.simplesimon;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class TamperAction {
    public static void makeToast(final String text) {
        Log.d("SimpleSimon", "Reaction: " + text);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SimpleSimonApplication.sAppInstance.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void debuggerDetectionNonTamperAction(Context context) {
        // Use Context object for more intricate reactions, like deleting files or accessing application info
        File cacheDir = context.getExternalCacheDir();
        Log.d("SimpleSimon", "External Cache Dir path accessible from reaction: " + cacheDir.getAbsolutePath());
        makeToast("DDG: Tampering not detected");
    }
    
    public static void debuggerDetectionTamperAction() {
        makeToast("DDG: Tampering detected");
    }

    public static void debuggerDetectionNonTamperAction2() {
        makeToast("DDG: Tampering2 not detected");
    }

    public static void debuggerDetectionTamperAction2() {
        makeToast("DDG: Tampering2 detected");
    }

    public static void rootDetectionNonTamperAction() {
        makeToast("RDG: Tampering not detected");
    }

    public static void rootDetectionTamperAction() {
        makeToast("RDG: Tampering detected");
    }

    public static void rootDetectionNonTamperAction2() {
        makeToast("RDG: Tampering not detected");
    }

    public static void rootDetectionTamperAction2() {
        makeToast("RDG: Tampering detected");
    }

    public static void hookDetectionNonTamperAction() {
        makeToast("HDG: Tampering not detected");
    }

    public static void hookDetectionTamperAction() {
        makeToast("HDG: Tampering detected");
    }

    public static void signatureCheckNonTamperAction() {
        makeToast("SCG: Tampering not detected");
    }

    public static void signatureCheckTamperAction() {
        makeToast("SCG: Tampering detected");
    }

    public static void emulatorDetectionNonTamperAction() {
        makeToast("EDG: Emulator not detected");
    }

    public static void emulatorDetectionTamperAction() {
        makeToast("EDG: Emulator detected");
    }

    public static void emulatorDetectionNonTamperAction2() {
        makeToast("EDG: Emulator2 not detected");
    }

    public static void emulatorDetectionTamperAction2() {
        makeToast("EDG: Emulator2 detected");
    }

    public static void dynamicInstrumentationDetectionNonTamperAction() {
        makeToast("DID: Tampering not detected");
    }

    public static void dynamicInstrumentationDetectionTamperAction() {
        makeToast("DID: Tampering detected");
    }

    public static void customGuardTamperAction() {
        makeToast("custom guard 1 tamper method called");
    }

    public static void customGuardNonTamperAction() {
        makeToast("custom guard 1 non-tamper method called");
    }

    public static void customGuardTamperAction2() {
        makeToast("custom guard 2 tamper method called");
    }

    public static void customGuardNonTamperAction2() {
        makeToast("custom guard 2 non-tamper method called");
    }

    public static void maliciousPackageDetectionGuardTamperAction() {
        makeToast("malicious package detection guard tamper method called");
    }

    public static void maliciousPackageDetectionGuardNonTamperAction() {
        makeToast("malicious package detection guard non-tamper method called");
    }

    public static void virtualizationDetectionTamperAction() {
        makeToast("VDG: Tampering detected");
    }

    public static void virtualizationDetectionNonTamperAction() {
        makeToast("VDG: Tampering not detected");
    }

    public static void virtualizationDetectionTamperAction2() {
        makeToast("VDG: Tampering2 detected");
    }

    public static void virtualizationDetectionNonTamperAction2() {
        makeToast("VDG: Tampering2 not detected");
    }

    public static void virtualControlDetectionTamperAction() {
        makeToast("VCG: Tampering detected");
    }

    public static void virtualControlDetectionNonTamperAction() {
        makeToast("VCG: Tampering not detected");
    }

    public static void virtualControlDetectionTamperAction2() {
        makeToast("VCG: Tampering2 detected");
    }

    public static void virtualControlDetectionNonTamperAction2() {
        makeToast("VCG: Tampering2 not detected");
    }
}
