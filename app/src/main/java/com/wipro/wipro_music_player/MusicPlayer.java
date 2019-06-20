package com.wipro.wipro_music_player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wipro.wipro_music_player.util.ConverterUtility;

public class MusicPlayer extends AppCompatActivity {
    TextView songTitle, songArtist, songLength;
    ImageButton playSong, stopSong, pauseSong, nextSong, previousSong;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player);

        Intent musicIntent = getIntent();
        Bundle extras = musicIntent.getExtras();
        assert extras != null;
        String artist = extras.getString("artist_name");
        String title = extras.getString("song_title");
        long length = extras.getLong("song_length");
        //Toast.makeText(this, artist + " " + title, Toast.LENGTH_SHORT).show();
        String songDuration = ConverterUtility.convertMillisecondsToMinutesAndSeconds(length);

        songArtist = findViewById(R.id.music_artist);
        songTitle = findViewById(R.id.music_title);
        songLength = findViewById(R.id.music_length);
        playSong = findViewById(R.id.button_play);
        stopSong = findViewById(R.id.button_stop);
        pauseSong = findViewById(R.id.button_pause);
        nextSong = findViewById(R.id.button_next);
        previousSong = findViewById(R.id.button_previous);
        seekBar = findViewById(R.id.seek_bar);

        songArtist.setText(artist);
        songTitle.setText(title);
        songLength.setText(songDuration);

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
                animateButtonClick(playSong);
            }
        });
    }
    // Listener for Playing the Next Song Image Button
    private void playNextSong() {
        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayToastMessage("Playing Next Song!");
                animateButtonClick(nextSong);
            }
        });
    }
    // Listener for Playing the Previous Song Image Button
    private void playPreviousSong() {
        previousSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayToastMessage("Playing Previous Song!");
                animateButtonClick(previousSong);
            }
        });
    }
    // Listener for Stopping the Song Image Button
    private void stopPlayingSong() {
        stopSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayToastMessage("Song stopped!");
                animateButtonClick(stopSong);
            }
        });
    }
    // Listener for Pausing the Song Image Button
    private void pausePlayingSong() {
        pauseSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayToastMessage("Song Paused!");
                animateButtonClick(pauseSong);
            }
        });
    }

    // General Toast Message method
    private void displayToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Set Animation for Image Button clicks
    private void animateButtonClick(ImageButton button) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
        button.startAnimation(animation);
    }
}
