package com.wipro.wipro_music_player;

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
        };

        MusicAdapter musicAdapter = new MusicAdapter(songs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recycler = findViewById(R.id.recycler_view);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(), layoutManager.getOrientation());

        recycler.setHasFixedSize(true);
        recycler.addItemDecoration(dividerItemDecoration);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(musicAdapter);
    }
}
