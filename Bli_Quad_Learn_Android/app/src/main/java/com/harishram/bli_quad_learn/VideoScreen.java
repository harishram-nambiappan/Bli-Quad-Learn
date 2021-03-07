package com.harishram.bli_quad_learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class VideoScreen extends AppCompatActivity implements SurfaceHolder.Callback {

    Bundle bun;
    SurfaceView sv;
    SurfaceHolder sh;
    MediaPlayer nmp;
    String path,ref,fname;
    StorageReference sr;
    SpeechRecognizer spr;
    Intent ri;
    ArrayList<String> spres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_screen);
        getSupportActionBar().hide();
        sr = FirebaseStorage.getInstance().getReference();
        spres = new ArrayList<String>();
        bun = getIntent().getBundleExtra("User");
        path = bun.getString("path");
        ref = path.split("/")[0];
        fname = path.split("/")[1]+"/"+path.split("/")[2];
        System.out.println(fname);
        sv = (SurfaceView)findViewById(R.id.surfaceView);
        sh = sv.getHolder();
        nmp = new MediaPlayer();
        sh.addCallback(this);
        while(true){
            spr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
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
                    spres = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if(spres.get(0).equals("Back")||spres.get(0).equals("back")){
                        nmp.stop();
                        Intent back_int = new Intent(getApplicationContext(),Materials.class);
                        Bundle mbun = new Bundle();
                        mbun.putString("Name",bun.getString("Name"));
                        mbun.putString("Address",bun.getString("Address"));
                        mbun.putString("course",bun.getString("course"));
                        mbun.putString("Query",bun.getString("Query"));
                        back_int.putExtra("User",mbun);
                        startActivity(back_int);
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
        /*Thread spt = new Thread(new Runnable(){

            @Override
            public void run() {
                while(true){
                    spr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
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
                             spres = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                             if(spres.get(0).equals("Back")||spres.get(0).equals("back")){
                                 nmp.stop();
                                 Intent back_int = new Intent(getApplicationContext(),Materials.class);
                                 Bundle mbun = new Bundle();
                                 mbun.putString("Name",bun.getString("Name"));
                                 mbun.putString("Address",bun.getString("Address"));
                                 mbun.putString("course",bun.getString("course"));
                                 mbun.putString("Query",bun.getString("Query"));
                                 back_int.putExtra("User",mbun);
                                 startActivity(back_int);
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
        });*/
        //spt.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
         sr.child(bun.getString("course")).child(fname).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){

             @Override
             public void onSuccess(Uri uri) {
                 try {
                     nmp.setDataSource(getApplicationContext(),uri);
                     nmp.setDisplay(sh);
                     nmp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                         @Override
                         public void onCompletion(MediaPlayer mp) {
                              nmp.stop();
                         }
                     });
                     nmp.prepare();
                     nmp.start();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }

             }
         });
        /*File tfile = new File(System.getenv("EXTERNAL_STORAGE")+"/Videos/temp.mp4");
        sr.child(bun.getString("course")).child(fname).getFile(tfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>(){

            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                try {
                    nmp.setDataSource(System.getenv("EXTERNAL_STORAGE")+"/Videos/temp.mp4");
                    nmp.setDisplay(sh);
                    nmp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            nmp.stop();
                        }
                    });
                    nmp.prepare();
                    nmp.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}