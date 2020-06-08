package com.vikram.school.ui.listfee;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vikram.school.R;
import com.vikram.school.ui.addfee.AddFeeActivity;
import com.vikram.school.ui.addfee.Fee;
import com.vikram.school.ui.addfee.printer.IPrintStatus;
import com.vikram.school.ui.addfee.printer.PrinterFormatter;
import com.vikram.school.ui.addfee.printer.USBPrinter;
import com.vikram.school.utility.Constants;
import com.vikram.school.utility.Utility;

import java.util.List;

public class ListFeesTableActivity extends AppCompatActivity implements IPrintStatus {
    private static final String TAG = "ListFeesTableActivity";
    private TableLayout mTableLayoutScoreBoard;
    private ListFeesViewModel listFeesViewModel;
    private String studentId;
    private String studentName;
    private String fatherName;
    private String studentClass;
    private String monthlyFee;
    private String examFee;
    private USBPrinter usbPrinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fees_table);

        listFeesViewModel = ViewModelProviders.of(this).get(ListFeesViewModel.class);
        mTableLayoutScoreBoard = (TableLayout) findViewById(R.id.score_board);
        Intent intent = getIntent();
        studentId = intent.getStringExtra("student_id");
        studentName = intent.getStringExtra("student_name");
        fatherName = intent.getStringExtra("father_name");
        studentClass = intent.getStringExtra("student_class");
        monthlyFee = intent.getStringExtra("monthlyFee");
        examFee = intent.getStringExtra("examFee");
        setTitle(studentName + " Total fees so far Rs. ");

        usbPrinter = new USBPrinter();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ListFeesTableActivity.this, AddFeeActivity.class);
                intent1.putExtra("student_id", studentId);
                intent1.putExtra("student_name", studentName);
                intent1.putExtra("student_class", studentClass);
                intent1.putExtra("father_name", fatherName);
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
        txtFeeMonth.setText(Utility.formatDate(feeMonth));
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
            txtDate.setText(Utility.formatDate(date, Utility.datePattern));
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
                        setTitle(studentName + " Total fees . " + totalFees);
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
                            trData.setTag(record);
                            //update fees detail
                            trData.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    Fee selectedFee = (Fee) view.getTag();
                                    Log.d(Constants.TAG, TAG + " Row item clicked " + selectedFee.get_id());
                                    Intent intent1 = new Intent(ListFeesTableActivity.this, AddFeeActivity.class);
                                    intent1.putExtra("student_id", studentId);
                                    intent1.putExtra("student_name", studentName);
                                    intent1.putExtra("student_class", studentClass);
                                    intent1.putExtra("father_name", fatherName);
                                    intent1.putExtra("amount", selectedFee.getAmount());
                                    intent1.putExtra("advance", selectedFee.getAdvanceFee());
                                    intent1.putExtra("remaining", selectedFee.getRemainingFee());
                                    intent1.putExtra("session", selectedFee.getSession());
                                    intent1.putExtra("fees_id", selectedFee.get_id());
                                    intent1.putExtra("fees_type", selectedFee.getFeeType());
                                    intent1.putExtra("is_update", true);

                                    startActivity(intent1);
                                    return false;
                                }
                            });
                            //print receipt
                            trData.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View view) {
                                    //get confirmation for printing receipt
                                    new AlertDialog.Builder(ListFeesTableActivity.this)
                                            .setTitle("Print fees receipt")
                                            .setMessage("Do you want to print this receipt?")
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Continue with print operation
                                                    Fee selectedFee = (Fee) view.getTag();
                                                    Log.d(Constants.TAG, TAG + " Row item clicked " + selectedFee.get_id());
                                                    Log.d(Constants.TAG, TAG + " Printing receipt...");
                                                    String formattedString = PrinterFormatter.format(selectedFee, studentName, fatherName);
                                                    Log.d(Constants.TAG, TAG + " Formatter String : " + formattedString);
                                                    usbPrinter.initAndPrint(ListFeesTableActivity.this, formattedString.getBytes());
                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, null)
                                            .setIcon(R.drawable.ic_printer)
                                            .show();
                                }
                            });
                        }
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

    @Override
    public void printStatus(boolean isCompleted) {
        if (isCompleted) {
            Log.d(Constants.TAG, TAG + " Receipt printed");
            Toast.makeText(this, "Receipt printed", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(Constants.TAG, TAG + " Receipt printing failed");
            Toast.makeText(this, "Receipt printing failed", Toast.LENGTH_SHORT).show();
        }
    }
}
