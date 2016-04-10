package com.dat.barnaulzoopark.ui.gallery;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.dat.barnaulzoopark.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DAT on 22-Feb-16.
 */
public class StackPhotoAlbumView extends LinearLayout {

    @Bind(R.id.image1)
    protected RoundedImageView image1;
    @Bind(R.id.image2)
    protected RoundedImageView image2;
    @Bind(R.id.image3)
    protected RoundedImageView image3;
    @Bind(R.id.card1)
    protected CardView card1;
    @Bind(R.id.card2)
    protected CardView card2;
    @Bind(R.id.card3)
    protected CardView card3;

    private Context context;

    public StackPhotoAlbumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.stack_album_view, this);
        ButterKnife.bind(this);
    }

    public void setData(final String[] data) {
        if (data != null) {
            if (data.length >= 3) {
                Picasso.with(context).load(data[0]).fit().centerCrop().into(image1);
                Picasso.with(context).load(data[1]).fit().centerCrop().into(image2);
                Picasso.with(context).load(data[2]).fit().centerCrop().into(image3);
            } else if (data.length == 2) {
                Picasso.with(context).load(data[0]).fit().centerCrop().into(image1);
                Picasso.with(context).load(data[1]).fit().centerCrop().into(image2);
                card3.setVisibility(GONE);
            } else if (data.length == 1) {
                Picasso.with(context).load(data[0]).fit().centerCrop().into(image1);
                card2.setVisibility(GONE);
                card3.setVisibility(GONE);
            }
        }
    }


}
