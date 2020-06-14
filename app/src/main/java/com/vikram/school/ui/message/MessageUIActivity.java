package com.vikram.school.ui.message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.vikram.school.R;
import com.vikram.school.ui.home.ClassesListResponse;
import com.vikram.school.ui.home.HomeViewModel;
import com.vikram.school.ui.message.list.ListMessageActivity;
import com.vikram.school.ui.slideshow.Classes;
import com.vikram.school.ui.student.AddStudentFragment;
import com.vikram.school.utility.Constants;
import com.vikram.school.utility.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class MessageUIActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String TAG = "MessageUIActivity";
    private EditText editMessage;
    private Spinner classSpinner;
    private ProgressBar mProgressBar;
    private Button btnSend;
    private HomeViewModel homeViewModel;
    private MessageViewModel messageViewModel;
    private String mSelectedClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_u_i);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        editMessage = findViewById(R.id.edit_message);
        classSpinner = findViewById(R.id.spinner_class);
        mProgressBar = findViewById(R.id.send_message_progress);
        btnSend = findViewById(R.id.btn_send);
        classSpinner.setOnItemSelectedListener(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        homeViewModel.getClasses().observe(this, new Observer<ClassesListResponse>() {
            @Override
            public void onChanged(ClassesListResponse classes) {
                updateRoomSpinner(classes);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_message:
                Intent intent = new Intent(this, ListMessageActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateRoomSpinner(ClassesListResponse classes) {
        List<String> stringClasses = new ArrayList<>();
        if (classes != null) {
            stringClasses.add("All");
            List<Classes> listClasses = classes.getClasses();
            for (Classes room : listClasses) {
                stringClasses.add(room.getClassName());
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MessageUIActivity.this, R.layout.room_spinner_item, stringClasses);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSelectedClass = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void sendMessage() {
        String message = editMessage.getText().toString();
        if (mSelectedClass == null || mSelectedClass.isEmpty()) {
            Toast.makeText(this, "Class name can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (message == null || message.isEmpty()) {
            Toast.makeText(this, "Message can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        //add school name in message
        message = message+"\nSMT Rooprani Vidya Mandir Lilambarpur Fatehpur";
        Message msgObject = new Message(message, mSelectedClass, PreferenceManager.instance().getSession());
        msgObject.setCreatedBy(PreferenceManager.instance().getSavedUserName());
        mProgressBar.setVisibility(View.VISIBLE);
        messageViewModel.sendMessage(msgObject).observe(this, new Observer<MessageResponse>() {
            @Override
            public void onChanged(MessageResponse response) {
                mProgressBar.setVisibility(View.GONE);
                if(response != null) {
                    Log.d(Constants.TAG, TAG+" Message sent successfully");
                    Toast.makeText(MessageUIActivity.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(Constants.TAG, TAG+" Error in sending message");
                    Toast.makeText(MessageUIActivity.this, "Unable to send message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
