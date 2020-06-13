package com.vikram.school.api;

import com.vikram.school.ui.addfee.AddFeeResponse;
import com.vikram.school.ui.addfee.Fee;
import com.vikram.school.ui.home.ClassFeesResponse;
import com.vikram.school.ui.home.ClassesListResponse;
import com.vikram.school.ui.listfee.ListFeesResponse;
import com.vikram.school.ui.login.LoginResult;
import com.vikram.school.ui.login.User;
import com.vikram.school.ui.message.Message;
import com.vikram.school.ui.message.MessageResponse;
import com.vikram.school.ui.slideshow.Classes;
import com.vikram.school.ui.slideshow.ClassesResponse;
import com.vikram.school.ui.student.ClassTeacherResponse;
import com.vikram.school.ui.student.Student;
import com.vikram.school.ui.student.StudentResponse;
import com.vikram.school.ui.student.liststudent.ListStudentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ISchoolAPI {
    @Headers({
            "Content-Type: application/json",
            "Accept-Charset: utf-8"
    })
    @POST("users/login/")
    Call<LoginResult> login(@Body User user);

    @Headers({
            "Content-Type: application/json",
            "Accept-Charset: utf-8"
    })
    @POST("student/")
    Call<StudentResponse> addStudent(@Body Student student, @Header("Authorization") String authHeader);

    @Headers({
            "Content-Type: application/json",
            "Accept-Charset: utf-8"
    })
    @POST("update-student/")
    Call<StudentResponse> updateStudent(@Body Student student, @Header("Authorization") String authHeader);

    @GET("/student/{class}/{session}")
    Call<ListStudentResponse> getStudentByClass(@Path("class") String className, @Path("session") String session, @Header("Authorization") String authHeader);

    @Headers({
            "Content-Type: application/json",
            "Accept-Charset: utf-8"
    })
    @POST("student-fee/")
    Call<AddFeeResponse> addFee(@Body Fee fee, @Header("Authorization") String authHeader);

    @Headers({
            "Content-Type: application/json",
            "Accept-Charset: utf-8"
    })
    @POST("student-fee-update/")
    Call<AddFeeResponse> updateFee(@Body Fee fee, @Header("Authorization") String authHeader);

    @GET("/student-fee/{studentId}/{session}")
    Call<ListFeesResponse> getStudentFeesByStudentId(@Path("studentId") String studentId, @Path("session") String session, @Header("Authorization") String authHeader);

    @Headers({
            "Content-Type: application/json",
            "Accept-Charset: utf-8"
    })
    @POST("classes/")
    Call<ClassesResponse> addClasses(@Body Classes classes, @Header("Authorization") String authHeader);

    @Headers({
            "Content-Type: application/json",
            "Accept-Charset: utf-8"
    })
    @POST("update-class/")
    Call<ClassesResponse> updateClasses(@Body Classes classes, @Header("Authorization") String authHeader);

    @GET("classes/{session}")
    Call<ClassesListResponse> getClasses(@Path("session") String session, @Header("Authorization") String authHeader);

    @GET("classes/{className}/{session}")
    Call<ClassTeacherResponse> getClassTeacherByClass(@Path("className") String className, @Path("session") String session, @Header("Authorization") String authHeader);

    @GET("classes-class-exam-fees/{className}/{session}")
    Call<ClassFeesResponse> getClassFeesByClass(@Path("className") String className, @Path("session") String session, @Header("Authorization") String authHeader);

    @Headers({
            "Content-Type: application/json",
            "Accept-Charset: utf-8"
    })
    @POST("send-message/")
    Call<MessageResponse> sendMessage(@Body Message message, @Header("Authorization") String authHeader);
}
