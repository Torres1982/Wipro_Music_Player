package com.wipro.wipro_music_player;

import android.content.Intent;
import android.media.MediaPlayer;
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
    ImageButton playSong, stopSong, nextSong, previousSong;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player);

        Intent musicIntent = getIntent();
        Bundle extras = musicIntent.getExtras();
        assert extras != null;
        String artist = extras.getString("artist_name");
        String title = extras.getString("song_title");
        String path = extras.getString("song_path");
        long length = extras.getLong("song_length");
        double size = extras.getDouble("song_size");

        String songDuration = ConverterUtility.convertMillisecondsToMinutesAndSeconds(length);
        double songSize = ConverterUtility.convertBytesToMegabytes(size);
        double songSizeRounded = ConverterUtility.roundDoubleValue(songSize, 2);
        Toast.makeText(this, "SIZE: " + songSizeRounded + " MB", Toast.LENGTH_SHORT).show();

        mediaPlayer = new MediaPlayer();
        songArtist = findViewById(R.id.music_artist);
        songTitle = findViewById(R.id.music_title);
        songLength = findViewById(R.id.music_length);
        playSong = findViewById(R.id.button_play);
        stopSong = findViewById(R.id.button_stop);
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
    }

    // Listener for Playing the Song Image Button
    private void playSong() {
        playSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButtonClick(playSong);

                if (mediaPlayer.isPlaying() && mediaPlayer != null) {
                    mediaPlayer.pause();
                    playSong.setImageResource(R.drawable.pause);
                    displayToastMessage("Song Paused!");
                } else {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        playSong.setImageResource(R.drawable.play);
                        displayToastMessage("Playing Song!");
                    }
                }
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
