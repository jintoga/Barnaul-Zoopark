package com.dat.barnaulzoopark.model;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DAT on 3/10/2017.
 */
@IgnoreExtraProperties
public class Animal {
    private String uid;
    private String speciesUid;
    private String photoSmall;
    private String photoBig;
    private String aboutOurAnimal;
    private long timeEnter;
    private long timeOfBirth;
    private Map<String, String> photos = new HashMap<>();
}
