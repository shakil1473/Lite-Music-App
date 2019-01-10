package com.example.shakil.musicplayer;

import java.io.File;
import java.util.ArrayList;

import static java.util.Collections.sort;

/**
 * Created by shakil on 09-Jan-19.
 */

public class ScanSongs {
    public ArrayList<File> scanSongs(File rootDirectory){

        ArrayList<File> songsArray = new ArrayList<File>();
        File[] files = rootDirectory.listFiles();

        for(File singleFile : files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                songsArray.addAll(scanSongs(singleFile));
            }
            else {
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".MP3") || singleFile.getName().endsWith(".wav")){
                    songsArray.add(singleFile);
                }
            }
        }

        return songsArray;

    }


}
