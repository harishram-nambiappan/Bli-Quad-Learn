package com.harishram.bli_quad_learn;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Materials extends AppCompatActivity {

    TextView header;
    LinearLayout ll;
    Bundle bun;
    StorageReference sr;
    DatabaseReference dbr;
    String res,course,ref,type;
    String[] resa;
    ArrayList<String> mat;
    ArrayList<String> bres;
    SpeechRecognizer spr;
    Intent ri;
    String msg;
    String nlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materials);
        getSupportActionBar().hide();
        bun = getIntent().getBundleExtra("User");
        course = bun.getString("course");
        nlist = "";
        ll = (LinearLayout) findViewById(R.id.ref_ll);
        header = (TextView) findViewById(R.id.textView9);
        sr = FirebaseStorage.getInstance().getReference();
        dbr = FirebaseDatabase.getInstance().getReference();
        System.out.println(course);
        mat = new ArrayList<String>();
        res = bun.getString("Query");
        resa = res.split(" ");
        if(resa.length == 6){
            if((resa[1].equals("Video")||resa[1].equals("video"))&&(resa[2].equals("Lectures")||resa[2].equals("lectures"))){
                header.setText("VIDEO LECTURES");
                type = "Video Lectures";
                if((resa[4].equals("Course")||resa[4].equals("course"))&&(resa[5].equals("Materials")||resa[5].equals("materials"))){
                    ref = "Course Materials";
                    sr.child(course).child("Video Lectures").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>(){

                        @Override
                        public void onSuccess(ListResult listResult) {
                            List<StorageReference> srl = listResult.getItems();
                            for(int i=0;i<srl.size();i++){
                                System.out.println(srl.get(i).getName());
                                TextView reft = new TextView(getApplicationContext());
                                mat.add(srl.get(i).getName());
                                reft.setText(srl.get(i).getName());
                                reft.setTypeface(null, Typeface.BOLD);
                                reft.setTextSize(18f);
                                ll.addView(reft);
                            }
                        }
                    });
                }
                //else if((resa[4].equals("Personal")||resa[4].equals("personal"))&&(resa[5].equals("References")||resa[5].equals("references"))){

                //}
            }
        }
        else if(resa.length == 5){
            if(resa[1].equals("Notes")||resa[1].equals("notes")){
                header.setText("NOTES");
                Thread req_comm = new Thread(new Runnable(){

                    @Override
                    public void run() {
                        try {
                            Socket sock = new Socket(bun.getString("Address"),2700);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                            bw.write(course);
                            bw.flush();
                            BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                            nlist = br.readLine();
                            sock.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                req_comm.start();
                try {
                    req_comm.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(nlist);
                String[] nla = nlist.split("/");
                for(int i=0;i<nla.length;i++){
                    TextView reft = new TextView(getApplicationContext());
                    mat.add(nla[i]);
                    reft.setText(nla[i]);
                    reft.setTypeface(null,Typeface.BOLD);
                    reft.setTextSize(18f);
                    ll.addView(reft);
                }
            }
        }
        spr = SpeechRecognizer.createSpeechRecognizer(this);
        ri = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        ri.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        spr.setRecognitionListener(new RecognitionListener() {

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
                bres = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                System.out.println(bres.get(0));
                String[] bresa = bres.get(0).split(" ");
                if (bresa.length == 1) {
                    if (bresa[0].equals("Back") || bresa[0].equals("back")) {
                        Bundle mbun = new Bundle();
                        mbun.putString("Name",bun.getString("Name"));
                        mbun.putString("Address",bun.getString("Address"));
                        mbun.putString("course",bun.getString("course"));
                        Intent logout = new Intent(getApplicationContext(), Menu_2.class);
                        logout.putExtra("User",mbun);
                        startActivity(logout);
                    }
                    else{
                        spr.startListening(ri);
                    }
                } else if (bresa.length == 2) {
                    int id = -1;
                    if (bresa[0].equals("Display") || bresa[0].equals("display")) {
                        if (bresa[1].equals("One") || bresa[1].equals("one") || bresa[1].equals("1")) {
                            id = 0;
                        } else if (bresa[1].equals("Two") || bresa[1].equals("two") || bresa[1].equals("2") || bresa[1].equals("to")) {
                            id = 1;
                        } else if (bresa[1].equals("Three") || bresa[1].equals("three") || bresa[1].equals("3") || bresa[1].equals("tree")) {
                            id = 2;
                        }
                    }
                    if(id!=-1){
                        String fname = mat.get(id);
                        if(header.getText().toString().equals("VIDEO LECTURES")) {
                            msg = course + "/Video Lectures/" + fname;
                            dbr.child(bun.getString("Name")).child("display").setValue(msg);
                            spr.startListening(ri);
                            Intent menu2 = new Intent(getApplicationContext(),Menu_2.class);
                            Bundle mbun = new Bundle();
                            mbun.putString("Name",bun.getString("Name"));
                            mbun.putString("Address",bun.getString("Address"));
                            mbun.putString("course",bun.getString("course"));
                            menu2.putExtra("User",mbun);
                            startActivity(menu2);
                        }
                        /*Thread disp_command = new Thread(new Runnable(){

                            @Override
                            public void run() {
                                try {
                                    Socket sock = new Socket(bun.getString("Address"),2600);
                                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                                    bw.write(msg);
                                    bw.flush();
                                    bw.close();
                                    sock.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        disp_command.start();*/
                        /*bun.putString("path",ref+"/"+type+"/"+fname);
                        Intent vs_intent = new Intent(getApplicationContext(),VideoScreen.class);
                        vs_intent.putExtra("User",bun);
                        startActivity(vs_intent);*/

                        else if(header.getText().toString().equals("NOTES")){
                            msg = course+"/"+fname;
                            bun.putString("path",msg);
                            Intent vn_intent = new Intent(getApplicationContext(),NotesDisplay.class);
                            vn_intent.putExtra("User",bun);
                            startActivity(vn_intent);
                        }
                    }
                    else{
                        spr.startListening(ri);
                    }
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