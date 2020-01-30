package ru.vasilev.testtaskvasilev.call;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.vasilev.testtaskvasilev.data.Album;

public interface APIService {
    @GET("albums")
    Call<List<Album>> loadAlbums();
}
