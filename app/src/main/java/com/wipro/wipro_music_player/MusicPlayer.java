package com.wipro.wipro_music_player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.wipro.wipro_music_player.controller.RealmController;
import com.wipro.wipro_music_player.model.FavouriteSongModel;
import com.wipro.wipro_music_player.model.UserSettingsModel;
import com.wipro.wipro_music_player.util.ConverterUtility;
import com.wipro.wipro_music_player.model.SongModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;

public class MusicPlayer extends AppCompatActivity {
    private TextView songTitle, songArtist, songSize, songLength, songTimeElapsed, footer;
    private ImageView songAlbumCover;
    private ImageButton playSong, stopSong, nextSong, previousSong;
    private SeekBar seekBar;
    private Switch shuffleSongsSwitch, repeatSongSwitch;
    private boolean isShuffleSongsSwitchOn, isRepeatSongSwitchOn;
    private MediaPlayer mediaPlayer;
    private String path;
    private int songIndex;
    private int songCurrentPosition;
    private int animationScale;
    private List<SongModel> listOfSongs = new ArrayList<>();
    private String artistNewSong;
    private String titleNewSong;
    private double sizeNewSong;
    private long durationNewSong;
    private Handler musicHandler = new Handler();
    private static AudioManager audioManager;
    private static AudioFocusRequest audioFocusRequest;
    private ConstraintLayout constraintLayout;
    private int greyColour, redColour, yellowColour, lightGreenColour;
    // Notifications
    public static NotificationManager notificationManager;
    private static ArrayList<String> listOfNotificationActions;
    private static ArrayList<String> listOfNotificationActionTitles;
    private static ArrayList<Integer> listOfDrawableImageButtons;
    private static ArrayList<PendingIntent> listOfNotificationPendingIntents;
    private static ArrayList<NotificationCompat.Action> listOfNotificationActionBuilders;
    private boolean isDefaultThemeOn, isDarkThemeOn;
    // Realm DB
    private Realm realm;
    private UserSettingsModel userSettingsFromRealmDb;
    private FavouriteSongModel favouriteSongFromRealmDb;
    private boolean isFavouriteSongsListOn;
    private int sortListStatus;

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

        songAlbumCover = findViewById(R.id.music_main_image);
        songArtist = findViewById(R.id.music_artist);
        songTitle = findViewById(R.id.music_title);
        songSize = findViewById(R.id.music_size);
        songLength = findViewById(R.id.music_length);
        songTimeElapsed = findViewById(R.id.time_elapsed);
        footer = findViewById(R.id.footer_text);
        playSong = findViewById(R.id.button_play);
        stopSong = findViewById(R.id.button_stop);
        nextSong = findViewById(R.id.button_next);
        previousSong = findViewById(R.id.button_previous);
        shuffleSongsSwitch = findViewById(R.id.switch_shuffle);
        repeatSongSwitch = findViewById(R.id.switch_repeat);
        constraintLayout = findViewById(R.id.music_player_main_layout);

        // Action Bar Menu
        isDefaultThemeOn = true;
        isDarkThemeOn = false;

        // Assign Colours
        greyColour = R.color.grey;
        redColour = R.color.red;
        yellowColour = R.color.yellow;
        lightGreenColour = R.color.light_green;

        // Assign Animations
        animationScale = R.anim.scale;
        int animationTranslate = R.anim.translate;

        // Seek Bar
        seekBar = findViewById(R.id.seek_bar);
        seekBar.setMax(100);
        songTimeElapsed.setText(R.string.initial_timer);

        // Used for Notifications
        listOfDrawableImageButtons = new ArrayList<>(Arrays.asList(R.drawable.previous, R.drawable.play, R.drawable.stop, R.drawable.next));
        listOfNotificationActions = new ArrayList<>(Arrays.asList(Constants.NotificationAction.PREVIOUS_SONG_ACTION,
                                                                  Constants.NotificationAction.PLAY_SONG_ACTION,
                                                                  Constants.NotificationAction.STOP_SONG_ACTION,
                                                                  Constants.NotificationAction.NEXT_SONG_ACTION));
        listOfNotificationActionTitles = new ArrayList<>(Arrays.asList("Previous", "Play", "Stop", "Next"));
        listOfNotificationPendingIntents = new ArrayList<>();
        listOfNotificationActionBuilders = new ArrayList<>();

        // Realm DB
        realm = Realm.getDefaultInstance();
        userSettingsFromRealmDb = RealmController.getUserSettingsFromDb(realm);
        initialConfigurationOfUserSettingsFromRealmDb(userSettingsFromRealmDb);

