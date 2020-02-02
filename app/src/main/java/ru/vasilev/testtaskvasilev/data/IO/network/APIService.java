package ru.vasilev.testtaskvasilev.data.IO.network;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.vasilev.testtaskvasilev.data.Album;
import ru.vasilev.testtaskvasilev.data.Photo;

public interface APIService {
    String URL = "https://jsonplaceholder.typicode.com/";

    @GET("albums")
    Observable<List<Album>> loadAlbums();

    @GET("photos")
    Observable<List<Photo>> loadPhotos(@Query("albumId") int albumId);
}
