package com.snatik.matches;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class TamperAction {
    public static void makeToast(final String text) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameApplication.sAppInstance.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void debuggerDetectionNonTamperAction() {
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

    public static void signatureCheckNonTamperAction2() {
        makeToast("SCG: Tampering2 not detected");
    }

    public static void signatureCheckTamperAction2() {
        makeToast("SCG: Tampering2 detected");
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

    public static void emulatorDetectionNonTamperAction3() {
        makeToast("EDG: Emulator3 not detected");
    }

    public static void emulatorDetectionTamperAction3() {
        makeToast("EDG: Emulator3 detected");
    }

    public static void dynamicInstrumentationDetectionNonTamperAction() {
        makeToast("DID: Tampering not detected");
    }

    public static void dynamicInstrumentationDetectionTamperAction() {
        makeToast("DID: Tampering detected");
    }

    public static void dynamicInstrumentationDetectionNonTamperAction2() {
        makeToast("DID: Tampering2 not detected");
    }

    public static void dynamicInstrumentationDetectionTamperAction2() {
        makeToast("DID: Tampering2 detected");
    }

    public static void resourceVerificationNonTamperAction() {
        makeToast("RVG: Tampering not detected");
    }

    public static void resourceVerificationTamperAction() {
        makeToast("RVG: Tampering detected");
    }

    public static void checksumNonTamperAction() {
        makeToast("CSG: Checksum tampering not detected");
    }

    public static void checksumTamperAction() {
        makeToast("CSG: Checksum tampering detected");
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

    public static void maliciousPackageDetectionGuardTamperAction() {
        makeToast("MPD: Tampering detected");
    }

    public static void maliciousPackageDetectionGuardNonTamperAction() {
        makeToast("MPD: Tampering not detected");
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
