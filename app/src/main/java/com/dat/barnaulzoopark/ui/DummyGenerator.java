package com.dat.barnaulzoopark.ui;

import com.dat.barnaulzoopark.ui.gallery.model.PhotoAlbum;

import java.util.ArrayList;

/**
 * Created by DAT on 23-Feb-16.
 */
public class DummyGenerator {
    public static ArrayList<PhotoAlbum> getDummyData() {

        ArrayList<PhotoAlbum> dummyData = new ArrayList<>();
        PhotoAlbum album1 = new PhotoAlbum();
        album1.setDate("2013 г.");
        album1.setName("День рождения зоопарка");
        String[] urls = new String[]{
                "http://s11.postimg.org/aft369v1v/dog_how_to_select_your_new_best_friend_thinkstoc.jpg",
                "http://s22.postimg.org/3ydo64c3l/cutest_cat_ever_snoopy_face_2.jpg",
                "http://s12.postimg.org/h2j3q4z7h/Y7_SJw_HIFt_Ks.jpg"
        };
        album1.setUrls(urls);

        PhotoAlbum album2 = new PhotoAlbum();
        album2.setDate("2014 г.");
        album2.setName("День рождения зоопарка");
        album2.setUrls(new String[]{
                "http://zoo22.ru/images/stories/fotoalbom_2013/1.png",
                "http://zoo22.ru/images/stories/fotoalbom_2013/5.png",
                "http://zoo22.ru/images/stories/fotoalbom_2013/14.png"
        });

        PhotoAlbum album3 = new PhotoAlbum();
        album3.setDate("2015 г.");
        album3.setName("Благоустройство зоопарка");
        album3.setUrls(new String[]{
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/1._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/13._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/19._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg"
        });

        PhotoAlbum album4 = new PhotoAlbum();
        album4.setDate("2015 г.");
        album4.setName("Благоустройство зоопарка");
        album4.setUrls(new String[]{
                "http://s11.postimg.org/aft369v1v/dog_how_to_select_your_new_best_friend_thinkstoc.jpg",
                "http://s22.postimg.org/3ydo64c3l/cutest_cat_ever_snoopy_face_2.jpg",
                "http://s12.postimg.org/h2j3q4z7h/Y7_SJw_HIFt_Ks.jpg"
        });
        PhotoAlbum album5 = new PhotoAlbum();
        album5.setDate("2015 г.");
        album5.setName("Благоустройство зоопарка");
        album5.setUrls(new String[]{
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/1._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/13._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/19._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg"
        });
        PhotoAlbum album6 = new PhotoAlbum();
        album6.setDate("2015 г.");
        album6.setName("Благоустройство зоопарка");
        album6.setUrls(new String[]{
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/1._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/13._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg"
        });

        PhotoAlbum album7 = new PhotoAlbum();
        album7.setDate("2015 г.");
        album7.setName("Благоустройство зоопарка");
        album7.setUrls(new String[]{
                "http://s11.postimg.org/aft369v1v/dog_how_to_select_your_new_best_friend_thinkstoc.jpg"
        });

        PhotoAlbum album8 = new PhotoAlbum();
        album8.setDate("2015 г.");
        album8.setName("Благоустройство зоопарка");
        album8.setUrls(new String[]{
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/1._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/13._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg"
        });

        PhotoAlbum album9 = new PhotoAlbum();
        album9.setDate("2015 г.");
        album9.setName("Благоустройство зоопарка");
        album9.setUrls(new String[]{
                "http://s11.postimg.org/aft369v1v/dog_how_to_select_your_new_best_friend_thinkstoc.jpg",
                "http://s22.postimg.org/3ydo64c3l/cutest_cat_ever_snoopy_face_2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/1._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/13._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg"
        });
        PhotoAlbum album10 = new PhotoAlbum();
        album10.setDate("2015 г.");
        album10.setName("Благоустройство зоопарка");
        album10.setUrls(new String[]{
                "http://s11.postimg.org/aft369v1v/dog_how_to_select_your_new_best_friend_thinkstoc.jpg",
                "http://s22.postimg.org/3ydo64c3l/cutest_cat_ever_snoopy_face_2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/1._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/13._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg"
        });

        PhotoAlbum album11 = new PhotoAlbum();
        album11.setDate("2015 г.");
        album11.setName("Благоустройство зоопарка");
        album11.setUrls(new String[]{
                "http://s11.postimg.org/aft369v1v/dog_how_to_select_your_new_best_friend_thinkstoc.jpg",
                "http://s22.postimg.org/3ydo64c3l/cutest_cat_ever_snoopy_face_2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/1._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/13._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg"
        });

        PhotoAlbum album12 = new PhotoAlbum();
        album12.setDate("2015 г.");
        album12.setName("Благоустройство зоопарка");
        album12.setUrls(new String[]{
                "http://s11.postimg.org/aft369v1v/dog_how_to_select_your_new_best_friend_thinkstoc.jpg",
                "http://s22.postimg.org/3ydo64c3l/cutest_cat_ever_snoopy_face_2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/1._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg",
                "http://zoo22.ru/images/stories/d_r_zooparka_2015/13._%D1%84%D0%BE%D1%82%D0%BE_%D0%92.%D0%91%D0%BE%D1%80%D0%B8%D1%81%D0%BE%D0%B2.jpg"
        });
        dummyData.add(album1);
        dummyData.add(album2);
        dummyData.add(album3);
        dummyData.add(album4);
        dummyData.add(album5);
        dummyData.add(album6);
        dummyData.add(album7);
        dummyData.add(album8);
        dummyData.add(album9);
        dummyData.add(album10);
        dummyData.add(album11);
        dummyData.add(album12);

        return dummyData;
    }

}
