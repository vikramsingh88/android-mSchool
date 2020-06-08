package com.vikram.school.ui.student;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vikram.school.api.ISchoolAPI;
import com.vikram.school.api.WebService;
import com.vikram.school.ui.login.LoginResult;
import com.vikram.school.utility.Constants;
import com.vikram.school.utility.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentRepository {
    private String TAG = "StudentRepository";
    private ISchoolAPI iSchoolAPI;

    public LiveData<StudentResponse> addStudent(Student student) {
        iSchoolAPI = WebService.instance().getRetrofitClient().create(ISchoolAPI.class);
        final MutableLiveData<StudentResponse> studentResult = new MutableLiveData<StudentResponse>();
        Call<StudentResponse> call = iSchoolAPI.addStudent(student);
        Log.d(Constants.TAG, TAG+" Try to add student");
        call.enqueue(new Callback<StudentResponse>() {
            @Override
            public void onResponse(Call<StudentResponse> call, Response<StudentResponse> response) {
                Log.d(Constants.TAG, TAG+" add student request success");
                StudentResponse tempResult = response.body();
                studentResult.setValue(tempResult);
            }

            @Override
            public void onFailure(Call<StudentResponse> call, Throwable t) {
                Log.e(Constants.TAG, TAG+" Error in adding student");
                studentResult.setValue(null);
            }
        });
        return studentResult;
    }

    //update student
    public LiveData<StudentResponse> updateStudent(Student student) {
        iSchoolAPI = WebService.instance().getRetrofitClient().create(ISchoolAPI.class);
        final MutableLiveData<StudentResponse> studentResult = new MutableLiveData<StudentResponse>();
        Call<StudentResponse> call = iSchoolAPI.updateStudent(student);
        Log.d(Constants.TAG, TAG+" Try to update student");
        call.enqueue(new Callback<StudentResponse>() {
            @Override
            public void onResponse(Call<StudentResponse> call, Response<StudentResponse> response) {
                Log.d(Constants.TAG, TAG+" update student request success");
                StudentResponse tempResult = response.body();
                studentResult.setValue(tempResult);
            }

            @Override
            public void onFailure(Call<StudentResponse> call, Throwable t) {
                Log.e(Constants.TAG, TAG+" Error in updating student");
            }
        });
        return studentResult;
    }

    //get class teacher by class name
    public LiveData<ClassTeacherResponse> getClassTeacherByClass(String className) {
        iSchoolAPI = WebService.instance().getRetrofitClient().create(ISchoolAPI.class);
        final MutableLiveData<ClassTeacherResponse> classTeacherResult = new MutableLiveData<ClassTeacherResponse>();
        Call<ClassTeacherResponse> call = iSchoolAPI.getClassTeacherByClass(className, PreferenceManager.instance().getSession());
        Log.d(Constants.TAG, TAG+" Try to get class teacher by class name");
        call.enqueue(new Callback<ClassTeacherResponse>() {
            @Override
            public void onResponse(Call<ClassTeacherResponse> call, Response<ClassTeacherResponse> response) {
                Log.d(Constants.TAG, TAG+" get class teacher by class name request success");
                ClassTeacherResponse tempResult = response.body();
                classTeacherResult.setValue(tempResult);
            }

            @Override
            public void onFailure(Call<ClassTeacherResponse> call, Throwable t) {
                Log.e(Constants.TAG, TAG+" Error in class teacher by class name");
            }
        });
        return classTeacherResult;
    }
}
