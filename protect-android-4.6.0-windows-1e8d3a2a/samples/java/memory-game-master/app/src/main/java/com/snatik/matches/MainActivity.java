package com.snatik.matches;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AlertDialog;

import android.widget.ImageView;

import com.snatik.matches.common.Shared;
import com.snatik.matches.engine.Engine;
import com.snatik.matches.engine.ScreenController;
import com.snatik.matches.engine.ScreenController.Screen;
import com.snatik.matches.events.EventBus;
import com.snatik.matches.events.ui.BackGameEvent;
import com.snatik.matches.ui.PopupManager;
import com.snatik.matches.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends FragmentActivity {

    private ImageView mBackgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Shared.context = getApplicationContext();
        Shared.engine = Engine.getInstance();
        Shared.eventBus = EventBus.getInstance();

        setContentView(R.layout.activity_main);
        mBackgroundImage = (ImageView) findViewById(R.id.background_image);

        Shared.activity = this;
        Shared.engine.start();
        Shared.engine.setBackgroundImageView(mBackgroundImage);

        // set background
        setBackgroundImage();

        // set menu
        ScreenController.getInstance().openScreen(Screen.MENU);

        loadTextFile();
    }

    @Override
    protected void onDestroy() {
        Shared.engine.stop();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (PopupManager.isShown()) {
            PopupManager.closePopup();
            if (ScreenController.getLastScreen() == Screen.GAME) {
                Shared.eventBus.notify(new BackGameEvent());
            }
        } else if (ScreenController.getInstance().onBack()) {
            super.onBackPressed();
        }
    }

    private void setBackgroundImage() {
        Bitmap bitmap = Utils.scaleDown(R.drawable.background, Utils.screenWidth(), Utils.screenHeight());
        bitmap = Utils.crop(bitmap, Utils.screenHeight(), Utils.screenWidth());
        bitmap = Utils.downscaleBitmap(bitmap, 2);
        mBackgroundImage.setImageBitmap(bitmap);
    }

    private void loadTextFile() {
        try {
            AssetFileDescriptor fileDescriptor = getAssets().openFd("textfile.txt");

            StringBuilder message = new StringBuilder("File length: ");
            message.append(fileDescriptor.getLength()).append("\n");

            // Test open
            InputStream is = getAssets().open("textfile.txt");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = is.read();
            }

            is.close();

            message.append("File content: ");
            message.append(byteArrayOutputStream.toString());

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_MinWidth);
            builder.setTitle("Resource encryption")
                    .setMessage(message.toString())
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

            builder.create().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
