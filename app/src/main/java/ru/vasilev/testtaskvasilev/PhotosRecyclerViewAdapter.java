package ru.vasilev.testtaskvasilev;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.vasilev.testtaskvasilev.data.Photo;

public class PhotosRecyclerViewAdapter extends RecyclerView.Adapter<PhotosRecyclerViewAdapter.ViewHolder> {

    private List<Photo> photos;
    private final OnClickPhotoListener mListener;

    public PhotosRecyclerViewAdapter(List<Photo> photos, OnClickPhotoListener mListener) {
        this.photos = photos;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_photo_item, parent, false);
        return new PhotosRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mItem =photos.get(position);
        Uri uri = Uri.parse(holder.mItem.getThumbnailUrl());
        Picasso.get()
                .load(uri)
                .resize(30, 30)
                .into(holder.mImageView);
        holder.mDescrView.setText(holder.mItem.getTitle());

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onClickPhoto(holder.mItem);
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDescrView;
        public final ImageView mImageView;
        public Photo mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDescrView = view.findViewById(R.id.photoDescription);
            mImageView = view.findViewById(R.id.photoPreview);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public interface OnClickPhotoListener {
        void onClickPhoto(Photo item);
    }
}
