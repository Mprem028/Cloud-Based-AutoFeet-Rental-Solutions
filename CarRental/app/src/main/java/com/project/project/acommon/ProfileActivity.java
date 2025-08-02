package com.project.project.acommon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.project.R;
import com.project.project.acommon.data.Users;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProfileActivity extends AppCompatActivity {


    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.textView)
    TextView tvTextView;

    @BindView(R.id.editdata)
    Button btnEditData;

    private Unbinder unbinderknife;

    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        unbinderknife = ButterKnife.bind(this);

        btnEditData.setVisibility(View.GONE);
        String name = LoginActivity.userObject.getString(Users.name);
        String phone = LoginActivity.userObject.getString(Users.phone);

        tvTextView.setText(
                        "Name: "+name
                        + "\nPhone: " + phone
                        + "\nAddress: "+LoginActivity.userObject.getString(Users.address)

        );


        btnEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, RegisterActivity.class);
                intent.putExtra("isEdit", true);
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
