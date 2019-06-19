package com.wipro.wipro_music_player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MusicPlayer extends AppCompatActivity {
    TextView songTitle, songArtist;
    ImageButton playSong, stopSong, pauseSong, nextSong, previousSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player);

        Intent musicIntent = getIntent();
        String artist = musicIntent.getStringExtra("artist_name");
        String title = musicIntent.getStringExtra("song_title");
        Toast.makeText(this, artist + " " + title, Toast.LENGTH_SHORT).show();

        songArtist = findViewById(R.id.music_artist);
        songTitle = findViewById(R.id.music_title);
        playSong = findViewById(R.id.button_play);
        stopSong = findViewById(R.id.button_stop);
        pauseSong = findViewById(R.id.button_pause);
        nextSong = findViewById(R.id.button_next);
        previousSong = findViewById(R.id.button_previous);

        songArtist.setText(artist);
        songTitle.setText(title);

        playSong();
        playNextSong();
        playPreviousSong();
        stopPlayingSong();
        pausePlayingSong();
    }

    // Listener for Playing the Song Image Button
    private void playSong() {
        playSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayToastMessage("Playing Song!");
            }
        });
    }
    // Listener for Playing the Next Song Image Button
    private void playNextSong() {
        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayToastMessage("Playing Next Song!");
            }
        });
    }
    // Listener for Playing the Previous Song Image Button
    private void playPreviousSong() {
        previousSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayToastMessage("Playing Previous Song!");
            }
        });
    }
    // Listener for Stopping the Song Image Button
    private void stopPlayingSong() {
        stopSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayToastMessage("Song stopped!");
            }
        });
    }
    // Listener for Pausing the Song Image Button
    private void pausePlayingSong() {
        pauseSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayToastMessage("Song Paused!");
            }
        });
    }

    // General Toast Message method
    private void displayToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
