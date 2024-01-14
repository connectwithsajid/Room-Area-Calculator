package com.arca_room_calculator;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private ProgressBar StartingPiProgressBar;
    private TextView StartingPi;
    private Button StartPiBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StartingPiProgressBar = findViewById(R.id.StartingPiProgressBar);
        StartingPi = findViewById(R.id.StartingPi);
        StartPiBtn = findViewById(R.id.SRaspberryPi);

        StartPiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartPiBtn.setEnabled(false);
                StartingPi.setText("Starting Raspberry Pi");
                StartingPiProgressBar.setVisibility(View.VISIBLE);
                blink();
                SendCommandToStartRaspberryPi();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after 30 seconds
                        WaitForRaspberryPiResponse();
                    }
                }, 30000);
            }
        });
    }

    private void WaitForRaspberryPiResponse() {

        DatabaseReference mRef = FirebaseDatabase.getInstance()
                .getReference().child("Raspberry Pi").child("Status");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                assert value != null;
                if (value.matches("Started Raspberry Pi Successfully")) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                           getPrediction();
                        }
                    }, 10000);
                }
                else{
                    StartPiBtn.setEnabled(true);
                    StartingPiProgressBar.setVisibility(View.INVISIBLE);
                    StartingPi.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(null, "Failed to read value.", databaseError.toException());
            }
            });
    }

    private void getPrediction() {

        FirebaseDatabase.getInstance().getReference().child("Raspberry Pi").child("Prediction")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(String.class).matches("Rectangle")){
                    Intent intent = new Intent(getApplicationContext(),Rectangle.class);
                    startActivity(intent);
                    finish();
                }else if(dataSnapshot.getValue(String.class).matches("Circle")){
                    //Open Circle
                    Intent nIntent = new Intent(getApplicationContext(),Circle.class);
                    startActivity(nIntent);
                    finish();
                }else{
                    Log.e("Error :","Some Error Has Occured!!!!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendCommandToStartRaspberryPi() {

        DatabaseReference mRef = FirebaseDatabase.getInstance()
                .getReference().child("Raspberry Pi").child("Status");
        mRef.setValue("Start Raspberry Pi");

    }

    private void blink(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 1000;    //in milissegunds
                try{Thread.sleep(timeToBlink);}catch (Exception e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView txt = findViewById(R.id.StartingPi);
                        if(txt.getVisibility() == View.VISIBLE){
                            txt.setVisibility(View.INVISIBLE);
                        }else{
                            txt.setVisibility(View.VISIBLE);
                        }
                        blink();
                    }
                });
            }
        }).start();
    }
}
