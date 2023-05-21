package com.example.barberappproject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class HomeActivity extends AppCompatActivity {
    private CardView profile_card;
    private CardView showBooking_card;

    private ArrayList<DataModel> dataSet;

    private RecyclerView recycleView;
    private LinearLayoutManager layoutManager;
    private CustomAdapter addapter;


    private CardView whatsapp_card;
    private CardView instagram_card;
    private CardView call_card;
    private CardView map;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dataSet = new ArrayList<DataModel>();
        db = FirebaseFirestore.getInstance();

        db.collection("appointments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Log.d("STAM", doc.getId() + " || " + doc.getData());
                        doc.get("is_available");
                    }
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                    int i = 0;
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Map m = doc.getData();
                        if (((String) m.get("userEmail") == null)) {
                            dataSet.add(new DataModel(
                                    ((Timestamp) m.get("startDate")).toDate()
                                    , (long) m.get("duration")
                                    , (String) m.get("userEmail")
                                    , (String) doc.getId()
                                    , i++
                            ));
                        }
                    }
                    recycleView = (RecyclerView) findViewById(R.id.my_recycler_view);
                    layoutManager = new LinearLayoutManager(HomeActivity.this); // new GridLayoutManager
                    recycleView.setLayoutManager(layoutManager);

                    recycleView.setItemAnimator(new DefaultItemAnimator());

                    addapter = new CustomAdapter(dataSet);
                    recycleView.setAdapter(addapter);
                }
           }
        });

        profile_card = (CardView) findViewById(R.id.card_view_Profile);
        profile_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
            }
        });



        showBooking_card = (CardView) findViewById(R.id.card_view_showbooking);
        showBooking_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ShowBookingActivity.class));
            }
        });




        whatsapp_card = (CardView)findViewById(R.id.card_view_whatsapp);
        whatsapp_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editText_mobile = "548083353";
                String editText_msg = "hii";
                String mobileNumber = editText_mobile.toString();
                String message = editText_msg.toString();

                boolean installed = appInstallOrNot("com.whatsapp");
                if (installed) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+972" + mobileNumber + "&text=" + message));
                    startActivity(intent);
                }
                else {
                    Toast.makeText(HomeActivity.this, "whatsapp not installed on your device", Toast.LENGTH_SHORT);}
            }
        });


        instagram_card=(CardView) findViewById(R.id.card_view_instagram);
        instagram_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri=Uri.parse("https://www.instagram.com/perchik_barber_shop/");//https://www.instagram.com/gal_izchaki/
                Intent instagram=new Intent(Intent.ACTION_VIEW,uri);
                instagram.setPackage("com.instagram.android");
                try{
                    startActivity(instagram);
                }catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.instagram.com/perchik_barber_shop/")));
                }
            }
        });

        call_card=(CardView) findViewById(R.id.card_view_contact);
        call_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone= "0547718173";
                String s="tel:"+phone;
                Intent intent=new Intent(Intent.ACTION_CALL);
                intent.setData((Uri.parse(s)));
                startActivity(intent);

            }
        });
        map=(CardView) findViewById(R.id.card_view_map);
        map.setOnClickListener(new View.OnClickListener(){
            String sSource="home";
            String sDestination="Barbar_Shop";
            @Override
            public void onClick(View view) {
                DisplayTrack(sSource,sDestination);
            }

        });
    }

    private  void DisplayTrack(String sSource,String sDestination){

        //if the device does not have map installed, then redirect it to play store
        try{
            //when google map is installed
            Uri uri=Uri.parse("https://www.google.co.in/maps/dir/"+sSource+"/"+sDestination);
            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (ActivityNotFoundException e)
        {
            //when google map is not installed
            Uri uri=Uri.parse(("//https://play.google.com/store/apps/details?id=com.google.android.apps.maps"));
            Intent intent= new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }


    private boolean appInstallOrNot(String url) {
        PackageManager packageManager = getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;

        } catch (PackageManager.NameNotFoundException e) {

            app_installed = false;
        }
        return app_installed;
    }
}
