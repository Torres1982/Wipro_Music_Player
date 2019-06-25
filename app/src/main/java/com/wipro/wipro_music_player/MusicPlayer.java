package com.wipro.wipro_music_player;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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

import static com.wipro.wipro_music_player.R.id.music_size;

public class MusicPlayer extends AppCompatActivity {
    final static String MUSIC_TAG = "MUSIC_TAG";
    private TextView songTitle, songArtist, songSize, songLength, songTimeElapsed;
    private ImageButton playSong, stopSong, nextSong, previousSong;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private String path;
    private int songIndex;
    private List<SongModel> listOfSongs = new ArrayList<>();
    private String artistNewSong;
    private String titleNewSong;
    private double sizeNewSong;
    private long durationNewSong;
    private Handler musicHandler = new Handler();

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

        listOfSongs = MusicListActivity.musicList;
        mediaPlayer = new MediaPlayer();

        songArtist = findViewById(R.id.music_artist);
        songTitle = findViewById(R.id.music_title);
        songSize = findViewById(R.id.music_size);
        songLength = findViewById(R.id.music_length);
        songTimeElapsed = findViewById(R.id.time_elapsed);
        playSong = findViewById(R.id.button_play);
        stopSong = findViewById(R.id.button_stop);
        nextSong = findViewById(R.id.button_next);
        previousSong = findViewById(R.id.button_previous);
        seekBar = findViewById(R.id.seek_bar);
        seekBar.setMax(100);
        songTimeElapsed.setText(R.string.initial_timer);

