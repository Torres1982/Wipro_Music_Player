package com.wipro.wipro_music_player.model;

public class AlbumCover {
    private int albumId;
    private int id;
    private String title;
    private String url;
    private String thumbnailUrl;

    public AlbumCover(int id, String albumCoverUrl) {
        this.id = id;
        this.thumbnailUrl = albumCoverUrl;
    }

    public int getAlbumId() { return albumId; }
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
    public String getThumbnailUrl() { return thumbnailUrl; }
}
