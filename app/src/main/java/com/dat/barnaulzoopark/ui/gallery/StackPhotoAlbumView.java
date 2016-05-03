package com.dat.barnaulzoopark.ui.gallery;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.dat.barnaulzoopark.R;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DAT on 22-Feb-16.
 */
public class StackPhotoAlbumView extends LinearLayout {

    @Bind(R.id.image1)
    protected SimpleDraweeView image1;
    @Bind(R.id.image2)
    protected SimpleDraweeView image2;
    @Bind(R.id.image3)
    protected SimpleDraweeView image3;
    @Bind(R.id.card1)
    protected CardView card1;
    @Bind(R.id.card2)
    protected CardView card2;
    @Bind(R.id.card3)
    protected CardView card3;


    public StackPhotoAlbumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.stack_album_view, this);
        ButterKnife.bind(this);
    }

    public void setData(final String[] data) {
        if (data != null) {
            if (data.length >= 3) {
                image1.setImageURI(Uri.parse(data[0]));
                image2.setImageURI(Uri.parse(data[1]));
                image3.setImageURI(Uri.parse(data[2]));
            } else if (data.length == 2) {
                image1.setImageURI(Uri.parse(data[0]));
                image2.setImageURI(Uri.parse(data[1]));
                card3.setVisibility(GONE);
            } else if (data.length == 1) {
                image1.setImageURI(Uri.parse(data[0]));
                card2.setVisibility(GONE);
                card3.setVisibility(GONE);
            }
        }
    }
}
