package com.harishram.bli_quad_learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class NotesDisplay extends AppCompatActivity {

    Bundle bun;
    TextView tv;
    SpeechRecognizer spr;
    Intent ri;
    String notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_display);
        getSupportActionBar().hide();
        bun = getIntent().getBundleExtra("User");
        tv = (TextView) findViewById(R.id.textView11);
        notes = "";
        Thread view_info = new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    Socket sock = new Socket(bun.getString("Address"),2800);
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                    bw.write(bun.getString("path"));
                    bw.flush();
                    BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    notes = br.readLine();
                    sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        view_info.start();
        try {
            view_info.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tv.setText(notes);
        spr = SpeechRecognizer.createSpeechRecognizer(this);
        ri = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        ri.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        ri.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
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
                  String res = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
                  if(res.equals("Back")||res.equals("back")){
                      Bundle mbun = new Bundle();
                      mbun.putString("Name",bun.getString("Name"));
                      mbun.putString("Address",bun.getString("Address"));
                      mbun.putString("course",bun.getString("course"));
                      mbun.putString("Query",bun.getString("Query"));
                      Intent mat_intent = new Intent(getApplicationContext(),Materials.class);
                      mat_intent.putExtra("User",mbun);
                      startActivity(mat_intent);
                  }
                  else{
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