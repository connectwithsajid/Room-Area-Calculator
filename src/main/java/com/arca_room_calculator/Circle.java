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

public class Circle extends AppCompatActivity {

    private TextView Radius;
    private TextView Height;
    private TextView Area;
    private ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);

        Radius = findViewById(R.id.Radius);
        Height = findViewById(R.id.Height);
        Area = findViewById(R.id.Area_Circle);

        pbar = findViewById(R.id.Pbar);
        pbar.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                FirebaseDatabase.getInstance().getReference().child("Raspberry Pi").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Radius.setText(dataSnapshot.child("Radius").getValue(String.class).concat("cm"));
                        Height.setText(dataSnapshot.child("Height").getValue(String.class).concat("cm"));
                        Area.setText(dataSnapshot.child("Area").getValue(String.class));
                        pbar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }, 10000);


    }
}
