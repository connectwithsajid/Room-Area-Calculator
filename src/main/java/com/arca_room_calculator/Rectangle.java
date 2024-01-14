package com.arca_room_calculator;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Rectangle extends AppCompatActivity {

    private TextView Length;
    private TextView Breadth;
    private TextView Height;
    private TextView Area;
    private ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectangle);

        Length = findViewById(R.id.Length_Rec);
        Breadth = findViewById(R.id.Breadth_Rec);
        Height = findViewById(R.id.Height_rec);
        Area = findViewById(R.id.Area_Rec);

        pbar = findViewById(R.id.Cbar);
        pbar.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                FirebaseDatabase.getInstance().getReference().child("Raspberry Pi").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Length.setText(dataSnapshot.child("Length").getValue(String.class).concat("cm"));
                        Breadth.setText(dataSnapshot.child("Breadth").getValue(String.class).concat("cm"));
                        Height.setText(dataSnapshot.child("Height").getValue(String.class).concat("cm"));
                        Area.setText(dataSnapshot.child("Area").getValue(String.class));
                        pbar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        },10000);

    }
}
