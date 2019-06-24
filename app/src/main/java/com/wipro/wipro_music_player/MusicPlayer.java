package com.wipro.wipro_music_player;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wipro.wipro_music_player.util.ConverterUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayer extends AppCompatActivity {
    TextView songTitle, songArtist, songLength;
    private ImageButton playSong, stopSong, nextSong, previousSong;
    SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private String path;
    private int songIndex;
    private List<SongModel> listOfSongs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player);

        Intent musicIntent = getIntent();
        Bundle extras = musicIntent.getExtras();
        assert extras != null;
        String artist = extras.getString("artist_name");
        String title = extras.getString("song_title");
        path = extras.getString("song_path");
        long length = extras.getLong("song_length");
        double size = extras.getDouble("song_size");
        songIndex = extras.getInt("song_position");

        double songSize = ConverterUtility.convertBytesToMegabytes(size);
        double songSizeRounded = ConverterUtility.roundDoubleValue(songSize, 2);
        displayToastMessage("SIZE: " + songSizeRounded + " MB");

        listOfSongs = MusicListActivity.musicList;
        mediaPlayer = new MediaPlayer();

        songArtist = findViewById(R.id.music_artist);
        songTitle = findViewById(R.id.music_title);
        songLength = findViewById(R.id.music_length);
        playSong = findViewById(R.id.button_play);
        stopSong = findViewById(R.id.button_stop);
        nextSong = findViewById(R.id.button_next);
        previousSong = findViewById(R.id.button_previous);
        seekBar = findViewById(R.id.seek_bar);

        updateViewDetails(artist, title, length);
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
                    playSong.setImageResource(R.drawable.play);
                    displayToastMessage("Song Paused!");
                } else {
                    if (mediaPlayer != null) {
                        startSong(path);
                        playSong.setImageResource(R.drawable.pause);
                        displayToastMessage("Playing Song!");
                    }
                }
            }
        });
    }

    // Listener for Playing the Next Song Image Button
    private void playNextSong() {
        nextSong.setOnClickListener(new View.OnClickListener() {
            String artistNext, titleNext, pathNext;
            long durationNext;
            double sizeNext, sizeNextRounded;

            @Override
            public void onClick(View v) {
                animateButtonClick(nextSong);
                displayToastMessage("Playing Next Song!");

                if (songIndex < listOfSongs.size() - 1) {
                    songIndex = songIndex + 1;
                } else {
                    songIndex = 0;
                }

                artistNext = listOfSongs.get(songIndex).getArtist();
                titleNext = listOfSongs.get(songIndex).getTitle();
                pathNext = listOfSongs.get(songIndex).getPath();
                durationNext = listOfSongs.get(songIndex).getLength();
                sizeNext = ConverterUtility.convertBytesToMegabytes(listOfSongs.get(songIndex).getSize());
                sizeNextRounded = ConverterUtility.roundDoubleValue(sizeNext, 2);
                path = pathNext;

                updateViewDetails(artistNext, titleNext, durationNext);
                resetWhenPlayingNextOrPreviousSongBeingPausedOrStopped(path);

                Log.i("MUSIC_TAG", "SIZE: " + sizeNextRounded + " MB. NEW SONG INDEX: " + songIndex);
            }
        });
    }

    // Listener for Playing the Previous Song Image Button
    private void playPreviousSong() {
        previousSong.setOnClickListener(new View.OnClickListener() {
            String artistPrevious, titlePrevious, pathPrevious;
            long durationPrevious;
            double sizePrevious, sizePreviousRounded;

            @Override
            public void onClick(View v) {
                animateButtonClick(previousSong);
                displayToastMessage("Playing Previous Song!");

                if (songIndex == 0) {
                    songIndex = listOfSongs.size() - 1;
                } else {
                    songIndex = songIndex - 1;
                }

                artistPrevious = listOfSongs.get(songIndex).getArtist();
                titlePrevious = listOfSongs.get(songIndex).getTitle();
                pathPrevious = listOfSongs.get(songIndex).getPath();
                durationPrevious = listOfSongs.get(songIndex).getLength();
                sizePrevious = ConverterUtility.convertBytesToMegabytes(listOfSongs.get(songIndex).getSize());
                sizePreviousRounded = ConverterUtility.roundDoubleValue(sizePrevious, 2);
                path = pathPrevious;

                updateViewDetails(artistPrevious, titlePrevious, durationPrevious);
                resetWhenPlayingNextOrPreviousSongBeingPausedOrStopped(path);

                Log.i("MUSIC_TAG", "SIZE: " + sizePreviousRounded + " MB. NEW SONG INDEX: " + songIndex);
            }
        });
    }

    // Listener for Stopping the Song Image Button
    private void stopPlayingSong() {
        stopSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButtonClick(stopSong);
                displayToastMessage("Song stopped!");
                mediaPlayer.reset();
                playSong.setImageResource(R.drawable.play);
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

    // Update the song artist, title and the length of the song
    private void updateViewDetails(String artist, String title, long duration) {
        String songDuration = ConverterUtility.convertMillisecondsToMinutesAndSeconds(duration);
        songArtist.setText(artist);
        songTitle.setText(title);
        songLength.setText(songDuration);
    }

    // Prepare and start playing the song
    private void startSong(String path) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Reset the Song when Next or Previous player button has been clicked
    private void resetWhenPlayingNextOrPreviousSongBeingPausedOrStopped(String path) {
        if (mediaPlayer.isPlaying() && mediaPlayer != null) {
            startSong(path);
        } else {
            mediaPlayer.reset();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }
}
