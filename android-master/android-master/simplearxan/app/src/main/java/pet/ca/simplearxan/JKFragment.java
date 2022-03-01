package pet.ca.simplearxan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class JKFragment extends BaseFragment implements View.OnClickListener {

    private static final String DETECT_RESULT = "DETECT_RESULT";
    private static MyHandler handler;
    private CheckBox rdg, hdg, edg;
    private CheckBox fdg, ddg, csg;
    private CheckBox scg, rvg, mpg, vdg,cus;
    private Button malware;
    private TextView result;
    Intent intent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_a4a, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.run).setOnClickListener(v -> detect());
        ((CheckBox) view.findViewById(R.id.selectAll)).setOnCheckedChangeListener((v, isChecked) -> setAllCheck(isChecked));
        rdg = view.findViewById(R.id.rdg);
        hdg = view.findViewById(R.id.hdg);
        edg = view.findViewById(R.id.edg);
        fdg = view.findViewById(R.id.fdg);
        ddg = view.findViewById(R.id.ddg);
        csg = view.findViewById(R.id.csg);
        scg = view.findViewById(R.id.scg);
        rvg = view.findViewById(R.id.rvg);
        mpg = view.findViewById(R.id.mpg);
        vdg = view.findViewById(R.id.vdg);
        cus = view.findViewById(R.id.cus);
        malware = view.findViewById(R.id.malware);
        malware.setOnClickListener(this);
        result = view.findViewById(R.id.result);
        handler = new MyHandler(this);
    }

    public void GGGGG(){
        intent= new Intent(getActivity().getApplication(), SecondActivity.class);
        getActivity().finish();
        startActivity(intent);
    }

    //預設勾選全部動態
    private void setAllCheck(boolean checked) {
        rdg.setChecked(checked);
        hdg.setChecked(checked);
        edg.setChecked(checked);
        fdg.setChecked(checked);
        ddg.setChecked(checked);
        csg.setChecked(checked);
        scg.setChecked(checked);
        rvg.setChecked(checked);
        mpg.setChecked(checked);
        vdg.setChecked(checked);
        cus.setChecked(checked);
    }

    //執行動態偵測
    private void detect() {
        result.setText("");
        new Thread(() -> {

            if (rdg.isChecked()) {
                GuardLocation.rdg();
            }

            if (hdg.isChecked()) {
                GuardLocation.hdg();
            }

            if (edg.isChecked()) {
                GuardLocation.edg();
            }

            if (ddg.isChecked()) {
                GuardLocation.ddg();
            }

            if (csg.isChecked()) {
                GuardLocation.csg();
            }

            if (scg.isChecked()) {
                GuardLocation.scg();
            }

            if (rvg.isChecked()) {
                GuardLocation.rvg();
            }

            if (fdg.isChecked()) {
                GuardLocation.fdg();
            }
            if (mpg.isChecked()){
                GuardLocation.mpg();
            }

            if (vdg.isChecked()){
                GuardLocation.vdg();
            }

            if(cus.isChecked()){
                GuardLocation.cg();
            }

        }).start();

    }

    //顯示動態偵測結果
    @Override
    public void setResult(Message msg) {
        String message = msg.getData().getString(DETECT_RESULT, "???????")
                + "\n" + result.getText();
        result.setText(message);
    }

    synchronized protected static void sendResult(String result) {
        Bundle bundle = new Bundle();
        bundle.putString(DETECT_RESULT, result);
        Message msg = handler.obtainMessage();
        msg.setData(bundle);
        msg.sendToTarget();
    }

    //以下為反應動作
    // Custom Guard
