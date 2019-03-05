package com.coffeehouse.restapi;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import com.coffeehouse.model.entity.Bill;
import com.coffeehouse.model.entity.Desk;
import com.coffeehouse.model.entity.DrinkType;
import com.coffeehouse.model.entity.User;

public interface TheCoffeeService {

    @POST("api/employee/login")
    Call<ResponseData<User>> login(@Body RequestBody requestBody);

    @GET("api/desk/get-all")
    Call<ResponseData<List<Desk>>> getListDesk();

    @GET("api/drink/menu")
    Call<ResponseData<List<DrinkType>>> getListDrink();

    @GET("api/order-history-detail")
    Call<ResponseData<Bill>> getOrderDetail(@Query("deskId") String deskId);
}

