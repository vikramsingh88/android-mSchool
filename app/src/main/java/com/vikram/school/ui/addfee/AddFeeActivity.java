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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vikram.school.R;
import com.vikram.school.ui.home.ClassFeesResponse;
import com.vikram.school.ui.home.HomeViewModel;
import com.vikram.school.utility.Constants;

import java.util.ArrayList;
import java.util.List;

public class AddFeeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
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
    private String studentId;
    private String studentClass;
    private String monthlyFee;
    private String examFee;

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
        picker=(DatePicker)findViewById(R.id.date);
        txtClassFees = (TextView) findViewById(R.id.txt_lbl_class_fees);
        txtClassExamFees = (TextView) findViewById(R.id.txt_lbl_class_exam_fees);

        List<String> feesType = new ArrayList<>();
        feesType.add(Constants.monthly);
        feesType.add(Constants.exam);
        feesType.add(Constants.advance);
        updateFeesTypeSpinner(feesType);

        Intent intent = getIntent();
        studentId = intent.getStringExtra("student_id");
        studentName = intent.getStringExtra("student_name");
        studentClass = intent.getStringExtra("student_class");
        editStudentName.setText(studentName);

        spinnerFeeType.setOnItemSelectedListener(this);

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
                        Log.d(Constants.TAG, TAG+" Student fees get request success");
                        monthlyFee = response.getClassFees();
                        examFee = response.getClassExamFees();
                        txtClassFees.setText(getString(R.string.class_fees)+ "  " +monthlyFee);
                        txtClassExamFees.setText(getString(R.string.class_exam_fees)+"  "+examFee);
                    } else {
                        Log.e(Constants.TAG, TAG+" getting student fees failed");
                    }
                } else {
                    Log.e(Constants.TAG, TAG+" Error in getting student fee");
                }
            }
        });
    }

    private String getCurrentDate(){
        StringBuilder builder=new StringBuilder();;
        builder.append((picker.getMonth() + 1)+"/");//month is 0 based
        builder.append(picker.getDayOfMonth()+"/");
        builder.append(picker.getYear());
        return builder.toString();
    }

    private void updateFeesTypeSpinner(List<String> feesType) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.room_spinner_item, feesType);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFeeType.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedFeeType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void addFee() {
        String studentName = editStudentName.getText().toString();
        String amount = editAmount.getText().toString();
        selectedDate = getCurrentDate();
        Log.d(Constants.TAG, TAG+" Selected Date :"+selectedDate);

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
        //calculate advance and remaining fee
        double advanceFee = 0;
        double remainingFee = 0;
        double feesPaid = Double.parseDouble(amount);
        if (monthlyFee != null && examFee != null) {
            double mFee = Double.parseDouble(monthlyFee);
            double eFee = Double.parseDouble(examFee);
            if (selectedFeeType.equalsIgnoreCase(Constants.monthly)) {
                if(mFee > feesPaid) {
                    remainingFee = mFee - feesPaid;
                } else {
                    advanceFee = feesPaid - mFee;
                }
            } else if (selectedFeeType.equalsIgnoreCase(Constants.exam)) {
                if(eFee > feesPaid) {
                    remainingFee = eFee - feesPaid;
                } else {
                    advanceFee = feesPaid - eFee;
                }
            }
        }
        Fee fee = new Fee(studentId, selectedFeeType, amount, selectedDate, String.valueOf(remainingFee), String.valueOf(advanceFee));

        addFeeViewModel.addFee(fee).observe(this, new Observer<AddFeeResponse>() {
            @Override
            public void onChanged(AddFeeResponse response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        Log.d(Constants.TAG, TAG+" Student fees added successfully");
                        Toast.makeText(AddFeeActivity.this, "Student fees added successfully", Toast.LENGTH_SHORT).show();
                        if (response.getFee() != null) {
                            Log.d(Constants.TAG, TAG + " Added student fees id "+response.getFee().get_id());
                        }
                        finish();
                    } else {
                        Log.e(Constants.TAG, TAG+" Adding student fees failed");
                        Toast.makeText(AddFeeActivity.this, "Unknown error, please try again!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(Constants.TAG, TAG+" Error in adding student fee");
                }
            }
        });
    }
}
