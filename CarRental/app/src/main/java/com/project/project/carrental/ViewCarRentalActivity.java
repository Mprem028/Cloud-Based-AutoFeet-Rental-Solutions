package com.project.project.carrental;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.project.project.R;
import com.project.project.Utils;
import com.project.project.acommon.LoginActivity;
import com.project.project.carrental.data.CarRental;
import com.project.project.fooddonor.data.DonorPost;
import com.project.project.globalnest.QRGeneratorTransaction;
import com.project.project.kisanmitra.OLXPost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ViewCarRentalActivity extends AppCompatActivity {


    private final String TAG = "MainActivity";

    @BindView(R.id.listview)
    ListView listView;

    @BindView(R.id.viewcart)
    Button btnViewCart;

    @BindView(R.id.offers)
    Button btnOffers;

    @BindView(R.id.buyitems)
    Button btnBuyItems;

    private Unbinder unbinderknife;

    static String name = "";

    private int CAMERA_REQUEST = 100;

    String filePath = "";

    boolean isUpdate;

    Map<String, List<String>> map = new HashMap<>();

    List<String> malls = new ArrayList<>();


    int totalAmount = 0;

    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kisan_items_list);

        unbinderknife = ButterKnife.bind(this);

        type = getIntent().getStringExtra("type");
        btnOffers.setVisibility(View.GONE);
        btnViewCart.setVisibility(View.GONE);
        btnBuyItems.setVisibility(View.GONE);

        fetchData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinderknife.unbind();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

        }
    }

    List<ParseObject> parseObjectList = new ArrayList<>();

    public void fetchData() {

        Utils.showToast(getApplicationContext(), "Please Wait....");

        ParseQuery<ParseObject> query = ParseQuery.getQuery(CarRental.class.getSimpleName());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    if (objects.size() > 0) {
                        parseObjectList = objects;
                        if(parseObjectList.size() > 0){
                            Utils.showToast(ViewCarRentalActivity.this, "Items available");
                        }else{
                            Utils.showToast(ViewCarRentalActivity.this, "Items not available");
                        }

                        CustomAdapter ca = new CustomAdapter(ViewCarRentalActivity.this, parseObjectList);
                        listView.setAdapter(ca);
                    } else {
                        Utils.showToast(getApplicationContext(), "No items available");
                    }
                } else {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "fetch error: " + e.getMessage());
                }
            }
        });
    }
    List<ParseObject> fileteredParseObjects = new ArrayList<>();
    private void filterByExpiry(){
        for(ParseObject po : parseObjectList){
            Date date = po.getCreatedAt();
            long createdTimeInMillis = date.getTime();
            String expiry = po.getString(DonorPost.expiry);
            if(expiry != null && !(expiry.equals(""))){
                int expiryInmillis =  Integer.parseInt(expiry) * 60 * 1000;

                long timeLapsed = System.currentTimeMillis() - createdTimeInMillis;
                if(timeLapsed <= expiryInmillis){
                    fileteredParseObjects.add(po);
                }
            }else{
                fileteredParseObjects.add(po);
            }
        }
    }

    public class CustomAdapter extends BaseAdapter {

        List<ParseObject> itemsList;

        Context mContext;

        ViewHolder viewHolder;

        public CustomAdapter(Context mContext, List<ParseObject> itemslist) {
            this.mContext = mContext;
            this.itemsList = itemslist;
        }

        @Override
        public int getCount() {
            return itemsList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final View result;

            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.items_list_rows, parent, false);

                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.textview);
                viewHolder.tvQuantity = (TextView) convertView.findViewById(R.id.quantity);
                viewHolder.btnAccept = (Button) convertView.findViewById(R.id.accept);
                viewHolder.btnOpenMaps = (Button) convertView.findViewById(R.id.btnMaps);
                viewHolder.btnDunzo = (Button) convertView.findViewById(R.id.btnDunzo);
                result = convertView;
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result = convertView;
            }

            final ParseObject item = itemsList.get(position);

            if(item.getBoolean(DonorPost.assigned)){
                viewHolder.btnAccept.setEnabled(false);
            }
            viewHolder.textView.setText(
                    "Type: " + item.getString(CarRental.type)+
                    "\n" + item.getString(CarRental.name)
                            + "\nPrice: " + item.getString(CarRental.price) + "\nDesc:  " +
                    item.getString(CarRental.description) + "\nPhone: " + item.getString(CarRental.phone)
            );

            if (item.getString(OLXPost.image) != null) {
                Picasso.with(mContext).load(item.getString(OLXPost.image)).into(viewHolder.imageView);
            }

            viewHolder.btnAccept.setText("Book SelfDrive");
            viewHolder.btnDunzo.setText("Book Driver");

//            viewHolder.btnDunzo.setVisibility(View.GONE);
            viewHolder.btnOpenMaps.setVisibility(View.GONE);

            viewHolder.btnOpenMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.openMaps(item.getString(DonorPost.location), mContext);
                }
            });

            viewHolder.btnDunzo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.put(CarRental.bookingStatus, true);
                    item.put(CarRental.bookedBy, LoginActivity.username);

                    item.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                notifyDataSetChanged();
                                Intent intent = new Intent(mContext, QRGeneratorTransaction.class);
                                intent.putExtra("text", ""+new Random().nextInt(9999999));
                                startActivity(intent);
                                finish();
                            }else{
                                Utils.showToast(getApplicationContext(), "Booking error: "+e.getMessage());
                            }
                        }
                    });
                }
            });

            viewHolder.btnAccept.setVisibility(View.VISIBLE);

            viewHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAlertDialog(item);
                }
            });

            return result;
        }

        public class ViewHolder {
            TextView textView;
            ImageView imageView;
            Button btnAccept;

            Button btnOpenMaps;

            Button btnDunzo;
            TextView tvQuantity;
        }
    }


    public void showAlertDialog(ParseObject item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewCarRentalActivity.this);

        builder.setTitle("Acknowledgement");

        builder.setMessage("The car rental services provided through this application are subject to availability and depend on the policies of the respective service providers. The app acts solely as a platform to connect users with car rental providers and does not own, maintain, or operate any vehicles. Users are responsible for reviewing and agreeing to the rental terms, including pricing, mileage limits, and insurance coverage, directly with the provider. The app is not liable for any damages, accidents, or violations occurring during the rental period or for any disputes between users and service providers. All personal information is handled in compliance with the app’s privacy policy and applicable data protection laws. Users are encouraged to verify charges and terms before confirming bookings. The app also does not guarantee uninterrupted service due to external factors like traffic, weather, or provider delays. By using this application, users acknowledge and agree to the outlined terms and conditions.");


        //Button One : Yes
        builder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.put(CarRental.bookingStatus, true);
                item.put(CarRental.bookedBy, LoginActivity.username);
                String phone = item.getString(DonorPost.phone);
                Utils.sendSMS(phone, "Dear User, your car rental has been accepted by "+LoginActivity.username + "\nPhone: "+LoginActivity.phone);

                item.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
//                                Utils.showToast(getApplicationContext(), "Booking Successful");
                            Intent intent = new Intent(ViewCarRentalActivity.this, QRGeneratorTransaction.class);
                            intent.putExtra("text", "" + new Random().nextInt(9999999));
                            startActivity(intent);
                            finish();
                        }else{
                            Utils.showToast(getApplicationContext(), "Booking error: "+e.getMessage());
                        }
                    }
                });
            }
        });


        //Button Two : No
        builder.setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog diag = builder.create();
        diag.show();
    }
}
