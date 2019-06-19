package com.wipro.wipro_music_player;

import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs_list);

        String path = findPath();
        //Toast.makeText(this,"PATH: " + path, Toast.LENGTH_SHORT).show();
        PlayerManager playerManager = new PlayerManager();
        ArrayList<String> songsList = playerManager.findSongs(path);
        Log.i("MUSIC_TAG", songsList + "");

//        ArrayList<HashMap<String, String>> songsListData = new ArrayList<>(songsList);
//        Log.i("MUSIC_TAG", songsListData + "");

        SongModel[] songs = new SongModel[] {
                new SongModel("Metallica", "One"),
                new SongModel("Nirvana", "On a Plain"),
                new SongModel("Opeth", "Face of Melinda"),
                new SongModel("ONA", "Znalazlam"),
                new SongModel("Oasis", "Little by Little"),
                new SongModel("AC/DC", "Thunderstruck"),
                new SongModel("In Flames", "Battles"),
                new SongModel("Dream Theater", "New Millenium"),
                new SongModel("SOAD", "Toxity"),
                new SongModel("Coma", "Better Way"),
                new SongModel("Megadeth", "Illusion"),
                new SongModel("Goodsmack", "Mama"),
                new SongModel("Ira", "Moj Dom"),
                new SongModel("Varius Manx", "Zabij Mnie"),
                new SongModel("Andy James", "Dual"),
                new SongModel("Creed", "Faceless Man"),
                new SongModel("Luxtorpeda", "Wilki Dwa"),
                new SongModel("Joe Satriani", "Friends"),
                new SongModel("Filter", "Hey Man Nice Shot"),
        };

        MusicAdapter musicAdapter = new MusicAdapter(songs);
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

    private String findPath() {
        String path = null;
        File sdCardFile;
        List<String> sdCardPossiblePath = Arrays.asList("external_sd", "ext_sd", "external", "extSdCard");

        for (String sdPath : sdCardPossiblePath) {
            File file = new File("/mnt/", sdPath);

            if (file.isDirectory() && file.canWrite()) {
                path = file.getAbsolutePath();
                File testWritable = new File(path, "test");

                if (testWritable.mkdirs()) {
                    testWritable.delete();
                } else {
                    path = null;
                }
            }
        }

        if (path != null) {
            sdCardFile = new File(path);
        } else {
            sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        }
        return sdCardFile.getAbsolutePath();
    }
}
