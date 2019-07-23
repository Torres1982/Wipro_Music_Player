package com.wipro.wipro_music_player;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface {
    String BASE_URL = "https://jsonplaceholder.typicode.com/";

    @GET("photos/{id}")
    Call<ResponseBody> getAlbumCover();
}
