package ru.vasilev.testtaskvasilev.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.vasilev.testtaskvasilev.R;
import ru.vasilev.testtaskvasilev.data.Album;
import ru.vasilev.testtaskvasilev.data.IO.IOType;
import ru.vasilev.testtaskvasilev.data.IO.db.DBHelper;
import ru.vasilev.testtaskvasilev.data.IO.network.APIService;
import ru.vasilev.testtaskvasilev.data.IO.network.RetrofitClient;
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
        updateList();
        return recyclerView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && ioType != null) {
            updateList();
        }
    }

    public void updateList() {
        switch (ioType) {
            case Network:
                APIService apiService = RetrofitClient.getInstance().create(APIService.class);
                fetchDate(apiService);
                break;
            case DB:
                DBHelper dbHelper = new DBHelper(getContext());
                displayData(dbHelper.selectAlbums());
                break;
        }
    }

    private void fetchDate(APIService apiService) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(apiService.loadAlbums()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayData));
    }

    private void displayData(List<Album> albums) {
        MyAlbumsRecyclerViewAdapter adapter = new MyAlbumsRecyclerViewAdapter(albums, mListener, ioType);
        recyclerView.setAdapter(adapter);
    }

    public IOType getIOType() {
        return ioType;
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
