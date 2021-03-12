package ontime.app.retrofit;


import ontime.app.model.RestaurantMenu.RstMenuExample;
import ontime.app.model.usermain.ExampleUser;
import ontime.app.okhttp.AppConstant;
import ontime.app.restaurant.model.readerOrder.ReaderExample;
import com.google.gson.JsonObject;

import org.json.JSONObject;
//
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST(AppConstant.GET_READER_ORDER_ACCEPT)
    Call<ReaderExample> getAPICallAcceptOrder(@Body JsonObject object);

    @POST(AppConstant.GET_READER_ORDER_REJECT)
    Call<ReaderExample> getAPICallRejectOrder(@Body JsonObject object);

    @POST(AppConstant.GET_READER_ORDER_COMPLETE)
    Call<ReaderExample> getAPICallCompleteOrder(@Body JsonObject object);

    @POST(AppConstant.GET_USER_CANCEL_ORDER)
    Call<ExampleUser> getAPICallUserCancleOrder(@Body JsonObject object,@Header("Authorization") String aut, @Header("Accept") String s, @Header("Accept-Language") String l, @Header("app-key") String key);

    @GET(AppConstant.GET_WALLET)
    Call<ExampleUser> getAPICallWallets(@Header("Authorization") String aut, @Header("Accept") String s, @Header("Accept-Language") String l, @Header("app-key") String key);


}
