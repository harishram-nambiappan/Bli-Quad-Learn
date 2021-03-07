package com.harishram.bli_quad_learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Menu_2 extends AppCompatActivity {

    Bundle bun;
    SpeechRecognizer spr;
    Intent ri;
    ArrayList<String> res;
    TextView cname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_2);
        getSupportActionBar().hide();
        cname = (TextView) findViewById(R.id.textView5);
        bun = getIntent().getBundleExtra("User");
        res = new ArrayList<String>();
        cname.setText(bun.getString("course"));
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
                 res = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                 System.out.println(res.get(0));
                 if(res.get(0).equals("Back")||res.get(0).equals("back")){
                     Intent menu_intent = new Intent(getApplicationContext(),Menu.class);
                     Bundle mbun = new Bundle();
                     mbun.putString("Name",bun.getString("Name"));
                     mbun.putString("Address",bun.getString("Address"));
                     menu_intent.putExtra("User",mbun);
                     startActivity(menu_intent);
                 }
                 else{
                     String[] resa = res.get(0).split(" ");
                     if(resa[0].equals("Get")||resa[0].equals("get")){
                         Intent materials = new Intent(getApplicationContext(),Materials.class);
                         bun.putString("Query",res.get(0));
                         materials.putExtra("User",bun);
                         startActivity(materials);
                     }
                     else if((resa[0].equals("Take")||resa[0].equals("take"))&&(resa[1].equals("Notes")||resa[1].equals("notes"))){
                          Intent notes_intent = new Intent(getApplicationContext(),TakeNotes.class);
                          notes_intent.putExtra("User",bun);
                          startActivity(notes_intent);
                     }
                     else{
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