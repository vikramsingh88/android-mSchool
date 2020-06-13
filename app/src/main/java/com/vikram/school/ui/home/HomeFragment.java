package com.vikram.school.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vikram.school.R;
import com.vikram.school.ui.message.MessageUIActivity;
import com.vikram.school.ui.slideshow.Classes;
import com.vikram.school.ui.slideshow.SlideshowFragment;
import com.vikram.school.ui.student.liststudent.ListStudentFragment;
import com.vikram.school.utility.Constants;
import com.vikram.school.utility.LineDividerItemDecorator;

import java.util.List;

//list of classes
public class HomeFragment extends AppCompatActivity {
    private String TAG = "HomeFragment";
    private RecyclerView mClassesRecyclerView;
    private ListClassesAdapter mListClassesAdapter;
    private HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mClassesRecyclerView = (RecyclerView) findViewById(R.id.list_classes);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mClassesRecyclerView.setLayoutManager(mLayoutManager);
        mClassesRecyclerView.addItemDecoration(new LineDividerItemDecorator(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HomeFragment.this, SlideshowFragment.class);
                startActivity(intent1);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        homeViewModel.getClasses().observe(this, new Observer<ClassesListResponse>() {
            @Override
            public void onChanged(ClassesListResponse classes) {
                updateClassesList(classes);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_send:
                Intent intent = new Intent(this, MessageUIActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateClassesList(ClassesListResponse classes) {
        if(classes != null) {
            if (classes.isSuccess()) {
                List<Classes> listClasses =classes.getClasses();
                mListClassesAdapter = new ListClassesAdapter(listClasses, new ListClassesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(String className, String monthlyFee, String examFee) {
                        Intent intent = new Intent(HomeFragment.this, ListStudentFragment.class);
                        intent.putExtra("selectedClass", className);
                        intent.putExtra("monthlyFee", monthlyFee);
                        intent.putExtra("examFee", examFee);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(String id, String classTeacher, String className, String monthlyFee, String examFee) {
                        //long click to update list
                        Intent intent1 = new Intent(HomeFragment.this, SlideshowFragment.class);
                        intent1.putExtra("isUpdate", true);
                        intent1.putExtra("id", id);
                        intent1.putExtra("classTeacher", classTeacher);
                        intent1.putExtra("className", className);
                        intent1.putExtra("monthlyFee", monthlyFee);
                        intent1.putExtra("examFee", examFee);
                        startActivity(intent1);
                    }
                });
                mClassesRecyclerView.setAdapter(mListClassesAdapter);
            } else {
                Log.d(Constants.TAG, TAG+" No classes available");
            }
        } else {
            Log.d(Constants.TAG, TAG+" Error in getting classes");
            Toast.makeText(HomeFragment.this, "Error in getting classes", Toast.LENGTH_SHORT).show();
        }
    }
}
