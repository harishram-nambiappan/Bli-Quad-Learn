package com.harishram.bli_quad_learn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Intro extends AppCompatActivity {

    TextView tv;
    SpeechRecognizer sr;
    int state_flag;
    ArrayList<String> res;
    DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getSupportActionBar().hide();
        tv = (TextView) findViewById(R.id.textView);
        tv.setText("Welcome, Say your name");
        state_flag = 0;
        dbr = FirebaseDatabase.getInstance().getReference();
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
                sr = SpeechRecognizer.createSpeechRecognizer(this);
                Intent srintent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                srintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                srintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                sr.setRecognitionListener(new RecognitionListener() {

                    @Override
                    public void onReadyForSpeech(Bundle params) {

                    }

                    @Override
                    public void onBeginningOfSpeech() {

                    }

                    @Override
                    public void onRmsChanged(float rmsdB) {

                    }

                    @Override
                    public void onBufferReceived(byte[] buffer) {

                    }

                    @Override
                    public void onEndOfSpeech() {
                        sr.stopListening();
                    }

                    @Override
                    public void onError(int error) {

                    }

                    @Override
                    public void onResults(Bundle results) {
                        res = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                        if (res.size() > 0) {
                            dbr.child(res.get(0)).addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    try {
                                        User user = snapshot.getValue(User.class);
                                        if (user != null) {
                                            System.out.println(user.getName());
                                            Intent menu_intent = new Intent(getApplicationContext(), Menu.class);
                                            Bundle bun = new Bundle();
                                            bun.putString("Name", user.getName());
                                            bun.putString("Address", user.getAddress());
                                            menu_intent.putExtra("User", bun);
                                            startActivity(menu_intent);
                                        } else {
                                            tv.setText("Credentials are invalid. Try again");
                                            sr.startListening(srintent);
                                        }

                                    } catch (Exception e) {
                                        //sr.startListening(srintent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            sr.startListening(srintent);
                        }


                    }

                    @Override
                    public void onPartialResults(Bundle partialResults) {

                    }

                    @Override
                    public void onEvent(int eventType, Bundle params) {

                    }
                });
                sr.startListening(srintent);

        }
        else{
            Intent perm_intent = new Intent(getApplicationContext(),AudioPermission.class);
            startActivity(perm_intent);
        }

    }
}