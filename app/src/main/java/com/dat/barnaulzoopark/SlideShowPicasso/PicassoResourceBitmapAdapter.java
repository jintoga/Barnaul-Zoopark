package com.dat.barnaulzoopark.SlideShowPicasso;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.Collection;

/**
 * Created by DAT on 06-Feb-16.
 */
public class PicassoResourceBitmapAdapter extends GenericPicassoBitmapAdapter<Integer> {

    public PicassoResourceBitmapAdapter(Context context, Collection<Integer> items, int width, int height) {
        super(context, items, width, height);
    }

    @Override
    protected RequestCreator createRequestCreator(Picasso picasso, Integer resourceId ) {
        return picasso.load(resourceId).noFade().skipMemoryCache();
    }


}
