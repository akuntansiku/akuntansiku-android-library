package id.co.akuntansiku.utils.retrofit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


import java.io.IOException;

import id.co.akuntansiku.utils.ConfigAkuntansiku;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(Activity context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Authorization", sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_API_TOKEN_TYPE, "null") + " " + sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_API_TOKEN, "null"))
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .connectTimeout(40, TimeUnit.SECONDS)
//                .readTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .build();

        OkHttpClient client = httpClient.build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ConfigAkuntansiku.AKUNTANSIKU_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
