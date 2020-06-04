package com.vikram.school.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.vikram.school.R;
import com.vikram.school.utility.Constants;

//add new class/update existing class
public class SlideshowFragment extends AppCompatActivity {
    private String TAG = "AddClassFragment";
    private EditText editClassTeacherName;
    private EditText editClassName;
    private EditText editClassFees;
    private EditText editClassExamFees;
    private Button btnAddClass;
    private ClassesViewModel classViewModel;
    private String classId;
    private boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_slideshow);
        classViewModel = ViewModelProviders.of(this).get(ClassesViewModel.class);
        editClassTeacherName = (EditText) findViewById(R.id.edit_class_teacher_name);
        editClassName = (EditText) findViewById(R.id.edit_class_name);
        editClassFees = (EditText) findViewById(R.id.edit_class_fees);
        editClassExamFees = (EditText) findViewById(R.id.edit_class_exam_fees);
        btnAddClass = (Button) findViewById(R.id.btn_add_class);

        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addClass(isUpdate);
            }
        });

        //check if activity launched for updating class data
        Intent intent = getIntent();
        isUpdate = intent.getBooleanExtra("isUpdate", false);
        if (isUpdate) {
            classId = intent.getStringExtra("id");
            editClassTeacherName.setText(intent.getStringExtra("classTeacher"));
            editClassName.setText(intent.getStringExtra("className"));
            editClassName.setEnabled(false);
            editClassFees.setText(intent.getStringExtra("monthlyFee"));
            editClassExamFees.setText(intent.getStringExtra("examFee"));
            setTitle(R.string.update_class);
            btnAddClass.setText(R.string.update_class);
        }
    }

    private void addClass(boolean isUpdate) {
        String classTeacherName = editClassTeacherName.getText().toString();
        String className = editClassName.getText().toString();
        String classFees = editClassFees.getText().toString();
        String classExamFees = editClassExamFees.getText().toString();

        if (classTeacherName == null || classTeacherName.isEmpty()) {
            Toast.makeText(this, "Class teacher name can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (className == null || className.isEmpty()) {
            Toast.makeText(this, "Class name can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (classFees == null || classFees.isEmpty()) {
            Toast.makeText(this, "Class fees can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (classExamFees == null || classExamFees.isEmpty()) {
            Toast.makeText(this, "Class exam fees can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Classes classes = new Classes(classTeacherName, className, classFees, classExamFees);
        //update
        if (isUpdate && classId != null) {
            classes.set_id(classId);
            classViewModel.updateClasses(classes).observe(this, new Observer<ClassesResponse>() {
                @Override
                public void onChanged(ClassesResponse response) {
                    if (response != null) {
                        if (response.isSuccess()) {
                            Log.d(Constants.TAG, TAG+" Class updated successfully");
                            Toast.makeText(SlideshowFragment.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                            if (response.getClasses() != null) {
                                Log.d(Constants.TAG, TAG + " updated class id "+response.getClasses().get_id());
                            }
                            editClassTeacherName.setText("");
                            editClassName.setText("");
                            finish();
                        } else {
                            Log.e(Constants.TAG, TAG+" Updating class failed");
                            Toast.makeText(SlideshowFragment.this, "updating a class failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(Constants.TAG, TAG+" Error in updating class");
                    }
                }
            });
        } else { //add new
            classViewModel.addClasses(classes).observe(this, new Observer<ClassesResponse>() {
                @Override
                public void onChanged(ClassesResponse response) {
                    if (response != null) {
                        if (response.isSuccess()) {
                            Log.d(Constants.TAG, TAG+" Class added successfully");
                            Toast.makeText(SlideshowFragment.this, "New class created successfully", Toast.LENGTH_SHORT).show();
                            if (response.getClasses() != null) {
                                Log.d(Constants.TAG, TAG + " Added class id "+response.getClasses().get_id());
                            }
                            editClassTeacherName.setText("");
                            editClassName.setText("");
                            finish();
                        } else {
                            Log.e(Constants.TAG, TAG+" Adding class failed");
                            Toast.makeText(SlideshowFragment.this, "Adding a new class failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(Constants.TAG, TAG+" Error in adding class");
                    }
                }
            });
        }
    }
}