        updateViewDetails(artist, title, size, length);
        setSeekBarListener();
        setPlaySongListener();
        setPlayNextSongListener();
        setPlayPreviousSongListener();
        setStopPlayingSongListener();
        setMediaPlayerListener();
    }

    // Listener for Playing the Song Image Button
    private void setPlaySongListener() {
        playSong.setOnClickListener(new View.OnClickListener() {
            int songCurrentPosition;

            @Override
            public void onClick(View v) {
                animateButtonClick(playSong);

                if (mediaPlayer.isPlaying() && mediaPlayer != null) {
                    songCurrentPosition = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();
                    playSong.setImageResource(R.drawable.play);
                    showToastMessageAndLogMessageTogether("Playing Song Paused!");
                } else {
                    // Paused status
                    if (mediaPlayer != null) {
                        if (songCurrentPosition > 0) {
                            mediaPlayer.seekTo(songCurrentPosition);
                            mediaPlayer.start();
                            showToastMessageAndLogMessageTogether("Playing Song Resumed!");
                            songCurrentPosition = 0;
                        // Stopped status
                        } else {
                            startSong(path);
                            showToastMessageAndLogMessageTogether("Playing Song!");
                        }
                        playSong.setImageResource(R.drawable.pause);
                    }
                }
            }
        });
    }

    // Listener for Playing the Next Song Image Button
    private void setPlayNextSongListener() {
        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButtonClick(nextSong);
                switchToNextSong();
            }
        });
    }

    // Listener for Playing the Previous Song Image Button
    private void setPlayPreviousSongListener() {
        previousSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButtonClick(previousSong);
                showToastMessageAndLogMessageTogether("Playing Previous Song!");

                if (songIndex == 0) {
                    songIndex = listOfSongs.size() - 1;
                } else {
                    songIndex = songIndex - 1;
                }
                updateToStartNewSong();
            }
        });
    }

    // Listener for Stopping the Song Image Button
    private void setStopPlayingSongListener() {
        stopSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying() && mediaPlayer != null) {
                    animateButtonClick(stopSong);
                    showToastMessageAndLogMessageTogether("Playing Song Stopped!");
                    playSong.setImageResource(R.drawable.play);
                    songTimeElapsed.setText(R.string.initial_timer);
                    mediaPlayer.reset();
                    musicHandler.removeCallbacks(musicUpdateProgressBar);
                    seekBar.setProgress(0);
                }
            }
        });
    }

    // Prepare and start playing the song
    private void startSong(String path) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            playSong.setImageResource(R.drawable.pause);
            seekBar.setProgress(0);
            updateSeekBar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loading a new Song when the Next or Previous Button has been clicked
    private void loadNewSongOnNextOrPreviousButtonClick() {
        artistNewSong = listOfSongs.get(songIndex).getArtist();
        titleNewSong = listOfSongs.get(songIndex).getTitle();
        sizeNewSong = listOfSongs.get(songIndex).getSize();
        durationNewSong = listOfSongs.get(songIndex).getLength();
        path = listOfSongs.get(songIndex).getPath();
    }

    // General Toast Message method
    private void displayToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Put Toast Message and Log Message together
    public void showToastMessageAndLogMessageTogether(String message) {
        displayToastMessage(message);
        Log.i(MUSIC_TAG, message);
    }

    // Set Animation for Image Button clicks
    private void animateButtonClick(ImageButton button) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
        button.startAnimation(animation);
    }

    // Update the song artist, title and the length of the song
    private void updateViewDetails(String artist, String title, double size, long duration) {
        String stringSongSize = getDoubleToStringValueOfConvertedAndRoundedSongSize(size);
        String songDuration = ConverterUtility.convertMillisecondsToMinutesAndSeconds(duration);

        songArtist.setText(artist);
        songTitle.setText(title);
        songSize.setText(stringSongSize);
        songLength.setText(songDuration);
    }

    // Get the String value of the song size (double)
    private String getDoubleToStringValueOfConvertedAndRoundedSongSize(double size) {
        double convertedSongSize = ConverterUtility.convertBytesToMegabytes(size);
        double convertedSongSizeRounded = ConverterUtility.roundDoubleValue(convertedSongSize, 2);
        return (Double.toString(convertedSongSizeRounded)) + " MB";
    }

    // Updating Seek Bar progress
    private void updateSeekBar() {
        musicHandler.postDelayed(musicUpdateProgressBar, 100);
    }

    // Set the Runnable Thread for updating the Seek Bar progress
    private Runnable musicUpdateProgressBar = new Runnable() {
        @Override
        public void run() {
            long totalTime = mediaPlayer.getDuration();
            long currentTime = mediaPlayer.getCurrentPosition();
            songTimeElapsed.setText(ConverterUtility.convertMillisecondsToMinutesAndSeconds(currentTime));
            int progress = ConverterUtility.getSeekBarProgressRatio(totalTime, currentTime);
            seekBar.setProgress(progress);
            musicHandler.postDelayed(this, 100);
        }
    };

    // Assign the Seek Bar listener
    private void setSeekBarListener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                musicHandler.removeCallbacks(musicUpdateProgressBar);
                Log.i(MUSIC_TAG, "Progress Bar User Interaction!");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicHandler.removeCallbacks(musicUpdateProgressBar);
                int totalTime = mediaPlayer.getDuration();
                int currentTime = ConverterUtility.getSeekBarProgressTime(seekBar.getProgress(), totalTime);
                mediaPlayer.seekTo(currentTime);
                updateSeekBar();
            }
        });
    }

    // Playing the Next Song either the 'Next Song' button is clicked or automatically play the song when the previous song has finished playing
    private void switchToNextSong() {
        showToastMessageAndLogMessageTogether("Playing Next Song!");

        if (songIndex < listOfSongs.size() - 1) {
            songIndex = songIndex + 1;
        } else {
            songIndex = 0;
        }
        updateToStartNewSong();
    }

    // Updating info on the Next Song play
    private void updateToStartNewSong() {
        loadNewSongOnNextOrPreviousButtonClick();
        updateViewDetails(artistNewSong, titleNewSong, sizeNewSong, durationNewSong);
        startSong(path);
        Log.i(MUSIC_TAG, "Artist: " + artistNewSong + ". New Song Index: " + songIndex);
    }

    // Set the On Completion Media Player Listener
    private void setMediaPlayerListener() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                switchToNextSong();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        musicHandler.removeCallbacksAndMessages(null);
        Log.i(MUSIC_TAG, "On Back Pressed!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer = null;
        Log.i(MUSIC_TAG, "Music Player Activity Destroyed!");
    }
}
