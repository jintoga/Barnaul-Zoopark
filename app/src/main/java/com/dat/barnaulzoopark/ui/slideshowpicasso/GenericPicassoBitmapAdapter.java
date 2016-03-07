package com.dat.barnaulzoopark.ui.slideshowpicasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;

import com.marvinlabs.widget.slideshow.adapter.BitmapAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by DAT on 06-Feb-16.
 */
public abstract class GenericPicassoBitmapAdapter<T> extends BitmapAdapter {
    // URLs of the images to load
    private List<T> items;
    private int width;
    private int height;
    // Targets currently in use by Picasso
    private SparseArray<SlideTarget> activeTargets;

    //==============================================================================================
    // GENERAL METHODS
    //==

    /**
     * Constructor
     *
     * @param context The context in which the adapter is created (activity)
     * @param items   The URLs of the images to load
     */
    public GenericPicassoBitmapAdapter(Context context, Collection<T> items, int width, int height) {
        super(context);
        this.items = new ArrayList<>(items);
        this.activeTargets = new SparseArray<SlideTarget>(3);
        this.width = width;
        this.height = height;
    }

    //==============================================================================================
    // INTERFACE IMPLEMENTATION: Adapter
    //==

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //==============================================================================================
    // BITMAP LOADING
    //==

    /**
     * Stop all running download tasks. This method should be called when your activity gets
     * stopped (in {#onStop})
     */
    public void shutdown() {
        activeTargets.clear();

        // Not necessary with the singleton usage
        // Picasso.with(this).shutdown();
    }

    @Override
    protected void onBitmapLoaded(int position, Bitmap bitmap) {
        activeTargets.remove(position);
        super.onBitmapLoaded(position, bitmap);
    }

    @Override
    protected void onBitmapNotAvailable(int position) {
        activeTargets.remove(position);
        super.onBitmapNotAvailable(position);
    }

    @Override
    protected void loadBitmap(final int position) {
        if (position < 0 || position >= items.size()) onBitmapNotAvailable(position);

        SlideTarget target = new SlideTarget(position);
        activeTargets.put(position, target);

        RequestCreator rc = createRequestCreator(Picasso.with(getContext()), items.get(position));
        if (width > 0 && height > 0) {
            rc.resize(width, height).centerInside().into(target);
        } else {
            rc.into(target);
        }
    }


    /**
     * Create the Picasso request. Subclasses can customize it by simply overriding this method. By
     * default, we could use noFade() and skipMemoryCache()
     *
     * @param picasso The picasso instance to use
     * @param item    The item for which to load the image
     * @return The request creator object from Picasso
     */
    protected abstract RequestCreator createRequestCreator(Picasso picasso, T item);


    /**
     * A target for Picasso to load the bitmap into
     */
    private class SlideTarget implements Target {
        int position;

        private SlideTarget(int position) {
            this.position = position;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
            GenericPicassoBitmapAdapter.this.onBitmapLoaded(position, bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable drawable) {
            GenericPicassoBitmapAdapter.this.onBitmapNotAvailable(position);
        }

        @Override
        public void onPrepareLoad(Drawable drawable) {
        }
    }

}
