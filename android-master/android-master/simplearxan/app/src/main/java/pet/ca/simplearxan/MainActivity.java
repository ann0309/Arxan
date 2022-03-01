package pet.ca.simplearxan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.WindowManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MainActivity extends AppCompatActivity {

    static Activity rootFlags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screenDetection();
        reflection();
        JNIUtils.setEnv();
        rootFlags = this;
        ViewPager pager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        pager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(pager);

    }

        // 動態偵測 跳dialog
    public static void rootDialog(){

        new AlertDialog.Builder(rootFlags).setNegativeButton("GG", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
        rootFlags.finish();
    }


    private void screenDetection(){
        //      防止螢幕擷取
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        //      螢幕覆蓋偵測#2 :當偵測到螢幕覆蓋時，直接使App無法操作
        getWindow().getDecorView().setFilterTouchesWhenObscured(true);
        System.out.println("Screen Detection complete");
    }


    private void reflection(){

        Class c = my.reflection.Employee.class;

        Field[] fields = c.getDeclaredFields();                   //取得Employee裡的成員變數

        Constructor con1 = c.getDeclaredConstructors()[1];         //取得Employee裡有參數的建構子
        Constructor con0 = c.getDeclaredConstructors()[1];         //取得Employee裡有參數的建構子

        System.out.println(Modifier.toString(c.getModifiers()));  //列印出class：Employee的修飾詞，也就是public
        System.out.println(c.getName());                          //列印出class：Employee的名稱

        try {
            String Mary = "Mary";
            String Kaoshung = "高雄";
            Object o = con1.newInstance(1,Mary,Kaoshung);    //形成物件
            //Field成員變數的實做
            String name = "name";
            Field fieldName = c.getDeclaredField(name);  //取得Employee裡的成員變數name
            fieldName.setAccessible(true);         //利用reflection機制把private改成public
            System.out.println(fieldName.get(o));  //印出物件o的name，也就是印出Mary
            fieldName.set(o, "Tom");               //把物件o裡的name，從Mary改成Tom
            System.out.println(fieldName.get(o));  //印出物件o的name，變成了Tom

            //方法的實做
            Method method = c.getMethod("getName", null);  //取得Employee裡的getName方法
            System.out.println(method.invoke(o, null));    //執行getName的方法，印出Tom

            System.out.println("PART 2");

            Object o2 = con0.newInstance(1,"Gary","Taipei");
            Field fieldName2 = c.getDeclaredField("id");  //取得Employee裡的成員變數name
            fieldName2.setAccessible(true);         //利用reflection機制把private改成public
            System.out.println(fieldName2.get(o2));  //印出物件o的name，也就是印出Mary
            fieldName2.set(o2, 2);               //把物件o裡的name，從Mary改成Tom
            System.out.println(fieldName2.get(o2));  //印出物件o的name，也就是印出Mary

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    //偵測螢幕覆蓋，判定可能有問題
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (((ev.getFlags() & MotionEvent.FLAG_WINDOW_IS_OBSCURED) == MotionEvent.FLAG_WINDOW_IS_OBSCURED)
//                || (ev.getFlags() & MotionEvent.FLAG_WINDOW_IS_PARTIALLY_OBSCURED) == MotionEvent.FLAG_WINDOW_IS_PARTIALLY_OBSCURED) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//            builder.setPositiveButton("確認覆蓋之app清單", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                    startActivity(intent);
//                }
//            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    finish();
//                }
//            }).setMessage("您的App已遭其他App覆蓋！").setCancelable(false);
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//            startActivity(intent);
//            builder.show();
//
//            return false;
//        }
//
//        getWindow().getDecorView().setFilterTouchesWhenObscured(false);
        return super.dispatchTouchEvent(ev);
    }

    private static class PageAdapter extends FragmentPagerAdapter {

        private static final String[] title = new String[]{
                "JAVA/Kotlin",
                "C/C++"
        };

        PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                return new JKFragment();
            }
            return new CCFragment();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
