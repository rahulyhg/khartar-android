package com.vardhamaninfo.khartargaccha.Retrofit;


import com.google.gson.JsonObject;
import com.vardhamaninfo.khartargaccha.Util.Constant;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface RestInterface {


    //Login User
    //"email_address", "password", "longitude", "latitude","device_id"
    @FormUrlEncoded
    @POST(Constant.LOGIN)
    Call<JsonObject> userLogin(@Field("email_address") String email_address,
                               @Field("password") String password,
                               @Field("longitude") String longitude,
                               @Field("latitude") String latitude,
                               @Field("device_id") String device_id);


    // Sign Up
    //"username", "email_address", "password", "latitude", "longitude", "user_type", "device_id", "mobile_number"
    @FormUrlEncoded
    @POST(Constant.REGISTRATION)
    Call<JsonObject> userSignUp(@Field("username") String username,
                                @Field("email_address") String email_address,
                                @Field("password") String password,
                                @Field("latitude") String latitude,
                                @Field("longitude") String longitude,
                                @Field("user_type") String user_type,
                                @Field("device_id") String device_id,
                                @Field("mobile_number") String mobile_number);


    //"email_address", "code", "password"
    @FormUrlEncoded
    @POST(Constant.POSTFORGOTPASSWORD)
    Call<JsonObject> userForgotPasswordConfirm(@Field("email_address") String email_address,
                                               @Field("code") String code,
                                               @Field("password") String password);


    //"latitude", "longitude"
    @FormUrlEncoded
    @POST(Constant.Update_loc + "{key_id}")
    Call<JsonObject> userUpdateLocation(@Field("latitude") String latitude,
                                        @Field("longitude") String longitude,
                                        @Path("key_id") String key_id);




    //date
    @FormUrlEncoded
    @POST(Constant.calender_events )
    Call<JsonObject> getCalenderEvents(@Field("date") String date);



    // Samadhan
    //"user_id", "question"
    @FormUrlEncoded
    @POST(Constant.Samadhan)
    Call<JsonObject> userSamadhan(@Field("user_id") String user_id,
                                  @Field("question") String question);

    //Single Tirth
    @GET(Constant.SINGLETIRTH + "{uid}")
    Call<JsonObject> getSingleTirth(@Path("uid") String uid);

    //Single Event
    @GET(Constant.GETSINGLEEVENT + "{evuid}")
    Call<JsonObject> getSingleEvent(@Path("evuid") String evuid);

    //Forgot Password
    @GET(Constant.GETFORGOTPASSWORD + "{email}")
    Call<JsonObject> forgotPassword(@Path("email") String email);


    //Panchang Date Detail
    @GET(Constant.Panchang + "/{date}")
    Call<JsonObject> getPanchangDateDetail(@Path("date") String date);

    //Single Sadhu Detail
    @GET(Constant.Single_uid + "{userid}")
    Call<JsonObject> getSingleSadhuDetail(@Path("userid") String userid);

    //Search Sadhu Detail
    @GET(Constant.UserSearch + "{name}" + "/Sadhu")
    Call<JsonObject> getSearchSadhuDetail(@Path("name") String name);

    //Search Sadhvi Detail
    @GET(Constant.UserSearch + "{name}" + "/Sadhvi")
    Call<JsonObject> getSearchSadhviDetail(@Path("name") String name);

    //Search Dadawadi Detail
    @GET(Constant.DadawadiSearch + "{name}" + "/dadawadi")
    Call<JsonObject> getSearchDadawadiDetail(@Path("name") String name);

    //Search Tirth Detail
    @GET(Constant.DadawadiSearch + "{name}" + "/tirth")
    Call<JsonObject> getSearchTirthDetail(@Path("name") String name);

    //Stavan Audio Detail
    @GET(Constant.S_sangrah_single + "{id}")
    Call<JsonObject> getStavanAudioDetail(@Path("id") String id);

    //Dadawadi Details
    @GET(Constant.Get_Dadawadi)
    Call<JsonObject> getDadawadiDetails();

    //Signal Dadawadi Details
    @GET(Constant.single_Get_Dadawadi + "{userid}")
    Call<JsonObject> getSignalDadawadiDetails(@Path("userid") String userid);

    //Event Details
    @GET(Constant.GETEVENT)
    Call<JsonObject> getEventDetails();


    //Constant.S_sangrah + "Pachakhan"
    //Gallery Details
    @GET(Constant.Gallery)
    Call<JsonObject> getGalleryDetails();

    //All Sadhu Details
    @GET("admin/users/status/sadhu")
    Call<JsonObject> getAllSadhuDetails();

//     //All Sadhu Details
//    @GET(Constant.Get_All_Users + "sadhu")
//    Call<JsonObject> getAllSadhuDetails();

    //All Sadhvi Details
    @GET(Constant.Get_All_Users + "sadhvi")
    Call<JsonObject> getAllSadhviDetails();

    //Stavan Details
    @GET(Constant.S_sangrah + "Stavan")
    Call<JsonObject> getStavanDetails();

    //Pachakhan Details
    @GET(Constant.S_sangrah + "Pachakhan")
    Call<JsonObject> getPachakhanDetails();


    //Stotra Fragment Details
    // @GET(Constant.S_sangrah + "StotraFragment")/stavansangrah/search/StotraFragment
    @GET("stavansangrah")
    Call<JsonObject> getStotraDetails();

    //Signal Tirth Details
    @GET(Constant.SINGLETIRTH + "{userid}")
    Call<JsonObject> getSignalTirthDetails(@Path("userid") String userid);

    //All Tirth Details
    @GET(Constant.GETTIRTH)
    Call<JsonObject> getAllTirthDetails();

    //Today Event Details
    @GET(Constant.Today_events + "{date}")
    Call<JsonObject> getTodayEventsDetails(@Path("date") String date);

    //Youtube Videos
    @GET(Constant.YOUTUBE_Channel_Id)
    Call<JsonObject> getYouTubeVideosDetails();


}


