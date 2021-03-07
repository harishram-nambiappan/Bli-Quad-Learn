package com.harishram.bli_quad_learn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class AudioPermission extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_permission);
        getSupportActionBar().hide();
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] results){
        if(requestCode == 1){
            if(permissions[0].equals(Manifest.permission.RECORD_AUDIO) &&(results[0] == PackageManager.PERMISSION_GRANTED)){
                Intent intro_intent = new Intent(getApplicationContext(),Intro.class);
                startActivity(intro_intent);
            }
        }
    }
}