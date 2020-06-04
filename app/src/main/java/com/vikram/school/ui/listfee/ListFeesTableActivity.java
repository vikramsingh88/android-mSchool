package com.vikram.school.ui.listfee;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vikram.school.R;
import com.vikram.school.ui.addfee.AddFeeActivity;
import com.vikram.school.ui.addfee.Fee;
import com.vikram.school.utility.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ListFeesTableActivity extends AppCompatActivity {
    private static final String TAG = "ListFeesTableActivity";
    private TableLayout mTableLayoutScoreBoard;
    private ListFeesViewModel listFeesViewModel;
    private String studentId;
    private String studentName;
    private String studentClass;
    private String datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    SimpleDateFormat simpleDateFormat;
    private String monthlyFee;
    private String examFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fees_table);

        listFeesViewModel = ViewModelProviders.of(this).get(ListFeesViewModel.class);
        mTableLayoutScoreBoard = (TableLayout) findViewById(R.id.score_board);
        Intent intent = getIntent();
        studentId = intent.getStringExtra("student_id");
        studentName = intent.getStringExtra("student_name");
        studentClass = intent.getStringExtra("student_class");
        monthlyFee = intent.getStringExtra("monthlyFee");
        examFee = intent.getStringExtra("examFee");
        setTitle(studentName + " Total fees so far Rs. ");

        simpleDateFormat = new SimpleDateFormat(datePattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ListFeesTableActivity.this, AddFeeActivity.class);
                intent1.putExtra("student_id", studentId);
                intent1.putExtra("student_name", studentName);
                intent1.putExtra("student_class", studentClass);
                startActivity(intent1);
            }
        });

        //table header
        TableRow tr = getTableHeader();
        mTableLayoutScoreBoard.addView(tr);
    }

    private TableRow getTableHeader() {
        TableRow tr = (TableRow) getLayoutInflater().inflate(R.layout.table_header_template, null);
        return tr;
    }

    private TableRow getTableData(String feeType, String feeMonth, String feeForMonth, String feePaid, String remainingFee, String advanceFee, String date) {
        TableRow tr = (TableRow) getLayoutInflater().inflate(R.layout.table_row_template, null);
        TextView txtFeeType = (TextView) tr.findViewById(R.id.txt_td_fees_type);
        TextView txtFeeMonth = (TextView) tr.findViewById(R.id.text_td_month);
        TextView txtFeeForMonth = (TextView) tr.findViewById(R.id.txt_td_fees_for_month);
        TextView txtFeePaid = (TextView) tr.findViewById(R.id.txt_td_fees_paid);
        TextView txtRemainingFees = (TextView) tr.findViewById(R.id.txt_td_remaining_fees);
        TextView txtAdvanceFee = (TextView) tr.findViewById(R.id.txt_td_advance_fee);
        TextView txtDate = (TextView) tr.findViewById(R.id.txt_td_date);

        txtFeeType.setText(feeType);
        txtFeeMonth.setText(feeMonth);
        txtFeeForMonth.setText(feeForMonth);
        txtFeePaid.setText(feePaid);
        if (remainingFee != null) {
            double temp = Double.parseDouble(remainingFee);
            if (temp > 0)
                txtRemainingFees.setBackgroundColor(getResources().getColor(R.color.warning));
        }
        txtRemainingFees.setText(remainingFee);
        txtAdvanceFee.setText(advanceFee);
        try {
            Date d = simpleDateFormat.parse(date);
            txtDate.setText(d.toString());
        } catch (Exception e) {
            txtDate.setText(date);
            e.printStackTrace();
        }

        return tr;
    }

    @Override
    protected void onResume() {
        super.onResume();
        cleanTable();
        listFeesViewModel.getStudentFeesByStudentId(studentId).observe(this, new Observer<ListFeesResponse>() {
            @Override
            public void onChanged(ListFeesResponse listFeesResponse) {
                if (listFeesResponse != null) {
                    Log.d(Constants.TAG, TAG + " Is get fees by student id success " + listFeesResponse.isSuccess());
                    List<Fee> fees = listFeesResponse.getFees();
                    if (fees != null) {
                        //calculate total fees
                        double totalFees = 0;
                        for (Fee fee : fees) {
                            String amount = fee.getAmount();
                            totalFees = totalFees + Double.parseDouble(amount);
                        }
                        setTitle(studentName + " Total fees so far Rs. "+totalFees);
                        for (int i = 0; i < fees.size(); i++) {
                            Fee record = fees.get(i);
                            String feeForMonth = "";
                            if (record.getFeeType().equalsIgnoreCase(Constants.monthly)) {
                                feeForMonth = monthlyFee;
                            } else if (record.getFeeType().equalsIgnoreCase(Constants.exam)) {
                                feeForMonth = examFee;
                            }
                            TableRow trData = getTableData(record.getFeeType(), record.getMonth(), feeForMonth, record.getAmount(), record.getRemainingFee(), record.getAdvanceFee(), record.getDate());
                            mTableLayoutScoreBoard.addView(trData);
                        }
                        //TableRow trData = getTableData("", "", "", total);
                        //mTableLayoutScoreBoard.addView(trData);
                    }
                }
            }
        });
    }

    private void cleanTable() {
        int childCount = mTableLayoutScoreBoard.getChildCount();
        // Remove all rows except the first one
        if (childCount > 1) {
            mTableLayoutScoreBoard.removeViews(1, childCount - 1);
        }
    }
}
