package com.dat.barnaulzoopark.ui.photosdetail;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseFragment;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Created by Dat on 10-Aug-16.
 */
public class PhotosDetailPageFragment extends BaseFragment {

    @Bind(R.id.photo_drawee_view)
    protected PhotoDraweeView photoDraweeView;
    private static final String KEY_PHOTO_URL = "PHOTO_URL";

    public static PhotosDetailPageFragment newInstance(String photo_url) {
        PhotosDetailPageFragment fragment = new PhotosDetailPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PHOTO_URL, photo_url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos_detail_page, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            String photo_url = getArguments().getString(KEY_PHOTO_URL);
            initPhotoDrawee(photo_url);
        }
        return view;
    }

    private void initPhotoDrawee(String photo_url) {
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setUri(photo_url);
        controller.setOldController(photoDraweeView.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || photoDraweeView == null) {
                    return;
                }
                photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        photoDraweeView.setController(controller.build());
    }
}
