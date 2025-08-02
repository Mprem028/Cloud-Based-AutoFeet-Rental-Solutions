package com.project.project.carrental;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.project.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SelectDriveActivity extends AppCompatActivity {


    private final String TAG = this.getClass().getSimpleName();



    @BindView(R.id.button1)
    Button btnBookDrive;

    @BindView(R.id.button2)
    Button btnSelfDrive;

    @BindView(R.id.driverDetails)
    TextView tvDriverDetails;

    private Unbinder unbinderknife;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrental_selectdrive);

        unbinderknife = ButterKnife.bind(this);


        btnBookDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDriverDetails.setText("DriveName: Kishore\n8767666778");
            }
        });

        btnSelfDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinderknife.unbind();

    }

}
