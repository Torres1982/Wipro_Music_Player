package com.wipro.wipro_music_player;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private SongModel [] listOfSongs;

    MusicAdapter(SongModel[] list) {
        listOfSongs = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View musicItem = inflater.inflate(R.layout.song_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(musicItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final SongModel songsList = listOfSongs[position];
        viewHolder.artistTextView.setText(listOfSongs[position].getArtist());
        viewHolder.titleTextView.setText(listOfSongs[position].getTitle());

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent musicPlayerIntent = new Intent(v.getContext(), MusicPlayer.class);
                musicPlayerIntent.putExtra("artist_name", songsList.getArtist());
                musicPlayerIntent.putExtra("song_title", songsList.getTitle());
                v.getContext().startActivity(musicPlayerIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfSongs.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView artistTextView;
        TextView titleTextView;
        LinearLayout linearLayout;

        ViewHolder(View view) {
            super(view);
            this.artistTextView = view.findViewById(R.id.song_artist);
            this.titleTextView = view.findViewById(R.id.song_title);
            linearLayout = view.findViewById(R.id.song_layout);
        }
    }
}
