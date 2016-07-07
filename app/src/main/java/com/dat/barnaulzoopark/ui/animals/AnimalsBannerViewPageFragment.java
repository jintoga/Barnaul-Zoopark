package com.dat.barnaulzoopark.ui.animals;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Nguyen on 6/20/2016.
 */
public class AnimalsBannerViewPageFragment extends Fragment implements View.OnClickListener {
    public static final String ARGUMENT_PHOTO = "Photo";

    @Bind(R.id.banner_image)
    protected SimpleDraweeView bannerImage;
    @Bind(R.id.banner_title)
    protected TextView bannerTitle;
    @Bind(R.id.banner_description)
    protected TextView bannerDescription;
    View view;

    private Uri photo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String photoUrl = getArguments().getString(ARGUMENT_PHOTO);
        if (photoUrl != null) {
            photo = Uri.parse(photoUrl);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_animals_banner_item, container, false);
        ButterKnife.bind(this, view);
        if (photo != null) {
            bannerImage.setImageURI(photo);
        }
        bannerImage.setOnClickListener(this);
        bannerTitle.setText("Banner's Title");
        bannerDescription.setText("Banner's Description asfjsaifj asifja isfias ");
        return view;
    }

    @Override
    public void onClick(View v) {
        if (photo != null) {
            Log.d("Fragment", photo.getPath());
        }
    }
}
