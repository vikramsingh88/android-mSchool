package com.vikram.school.ui.student;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.vikram.school.R;
import com.vikram.school.ui.home.ClassesListResponse;
import com.vikram.school.ui.home.HomeViewModel;
import com.vikram.school.ui.slideshow.Classes;
import com.vikram.school.utility.Constants;
import com.vikram.school.utility.ImagePickerActivity;
import com.vikram.school.utility.PreferenceManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddStudentFragment extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String TAG = "AddStudentFragment";
    private Spinner spinner;
    private EditText editStudentName;
    private EditText editFatherName;
    private EditText editAddress;
    private EditText editMobile;
    private EditText editClassTeacher;
    private CircularImageView imgProfile;
    private Button btnAddStudent;
    private String mSelectedClass;
    private HomeViewModel homeViewModel;
    private StudentViewModel studentViewModel;
    public static final int REQUEST_IMAGE = 100;
    private String selectedClass;
    private boolean isUpdate;
    private String studentId;
    private ProgressBar mAddProgressBar;
    private Bitmap bitmap;
    private CheckBox chkTransport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_add_student);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        spinner = (Spinner) findViewById(R.id.spinner);
        editStudentName = (EditText) findViewById(R.id.edit_student_name);
        editFatherName = (EditText) findViewById(R.id.edit_father_name);
        editAddress = (EditText) findViewById(R.id.edit_address);
        editClassTeacher = (EditText) findViewById(R.id.edit_class_teacher);
        btnAddStudent = (Button) findViewById(R.id.btn_add_student);
        imgProfile = (CircularImageView) findViewById(R.id.img_student);
        spinner.setOnItemSelectedListener(this);
        mAddProgressBar = findViewById(R.id.add_student_progress);
        editMobile = findViewById(R.id.edit_mobile);
        chkTransport = findViewById(R.id.ckb_transport);

        Intent intent = getIntent();
        selectedClass = intent.getStringExtra("student_class");
        isUpdate = intent.getBooleanExtra("is_update", false);
        if (isUpdate) {
            studentId = intent.getStringExtra("student_id");
            editStudentName.setText(intent.getStringExtra("student_name"));
            editFatherName.setText(intent.getStringExtra("father_name"));
            editAddress.setText(intent.getStringExtra("address"));
            editMobile.setText(intent.getStringExtra("mobile"));
            chkTransport.setChecked(intent.getBooleanExtra("transport", false));
            spinner.setEnabled(false);
            editClassTeacher.setText(intent.getStringExtra("class_teacher"));
            btnAddStudent.setText(R.string.update_student);
            setTitle(R.string.update_student);
            if(intent.getStringExtra("image") != null) {
                byte[] imageAsBytes = Base64.decode(intent.getStringExtra("image"), Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                imgProfile.setImageBitmap(bitmap);
            }
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

        // Clearing older images from cache directory
        // don't call this line if you want to choose multiple images in the same activity
        // call this once the bitmap(s) usage is over
        ImagePickerActivity.clearCache(this);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProfileImageClick();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // update this bitmap to your server
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    // loading profile image from local cache
                    loadProfile(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadProfile(Uri uri) {
        Log.d(TAG, "Image cache path: " + uri);
        imgProfile.setImageURI(uri);
    }

    void onProfileImageClick() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            launchCameraIntent();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Log.d(TAG, "Permission denied");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(AddStudentFragment.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 100);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 100);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void addStudent() {
        String studentName = editStudentName.getText().toString();
        String fatherName = editFatherName.getText().toString();
        String address = editAddress.getText().toString();
        String classTeacher = editClassTeacher.getText().toString();
        String mobile = editMobile.getText().toString();
        boolean isTransport = chkTransport.isChecked();

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

        if (bitmap == null) {
            Toast.makeText(this, "Student image can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        //mobile number validation
        if(mobile != null && !mobile.isEmpty()) {
            if (mobile.startsWith("+91")) {
                Toast.makeText(this, "Remove country code from mobile number", Toast.LENGTH_SHORT).show();
                return;
            }
            mobile = "+91"+mobile;
        }
        Student student = new Student(studentName, fatherName, address, mSelectedClass, classTeacher, mobile);
        if (bitmap != null) {
            student.setImage(getBase64FromBitmap(bitmap));
        }
        //Log.d(Constants.TAG, TAG + " Image : " + student.getImage());
        Log.d(Constants.TAG, TAG + " Session : " + PreferenceManager.instance().getSession());
        student.setSession(PreferenceManager.instance().getSession());
        student.setTransport(isTransport);
        mAddProgressBar.setVisibility(View.VISIBLE);
        if (isUpdate && studentId != null) {
            student.set_id(studentId);
            studentViewModel.updateStudent(student).observe(this, new Observer<StudentResponse>() {
                @Override
                public void onChanged(StudentResponse response) {
                    mAddProgressBar.setVisibility(View.INVISIBLE);
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
                    mAddProgressBar.setVisibility(View.INVISIBLE);
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
        byte[] byteArray = byteArrayOutputStream.toByteArray();
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
                        Log.d(Constants.TAG, TAG + " Get class teacher by class name success");
                        if (response.getClassTeacherName() != null) {
                            editClassTeacher.setText(response.getClassTeacherName());
                        }
                    } else {
                        Log.e(Constants.TAG, TAG + " getting class teacher by class name failed");
                    }
                } else {
                    Log.e(Constants.TAG, TAG + " Error in getting class teacher by class name");
                }
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
