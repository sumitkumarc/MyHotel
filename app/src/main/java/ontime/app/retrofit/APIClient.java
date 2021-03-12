package ontime.app.retrofit;


import ontime.app.okhttp.AppConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class APIClient {
    static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @NotNull
                @Override
                public Response intercept(@NotNull Chain chain) throws IOException {
                    Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjBjM2U4OGM2ZGQxOGU0NTk3OWJkNzI5MjkxZmIyMjk3ZWQ4Y2UwNTllNmViZGQ3NjhjYjkyNzg4NzhjYjQwMTFiYjdjNDYxNjIyZjcyNmU5In0.eyJhdWQiOiI3IiwianRpIjoiMGMzZTg4YzZkZDE4ZTQ1OTc5YmQ3MjkyOTFmYjIyOTdlZDhjZTA1OWU2ZWJkZDc2OGNiOTI3ODg3OGNiNDAxMWJiN2M0NjE2MjJmNzI2ZTkiLCJpYXQiOjE2MDE5MDU0NTgsIm5iZiI6MTYwMTkwNTQ1OCwiZXhwIjoxNjMzNDQxNDU4LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.dBw5ZB1A4YKHelGWh_Biso1gG_GY5YqfQRixvdZ_QA9fby8oJVgjJTGWv2fMU-QYrNkFiB7fC4L2o9xnErPpOGGAlKmDiYYiqKfKuooe23NZNC8g53zcbRpMlQJECf4JUH-PoVxqwPEOXaeYhz3cDmwRvpUAA8tTj4prC5Jo6kc_QGm8fr-fXk47gvIyuexoapIOdHQ2BnvKV0lf0IXnH-_f3sz2pAwQ0Tt3SXccquUv8L5_qwX2YA8S26h2VufKn2D5sKNDkZot4Ok4Q2V_G0T0X5t_pJSjQrzoeSu8_NRU0qBCof2ENUaybGbkeFgm-LPtAsckCN8HXidpzika0riHamG58aEaiqE3bIr6q2JiWTf0tg0yzztM_arP27dOLgfqNh6U603CtF-Mpc-5E1zy89ppGPu-YmDcTD8Rsaw_H7RGSbrYmHo-ZLubrWwrkACPxGL_wJT2SC5NeZV5pDfsZOqxtgGOCIz7hIXIUrT7yE9LhNccKv3ygDgdKxyuyHe21XcMty9xH44JZGPEbYoh5Ras5j4rWfLQKD2oJMJrzYLUhQU6KJcowQuAR_fRf2MHKTYKzJJYVYpUTIaYq-k81jOmW4P3a-fp2fb149on8p770S9B4DweQxdRUy1WDtu-Qctmi2YIJaN3Jy-F_XeGjKXPO5bBp2h7GSG0YGQ").build();
                    return chain.proceed(request);
                }
            });

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstant.BASE_URL1)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }

        return retrofit;
    }

}
