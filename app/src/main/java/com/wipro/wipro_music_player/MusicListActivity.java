package com.wipro.wipro_music_player;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MusicListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs_list);

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
                new SongModel("Coma", "Better Wat"),
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
        dividerItemDecoration.setDrawable(divider);

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(dividerItemDecoration);
        recycler.setAdapter(musicAdapter);
    }
}
