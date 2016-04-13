package com.dat.barnaulzoopark.ui.slideshowpicasso;

import android.content.Context;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import java.util.Collection;

/**
 * Created by DAT on 06-Feb-16.
 */
public class PicassoRemoteBitmapAdapter extends GenericPicassoBitmapAdapter<String> {

    public PicassoRemoteBitmapAdapter(Context context, Collection<String> items, int width, int height) {
        super(context, items, width, height);
    }

    @Override
    protected RequestCreator createRequestCreator(Picasso picasso, String url) {
        return picasso.load(url).noFade().skipMemoryCache();
    }
}