package com.vikram.school.ui.listfee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vikram.school.R;
import com.vikram.school.ui.addfee.AddFeeActivity;
import com.vikram.school.ui.addfee.Fee;
import com.vikram.school.utility.Constants;
import com.vikram.school.utility.LineDividerItemDecorator;

import java.util.List;

public class ListFeesActivity extends AppCompatActivity {
    private String TAG = "ListFeesActivity";
    private ListFeesViewModel listFeesViewModel;
    private RecyclerView mRecyclerView;
    private ListFeesAdapter listFeesAdapter;
    private String studentId;
    private String studentName;
    private String studentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fees);

        Intent intent = getIntent();
        studentId = intent.getStringExtra("student_id");
        studentName = intent.getStringExtra("student_name");
        studentClass = intent.getStringExtra("student_class");
        setTitle(studentName + " Total fees so far Rs. ");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ListFeesActivity.this, AddFeeActivity.class);
                intent1.putExtra("student_id", studentId);
                intent1.putExtra("student_name", studentName);
                intent1.putExtra("student_class", studentClass);
                startActivity(intent1);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.list_fees);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new LineDividerItemDecorator(this));

        listFeesViewModel = ViewModelProviders.of(this).get(ListFeesViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                        listFeesAdapter = new ListFeesAdapter(fees, studentName, new ListFeesAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Fee fee) {
                                Log.d(Constants.TAG, TAG + " Selected student fee ID : " + fee.get_id());
                            }
                        });
                        mRecyclerView.setAdapter(listFeesAdapter);
                    }
                }
            }
        });
    }
}
