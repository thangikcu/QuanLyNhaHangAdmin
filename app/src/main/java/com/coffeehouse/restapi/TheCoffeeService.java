package com.coffeehouse.restapi;

import com.coffeehouse.model.entity.Bill;
import com.coffeehouse.model.entity.Desk;
import com.coffeehouse.model.entity.DrinkType;
import com.coffeehouse.model.entity.User;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TheCoffeeService {

    @POST("api/employee/login")
    Call<ResponseData<User>> login(@Body RequestBody requestBody);

    @POST("/api/employee/change-password")
    Call<ResponseData<String>> changePassword(@Body RequestBody requestBody);

    @GET("api/desk/get-all")
    Call<ResponseData<List<Desk>>> getListDesk();

    @GET("api/drink/menu")
    Call<ResponseData<List<DrinkType>>> getListDrink();

    @GET("api/order-history/bill-by-deskId")
    Call<ResponseData<Bill>> getOrderDetail(@Query("deskId") String deskId);

    @POST("api/order-history/create-bill")
    Call<ResponseData<Bill>> createBill(@Body RequestBody requestBody);

    @POST("api/order-history/update-bill")
    Call<ResponseData<String>> updateBill(@Body RequestBody requestBody);

    @POST("api/order-history/paid-bill")
    Call<ResponseData<String>> payment(@Query("orderId") String orderId);

    @GET("api/beverage/get-all")
    Call<ResponseData<List<DrinkType>>> getAllDrinkType();

    @POST("api/drink/save")
    Call<ResponseData<String>> addDrink(@Body RequestBody requestBody);
}

