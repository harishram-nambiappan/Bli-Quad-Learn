package com.harishram.bli_quad_learn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {

    FirebaseStorage fs;
    StorageReference sr;
    LinearLayout ll;
    Bundle bun;
    ArrayList<String> subl;
    SpeechRecognizer spr;
    Intent ri;
    String name;
    DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        bun = getIntent().getBundleExtra("User");
        name = bun.getString("Name");
        System.out.println(name);
        fs = FirebaseStorage.getInstance();
        sr = fs.getReference();
        dbr = FirebaseDatabase.getInstance().getReference();
        ll = (LinearLayout) findViewById(R.id.menu_ll);
        subl = new ArrayList<String>();
        /*sr.child(name).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>(){

            @Override
            public void onSuccess(ListResult listResult) {
                List<StorageReference> sub = listResult.getPrefixes();
                for(int i=0;i<sub.size();i++){
                    TextView course = new TextView(getApplicationContext());
                    course.setText(sub.get(i).getName());
                    course.setTypeface(null, Typeface.BOLD);
                    course.setTextSize(24f);
                    course.setTextColor(Color.WHITE);
                    subl.add(sub.get(i).getName());
                    ll.addView(course);
                }
            }
        });*/
        dbr.child(bun.getString("Name")).child("Courses").addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Course course = snapshot.getValue(Course.class);
                    subl.add(course.getCourse());
                    TextView sub = new TextView(getApplicationContext());
                    sub.setTypeface(null,Typeface.BOLD);
                    sub.setText(course.getCourse());
                    sub.setTextSize(24f);
                    sub.setTextColor(Color.WHITE);
                    ll.addView(sub);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        spr = SpeechRecognizer.createSpeechRecognizer(this);
        ri = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        ri.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        ri.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
        spr.setRecognitionListener(new RecognitionListener(){

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
                spr.stopListening();
            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                 ArrayList<String> res = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                 System.out.println(res.get(0));
                 String[] resa = res.get(0).split(" ");
                 if(resa.length == 1){
                     if(resa[0].equals("Logout")||resa[0].equals("logout")){
                         Intent logout = new Intent(getApplicationContext(),Intro.class);
                         startActivity(logout);
                     }
                 }
                 else if(resa.length == 2){
                     int id = -1;
                     if(res.get(0).equals("Log out")||(res.get(0).equals("log out"))){
                         Intent logout = new Intent(getApplicationContext(),Intro.class);
                         startActivity(logout);
                     }
                     else if(resa[0].equals("Select")||resa[0].equals("select")) {
                         if (resa[1].equals("One") || resa[1].equals("one") || resa[1].equals("1")) {
                                 id = 0;
                         } else if (resa[1].equals("Two") || resa[1].equals("two") || resa[1].equals("2")) {
                                 id = 1;
                         } else if (resa[1].equals("Three") || resa[1].equals("three") || resa[1].equals("3")) {
                                 id = 2;
                         }
                         if(id!=-1){
                             String cname = subl.get(id);
                             System.out.println(cname);
                             bun.putString("course",cname);
                             Intent menu2 = new Intent(getApplicationContext(),Menu_2.class);
                             menu2.putExtra("User",bun);
                             startActivity(menu2);
                         }
                         else{
                             spr.startListening(ri);
                         }
                     }
                     else if(id == -1){
                         spr.startListening(ri);
                     }

                 }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
        spr.startListening(ri);
    }
}