package com.hosp.oxygen.entry.util;




import com.hosp.oxygen.entry.ui.airportuser.AirportUserRequest;
import com.hosp.oxygen.entry.ui.airportuser.AirportUserResponse;
import com.hosp.oxygen.entry.ui.hospuser.HospUSerQRCodeResponse;
import com.hosp.oxygen.entry.ui.hospuser.HospUserFormRequest;
import com.hosp.oxygen.entry.ui.hospuser.HospUserFormResponse;
import com.hosp.oxygen.entry.ui.hospuser.HospUserQRCodeReq;
import com.hosp.oxygen.entry.ui.hospuser.ImageUploadResponse;
import com.hosp.oxygen.entry.ui.login.LoginRequest;
import com.hosp.oxygen.entry.ui.login.LoginResponse;
import com.hosp.oxygen.entry.ui.signup.SignUpRequest;
import com.hosp.oxygen.entry.ui.signup.SignUpResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIInterface {

    @POST("/api/tokens")
    Call<LoginResponse> login(@Header("Content-Type") String Content_Type, @Body LoginRequest user);

    @POST("/api/signup")
    Call<SignUpResponse> signup(@Header("Content-Type") String Content_Type, @Body SignUpRequest user);

    @POST("/api/formbuilderapp/saveOxygenConcentration")
    Call<AirportUserResponse> syncoc(@Header("Content-Type") String Content_Type, @Header("Authorization") String Authorization, @Body AirportUserRequest user);

    @GET("/api/formbuilderapp/getOxygenConcentration")
    Call<HospUSerQRCodeResponse> getOxygenConcentration(@Header("Content-Type") String Content_Type, @Header("Authorization") String Authorization, @Query("qrdata") String qrdata);

    @POST("/api/formbuilderapp/saveHospitalData")
    Call<HospUserFormResponse> saveHospitalData(@Header("Content-Type") String Content_Type, @Header("Authorization") String Authorization, @Body HospUserFormRequest user);

    @Multipart
    @POST("/api/uploadS3/s3")
    Call<ImageUploadResponse> Imageupload(@Header("Authorization") String Authorization, @Part MultipartBody.Part file);

//    http://aeltest.nabh.co:5002/api/onsiteassessment/getAllocHospsForLoggedInAsr_app?limit=10&offset=1
   /* @GET("/api/onsiteassessment/getAllocHospsForLoggedInAsr_app") //ayush
//    @GET("/api/onsiteassessment/getAllocationListForLoggedInUser_app")
    Call<List<HospitalResponse>>getHospitalList(@Header("Content-Type") String Content_Type, @Header("Authorization") String Authorization,@Query("limit") int limit, @Query("offset") int offset);

    @GET("/api/onsiteassessment/getAllocationListForLoggedInUser_app")
    Call<AllocationResponse>getAllocationList(@Header("Content-Type") String Content_Type, @Header("Authorization") String Authorization, @Query("limit") int limit, @Query("offset") int offset);

    @GET
    Call<SosModel> getScopesForLoggedInUser(@Header("Content-Type") String Content_Type, @Header("Authorization") String Authorization, @Url String url);

  *//*  //    formbuilder/getFormBuilder
    @GET("/api/formbuilder/getFormBuilder")
    Call<GetJsonQuestionData> getQuestionData(@Header("Content-Type") String Content_Type, @Header("Authorization") String Authorization,@Query("form_id") String form_id);*//*

    //    formbuilder/getFormBuilder
    @GET
    Call<GetJsonQuestionData> getQuestionData(@Header("Content-Type") String Content_Type, @Header("Authorization") String Authorization,@Url String url);

//    http://aeltest.nabh.co:5002/api/formbuilderapp/assessment

    @POST
    Call<GeneralModelResponse> SubmitAsmt(@Header("Content-Type") String Content_Type, @Header("Authorization") String Authorization,@Url String url);

    @POST("/api/formbuilderapp/assessment")
    Call<EntityResponse> saveOrUpdateAssessment(@Header("Content-Type") String Content_Type,@Header("Authorization") String Authorization, @Body SectionQuestionResponse data);

    @POST("/api/onsiteassessment/saveIntigrity")
    Call<EntityResponse> saveIntigrity(@Header("Content-Type") String Content_Type,@Header("Authorization") String Authorization, @Body SaveIntigrityRequest data);

    @POST("/api/onsiteassessment/saveGen")
    Call<GeneralModelResponse> saveGeneralData(@Header("Content-Type") String Content_Type,@Header("Authorization") String Authorization, @Body GeneralModel data);

    @POST("/api/onsiteassessment/mobileOTPgenration")
    Call<GeneralModelResponse> mobileOTPgenration(@Header("Content-Type") String Content_Type,@Header("Authorization") String Authorization, @Body MobileOtpGenerateRequest data);

    @POST("/api/onsiteassessment/verifyMobileOtp")
    Call<GeneralModelResponse> verifyMobileOtp(@Header("Content-Type") String Content_Type,@Header("Authorization") String Authorization, @Body MobileOtpGenerateRequest data);

    @POST("/api/onsiteassessment/saveSos")
    Call<GeneralModelResponse> saveSosData(@Header("Content-Type") String Content_Type,@Header("Authorization") String Authorization, @Body SosMainModel data);

    @Multipart
    @POST("/api/uploadS3/s3")
    Call<ImageUploadResponse> Imageupload(@Header("Authorization") String Authorization,@Part MultipartBody.Part file);
*/
}
