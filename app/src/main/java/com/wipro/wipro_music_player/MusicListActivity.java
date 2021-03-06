package com.wipro.wipro_music_player;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.wipro.wipro_music_player.controller.RealmController;
import com.wipro.wipro_music_player.model.FavouriteSongModel;
import com.wipro.wipro_music_player.model.UserSettingsModel;
import com.wipro.wipro_music_player.util.SortUtility;
import com.wipro.wipro_music_player.util.PermissionUtility;
import com.wipro.wipro_music_player.model.SongModel;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MusicListActivity extends AppCompatActivity {
    static List<SongModel> musicList;
    private Realm realm;
    private UserSettingsModel userSettingsFromRealmDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs_list);

        PermissionUtility.checkStoragePermissions(getApplicationContext(), this);
        realm = Realm.getDefaultInstance();
        //RealmController.removeAllSongsFromFavourites(realm);
        userSettingsFromRealmDb = RealmController.getUserSettingsFromDb(realm);
        setUpMusicListCheckIfUserSettingsFromRealmDbIsNotEmpty();
        setUpSortingListBySortingListStatusFromRealmDb();
        setUpRecyclerViewAndAdapter();
    }

    // Check if the User Settings from the Real DB is Empty or not
    private void setUpMusicListCheckIfUserSettingsFromRealmDbIsNotEmpty() {
        if (userSettingsFromRealmDb != null) {
            setUpSongListFromUserSettingsFromRealmDb();
        } else {
            musicList = getAllAudioFromDevice(this);
        }
    }

    // Initial Set Up of Song List according to the settings retrieved from Realm DB
    private void setUpSongListFromUserSettingsFromRealmDb() {
        int defaultSongListFromDb = userSettingsFromRealmDb.getSongsListStatus();

        if (defaultSongListFromDb == Constants.UserSettings.SONGS_LIST_STATUS_ALL_SONGS) {
            musicList = getAllAudioFromDevice(this);
        } else if (defaultSongListFromDb == Constants.UserSettings.SONGS_LIST_STATUS_FAVOURITE_SONGS) {
            musicList = getListOfFavouriteSongsFromRealmDb();
        }
    }

    // Initial Set Up to handle the List Sorting by given Status
    private void setUpSortingListBySortingListStatusFromRealmDb() {
        if (userSettingsFromRealmDb != null) {
            int sortingListStatus = userSettingsFromRealmDb.getSortingListStatus();

            if (sortingListStatus == Constants.UserSettings.SORT_LIST_BY_ARTIST) {
                SortUtility.sortMusicListAscendingByArtist(musicList);
            } else if (sortingListStatus == Constants.UserSettings.SORT_LIST_BY_TITLE) {
                SortUtility.sortMusicListAscendingBySongTitle(musicList);
            }
        }
    }

    // Retrieve all the mp3 files from the External SD Card
    public List<SongModel> getAllAudioFromDevice(final Context context) {
        List<SongModel> listOfSongs = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {MediaStore.Audio.AudioColumns.DATA,
                                MediaStore.Audio.AudioColumns.TITLE,
                                MediaStore.Audio.AudioColumns.ALBUM,
                                MediaStore.Audio.ArtistColumns.ARTIST,
                                MediaStore.Audio.AudioColumns.DURATION,
                                MediaStore.Audio.AudioColumns.SIZE,
                                MediaStore.Audio.AudioColumns.DISPLAY_NAME,
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                SongModel songModel = new SongModel();

                String artist = cursor.getString(3);
                String title = cursor.getString(1);
                String path = cursor.getString(0);
                long duration = cursor.getLong(4);
                double size = cursor.getLong(5);
                //String album = cursor.getString(2);
                //String name = cursor.getString(6);

                songModel.setArtist(artist);
                songModel.setTitle(title);
                songModel.setPath(path);
                songModel.setLength(duration);
                songModel.setSize(size);
                listOfSongs.add(songModel);
                //Log.i(Constants.LogTags.MUSIC_TAG, "ARTIST: " + artist + ". TITLE: " + title + ". LENGTH: " + duration + ". SIZE: " + size + ". PATH: " + path);
            }
            cursor.close();
        } else {
            setUpToastAndLogMessages("All Songs List is Empty!");
        }
        return listOfSongs;
    }

    // Generate the list of Favourite Songs from the Realm DB
    public List<SongModel> getListOfFavouriteSongsFromRealmDb() {
        List<SongModel> favouriteSongsList = new ArrayList<>();
        List<FavouriteSongModel> songsListFromDb = RealmController.getFavouriteSongsFromRealmDb(realm);

        if (!songsListFromDb.isEmpty()) {
            for (FavouriteSongModel song : songsListFromDb) {
                SongModel songModel = new SongModel();
                songModel.setArtist(song.getSongArtist());
                songModel.setTitle(song.getSongTitle());
                songModel.setPath(song.getSongPath());
                songModel.setLength(song.getSongLength());
                songModel.setSize(song.getSongSize());
                favouriteSongsList.add(songModel);
                //Log.i(Constants.LogTags.MUSIC_TAG, "ARTIST: " + song.getSongArtist() + ". TITLE: " + song.getSongTitle() + ". LENGTH: " + song.getSongLength() + ". SIZE: " + song.getSongSize() + ". PATH: " + song.getSongPath());
            }
        } else {
            setUpToastAndLogMessages("Favourite Songs List is Empty!");
        }
        return favouriteSongsList;
    }

    // Set Up Messages for Toast and Log
    private void setUpToastAndLogMessages(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.i(Constants.LogTags.MUSIC_TAG, message);
    }

    // Prepare Recycler View and Adapter
    private void setUpRecyclerViewAndAdapter() {
        List<SongModel> music = new ArrayList<>();

        for (int i = 0; i < musicList.size(); i++) {
            music.add(new SongModel(musicList.get(i).getArtist(),
                    musicList.get(i).getTitle(),
                    musicList.get(i).getPath(),
                    musicList.get(i).getLength(),
                    musicList.get(i).getSize()));
        }

        MusicAdapter musicAdapter = new MusicAdapter(music);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recycler = findViewById(R.id.recycler_view);
        Drawable divider = ContextCompat.getDrawable(recycler.getContext(), R.drawable.divider);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(), layoutManager.getOrientation());

        assert divider != null;
        dividerItemDecoration.setDrawable(divider);

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(dividerItemDecoration);
        recycler.setAdapter(musicAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_songs_list, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String messageString;

        switch (item.getItemId()) {
            case R.id.item_select_all_songs_list:
                musicList = getAllAudioFromDevice(this);
                messageString = "Menu Item - All Songs List Selected!";
                RealmController.updateUserSettingsSongListStatus(realm, Constants.UserSettings.SONGS_LIST_STATUS_ALL_SONGS);
                setUpSortingListBySortingListStatusFromRealmDb();
                break;
            case R.id.item_select_favourite_songs_list:
                musicList = getListOfFavouriteSongsFromRealmDb();
                messageString = "Menu Item - Favourite Songs List Selected!";
                RealmController.updateUserSettingsSongListStatus(realm, Constants.UserSettings.SONGS_LIST_STATUS_FAVOURITE_SONGS);
                setUpSortingListBySortingListStatusFromRealmDb();
                break;
            case R.id.item_sort_by_default:
                setUpMusicListCheckIfUserSettingsFromRealmDbIsNotEmpty();
                messageString = "Menu Item - Sort by Default Selected!";
                RealmController.updateUserSettingsSortingListStatus(realm, Constants.UserSettings.SORT_LIST_BY_DEFAULT);
                break;
            case R.id.item_sort_by_artist:
                SortUtility.sortMusicListAscendingByArtist(musicList);
                messageString = "Menu Item - Sort by Artist Selected!";
                RealmController.updateUserSettingsSortingListStatus(realm, Constants.UserSettings.SORT_LIST_BY_ARTIST);
                break;
            case R.id.item_sort_by_title:
                SortUtility.sortMusicListAscendingBySongTitle(musicList);
                messageString = "Menu Item - Sort by Title Selected!";
                RealmController.updateUserSettingsSortingListStatus(realm, Constants.UserSettings.SORT_LIST_BY_TITLE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        addSortMethodAndLogMessageAndRecyclerViewAndAdapterTogether(messageString);
        return true;
    }

    // Helper method to add methods together to employ modularity (not to repeat code)
    private void addSortMethodAndLogMessageAndRecyclerViewAndAdapterTogether(String string) {
        Log.i(Constants.LogTags.MUSIC_TAG, string);
        setUpRecyclerViewAndAdapter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(Constants.LogTags.MUSIC_TAG, "Application Has Closed!");
        realm.close();
    }
}
