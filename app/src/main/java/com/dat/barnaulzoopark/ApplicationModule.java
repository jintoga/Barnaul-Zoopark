package com.dat.barnaulzoopark;

import android.content.Context;
import com.dat.barnaulzoopark.pushnotification.NotificationApi;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DAT on 2/26/2017.
 */

@Module
class ApplicationModule {
    private BZApplication application;

    ApplicationModule(BZApplication application) {
        this.application = application;
    }

    @Provides
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    BZApplication provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @Singleton
    OkHttpClient provideHttpClient() {
        OkHttpClient.Builder okHttp = new OkHttpClient.Builder();
        return okHttp.build();
    }

    @Provides
    @Singleton
    NotificationApi provideNotificationApi() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(chain -> {
            String token = "key=" + BuildConfig.FCM_TOKEN;
            Request request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", token)
                .build();
            return chain.proceed(request);
        }).build();

        //Temporary solution for sending push notification using Firebase Cloud Messaging
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(okHttpClient)
            .build();
        return retrofit.create(NotificationApi.class);
    }

    @Provides
    @Singleton
    PreferenceHelper providePreferenceHelper() {
        return new PreferenceHelper(application.getApplicationComponent().context());
    }
}
