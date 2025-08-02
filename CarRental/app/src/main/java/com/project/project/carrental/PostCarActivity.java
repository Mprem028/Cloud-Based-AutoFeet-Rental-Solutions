package com.project.project.carrental;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.project.project.R;
import com.project.project.Utils;
import com.project.project.acommon.LoginActivity;
import com.project.project.carrental.data.CarRental;
import com.project.project.kisanmitra.OneShotPreviewActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PostCarActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText etTitle;

    @BindView(R.id.editText2)
    EditText etRcNumber;

    @BindView(R.id.editText3)
    EditText etDescription;

    @BindView(R.id.editText4)
    EditText etPrice;

    @BindView(R.id.submit)
    TextView btnSubmit;

    @BindView(R.id.btnclick)
    Button btnClick;

    @BindView(R.id.imageview)
    ImageView ivImageView;

    @BindView(R.id.spinner)
    Spinner spinnerCarRentalType;

    String[] CarRentalTypes = {"Premium", "Mid Range"};

    private Unbinder unbinderknife;
    private String TAG = "RegisterActivity";
    private File file;

    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_carrental_activity);

        unbinderknife = ButterKnife.bind(this);

        ivImageView.setImageResource(android.R.drawable.ic_menu_upload);


        ArrayAdapter<String> aa1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CarRentalTypes);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCarRentalType.setAdapter(aa1);


        spinnerCarRentalType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedType = CarRentalTypes[i].toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ivImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                launchCamera(100);
                Intent intent = new Intent(PostCarActivity.this, OneShotPreviewActivity.class);
                startActivity(intent);
            }
        });


        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Utils.showToast(getApplicationContext(), "button clikc");

                String name = etTitle.getText().toString();
                String descrption = etDescription.getText().toString();
                String rcNumber = etRcNumber.getText().toString();
                String price = etPrice.getText().toString();

                if (name.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter name");
                } else if (rcNumber.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter rcNumber");
                } else if (descrption.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter description");
                } else if (OneShotPreviewActivity.filePath.equals("")) {
                    Utils.showToast(getApplicationContext(), "Capture image");
                } else if (price.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter Price");
                } else {
                    saveData(name, rcNumber, descrption, OneShotPreviewActivity.fileURL, price);
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast(getApplicationContext(), "Submit clicked");
                Log.i(TAG, "Submit clicked");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinderknife.unbind();
    }


    private static File getOutputMediaFilepath() {
        File mediaStorageDir = new File("/sdCarRentald/cameraface/");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void launchCamera(int requestcode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = getOutputMediaFilepath();
        Log.i(TAG, "File path: " + file.getAbsolutePath());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            file = getOutputMediaFilepath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        } else {
            file = getOutputMediaFilepath();
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(intent, requestcode);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OneShotPreviewActivity.filePath.equals("")) {
            ivImageView.setImageURI(Uri.fromFile(new File(OneShotPreviewActivity.filePath)));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {


            }
        }
    }
    String selectedType = "";
    public void saveData(String name, String rcNumber, String description, String url, String price) {

        File file = new File(url);
        ParseFile parseFile = new ParseFile(file);

        ParseObject parseObject = new ParseObject(CarRental.class.getSimpleName());
        parseObject.put(CarRental.name, name);
        parseObject.put(CarRental.rcNumber, rcNumber);
        parseObject.put(CarRental.description, description);
        parseObject.put(CarRental.image, url);
//        parseObject.put(CarRental.image, parseFile);
        parseObject.put(CarRental.location, LoginActivity.address);
        parseObject.put(CarRental.username, LoginActivity.username);
        parseObject.put(CarRental.phone, LoginActivity.phone);
        parseObject.put(CarRental.price, price);
        parseObject.put(CarRental.type, selectedType);

        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Utils.showToast(getApplicationContext(), "Your post added successfully");
                    finish();
                } else {
                    Utils.showToast(getApplicationContext(), "Your post added error: " + e.getMessage());
                    finish();
                }
            }
        });

    }
}
