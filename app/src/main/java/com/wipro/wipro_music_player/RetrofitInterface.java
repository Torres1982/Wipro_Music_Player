package com.wipro.wipro_music_player;

import com.wipro.wipro_music_player.model.AlbumCover;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitInterface {
    String BASE_URL = "https://jsonplaceholder.typicode.com/";

    @GET("photos/{album_id}")
    Call<AlbumCover> getAlbumCover(@Path("album_id") int id);
}
