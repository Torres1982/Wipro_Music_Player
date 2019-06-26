package com.wipro.wipro_music_player;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private List<SongModel> listOfSongs;

    MusicAdapter(List<SongModel> list) {
        listOfSongs = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View musicItem = inflater.inflate(R.layout.song_row, parent, false);
        return new ViewHolder(musicItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        final SongModel songsList = listOfSongs.get(position);
        viewHolder.artistTextView.setText(listOfSongs.get(position).getArtist());
        viewHolder.titleTextView.setText(listOfSongs.get(position).getTitle());

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putString("artist_name", songsList.getArtist());
                extras.putString("song_title", songsList.getTitle());
                extras.putString("song_path", songsList.getPath());
                extras.putLong("song_length", songsList.getLength());
                extras.putDouble("song_size", songsList.getSize());
                extras.putInt("song_position", position);
                Intent musicPlayerIntent = new Intent(v.getContext(), MusicPlayer.class);
                musicPlayerIntent.putExtras(extras);
                v.getContext().startActivity(musicPlayerIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfSongs.size();
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
