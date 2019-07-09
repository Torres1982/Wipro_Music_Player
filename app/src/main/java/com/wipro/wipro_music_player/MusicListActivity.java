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
import android.widget.Toast;

import com.wipro.wipro_music_player.util.PermissionUtility;
import com.wipro.wipro_music_player.SongModel;

import java.util.ArrayList;
import java.util.List;

public class MusicListActivity extends AppCompatActivity {
    static List<SongModel> musicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs_list);

        PermissionUtility.checkStoragePermissions(getApplicationContext(), this);
        musicList = getAllAudioFromDevice(this);
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

    // retrieve all the mp3 files from the External SD Card
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

                Log.i(Constants.LogTags.MUSIC_TAG, "ARTIST: " + artist + ". TITLE: " + title + ". LENGTH: " + duration + ". SIZE: " + size + ". PATH: " + path);
            }
            cursor.close();
        } else {
            String message = "Music List is Empty!";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Log.i(Constants.LogTags.MUSIC_TAG, message);
        }
        return listOfSongs;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(Constants.LogTags.MUSIC_TAG, "Application Has Closed!");
    }
}
