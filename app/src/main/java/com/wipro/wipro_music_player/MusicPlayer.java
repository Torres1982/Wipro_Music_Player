package com.wipro.wipro_music_player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.wipro.wipro_music_player.util.ConverterUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayer extends AppCompatActivity {
    final static String MUSIC_TAG = "MUSIC_TAG";
    private static final String ACTION_BAR_CHANNEL_ID = "com.wipro.music.player";
    static final int ACTION_BAR_NOTIFICATION_ID = 100;
    private TextView songTitle, songArtist, songSize, songLength, songTimeElapsed;
    private ImageButton playSong, stopSong, nextSong, previousSong;
    private SeekBar seekBar;
    private Switch shuffleSongsSwitch, repeatSongSwitch;
    private boolean isShuffleSongsSwitchOn, isRepeatSongSwitchOn;
    private MediaPlayer mediaPlayer;
    private String path;
    private int songIndex;
    private int songCurrentPosition;
    private List<SongModel> listOfSongs = new ArrayList<>();
    private String artistNewSong;
    private String titleNewSong;
    private double sizeNewSong;
    private long durationNewSong;
    private Handler musicHandler = new Handler();
    public static NotificationManager notificationManager;

    private static AudioManager audioManager;
    private static AudioFocusRequest audioFocusRequest;
    private static int focusRequest;

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
        shuffleSongsSwitch = findViewById(R.id.switch_shuffle);
        repeatSongSwitch = findViewById(R.id.switch_repeat);
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
        setShuffleSongsListener();
        setRepeatSongListener();
        showActionBarNotification();
        startPlaying(path);
        controlAudioFocus();
    }

    // Listener for Playing the Song Image Button
    private void setPlaySongListener() {
        playSong.setOnClickListener(new View.OnClickListener() {
            //int songCurrentPosition;

            @Override
            public void onClick(View v) {
                animateButtonClick(playSong);

                if (mediaPlayer.isPlaying() && mediaPlayer != null) {
                    pausePlaying();
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
                            startPlaying(path);
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
                    stopPlaying();
                }
            }
        });
    }

    // Stop plying a Song
    public void stopPlaying() {
        animateButtonClick(stopSong);
        showToastMessageAndLogMessageTogether("Playing Song Stopped!");
        playSong.setImageResource(R.drawable.play);
        songTimeElapsed.setText(R.string.initial_timer);
        mediaPlayer.reset();
        musicHandler.removeCallbacks(musicUpdateProgressBar);
        seekBar.setProgress(0);
    }

    // Prepare and start playing the song
    private void startPlaying(String path) {
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

    // Pause the Playing Song
    private void pausePlaying() {
        songCurrentPosition = mediaPlayer.getCurrentPosition();
        mediaPlayer.pause();
        playSong.setImageResource(R.drawable.play);
        showToastMessageAndLogMessageTogether("Playing Song Paused!");
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

        if (isRepeatSongSwitchOn) {
            Log.i(MUSIC_TAG, "Song at index " + songIndex + " is repeated!");
        } else if (isShuffleSongsSwitchOn) {
            songIndex = ConverterUtility.generateRandomSongIndex(listOfSongs.size());
        } else {
            if (songIndex < listOfSongs.size() - 1) {
                songIndex = songIndex + 1;
            } else {
                songIndex = 0;
            }
        }
        updateToStartNewSong();
    }

    // Updating info on the Next Song play
    private void updateToStartNewSong() {
        loadNewSongOnNextOrPreviousButtonClick();
        updateViewDetails(artistNewSong, titleNewSong, sizeNewSong, durationNewSong);
        startPlaying(path);
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

    // Listener for the Switch used for shuffling all Songs
    private void setShuffleSongsListener() {
        shuffleSongsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isShuffleSongsSwitchOn = isChecked;
                handleShuffleAndRepeatSongsListeners(isChecked,"Shuffle ON", "Shuffle OFF", shuffleSongsSwitch, repeatSongSwitch);
            }
        });
    }

    // Listener for the Switch used for repeating a single Song
    private void setRepeatSongListener() {
        repeatSongSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRepeatSongSwitchOn = isChecked;
                handleShuffleAndRepeatSongsListeners(isChecked, "Repeat ON", "Repeat OFF", repeatSongSwitch, shuffleSongsSwitch);
            }
        });
    }

    // Handles common functionality for Shuffle and Repeat Song Listeners
    private void handleShuffleAndRepeatSongsListeners(boolean isChecked, String messageOn, String messageOff, Switch switchOne, Switch switchTwo) {
        if (isChecked) {
            showToastMessageAndLogMessageTogether(messageOn);
            switchOne.setTextColor(Color.rgb(255, 0, 0));
            switchTwo.setChecked(false);
        } else {
            showToastMessageAndLogMessageTogether(messageOff);
            switchOne.setTextColor(Color.rgb(168, 168, 168));
        }
        Log.i(MUSIC_TAG, "Shuffle Switch: " + isShuffleSongsSwitchOn + ". Repeat Switch: " + isRepeatSongSwitchOn);
    }

    // Create an Action Bar Notification
    public void showActionBarNotification() {
        String songArtist = listOfSongs.get(songIndex).getArtist();
        String songTitle = listOfSongs.get(songIndex).getTitle();

        Intent notificationIntent = new Intent(this, MusicPlayer.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationChannel notificationChannel = new NotificationChannel(ACTION_BAR_CHANNEL_ID, "Channel 100", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setSound(null, null);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(notificationChannel);
        Notification.Builder notificationBuilder = new Notification.Builder(this, ACTION_BAR_CHANNEL_ID);

        notificationBuilder
                .setContentTitle(songTitle)
                .setContentText(songArtist)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon_notification)
                .setTicker(songTitle)
                .setOngoing(true);
        Notification notification = notificationBuilder.build();
        notificationManager.notify(ACTION_BAR_NOTIFICATION_ID, notification);
    }

    // Handles Audio Focus states
    private void controlAudioFocus() {
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setWillPauseWhenDucked(true)
                .setOnAudioFocusChangeListener(new AudioManager.OnAudioFocusChangeListener() {
                    @Override
                    public void onAudioFocusChange(int focusChange) {
                        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                            audioManager.abandonAudioFocusRequest(audioFocusRequest);
                            stopPlaying();
                            Log.i(MusicPlayer.MUSIC_TAG, "Focus Change: Audio Focus Loss.");
                        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                            pausePlaying();
                            Log.i(MusicPlayer.MUSIC_TAG, "Focus Change: Audio Focus Loss Transient.");
                        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                            mediaPlayer.setVolume(0.2f, 0.2f);
                            Log.i(MusicPlayer.MUSIC_TAG, "Focus Change: Audio Focus Loss Transient Can Duck.");
                        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                            mediaPlayer.setVolume(0.7f, 0.7f);
                            startPlaying(path);
                            Log.i(MusicPlayer.MUSIC_TAG, "Focus Change: Audio Focus Gain.");
                        }
                    }
                })
                .build();

        mediaPlayer.setAudioAttributes(audioAttributes);
        audioManager.requestAudioFocus(audioFocusRequest);
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
