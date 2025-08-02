package com.project.project.globalnest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.project.project.R;
import com.project.project.Utils;
import com.project.project.globalnest.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;

public class QRGeneratorTransaction extends Activity {

    ImageView imageView;
    TextView textView;
    static String text;
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcodetransaction);

        imageView = (ImageView) findViewById(R.id.qrcodeview);
        textView = (TextView) findViewById(R.id.textview);

        Button btnSend = (Button) findViewById(R.id.send);
        EditText etAmount = (EditText) findViewById(R.id.amount);

        text = getIntent().getStringExtra("text");

        CreateQRCode(text);
        textView.setText("\nDetails\n" + text);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etAmount.getText().toString().equals("")){
                    Utils.showToast(QRGeneratorTransaction.this, "Enter amount");
                }else{
                    Utils.showToast(QRGeneratorTransaction.this, "Amount sent successfullly");
                    finish();
                }
            }
        });


    }


    public Bitmap CreateQRCode(String qrText) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(qrText, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            imageView.setImageBitmap(bmp);
            SaveImage(bmp);
            return bmp;


        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void SaveImage(Bitmap finalBitmap) {

//        String root = Environment.getExternalStorageDirectory().toString();
        String root = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString();
        File myDir = new File(root + "/project");
        myDir.mkdirs();

        String fname = "QR-"   + text + ".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
