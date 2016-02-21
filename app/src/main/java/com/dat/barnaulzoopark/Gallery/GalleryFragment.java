package com.dat.barnaulzoopark.Gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dat.barnaulzoopark.Gallery.Adapters.GalleryAlbumAdapter;
import com.dat.barnaulzoopark.Gallery.Adapters.GalleryAlbumHorizontalAdapter;
import com.dat.barnaulzoopark.Gallery.Model.GalleryAlbum;
import com.dat.barnaulzoopark.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by DAT on 07-Feb-16.
 */
public class GalleryFragment extends Fragment {

    @InjectView(R.id.recyclerViewAlbums)
    protected RecyclerView recyclerViewAlbums;
    GalleryAlbumAdapter adapter;
    GalleryAlbumHorizontalAdapter adapter2;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.inject(this, view);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewAlbums.setLayoutManager(staggeredGridLayoutManager);


        ArrayList<GalleryAlbum> data = getDummyData();
        adapter = new GalleryAlbumAdapter(data, getContext());

       /* adapter2 = new GalleryAlbumHorizontalAdapter(data, getContext());
        recyclerViewAlbums.setAdapter(adapter2);*/
    }

    public ArrayList<GalleryAlbum> getDummyData() {

        ArrayList<GalleryAlbum> dummyData = new ArrayList<>();
        GalleryAlbum album1 = new GalleryAlbum();
        album1.setDate("2013 г.");
        album1.setName("День рождения зоопарка");
        String[] urls = new String[]{
                "http://s11.postimg.org/aft369v1v/dog_how_to_select_your_new_best_friend_thinkstoc.jpg",
                "http://s22.postimg.org/3ydo64c3l/cutest_cat_ever_snoopy_face_2.jpg",
                "http://s12.postimg.org/h2j3q4z7h/Y7_SJw_HIFt_Ks.jpg"
        };
        album1.setUrls(urls);

        GalleryAlbum album2 = new GalleryAlbum();
        album2.setDate("2014 г.");
        album2.setName("День рождения зоопарка");
        album2.setUrls(new String[]{
                "http://zoo22.ru/images/stories/fotoalbom_2013/1.png",
                "http://zoo22.ru/images/stories/fotoalbom_2013/5.png",
                "http://zoo22.ru/images/stories/fotoalbom_2013/14.png"
        });

        GalleryAlbum album3 = new GalleryAlbum();
        album3.setDate("2015 г.");
        album3.setName("Благоустройство зоопарка");
        album3.setUrls(new String[]{
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/1._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/13._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/19._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg"
        });

        GalleryAlbum album4 = new GalleryAlbum();
        album4.setDate("2015 г.");
        album4.setName("Благоустройство зоопарка");
        album4.setUrls(new String[]{
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/1._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/13._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/19._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg"
        });
        GalleryAlbum album5 = new GalleryAlbum();
        album5.setDate("2015 г.");
        album5.setName("Благоустройство зоопарка");
        album5.setUrls(new String[]{
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/1._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/13._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/19._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg"
        });
        dummyData.add(album1);
        dummyData.add(album2);
        dummyData.add(album3);
        dummyData.add(album4);
        dummyData.add(album5);

        return dummyData;
    }
}
