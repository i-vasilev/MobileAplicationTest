package ru.vasilev.testtaskvasilev.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.vasilev.testtaskvasilev.R;
import ru.vasilev.testtaskvasilev.data.Album;
import ru.vasilev.testtaskvasilev.data.IO.IOType;
import ru.vasilev.testtaskvasilev.data.IO.db.DBHelper;
import ru.vasilev.testtaskvasilev.data.IO.network.APIService;
import ru.vasilev.testtaskvasilev.data.IO.network.ApiServiceFactory;
import ru.vasilev.testtaskvasilev.data.adapters.MyAlbumsRecyclerViewAdapter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AlbumsFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private IOType ioType;

    public static final String IO_TYPE_NAME = "IO-TYPE";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AlbumsFragment() {
    }

    public static AlbumsFragment newInstance(IOType ioType) {
        AlbumsFragment fragment = new AlbumsFragment();
        Bundle args = new Bundle();
        args.putString(IO_TYPE_NAME, ioType.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_albums_list, container, false);
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ioType = IOType.valueOf(getArguments().getString(IO_TYPE_NAME));

        switch (ioType) {
            case Network:
                ApiServiceFactory apiServiceFactory = new ApiServiceFactory();
                APIService apiService = apiServiceFactory.Read();
                final Call<List<Album>> listCallAlbums = apiService.loadAlbums();

                listCallAlbums.enqueue(new Callback<List<Album>>() {
                    @Override
                    public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                        if (response.isSuccessful()) {
                            List<Album> albums = response.body();
                            MyAlbumsRecyclerViewAdapter adapter = new MyAlbumsRecyclerViewAdapter(albums, mListener, ioType);
                            recyclerView.setAdapter(adapter);
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Album>> call, Throwable t) {
                    }
                });
                break;
            case DB:
                DBHelper dbHelper = new DBHelper(getContext());
                final List<Album> albums = dbHelper.selectAlbums();
                MyAlbumsRecyclerViewAdapter adapter = new MyAlbumsRecyclerViewAdapter(albums, mListener, ioType);
                recyclerView.setAdapter(adapter);
                recyclerView.getAdapter().notifyDataSetChanged();
                break;
        }

        return recyclerView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Album item, IOType ioType);
    }
}