//    public static void customRootDRMTamper(){sendResult("Custom Guard: Root DRM ... Tamper!");}
//
//    public static void customRootDRMNonTamper(){sendResult("Custom Guard: Root DRM ... Non-Tamper!");}

    public static void customUSBConnectedTamper(){sendResult("Custom Guard: USBConnected ... Tamper!");}

    public static void customUSBConnectedNonTamper(){sendResult("Custom Guard: USBConnected ... Non-Tamper!");}

    public static void customDebugModeTamper(){sendResult("Custom Guard: DebugMode ... Tamper!");}

    public static void customDebugModeNonTamper(){sendResult("Custom Guard: DebugMode ... Non-Tamper!");}

    // Root Detection
    public static void rootTamper() {
        sendResult("Root[root] ... Tamper!");
    }

    public static void rootNonTamper() {
        sendResult("Root[root] ... Non-Tamper!");
    }

    public static void rootManagementAppsTamper() {
        sendResult("Root[rootManagementApps] ... Tamper!");
    }

    public static void rootManagementAppsNonTamper() {
        sendResult("Root[rootManagementApps] ... Non-Tamper!");
    }

    public static void rootHidingAppsTamper() {
        sendResult("Root[rootHidingApps] ... Tamper!");
    }

    public static void rootHidingAppsNonTamper() {
        sendResult("Root[rootHidingApps] ... Non-Tamper!");
    }

    public static void rootRequiringAppsTamper() {
        sendResult("Root[rootRequiringApps] ... Tamper!");
    }

    public static void rootRequiringAppsNonTamper() {
        sendResult("Root[rootRequiringApps] ... Non-Tamper!");
    }

    public static void rootMagiskHideTamper() {
        sendResult("Root[rootMagiskHide] ... Tamper!");
    }

    public static void rootMagiskHideNonTamper() {
        sendResult("Root[rootMagiskHide] ... Non-Tamper!");
    }

    public static void rootTargetOSTamper() { sendResult("Root[targetOS 1~11] ... Tamper!"); }

    public static void rootTargetOSNonTamper() { sendResult("Root[targetOS 1~11] ... Non-Tamper!"); }



    // Hook Detection
    public static void hookTargetTamper() {
        sendResult("Hook[hookTarget] ... Tamper!");
    }

    public static void hookTargetNonTamper() {
        sendResult("Hook[hookTarget] ... Non-Tamper!");
    }

    public static void hookRequiringAppsTamper() {
        sendResult("Hook[hookRequiringApps] ... Tamper!");
    }

    public static void hookRequiringAppsNonTamper() {
        sendResult("Hook[hookRequiringApps] ... Non-Tamper!");
    }

    public static void hookMagiskHiddenModulesTamper() {
        sendResult("Hook[MagiskHiddenModules] ... Tamper!");
    }

    public static void hookMagiskHiddenModulesNonTamper() {
        sendResult("Hook[MagiskHiddenModules] ... Non-Tamper!");
    }

    public static void hookInstalledTamper() {
        sendResult("Hook[hookInstalled] ... Tamper!");
    }

    public static void hookInstalledNonTamper() {
        sendResult("Hook[hookInstalled] ... Non-Tamper!");
    }

    // Emulator Detection
    public static void emulatorTamper() {
        sendResult("Emulator ... Tamper!");
    }

    public static void emulatorNonTamper() {
        sendResult("Emulator ... Non-Tamper!");
    }

    // Frida Detection
    public static void fridaTamper() {
        sendResult("Frida ... Tamper!");
    }

    public static void fridaNonTamper() {
        sendResult("Frida ... Non-Tamper!");
    }

    // Debug Detection
    public static void debuggerTamper() {
        sendResult("Debugger ... Tamper!");
    }

    public static void debuggerNonTamper() {
        sendResult("Debugger ... Non-Tamper!");
    }

    // Check Sum
    public static void checksumTamper() {
        sendResult("Checksum ... Tamper!");
    }

    public static void checksumNonTamper() {
        sendResult("Checksum ... Non-Tamper!");
    }


    // Signature Check
    public static void signatureTamper() {
        sendResult("Signature ... Tamper!");
    }

    public static void signatureNonTamper() {
        sendResult("Signature ... Non-Tamper!");
    }

    // Resource Verification
    public static void rvgTamper() {
        sendResult("Resource Verify ... Tamper!");
    }

    public static void rvgNonTamper() {
        sendResult("Resource Verify ... Non-Tamper!");
    }

    // Malicious Detection
    public static void mpgTamper() {
        sendResult("Malicious Package ... Tamper!");
    }

    public static void mpgNonTamper() {
        sendResult("Malicious Package ... Non-Tamper!");
    }

    // Virtualization Detection
    public static void vdTamper() {
        sendResult("Virtualization ... Tamper!");
    }

    public static void vdNonTamper() {
        sendResult("Virtualization ... Non-Tamper!");
    }

    public static void vdModifiedPropsTamper() { sendResult("Virtualization[ModifiedProps] ... Tamper!"); }

    public static void vdModifiedPropsNonTamper() { sendResult("Virtualization[ModifiedProps] ... Non-Tamper!"); }

    public static void vdModifiedSecurityPropsTamper() { sendResult("Virtualization[ModifiedSecurityProps] ... Tamper!"); }

    public static void vdModifiedSecurityPropsNonTamper() { sendResult("Virtualization[ModifiedSecurityProps] ... Non-Tamper!");}

    public static void vdModifiedPermissionsTamper() {
        sendResult("Virtualization[ModifiedPermissions] ... Tamper!");
    }

    public static void vdModifiedPermissionsNonTamper() {
        sendResult("Virtualization[ModifiedPermissions] ... Non-Tamper!");
    }

    public static void vdModifiedPackageNameTamper() {
        sendResult("Virtualization[ModifiedPackageName] ... Tamper!");
    }

    public static void vdModifiedPackageNameNonTamper() {
        sendResult("Virtualization[ModifiedPackageName] ... Non-Tamper!");
    }

    public static void vdWorkProfileManagementAppsTamper() {
        sendResult("Virtualization[WorkProfileManagementApps] ... Tamper!");
    }

    public static void vdWorkProfileManagementAppsNonTamper() {
        sendResult("Virtualization[WorkProfileManagementApps] ... Non-Tamper!");
    }

    public static void vdClonedAppsTamper() {
        sendResult("Virtualization[ClonedApps] ... Tamper!");
    }

    public static void vdClonedAppsNonTamper() {
        sendResult("Virtualization[ClonedApps] ... Non-Tamper!");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.malware:
//                android.os.Process.killProcess(android.os.Process.myPid());
                GGGGG();
                break;
        }
    }
}
