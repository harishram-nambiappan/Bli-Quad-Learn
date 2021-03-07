package com.harishram.bli_quad_learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TakeNotes extends AppCompatActivity {

    SpeechRecognizer spr;
    Bundle bun;
    EditText note_txt;
    Intent ri;
    DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_notes);
        getSupportActionBar().hide();
        note_txt = (EditText) findViewById(R.id.editTextTextMultiLine);
        note_txt.setText("");
        bun = getIntent().getBundleExtra("User");
        dbr = FirebaseDatabase.getInstance().getReference();
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
                 String msg = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
                 if(msg.equals("Back")||msg.equals("back")){
                      Intent back_intent = new Intent(getApplicationContext(),Menu_2.class);
                      back_intent.putExtra("User",bun);
                      startActivity(back_intent);
                 }
                 else if(msg.split(" ").length == 2){
                     String[] msgs = msg.split(" ");
                     if((msgs[0].equals("Save")||msgs[0].equals("save"))&&(msgs[1].equals("Notes")||msgs[1].equals("notes"))){
                         Thread disp_command = new Thread(new Runnable(){

                             @Override
                             public void run() {
                                 try {
                                     Socket sock = new Socket(bun.getString("Address"),2600);
                                     BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                                     bw.write(bun.getString("course")+"/"+note_txt.getText().toString());
                                     //bw.write(note_txt.getText().toString());
                                     bw.flush();
                                     bw.close();
                                     sock.close();
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }

                             }
                         });
                         disp_command.start();
                         Intent back_intent = new Intent(getApplicationContext(),Menu_2.class);
                         back_intent.putExtra("User",bun);
                         startActivity(back_intent);
                     }
                     else if((msgs[0].equals("Stop")||msgs[0].equals("stop"))&&(msgs[1].equals("Display")||msgs[1].equals("display"))){
                         dbr.child(bun.getString("Name")).child("display").setValue("off");
                         spr.startListening(ri);
                     }
                 }
                 else{
                     note_txt.setText(msg);
                     spr.startListening(ri);
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