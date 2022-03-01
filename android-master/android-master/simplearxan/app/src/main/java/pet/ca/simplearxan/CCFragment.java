package pet.ca.simplearxan;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

    //EIT Android 頁面Fragment
public class CCFragment extends BaseFragment {

    private static final String DETECT_RESULT = "DETECT_RESULT";
    private static MyHandler handler;

    private CheckBox rdg, hdg, fdg, ddg, csg;

    private TextView result;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.run).setOnClickListener(v -> detect());
        ((CheckBox) view.findViewById(R.id.selectAll)).setOnCheckedChangeListener((v, isChecked) -> setAllCheck(isChecked));
        rdg = view.findViewById(R.id.rdg);
        hdg = view.findViewById(R.id.hdg);
        fdg = view.findViewById(R.id.fdg);
        ddg = view.findViewById(R.id.ddg);
        csg = view.findViewById(R.id.csg);
        result = view.findViewById(R.id.result);
        handler = new MyHandler(this);
    }

    private void setAllCheck(boolean checked) {
        rdg.setChecked(checked);
        hdg.setChecked(checked);
        fdg.setChecked(checked);
        ddg.setChecked(checked);
        csg.setChecked(checked);
    }

    private void detect() {
        result.setText("");
        if (rdg.isChecked()) {
            JNIUtils.root();
        }

        if (hdg.isChecked()) {
            JNIUtils.hook();
        }

        if (ddg.isChecked()) {
            JNIUtils.debugger();
        }

        if (csg.isChecked()) {
            JNIUtils.checksum();
        }

        if (fdg.isChecked()) {
            JNIUtils.fdg();
        }

    }

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

    public static void hookTamper() {
        sendResult("Hook ... Tamper!");
    }

    public static void hookNonTamper() {
        sendResult("Hook ... Non-Tamper!");
    }

    public static void fdgTamper() {
        sendResult("Frida ... Tamper!");
    }

    public static void fdgNonTamper() {
        sendResult("Frida ... Non-Tamper!");
    }

    public static void debuggerTamper() {
        sendResult("Debugger ... Tamper!");
    }

    public static void debuggerNonTamper() {
        sendResult("Debugger ... Non-Tamper!");
    }

    public static void checksumTamper() {
        sendResult("Checksum ... Tamper!");
    }

    public static void checksumNonTamper() {
        sendResult("Checksum ... Non-Tamper!");
    }
}
