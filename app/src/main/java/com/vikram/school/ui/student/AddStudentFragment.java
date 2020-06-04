package com.vikram.school.ui.student;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Base64;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.vikram.school.R;
import com.vikram.school.ui.home.ClassesListResponse;
import com.vikram.school.ui.home.HomeViewModel;
import com.vikram.school.ui.slideshow.Classes;
import com.vikram.school.utility.Constants;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddStudentFragment extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String TAG = "AddStudentFragment";
    private Spinner spinner;
    private EditText editStudentName;
    private EditText editFatherName;
    private EditText editAddress;
    private EditText editClassTeacher;
    private ImageView imgProfile;
    private Button btnAddStudent;
    private String mSelectedClass;
    private HomeViewModel homeViewModel;
    private StudentViewModel studentViewModel;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Bitmap roundedBitmap;
    private String selectedClass;
    private boolean isUpdate;
    private String studentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_add_student);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        spinner = (Spinner) findViewById(R.id.spinner);
        editStudentName = (EditText)findViewById(R.id.edit_student_name);
        editFatherName = (EditText)findViewById(R.id.edit_father_name);
        editAddress = (EditText)findViewById(R.id.edit_address);
        editClassTeacher = (EditText)findViewById(R.id.edit_class_teacher);
        btnAddStudent = (Button) findViewById(R.id.btn_add_student);
        imgProfile = (ImageView) findViewById(R.id.img_student);
        spinner.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        selectedClass = intent.getStringExtra("student_class");
        isUpdate = intent.getBooleanExtra("is_update", false);
        if (isUpdate) {
            studentId = intent.getStringExtra("student_id");
            editStudentName.setText(intent.getStringExtra("student_name"));
            editFatherName.setText(intent.getStringExtra("father_name"));
            editAddress.setText(intent.getStringExtra("address"));
            spinner.setEnabled(false);
            editClassTeacher.setText(intent.getStringExtra("class_teacher"));
            btnAddStudent.setText(R.string.update_student);
            setTitle(R.string.update_student);
        }

        homeViewModel.getClasses().observe(this, new Observer<ClassesListResponse>() {
            @Override
            public void onChanged(ClassesListResponse classes) {
                updateRoomSpinner(classes);
            }
        });

        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudent();
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePicture();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)  {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            roundedBitmap = getRoundedRectBitmap(photo);
            imgProfile.setImageBitmap(roundedBitmap);
        }
    }

    private void capturePicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            } else {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    private Bitmap getRoundedRectBitmap(Bitmap bitmap) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, 200, 200);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(50, 50, 50, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError o) {
            o.printStackTrace();
        }
        return result;
    }
    
    private void addStudent() {
        String studentName = editStudentName.getText().toString();
        String fatherName = editFatherName.getText().toString();
        String address = editAddress.getText().toString();
        String classTeacher = editClassTeacher.getText().toString();
        
        if (studentName == null || studentName.isEmpty()) {
            Toast.makeText(this, "Student name can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fatherName == null || fatherName.isEmpty()) {
            Toast.makeText(this, "Father name can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (address == null || address.isEmpty()) {
            Toast.makeText(this, "Address name can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (classTeacher == null || classTeacher.isEmpty()) {
            Toast.makeText(this, "Class teacher name can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mSelectedClass == null || mSelectedClass.isEmpty()) {
            Toast.makeText(this, "Student class can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Student student = new Student(studentName, fatherName, address, mSelectedClass, classTeacher);
        if (roundedBitmap != null) {
            student.setImage(getBase64FromBitmap(roundedBitmap));
        }
        Log.d(Constants.TAG, TAG+" Image : "+student.getImage());
        if (isUpdate && studentId != null) {
            student.set_id(studentId);
            studentViewModel.updateStudent(student).observe(this, new Observer<StudentResponse>() {
                @Override
                public void onChanged(StudentResponse response) {
                    if (response != null) {
                        if (response.isSuccess()) {
                            Log.d(Constants.TAG, TAG + " Student updated successfully");
                            Toast.makeText(AddStudentFragment.this, "Student details updated successfully", Toast.LENGTH_SHORT).show();
                            editStudentName.setText("");
                            editFatherName.setText("");
                            editAddress.setText("");
                            if (response.getStudent() != null) {
                                Log.d(Constants.TAG, TAG + " updated student id " + response.getStudent().get_id());
                            }
                            finish();
                        } else {
                            Log.e(Constants.TAG, TAG + " updating student failed");
                            Toast.makeText(AddStudentFragment.this, "updating student failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(Constants.TAG, TAG + " Error in updating student");
                        Toast.makeText(AddStudentFragment.this, "Updating student details failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            studentViewModel.addStudent(student).observe(this, new Observer<StudentResponse>() {
                @Override
                public void onChanged(StudentResponse response) {
                    if (response != null) {
                        if (response.isSuccess()) {
                            Log.d(Constants.TAG, TAG + " Student added successfully");
                            Toast.makeText(AddStudentFragment.this, "Student added successfully", Toast.LENGTH_SHORT).show();
                            editStudentName.setText("");
                            editFatherName.setText("");
                            editAddress.setText("");
                            if (response.getStudent() != null) {
                                Log.d(Constants.TAG, TAG + " Added student id " + response.getStudent().get_id());
                            }
                            finish();
                        } else {
                            Log.e(Constants.TAG, TAG + " Adding student failed");
                            Toast.makeText(AddStudentFragment.this, "Adding a new student failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(Constants.TAG, TAG + " Error in adding student");
                        Toast.makeText(AddStudentFragment.this, "Adding a new student failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private String getBase64FromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    private void updateRoomSpinner(ClassesListResponse classes) {
        List<String> stringClasses = new ArrayList<>();
        if (classes != null) {
            List<Classes> listClasses = classes.getClasses();
            for (Classes room : listClasses) {
                stringClasses.add(room.getClassName());
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddStudentFragment.this, R.layout.room_spinner_item, stringClasses);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        if (selectedClass != null) {
            int spinnerPosition = dataAdapter.getPosition(selectedClass);
            spinner.setSelection(spinnerPosition);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSelectedClass = parent.getItemAtPosition(position).toString();

        // get class teacher by class name
        studentViewModel.getClassTeacherByClass(mSelectedClass).observe(this, new Observer<ClassTeacherResponse>() {
            @Override
            public void onChanged(ClassTeacherResponse response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        Log.d(Constants.TAG, TAG+" Get class teacher by class name success");
                        if (response.getClassTeacherName() != null) {
                            editClassTeacher.setText(response.getClassTeacherName());
                        }
                    } else {
                        Log.e(Constants.TAG, TAG+" getting class teacher by class name failed");
                    }
                } else {
                    Log.e(Constants.TAG, TAG+" Error in getting class teacher by class name");
                }
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
