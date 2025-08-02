package com.project.project.acommon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.project.R;

import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    String TAG = "SplashActivity";
    private Unbinder unbinderknife;

    public static String name = "", username, password, phone, address;

    public static Class sClass = com.project.project.droneredzone.HomeActivity.class;


    Button btnContinue, btnHome, btnAbount;
    ImageView ivLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.fooddonor_main_activity);


        btnContinue = findViewById(R.id.btncontinue);
        btnHome = (Button) findViewById(R.id.home);
        btnAbount = (Button) findViewById(R.id.about);
        ivLogo = findViewById(R.id.logo);


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnAbount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        addAnimation();

        /*btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                sClass = HomeActivity.class;
                startActivity(intent);
            }
        });*/


//        AutoPermissions.Companion.loadAllPermissions(this, 1);

//        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        String deviceID = tm.getDeviceId();
//        TextView tv = null;


//         355884057027276
//        Sridhar phone IMEI 355656102735933


//        if (!deviceID.contains("869671030448052")) {
//            tv.setText("");
//        }



        /*finish();
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        sClass = NotificationActivity.class;
        startActivity(intent);*/

       /* Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                sClass = HomeActivity.class;
                startActivity(intent);
            }
        }, 2000);*/
    }


    public void addAnimation(){
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

//Setup anim with desired properties
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
        anim.setDuration(1000); //Put desired duration per anim cycle here, in milliseconds

//Start animation
        ivLogo.startAnimation(anim);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
