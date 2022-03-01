package pet.ca.simplearxan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

    //測試用第二個頁面
public class SecondActivity extends AppCompatActivity {
    private Button btn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findBtn();
    }
    private void findBtn(){
        btn = findViewById(R.id.sec_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondActivity.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

}
