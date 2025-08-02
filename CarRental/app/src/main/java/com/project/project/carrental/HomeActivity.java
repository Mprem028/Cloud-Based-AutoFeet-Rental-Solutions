package com.project.project.carrental;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.project.project.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeActivity extends AppCompatActivity {


    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.button1)
    Button btnUploadCar;

    @BindView(R.id.button2 )
    Button btnViewCars;



    private Unbinder unbinderknife;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carrental_activity_home);

        unbinderknife = ButterKnife.bind(this);

        btnUploadCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PostCarActivity.class);
                startActivity(intent);
            }
        });

        btnViewCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ViewCarRentalActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinderknife.unbind();

    }

}
