package ru.vasilev.testtaskvasilev.call;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.vasilev.testtaskvasilev.data.Album;
import ru.vasilev.testtaskvasilev.data.Photo;

public interface APIService {
    String URL = "https://jsonplaceholder.typicode.com/";

    @GET("albums")
    Call<List<Album>> loadAlbums();

    @GET("photos")
    Call<List<Photo>> loadPhotos(@Query("albumId") int albumId);
}