        startViewAnimation(footer, animationTranslate);
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
        controlAudioFocus();
        startPlaying(path);
        MusicTagger.setAlbumCoverFromId3v2Tag(this, songAlbumCover, path);
    }

    // Initial Configuration of User Settings retrieved from the Realm DB
    private void initialConfigurationOfUserSettingsFromRealmDb(UserSettingsModel settings) {
        if (settings != null) {
            isFavouriteSongsListOn = settings.getSongsListStatus() == Constants.UserSettings.SONGS_LIST_STATUS_FAVOURITE_SONGS;
            isShuffleSongsSwitchOn = settings.getShuffleSwitchStatus() == Constants.UserSettings.SHUFFLE_SWITCH_STATUS_ON;
            isRepeatSongSwitchOn = settings.getRepeatSwitchStatus() == Constants.UserSettings.REPEAT_SWITCH_STATUS_ON;
            sortListStatus = settings.getSortingListStatus();
            setUpThemesWithSettingsFromRealmDb();
            setUpSwitchesWithSettingsFromRealmDb();
        } else {
            isFavouriteSongsListOn = false;
        }
    }

    // Initial Set Up of Theme according to the settings gathered from Realm DB
    private void setUpThemesWithSettingsFromRealmDb() {
        int defaultThemeFromDb = userSettingsFromRealmDb.getDefaultThemeStatus();

        if (defaultThemeFromDb == Constants.UserSettings.DEFAULT_THEME_STATUS_ON) {
            updateDefaultThemeMenuItemOption();
        } else {
            updateDarkThemeMenuItemOption();
        }
    }

    // Initial Set Up of Switches according to the settings gathered from Realm DB
    private void setUpSwitchesWithSettingsFromRealmDb() {
        int shuffleSwitchFromDb = userSettingsFromRealmDb.getShuffleSwitchStatus();
        int repeatSwitchFromDb = userSettingsFromRealmDb.getRepeatSwitchStatus();
        isShuffleSongsSwitchOn = shuffleSwitchFromDb == Constants.UserSettings.SHUFFLE_SWITCH_STATUS_ON;
        isRepeatSongSwitchOn = repeatSwitchFromDb == Constants.UserSettings.REPEAT_SWITCH_STATUS_ON;
        activateSwitches();
    }

    // Switch the Switches on or off when the application starts
    private void activateSwitches() {
        shuffleSongsSwitch.setChecked(isShuffleSongsSwitchOn);
        repeatSongSwitch.setChecked(isRepeatSongSwitchOn);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setFavouriteSongMenuItemIcon(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    // Set the Icon for Favourite Song on the Menu Bar
    private void setFavouriteSongMenuItemIcon(Menu menu) {
        MenuItem settingsItem = menu.findItem(R.id.item_favourites);
        // Check if the Playing song is in the Favourite Songs list in DB, and change the icon accordingly
        favouriteSongFromRealmDb = RealmController.checkIfSongIsInRealmDBbySongPath(realm, path);
        settingsItem.setIcon(favouriteSongFromRealmDb == null ? ContextCompat.getDrawable(this, R.drawable.icon_favourite_off) : ContextCompat.getDrawable(this, R.drawable.icon_favourite_on));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Currently Playing Song details
        String currentSongTitle = listOfSongs.get(songIndex).getTitle();
        String currentSongArtist = listOfSongs.get(songIndex).getArtist();
        long currentSongLength = listOfSongs.get(songIndex).getLength();
        double currentSongSize = listOfSongs.get(songIndex).getSize();

        switch (item.getItemId()) {
            case R.id.item_favourites:
                favouriteSongFromRealmDb = RealmController.checkIfSongIsInRealmDBbySongPath(realm, path);
                Log.i(Constants.LogTags.MUSIC_TAG, "Is song Found in DB? " + favouriteSongFromRealmDb);

                if (favouriteSongFromRealmDb == null) {
                    RealmController.addSongToFavourites(realm, currentSongTitle, currentSongArtist, path, currentSongLength, currentSongSize);
                    item.setIcon(R.drawable.icon_favourite_on);
                    Log.i(Constants.LogTags.MUSIC_TAG, "Song Added To Favourites: " + path);
                } else {
                    RealmController.removeSongFromFavourites(realm, path);
                    item.setIcon(R.drawable.icon_favourite_off);
                    Log.i(Constants.LogTags.MUSIC_TAG, "Song Removed from Favourites: " + path);
                }
                break;
            case R.id.item_default_theme:
                updateDefaultThemeMenuItemOption();
                Log.i(Constants.LogTags.MUSIC_TAG, "Menu Default Theme Selected!");
                break;
            case R.id.item_dark_theme:
                updateDarkThemeMenuItemOption();
                Log.i(Constants.LogTags.MUSIC_TAG, "Menu Dark Theme Selected!");
                break;
            case R.id.item_tags:
                Log.i(Constants.LogTags.MUSIC_TAG, "Menu Update Tags Selected!");
                break;
            case R.id.item_about:
                Log.i(Constants.LogTags.MUSIC_TAG, "Menu About Selected!");
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    // Update Default Theme Item option
    private void updateDefaultThemeMenuItemOption() {
        setSelectedThemes(lightGreenColour, redColour);
        isDefaultThemeOn = true;
        isDarkThemeOn = false;
    }

    // Update Dark Theme Item option
    private void updateDarkThemeMenuItemOption() {
        setSelectedThemes(redColour, yellowColour);
        isDefaultThemeOn = false;
        isDarkThemeOn = true;
    }

    // Set the Light or Dark Theme from the Menu Selection
    private void setSelectedThemes(int backgroundColour, int textColour) {
        ViewCompat.setBackgroundTintList(constraintLayout, ContextCompat.getColorStateList(this, backgroundColour));
        setColourForTextViews(textColour);
        setColourForSwitches(textColour);
    }

    // Control the Colours for the Text Views
    private void setColourForTextViews(int textColour) {
        ArrayList<TextView> listOfTextViews = new ArrayList<>(Arrays.asList(songArtist, songTitle, songSize, songLength, songTimeElapsed, footer));

        for (TextView textView: listOfTextViews) {
            textView.setTextColor(ContextCompat.getColor(this, textColour));
        }
    }

    // Control the Colours for the Switches
    private void setColourForSwitches(int textColour) {
        if (isShuffleSongsSwitchOn) {
            shuffleSongsSwitch.setTextColor(ContextCompat.getColor(this, textColour));
            repeatSongSwitch.setTextColor(ContextCompat.getColor(this, greyColour));
        } else if (isRepeatSongSwitchOn) {
            repeatSongSwitch.setTextColor(ContextCompat.getColor(this, textColour));
            shuffleSongsSwitch.setTextColor(ContextCompat.getColor(this, greyColour));
        } else {
            repeatSongSwitch.setTextColor(ContextCompat.getColor(this, greyColour));
            shuffleSongsSwitch.setTextColor(ContextCompat.getColor(this, greyColour));
        }
    }

    // Listener for Playing the Song Image Button
    private void setPlaySongListener() {
        playSong.setOnClickListener(v -> {
            startViewAnimation(playSong, animationScale);

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
        });
    }

    // Listener for Playing the Next Song Image Button
    private void setPlayNextSongListener() {
        nextSong.setOnClickListener(v -> {
            startViewAnimation(nextSong, animationScale);
            switchToNextSong();
        });
    }

    // Listener for Playing the Previous Song Image Button
    private void setPlayPreviousSongListener() {
        previousSong.setOnClickListener(v -> {
            startViewAnimation(previousSong, animationScale);
            Log.i(Constants.LogTags.MUSIC_TAG, "Switched to Previous Song!");
            songIndex = songIndex == 0 ? listOfSongs.size() - 1 : songIndex - 1;
            updateToStartNewSong();
        });
    }

    // Listener for Stopping the Song Image Button
    private void setStopPlayingSongListener() {
        stopSong.setOnClickListener(v -> {
            startViewAnimation(stopSong, animationScale);

            if (mediaPlayer.isPlaying() && mediaPlayer != null) {
                stopPlaying();
            }
        });
    }

    // Stop plying a Song
    public void stopPlaying() {
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
        Log.i(Constants.LogTags.MUSIC_TAG, message);
    }

    // Animate different Views (ImageButton on click or starting footer animation)
    private void startViewAnimation(View view, int anim) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), anim);
        view.startAnimation(animation);
    }

    // Update the song artist, title and the length of the song
    private void updateViewDetails(String artist, String title, double size, long duration) {
        String stringSongSize = ConverterUtility.getDoubleToStringValueOfConvertedAndRoundedSongSize(size);
        String songDuration = ConverterUtility.convertMillisecondsToMinutesAndSeconds(duration);

        songArtist.setText(artist);
        songTitle.setText(title);
        songSize.setText(stringSongSize);
        songLength.setText(songDuration);
        invalidateOptionsMenu();
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
                Log.i(Constants.LogTags.MUSIC_TAG, "Progress Bar User Interaction!");
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
        Log.i(Constants.LogTags.MUSIC_TAG, "Switched to Next Song!");

        if (isRepeatSongSwitchOn) {
            Log.i(Constants.LogTags.MUSIC_TAG, "Song at index " + songIndex + " is repeated!");
        } else if (isShuffleSongsSwitchOn) {
            songIndex = ConverterUtility.generateRandomSongIndex(listOfSongs.size());
        } else {
            songIndex = songIndex < listOfSongs.size() - 1 ? songIndex + 1 : 0;
        }
        updateToStartNewSong();
    }

    // Updating info on the Next Song play
    private void updateToStartNewSong() {
        loadNewSongOnNextOrPreviousButtonClick();
        updateViewDetails(artistNewSong, titleNewSong, sizeNewSong, durationNewSong);
        MusicTagger.setAlbumCoverFromId3v2Tag(this, songAlbumCover, path);
        startPlaying(path);
        showActionBarNotification();
        Log.i(Constants.LogTags.MUSIC_TAG, songIndex + " : " + artistNewSong + " - " + titleNewSong + ".");
    }

    // Set the On Completion Media Player Listener
    private void setMediaPlayerListener() {
        mediaPlayer.setOnCompletionListener(mp -> switchToNextSong());
    }

    // Listener for the Switch used for shuffling all Songs
    private void setShuffleSongsListener() {
        shuffleSongsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isShuffleSongsSwitchOn = isChecked;
            handleShuffleAndRepeatSongsListeners(isChecked,"Shuffle ON", "Shuffle OFF", shuffleSongsSwitch, repeatSongSwitch);
        });
    }

    // Listener for the Switch used for repeating a single Song
    private void setRepeatSongListener() {
        repeatSongSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isRepeatSongSwitchOn = isChecked;
            handleShuffleAndRepeatSongsListeners(isChecked, "Repeat ON", "Repeat OFF", repeatSongSwitch, shuffleSongsSwitch);
        });
    }

    // Handles common functionality for Shuffle and Repeat Song Listeners
    private void handleShuffleAndRepeatSongsListeners(boolean isChecked, String messageOn, String messageOff, Switch switchOne, Switch switchTwo) {
        int textColour = ContextCompat.getColor(this, redColour);

        if (isDefaultThemeOn) {
            textColour = ContextCompat.getColor(this, redColour);
        } else if (isDarkThemeOn) {
            textColour = ContextCompat.getColor(this, yellowColour);
        }

        if (isChecked) {
            showToastMessageAndLogMessageTogether(messageOn);
            switchOne.setTextColor(textColour);
            switchTwo.setChecked(false);
        } else {
            showToastMessageAndLogMessageTogether(messageOff);
            switchOne.setTextColor(ContextCompat.getColor(this, greyColour));
        }
        Log.i(Constants.LogTags.MUSIC_TAG, "Shuffle Switch: " + isShuffleSongsSwitchOn + ". Repeat Switch: " + isRepeatSongSwitchOn);
    }

    // Create an Action Bar Notification
    public void showActionBarNotification() {
        String songTitle = listOfSongs.get(songIndex).getTitle();
        String songArtist = listOfSongs.get(songIndex).getArtist();

        PendingIntent pendingIntent = createNotificationPendingIntent();
        setUpNotificationChannel();
        createNotificationActionPendingIntents();
        createNotificationActionBuilders();
        createNotificationCompatBuilder(pendingIntent, songTitle, songArtist);
    }

    // Create the Notification Pending Intent to start the Notification
    private PendingIntent createNotificationPendingIntent() {
        Intent notificationIntent = new Intent(this, MusicPlayer.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getService(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // Prepare the Notification Channel
    private void setUpNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(Constants.NotificationIdentifier.NOTIFICATION_CHANNEL_ID, "Channel 100", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setSound(null, null);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(notificationChannel);
    }

    // Create the Notification Pending Intents to control Notification Actions
    private void createNotificationActionPendingIntents() {
        int requestCodeCounter = 0;

        for (String action: listOfNotificationActions) {
            requestCodeCounter++;
            Intent intent = new Intent(this, MusicReceiver.class);
            intent.setAction(action);
            intent.putExtra(Constants.NotificationAction.NOTIFICATION_ACTION_KEY, action);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCodeCounter, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            listOfNotificationPendingIntents.add(pendingIntent);
        }
    }

    // Create the Notification Action Builders
    private void createNotificationActionBuilders() {
        int actionCounter = 0;

        for (String title: listOfNotificationActionTitles) {
            NotificationCompat.Action action = new NotificationCompat.Action.Builder(listOfDrawableImageButtons.get(actionCounter), title, listOfNotificationPendingIntents.get(actionCounter)).build();
            listOfNotificationActionBuilders.add(action);
            actionCounter++;
        }
    }

    // Create the Main Notification Builder to send Notification/notify()
    private void createNotificationCompatBuilder(PendingIntent pendingIntent, String songTitle, String songArtist) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Constants.NotificationIdentifier.NOTIFICATION_CHANNEL_ID);
        notificationBuilder
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setColor(ContextCompat.getColor(this, R.color.red))
                .setContentTitle(songTitle)
                .setContentText(songArtist)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.music))
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.guitar)))
                .addAction(listOfNotificationActionBuilders.get(0))
                //.addAction(listOfNotificationActionBuilders.get(1))
                .addAction(listOfNotificationActionBuilders.get(2))
                .addAction(listOfNotificationActionBuilders.get(3))
                .setTicker(songTitle)
                .setOnlyAlertOnce(true)
                .setOngoing(true);
        //.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        Notification notification = notificationBuilder.build();
        notificationManager.notify(Constants.NotificationIdentifier.NOTIFICATION_ID, notification);
    }

    // Handles Audio Focus states
    private void controlAudioFocus() {
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setFocusGain(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setWillPauseWhenDucked(true)
                .setOnAudioFocusChangeListener(focusChange -> {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        Log.i(Constants.LogTags.MUSIC_TAG, "Focus Change: Audio Focus Loss.");
                        stopPlaying();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                        Log.i(Constants.LogTags.MUSIC_TAG, "Focus Change: Audio Focus Loss Transient.");
                        pausePlaying();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        Log.i(Constants.LogTags.MUSIC_TAG, "Focus Change: Audio Focus Loss Transient Can Duck.");
                        pausePlaying();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        Log.i(Constants.LogTags.MUSIC_TAG, "Focus Change: Audio Focus Gain.");
                        mediaPlayer.seekTo(songCurrentPosition);
                        mediaPlayer.start();
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
        Log.i(Constants.LogTags.MUSIC_TAG, "On Back Pressed!");
    }

    @Override
    public void onPause() {
        super.onPause();
        updateUserSettings();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer = null;
        audioManager.abandonAudioFocusRequest(audioFocusRequest);
        notificationManager.cancel(Constants.NotificationIdentifier.NOTIFICATION_ID);
        realm.close();
        Log.i(Constants.LogTags.MUSIC_TAG, "Music Player Activity Destroyed!");
    }

    // Handles updates for User Settings
    private void updateUserSettings() {
        int defaultThemeStatus, darkThemeStatus, shuffleSwitchStatus, repeatSwitchStatus, songsListStatus;

        // Manage Themes
        if (isDefaultThemeOn) {
            defaultThemeStatus = Constants.UserSettings.DEFAULT_THEME_STATUS_ON;
            darkThemeStatus = Constants.UserSettings.DARK_THEME_STATUS_OFF;
        } else {
            defaultThemeStatus = Constants.UserSettings.DEFAULT_THEME_STATUS_OFF;
            darkThemeStatus = Constants.UserSettings.DARK_THEME_STATUS_ON;
        }

        shuffleSwitchStatus = isShuffleSongsSwitchOn ? Constants.UserSettings.SHUFFLE_SWITCH_STATUS_ON : Constants.UserSettings.SHUFFLE_SWITCH_STATUS_OFF;
        repeatSwitchStatus = isRepeatSongSwitchOn ? Constants.UserSettings.REPEAT_SWITCH_STATUS_ON : Constants.UserSettings.REPEAT_SWITCH_STATUS_OFF;
        songsListStatus = isFavouriteSongsListOn ? Constants.UserSettings.SONGS_LIST_STATUS_FAVOURITE_SONGS : Constants.UserSettings.SONGS_LIST_STATUS_ALL_SONGS;
        // Insert or Update User Settings in the Realm DB
        RealmController.saveUserSettings(realm, defaultThemeStatus, darkThemeStatus, shuffleSwitchStatus, repeatSwitchStatus, songsListStatus, sortListStatus);
    }
}

// TODO Maybe it needs to be implemented later in showActionBarNotification method
//RemoteViews notificationCompact = new RemoteViews(getPackageName(), R.layout.notification_compact);
//RemoteViews notificationExpanded = new RemoteViews(getPackageName(), R.layout.notification_expanded);
//notificationCompact.setOnClickPendingIntent(R.id.button_previous, pendingPreviousIntent);
//.setCustomContentView(notificationCompact)
//.setCustomBigContentView(notificationExpanded)
