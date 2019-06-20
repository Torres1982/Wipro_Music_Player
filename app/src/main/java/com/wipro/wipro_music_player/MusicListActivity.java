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

import com.wipro.wipro_music_player.util.ConverterUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicListActivity extends AppCompatActivity {
    List<SongModel> musicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs_list);

        //PlayerManager playerManager = new PlayerManager();
        //ArrayList<String> songsList = playerManager.findSongs(path);
        //Log.i("MUSIC_TAG", " " + songsList);

        //String songLength = ConverterUtility.convertMillisecondsToMinutesAndSeconds(764000);
        //double songSize = ConverterUtility.convertBytesToMegabytes(4845638);
        //Toast.makeText(this,"Song Length: " + songLength, Toast.LENGTH_SHORT).show();
        //Log.i("MUSIC_TAG", "DURATION: " + songLength + " SIZE: " + songSize + " MB");

        musicList = getAllAudioFromDevice(this);
        //Toast.makeText(this,"LIST: " + musicList.get(0).getArtist(), Toast.LENGTH_SHORT).show();

        List<SongModel> music = new ArrayList<>();

        for (int i = 0; i < musicList.size(); i++) {
            music.add(new SongModel(musicList.get(i).getArtist(), musicList.get(i).getTitle(), musicList.get(i).getLength()));
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
                // Create a model object.
                SongModel songModel = new SongModel();

                String artist = cursor.getString(3);
                String title = cursor.getString(1);
                long duration = cursor.getLong(4);
                double size = cursor.getDouble(5);

                //String path = cursor.getString(0);
                //String album = cursor.getString(2);
                //String name = cursor.getString(6);

                // Set data to the model object
                songModel.setArtist(artist);
                songModel.setTitle(title);
                songModel.setLength(duration);

                //Log.i("MUSIC_TAG", "ARTIST: " + artist);
                //Log.i("MUSIC_TAG", "TITLE: " + title);
                //Log.i("MUSIC_TAG", "DURATION: " + duration + " SIZE: " + size);
                //Log.i("MUSIC_TAG", "PATH: " + path);
                //Log.i("MUSIC_TAG", "ALBUM: " + album);
                //Log.i("MUSIC_TAG", "NAME: " + name);

                // Add the model object to the list
                listOfSongs.add(songModel);
            }
            cursor.close();
        } else {
            Toast.makeText(this,"Music List is Empty!", Toast.LENGTH_SHORT).show();
            Log.i("MUSIC_TAG", "Music List is Empty!");
        }
        return listOfSongs;
    }
}
