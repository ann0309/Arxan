package Arxan;

import android.content.Context;
import android.os.Debug;
import android.provider.Settings;

// 所以Custom Guard 偵測規則
public class CustomDetectRule {
    //偵測是否有被掛載debugger 類似debug detection功能
    public static boolean customGuard_DebugMode(Context context){
        if(Debug.isDebuggerConnected()) {
            return true;// debugging enabled
        } else {
            return false;//;debugging does not enabled
        }
    }

    //偵測是否有開啟 開發人員模式底下的USB除錯功能
    public static boolean customGuard_USBConnected(Context context) {
        int adb = Settings.Secure.getInt(context.getContentResolver(), Settings.Global.ADB_ENABLED, 0);
        if(adb == 1) {
            return true;// tamper action is called
        }else {
            return false;// non-tamper action is called
        }

    }

    //偵測Root magisk DRM模組是否有開啟，這個偵測結果有問題 沒有進行使用
//    public static boolean customGuard_RootDRM(Context context){
//        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningServiceInfo> list = manager.getRunningServices(Integer.MAX_VALUE);
//
//        for (ActivityManager.RunningServiceInfo service : list){
////            if("drm.drmManager".equals(service.service.getClassName())) {
//            if(service.service.getClassName().equals("drm.drmManager")) {
//                System.out.println("BAOJEM "+service.service.getClassName());
//                return false;
//            }
//        }
//        return true;
//    }

}
