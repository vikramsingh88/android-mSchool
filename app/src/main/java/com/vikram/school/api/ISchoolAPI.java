package com.vikram.school.api;

import com.vikram.school.ui.addfee.AddFeeResponse;
import com.vikram.school.ui.addfee.Fee;
import com.vikram.school.ui.home.ClassFeesResponse;
import com.vikram.school.ui.home.ClassesListResponse;
import com.vikram.school.ui.listfee.ListFeesResponse;
import com.vikram.school.ui.login.LoginResult;
import com.vikram.school.ui.login.User;
import com.vikram.school.ui.slideshow.Classes;
import com.vikram.school.ui.slideshow.ClassesResponse;
import com.vikram.school.ui.student.ClassTeacherResponse;
import com.vikram.school.ui.student.Student;
import com.vikram.school.ui.student.StudentResponse;
import com.vikram.school.ui.student.liststudent.ListStudentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ISchoolAPI {
    @Headers({
            "Content-Type: application/json",
            "Accept-Charset: utf-8"
    })
    @POST("login/")
    Call<LoginResult> login(@Body User user);

    @Headers({
            "Content-Type: application/json",
            "Accept-Charset: utf-8"
    })
    @POST("student/")
    Call<StudentResponse> addStudent(@Body Student student);

    @Headers({
            "Content-Type: application/json",
            "Accept-Charset: utf-8"
    })
    @POST("update-student/")
    Call<StudentResponse> updateStudent(@Body Student student);

    @GET("/student/{class}")
    Call<ListStudentResponse> getStudentByClass(@Path("class") String className);

    @Headers({
            "Content-Type: application/json",
            "Accept-Charset: utf-8"
    })
    @POST("student-fee/")
    Call<AddFeeResponse> addFee(@Body Fee fee);

    @GET("/student-fee/{studentId}")
    Call<ListFeesResponse> getStudentFeesByStudentId(@Path("studentId") String studentId);

    @Headers({
            "Content-Type: application/json",
            "Accept-Charset: utf-8"
    })
    @POST("classes/")
    Call<ClassesResponse> addClasses(@Body Classes classes);

    @Headers({
            "Content-Type: application/json",
            "Accept-Charset: utf-8"
    })
    @POST("update-class/")
    Call<ClassesResponse> updateClasses(@Body Classes classes);

    @GET("classes/")
    Call<ClassesListResponse> getClasses();

    @GET("classes/{className}")
    Call<ClassTeacherResponse> getClassTeacherByClass(@Path("className") String className);

    @GET("classes-class-exam-fees/{className}")
    Call<ClassFeesResponse> getClassFeesByClass(@Path("className") String className);
}