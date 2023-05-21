package com.example.barberappproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AdminHomeActivity extends AppCompatActivity implements View.OnClickListener  {
    private Button menu;
    private Button logout;
    private RecyclerView recycleView;
    private LinearLayoutManager layoutManager;
    private CustomAdapter addapter;
    private CardView AddAppointment;
    private FirebaseFirestore db;
    private ArrayList<DataModel> dataSet;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        dataSet = new ArrayList<DataModel>();
        db = FirebaseFirestore.getInstance();

        logout = (Button) findViewById(R.id.signOutAdmin);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminHomeActivity.this,MainActivity.class));
            }
        });

        menu= (Button) findViewById(R.id.buttonMenuAdmin);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this,HomeActivity.class));
            }
        });

        db.collection("appointments").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int i = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map m = document.getData();
                            dataSet.add(new DataModel(
                                    ((Timestamp)m.get("startDate")).toDate()
                                    , (long) m.get("duration")
                                    , (String) m.get("userEmail")
                                    , (String) document.getId()
                                    , i++
                            ));
                        }
                        recycleView = (RecyclerView) findViewById(R.id.Admin_recycler_view);
                        layoutManager = new LinearLayoutManager(AdminHomeActivity.this); // new GridLayoutManager
                        recycleView.setLayoutManager(layoutManager);

                        recycleView.setItemAnimator(new DefaultItemAnimator());

                        addapter = new CustomAdapter(dataSet);
                        recycleView.setAdapter(addapter);
                }
            });

        AddAppointment = (CardView) findViewById(R.id.card_Add_Appointment);
        AddAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this,AdminCreateAppointment.class));
            }
        });

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.card_view_Profile:
                startActivity(new Intent(this, AdminCreateAppointment.class));
                break;
        }
    }
}