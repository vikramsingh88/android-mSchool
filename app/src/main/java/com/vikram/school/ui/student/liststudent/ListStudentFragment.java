package com.vikram.school.ui.student.liststudent;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vikram.school.R;
import com.vikram.school.ui.addfee.AddFeeActivity;
import com.vikram.school.ui.listfee.ListFeesActivity;
import com.vikram.school.ui.listfee.ListFeesTableActivity;
import com.vikram.school.ui.student.AddStudentFragment;
import com.vikram.school.ui.student.Student;
import com.vikram.school.utility.Constants;
import com.vikram.school.utility.LineDividerItemDecorator;

import java.util.List;

public class ListStudentFragment extends AppCompatActivity {
    private String TAG = "ListStudentFragment";
    private ListStudentViewModel listStudentViewModel;
    private String selectedClass;
    private String monthlyFee;
    private String examFee;
    private RecyclerView mRecyclerView;
    private ListStudentAdapter listStudentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list_student);
        mRecyclerView = (RecyclerView) findViewById(R.id.list_students);
        final Intent intent = getIntent();
        selectedClass = intent.getStringExtra("selectedClass");
        monthlyFee = intent.getStringExtra("monthlyFee");
        examFee = intent.getStringExtra("examFee");

        setTitle(selectedClass+" Class Students");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new LineDividerItemDecorator(this));

        listStudentViewModel = ViewModelProviders.of(this).get(ListStudentViewModel.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ListStudentFragment.this, AddStudentFragment.class);
                intent1.putExtra("student_class", selectedClass);
                startActivity(intent1);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        listStudentViewModel.getStudentByClass(selectedClass).observe(this, new Observer<ListStudentResponse>() {
            @Override
            public void onChanged(ListStudentResponse listStudentResponse) {
                if (listStudentResponse != null) {
                    Log.d(Constants.TAG, TAG+ " Is get students by class success "+listStudentResponse.isSuccess());
                    List<Student> students = listStudentResponse.getStudents();
                    if (students != null) {
                        listStudentAdapter = new ListStudentAdapter(students, new ListStudentAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Student student) {
                                Log.d(Constants.TAG, TAG+" Selected student ID : "+student.get_id());
                                //Intent intent1 = new Intent(ListStudentFragment.this, ListFeesActivity.class);
                                Intent intent1 = new Intent(ListStudentFragment.this, ListFeesTableActivity.class);
                                intent1.putExtra("student_id", student.get_id());
                                intent1.putExtra("student_name", student.getStudentName());
                                intent1.putExtra("father_name", student.getFatherName());
                                intent1.putExtra("student_class", student.getStudentClass());
                                intent1.putExtra("monthlyFee", monthlyFee);
                                intent1.putExtra("examFee", examFee);
                                startActivity(intent1);
                            }

                            @Override
                            public void onItemLongClick(Student student) {
                                Intent intent1 = new Intent(ListStudentFragment.this, AddStudentFragment.class);
                                intent1.putExtra("student_id", student.get_id());
                                intent1.putExtra("student_name", student.getStudentName());
                                intent1.putExtra("father_name", student.getFatherName());
                                intent1.putExtra("address", student.getAddress());
                                intent1.putExtra("student_class", student.getStudentClass());
                                intent1.putExtra("class_teacher", student.getClassTeacher());
                                intent1.putExtra("monthlyFee", monthlyFee);
                                intent1.putExtra("examFee", examFee);
                                intent1.putExtra("is_update", true);
                                startActivity(intent1);
                            }
                        });
                        mRecyclerView.setAdapter(listStudentAdapter);
                    }
                }
            }
        });
    }
}
