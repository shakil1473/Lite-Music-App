package com.example.shakil.musicplayer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;



public class MainActivity extends AppCompatActivity {


    private ArrayList<File>mySongs;
    private ListView listViewPlayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewPlayList = findViewById(R.id.list_view_playlist);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                ScanSongs scanSongs = new ScanSongs();
                mySongs = scanSongs.scanSongs(Environment.getExternalStorageDirectory());

            } else {
                requestPermission();
            }
        }


        String[] songList = new String[mySongs.size()];

        for(int i = 0;i<mySongs.size();i++){
            Log.v("song name",mySongs.get(i).getName().toString());
            songList [i] = mySongs.get(i).getName().toString().replace(".mp3","").replace("MP3","").replace("wva","");
        }

        //Arrays.sort(songList);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.songs_layout,R.id.txtView_songs, songList);
        listViewPlayList.setAdapter(arrayAdapter);

        listViewPlayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startActivity(new Intent(getApplicationContext(), PlayerActivity.class).putExtra("pos",position).putExtra("songs",mySongs));
            }
        });

    }




    protected boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    protected void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ScanSongs scanSongs = new ScanSongs();
                    mySongs = scanSongs.scanSongs(Environment.getExternalStorageDirectory());
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }


}
