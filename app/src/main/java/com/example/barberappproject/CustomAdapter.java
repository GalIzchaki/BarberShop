package com.example.barberappproject;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>  {

    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String ADMIN_EMAIL = "izchakig@gmail.com";


    private final ArrayList<DataModel> dataSet;

    public CustomAdapter(ArrayList<DataModel> dataSet) {

        this.dataSet = dataSet;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView startDate;
        TextView userEmail;
        DataModel dm;

        public MyViewHolder (View itemView, DataModel dm )
        {
            super(itemView);
            this.dm = dm;
            startDate = ( TextView) itemView.findViewById(R.id.adapter_startDate);
            userEmail = ( TextView) itemView.findViewById(R.id.adapter_userEmail);

            itemView.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                    if (currentFirebaseUser.getEmail().equals(ADMIN_EMAIL)) {

                        db.collection("appointments").document(dm.getDocId()).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });


                    } else {
                        db.collection("appointments").document(dm.getDocId()).update(
                                "userEmail", null);
                    }


                }
            });

            itemView.findViewById(R.id.btn_assign).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

                    db.collection("appointments").document(dm.getDocId()).update(
                                    "userEmail", currentFirebaseUser.getEmail());

                }
            });
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext() ).inflate(R.layout.activity_adaptor_item , parent ,false);

        MyViewHolder myViewHolder = new MyViewHolder(view, dataSet.get(i));

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder,  int listPosition) {
        TextView startDate = viewHolder.startDate;
        String dateString = dataSet.get(listPosition).startDate == null ? "Oopsie..." :dataSet.get(listPosition).startDate.toString();
        startDate.setText(dateString);

        TextView userEmail = viewHolder.userEmail;
        String emailString = dataSet.get(listPosition).userEmail == null ? "Not assigned" : dataSet.get(listPosition).userEmail.toString();
        userEmail.setText(emailString);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
