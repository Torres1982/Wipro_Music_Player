package com.wipro.wipro_music_player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MusicPlayer extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player);

        Intent musicIntent = getIntent();
        String artist = musicIntent.getStringExtra("artist_name");
        String title = musicIntent.getStringExtra("song_title");
        Toast.makeText(this, artist + " " + title, Toast.LENGTH_SHORT).show();
    }
}
