package com.coffeehouse.restapi;

import com.coffeehouse.model.entity.Bill;
import com.coffeehouse.model.entity.Desk;
import com.coffeehouse.model.entity.DrinkType;
import com.coffeehouse.model.entity.TurnOver;
import com.coffeehouse.model.entity.TurnOverDetail;
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

    @GET("api/desk/save-desk")
    Call<ResponseData<String>> addDesk(@Query("deskId") String deskId);

    @GET("api/desk/delete-desk-by-id")
    Call<ResponseData<String>> deleteDesk(@Query("deskId") String deskId);

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

    @POST("api/drink/update")
    Call<ResponseData<String>> updateDrink(@Body RequestBody requestBody);

    @GET("api/drink/delete-by-id")
    Call<ResponseData<String>> deleteDrink(@Query("beverageId") long drinkId);

    @POST("api/beverage/save")
    Call<ResponseData<String>> addDrinkType(@Body RequestBody requestBody);

    @POST("api/beverage/update-beverage")
    Call<ResponseData<String>> updateDrinkType(@Body RequestBody requestBody);

    @GET("/api/beverage/delete-beverage-by-id")
    Call<ResponseData<String>> deleteDrinkType(@Query("beverageId") long drinkTypeId);

    @GET("api/employee/generate-qr-code")
    Call<ResponseData<String>> generateQrcode();

    @POST("api/employee/decrypt-qr-code")
    Call<ResponseData<String>> decryptQrcode(@Body RequestBody requestBody);

    @GET("api/employee/get-employees")
    Call<ResponseData<List<User>>> getListEmployee();

    @POST("api/employee/register")
    Call<ResponseData<User>> addEmployee(@Body RequestBody requestBody);

    @POST("api/employee/update-employee")
    Call<ResponseData<String>> updateEmployee(@Body RequestBody requestBody);

    @GET("api/employee/get-employee-by-id")
    Call<ResponseData<User>> getWorkingTimeReport(@Query("employeeId") String employeeId,
                                                  @Query("month") String month);

    @GET("api/order-history/turn-over")
    Call<ResponseData<List<TurnOver>>> getTurnOver(@Query("yearOrMonth") String yearOrMonth,
                                                   @Query("turnOverStatus") String turnOverStatus);

    @GET("api/order-history/turn-over-details")
    Call<ResponseData<List<TurnOverDetail>>> getTurnOverDetail(@Query("yearOrMonth") String yearOrMonth,
                                                               @Query("turnOverStatus") String turnOverStatus,
                                                               @Query("pageIndex") int pageIndex);
}

