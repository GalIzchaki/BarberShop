package com.example.barberappproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class ShowBookingActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private ArrayList<DataModel> dataSet;
    private RecyclerView recycleView;
    private LinearLayoutManager layoutManager;
    private CustomAdapter addapter;
    private Button menu;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_booking);
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
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    int i = 0;
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Map m = doc.getData();
                        if (currentFirebaseUser.getEmail().equals((String) m.get("userEmail"))) {
                            dataSet.add(new DataModel(
                                    ((Timestamp) m.get("startDate")).toDate()
                                    , (long) m.get("duration")
                                    , (String) m.get("userEmail")
                                    , (String) doc.getId()
                                    , i++
                            ));
                        }
                    }
                    recycleView = (RecyclerView) findViewById(R.id.recyclerView);
                    layoutManager = new LinearLayoutManager(ShowBookingActivity.this); // new GridLayoutManager
                    recycleView.setLayoutManager(layoutManager);

                    recycleView.setItemAnimator(new DefaultItemAnimator());

                    addapter = new CustomAdapter(dataSet);
                    recycleView.setAdapter(addapter);
                }
            }
        });

        logout = (Button) findViewById(R.id.signOutU);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ShowBookingActivity.this, MainActivity.class));
            }
        });

        menu = (Button) findViewById(R.id.buttonMenuU);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowBookingActivity.this, HomeActivity.class));
            }
        });


        //public void funcDelete (View v){
         //   Button b = (Button) v;
        //    b.setOnClickListener(new View.OnClickListener() {
         //       @Override
         //       public void onClick(View v) {
         //           Toast.makeText(context, "Isn't deleted", Toast.LENGTH_SHORT).show();
         //           rootDatabaseref.child("booking").child("date").removeValue();
         //       }
         //   });
      //  }
    }
}




