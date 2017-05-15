package com.dat.barnaulzoopark.pushnotification;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by DAT on 5/15/2017.
 */

public interface NotificationApi {
    @POST("fcm/send")
    Observable<Object> sendNotification(@Body RequestBody requestBody);
}
