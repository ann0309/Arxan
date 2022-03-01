package pet.ca.simplearxan;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

//回傳動態偵測結果
public class MyHandler extends Handler {
    private WeakReference<BaseFragment> reference;

    MyHandler(BaseFragment fragment) {
        reference = new WeakReference<>(fragment);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        BaseFragment fragment = reference.get();
        if (fragment != null) {
            fragment.setResult(msg);
        }
    }
}
