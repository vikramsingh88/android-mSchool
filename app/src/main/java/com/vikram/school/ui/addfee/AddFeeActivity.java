package com.vikram.school.ui.addfee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vikram.school.R;
import com.vikram.school.ui.addfee.printer.IPrintStatus;
import com.vikram.school.ui.addfee.printer.PrinterFormatter;
import com.vikram.school.ui.addfee.printer.USBPrinter;
import com.vikram.school.ui.home.ClassFeesResponse;
import com.vikram.school.ui.home.HomeViewModel;
import com.vikram.school.utility.Constants;
import com.vikram.school.utility.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class AddFeeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, IPrintStatus {
    private String TAG = "AddFeeActivity";
    private EditText editStudentName;
    private Spinner spinnerFeeType;
    private EditText editAmount;
    private Button btnAddFee;
    private DatePicker picker;
    private TextView txtClassFees;
    private TextView txtClassExamFees;
    private String selectedFeeType;
    private String selectedDate;
    private AddFeeViewModel addFeeViewModel;
    private HomeViewModel homeViewModel;
    private String studentName;
    private String fatherName;
    private String studentId;
    private String studentClass;
    private String monthlyFee;
    private String examFee;
    private ProgressBar mAddProgress;
    private USBPrinter usbPrinter;
    private String amount;
    private String advance;
    private String remaining;
    private String session;
    private String feesId;
    private String type;
    private boolean isUpdate;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fee);
        addFeeViewModel = ViewModelProviders.of(this).get(AddFeeViewModel.class);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        editStudentName = (EditText) findViewById(R.id.edit_student_name);
        spinnerFeeType = (Spinner) findViewById(R.id.spinner_fee_type);
        editAmount = (EditText) findViewById(R.id.edit_amount);
        btnAddFee = (Button) findViewById(R.id.btn_add_fee);
        picker = (DatePicker) findViewById(R.id.date);
        txtClassFees = (TextView) findViewById(R.id.txt_lbl_class_fees);
        txtClassExamFees = (TextView) findViewById(R.id.txt_lbl_class_exam_fees);
        mAddProgress = findViewById(R.id.add_fee_progress);

        usbPrinter = new USBPrinter();

        List<String> feesType = new ArrayList<>();
        feesType.add(Constants.monthly);
        feesType.add(Constants.exam);
        feesType.add(Constants.advance);
        feesType.add(Constants.transport);

        Intent intent = getIntent();
        studentId = intent.getStringExtra("student_id");
        studentName = intent.getStringExtra("student_name");
        fatherName = intent.getStringExtra("father_name");
        studentClass = intent.getStringExtra("student_class");
        mobile = intent.getStringExtra("mobile");
        editStudentName.setText(studentName);
        //check if activity launched for fees update
        isUpdate = intent.getBooleanExtra("is_update", false);
        if (isUpdate) {
            amount = intent.getStringExtra("amount");
            advance = intent.getStringExtra("advance");
            remaining = intent.getStringExtra("remaining");
            session = intent.getStringExtra("session");
            feesId = intent.getStringExtra("fees_id");
            type = intent.getStringExtra("fees_type");
            spinnerFeeType.setEnabled(false);
            editAmount.setText(amount);
            btnAddFee.setText(R.string.update_fees);
        }

        spinnerFeeType.setOnItemSelectedListener(this);

        updateFeesTypeSpinner(feesType);

        btnAddFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFee();
            }
        });

        //update class fess details
        homeViewModel.getClassFeesByClass(studentClass).observe(this, new Observer<ClassFeesResponse>() {
            @Override
            public void onChanged(ClassFeesResponse response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        Log.d(Constants.TAG, TAG + " Student fees get request success");
                        monthlyFee = response.getClassFees();
                        examFee = response.getClassExamFees();
                        txtClassFees.setText(getString(R.string.class_fees) + "  " + monthlyFee);
                        txtClassExamFees.setText(getString(R.string.class_exam_fees) + "  " + examFee);
                    } else {
                        Log.e(Constants.TAG, TAG + " getting student fees failed");
                    }
                } else {
                    Log.e(Constants.TAG, TAG + " Error in getting student fee");
                }
            }
        });
    }

    private String getCurrentDate() {
        StringBuilder builder = new StringBuilder();
        builder.append(picker.getDayOfMonth() + "/");
        builder.append((picker.getMonth() + 1) + "/");//month is 0 based
        builder.append(picker.getYear());
        return builder.toString();
    }

    private void updateFeesTypeSpinner(List<String> feesType) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.room_spinner_item, feesType);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFeeType.setAdapter(dataAdapter);
        if (type != null) {
            int spinnerPosition = dataAdapter.getPosition(type);
            spinnerFeeType.setSelection(spinnerPosition);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedFeeType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void addFee() {
        final String studentName = editStudentName.getText().toString();
        String amount = editAmount.getText().toString();
        selectedDate = getCurrentDate();
        Log.d(Constants.TAG, TAG + " Selected Date :" + selectedDate);

        if (studentName == null || studentName.isEmpty()) {
            Toast.makeText(this, "Student name can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount == null || amount.isEmpty()) {
            Toast.makeText(this, "Amount can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedFeeType == null || selectedFeeType.isEmpty()) {
            Toast.makeText(this, "Fee type can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDate == null || selectedDate.isEmpty()) {
            Toast.makeText(this, "Date can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (studentId == null || studentId.isEmpty()) {
            Toast.makeText(this, "Unknown error, please try again!", Toast.LENGTH_SHORT).show();
            return;
        }
        // check if logged in user is admin
        if (!PreferenceManager.instance().isAdmin()) {
            Toast.makeText(this, "You are not authorized to add fees", Toast.LENGTH_SHORT).show();
            return;
        }
        //calculate advance and remaining fee
        double advanceFee = 0;
        double remainingFee = 0;
        double feesPaid = Double.parseDouble(amount);
        if (monthlyFee != null && examFee != null) {
            double mFee = Double.parseDouble(monthlyFee);
            double eFee = Double.parseDouble(examFee);
            if (selectedFeeType.equalsIgnoreCase(Constants.monthly)) {
                if (mFee > feesPaid) {
                    remainingFee = mFee - feesPaid;
                } else {
                    advanceFee = feesPaid - mFee;
                }
            } else if (selectedFeeType.equalsIgnoreCase(Constants.exam)) {
                if (eFee > feesPaid) {
                    remainingFee = eFee - feesPaid;
                } else {
                    advanceFee = feesPaid - eFee;
                }
            }
        }
        Fee fee = new Fee(studentId, selectedFeeType, amount, selectedDate, String.valueOf(remainingFee), String.valueOf(advanceFee));
        Log.d(Constants.TAG, TAG + " Session : " + PreferenceManager.instance().getSession());
        fee.setSession(PreferenceManager.instance().getSession());
        if (mobile != null) {
            fee.setMobile(mobile);
        }
        fee.setName(studentName);
        mAddProgress.setVisibility(View.VISIBLE);
        //update
        if (isUpdate) {
            fee.set_id(feesId);
            addFeeViewModel.updateFee(fee).observe(this, new Observer<AddFeeResponse>() {
                @Override
                public void onChanged(AddFeeResponse response) {
                    mAddProgress.setVisibility(View.INVISIBLE);
                    if (response != null) {
                        if (response.isSuccess()) {
                            Log.d(Constants.TAG, TAG + " Student fees updated successfully");
                            Toast.makeText(AddFeeActivity.this, "Student fees updated successfully", Toast.LENGTH_SHORT).show();
                            if (response.getFee() != null) {
                                Log.d(Constants.TAG, TAG + " Updated student fees id " + response.getFee().get_id());
                                Log.d(Constants.TAG, TAG + " Printing receipt...");
                                String formattedString = PrinterFormatter.format(response.getFee(), studentName, fatherName);
                                Log.d(Constants.TAG, TAG + " Formatter String : " + formattedString);
                                usbPrinter.initAndPrint(AddFeeActivity.this, formattedString.getBytes());
                            }
                        } else {
                            Log.e(Constants.TAG, TAG + " Updating student fees failed");
                            Toast.makeText(AddFeeActivity.this, "Unknown error, please try again!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(Constants.TAG, TAG + " Error in updating student fee");
                        Toast.makeText(AddFeeActivity.this, "Unknown error, please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            //create new
            fee.setCreatedBy(PreferenceManager.instance().getSavedUserName());
            addFeeViewModel.addFee(fee).observe(this, new Observer<AddFeeResponse>() {
                @Override
                public void onChanged(AddFeeResponse response) {
                    mAddProgress.setVisibility(View.INVISIBLE);
                    if (response != null) {
                        if (response.isSuccess()) {
                            Log.d(Constants.TAG, TAG + " Student fees added successfully");
                            Toast.makeText(AddFeeActivity.this, "Student fees added successfully", Toast.LENGTH_SHORT).show();
                            if (response.getFee() != null) {
                                Log.d(Constants.TAG, TAG + " Added student fees id " + response.getFee().get_id());
                                Log.d(Constants.TAG, TAG + " Printing receipt...");
                                String formattedString = PrinterFormatter.format(response.getFee(), studentName, fatherName);
                                Log.d(Constants.TAG, TAG + " Formatter String : " + formattedString);
                                usbPrinter.initAndPrint(AddFeeActivity.this, formattedString.getBytes());
                            }
                        } else {
                            Log.e(Constants.TAG, TAG + " Adding student fees failed");
                            Toast.makeText(AddFeeActivity.this, "Unknown error, please try again!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(Constants.TAG, TAG + " Error in adding student fee");
                        Toast.makeText(AddFeeActivity.this, "Unknown error, please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        usbPrinter.onDestroy(this);
    }

    @Override
    public void printStatus(boolean isCompleted) {
        if (isCompleted) {
            Log.d(Constants.TAG, TAG + " Receipt printed");
        } else {
            Log.d(Constants.TAG, TAG + " Receipt printing failed");
        }
        finish();
    }
}
