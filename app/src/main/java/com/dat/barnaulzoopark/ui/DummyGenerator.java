package com.dat.barnaulzoopark.ui;

import com.dat.barnaulzoopark.ui.gallery.model.Photo;
import com.dat.barnaulzoopark.ui.gallery.model.PhotoAlbum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 23-Feb-16.
 */
public class DummyGenerator {

    static List<PhotoAlbum> dummyData = new ArrayList<>();

    public static List<PhotoAlbum> getDummyData() {

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
                "http://www.zoo22.ru/upload/iblock/f5f/f5ff2baf4217f4c415a238c98008e68f.png",
                "http://www.zoo22.ru/upload/iblock/d01/d01e2fbf7c72c0d8e0d07814156ae9fd.png",
                "http://www.zoo22.ru/upload/iblock/d54/d545cc7012a53682658dfed57da70251.png"
        });

        PhotoAlbum album3 = new PhotoAlbum();
        album3.setDate("2015 г.");
        album3.setName("Благоустройство зоопарка");
        album3.setUrls(new String[]{
                "http://www.zoo22.ru/upload/iblock/05a/05ab85cdf16792f2efeb1a279ba399b0.jpg",
                "http://www.zoo22.ru/upload/iblock/024/024d113a2d4b8f44554eef348fc9affb.png",
                "http://www.zoo22.ru/upload/iblock/e55/e55f7897ac7a6f628900f1ef41558f26.png"
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
                "http://www.zoo22.ru/upload/iblock/05a/05ab85cdf16792f2efeb1a279ba399b0.jpg",
                "http://www.zoo22.ru/upload/iblock/024/024d113a2d4b8f44554eef348fc9affb.png",
                "http://www.zoo22.ru/upload/iblock/e55/e55f7897ac7a6f628900f1ef41558f26.png"
        });
        PhotoAlbum album6 = new PhotoAlbum();
        album6.setDate("2015 г.");
        album6.setName("Благоустройство зоопарка");
        album6.setUrls(new String[]{
                "http://www.zoo22.ru/upload/iblock/05a/05ab85cdf16792f2efeb1a279ba399b0.jpg",
                "http://www.zoo22.ru/upload/iblock/e55/e55f7897ac7a6f628900f1ef41558f26.png"
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
                "http://www.zoo22.ru/upload/iblock/05a/05ab85cdf16792f2efeb1a279ba399b0.jpg",
                "http://www.zoo22.ru/upload/iblock/e55/e55f7897ac7a6f628900f1ef41558f26.png"
        });

        PhotoAlbum album9 = new PhotoAlbum();
        album9.setDate("2015 г.");
        album9.setName("Благоустройство зоопарка");
        album9.setUrls(new String[]{
                "http://s11.postimg.org/aft369v1v/dog_how_to_select_your_new_best_friend_thinkstoc.jpg",
                "http://s22.postimg.org/3ydo64c3l/cutest_cat_ever_snoopy_face_2.jpg",
                "http://www.zoo22.ru/upload/iblock/05a/05ab85cdf16792f2efeb1a279ba399b0.jpg",
                "http://www.zoo22.ru/upload/iblock/024/024d113a2d4b8f44554eef348fc9affb.png",
                "http://www.zoo22.ru/upload/iblock/e55/e55f7897ac7a6f628900f1ef41558f26.png"
        });
        PhotoAlbum album10 = new PhotoAlbum();
        album10.setDate("2015 г.");
        album10.setName("Благоустройство зоопарка");
        album10.setUrls(new String[]{
                "http://s11.postimg.org/aft369v1v/dog_how_to_select_your_new_best_friend_thinkstoc.jpg",
                "http://s22.postimg.org/3ydo64c3l/cutest_cat_ever_snoopy_face_2.jpg",
                "http://www.zoo22.ru/upload/iblock/05a/05ab85cdf16792f2efeb1a279ba399b0.jpg",
                "http://www.zoo22.ru/upload/iblock/e55/e55f7897ac7a6f628900f1ef41558f26.png"
        });

        PhotoAlbum album11 = new PhotoAlbum();
        album11.setDate("2015 г.");
        album11.setName("Благоустройство зоопарка");
        album11.setUrls(new String[]{
                "http://s11.postimg.org/aft369v1v/dog_how_to_select_your_new_best_friend_thinkstoc.jpg",
                "http://s22.postimg.org/3ydo64c3l/cutest_cat_ever_snoopy_face_2.jpg",
                "http://www.zoo22.ru/upload/iblock/05a/05ab85cdf16792f2efeb1a279ba399b0.jpg",
                "http://www.zoo22.ru/upload/iblock/024/024d113a2d4b8f44554eef348fc9affb.png",
                "http://www.zoo22.ru/upload/iblock/e55/e55f7897ac7a6f628900f1ef41558f26.png"
        });

        PhotoAlbum album12 = new PhotoAlbum();
        album12.setDate("2015 г.");
        album12.setName("Благоустройство зоопарка");
        album12.setUrls(new String[]{
                "http://s11.postimg.org/aft369v1v/dog_how_to_select_your_new_best_friend_thinkstoc.jpg",
                "http://s22.postimg.org/3ydo64c3l/cutest_cat_ever_snoopy_face_2.jpg",
                "http://www.zoo22.ru/upload/iblock/05a/05ab85cdf16792f2efeb1a279ba399b0.jpg",
                "http://www.zoo22.ru/upload/iblock/024/024d113a2d4b8f44554eef348fc9affb.png",
                "http://www.zoo22.ru/upload/iblock/e55/e55f7897ac7a6f628900f1ef41558f26.png"
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

        int i = 0;
        for (PhotoAlbum album : dummyData) {
            album.setId(String.valueOf(i++));
        }
        return dummyData;
    }

    public static List<Photo> getPhotoAlbumById(String albumId) {
        List<Photo> data = new ArrayList<>();
        for (PhotoAlbum album : dummyData) {
            if (albumId.equals(album.getId())) {
                for (int i = 0; i < album.getUrls().length; i++) {
                    Photo photo = new Photo();
                    photo.setDate(album.getDate());
                    photo.setId(String.valueOf(i));
                    photo.setUrl(album.getUrls()[i]);
                    data.add(photo);
                }
            }
        }
        return data;
    }

}
