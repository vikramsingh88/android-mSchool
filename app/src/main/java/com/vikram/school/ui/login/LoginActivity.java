package com.vikram.school.ui.login;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.vikram.school.MainActivity;
import com.vikram.school.R;
import com.vikram.school.ui.home.HomeFragment;
import com.vikram.school.utility.Constants;
import com.vikram.school.utility.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String TAG = "LoginActivity";
    private EditText editUserName;
    private EditText editPassword;
    private CheckBox checkBox;
    private Button btnLogin;
    private Spinner spinSession;
    private ProgressBar mLoginProgress;

    private LoginViewModel loginViewModel;
    private PreferenceManager preferences;

    private String selectedSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = PreferenceManager.instance();
        editUserName = findViewById(R.id.edit_user_name);
        editPassword = findViewById(R.id.edit_password);
        checkBox = findViewById(R.id.check_box);
        btnLogin = findViewById(R.id.btn_login);
        spinSession = findViewById(R.id.spinner_session);
        mLoginProgress = findViewById(R.id.login_progress);
        spinSession.setOnItemSelectedListener(this);
        updateSessionSpinner();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAttempt();
            }
        });

        //get ViewModel using ViewModelProvider and then fetch data
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    private void updateSessionSpinner() {
        List<String> stringClasses = new ArrayList<>();
        stringClasses.add("2019-2020");
        stringClasses.add("2020-2021");
        stringClasses.add("2021-2022");
        stringClasses.add("2022-2023");
        stringClasses.add("2023-2024");
        stringClasses.add("2024-2025");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(LoginActivity.this, R.layout.room_spinner_item, stringClasses);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSession.setAdapter(dataAdapter);
        if (PreferenceManager.instance().getSession() != null) {
            int spinnerPosition = dataAdapter.getPosition(PreferenceManager.instance().getSession());
            spinSession.setSelection(spinnerPosition);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences.getSavedUserName() != null) {
            checkBox.setChecked(true);
            editUserName.setText(preferences.getSavedUserName());
            editPassword.setText(preferences.getSavedPassword());
        } else {
            checkBox.setChecked(false);
            editUserName.setText("");
            editPassword.setText("");
        }
    }

    private void loginAttempt() {
        final String userName = editUserName.getText().toString();
        final String password = editPassword.getText().toString();

        if (userName == null || userName.isEmpty()) {
            Toast.makeText(this, "User name can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password == null || password.isEmpty()) {
            Toast.makeText(this, "Password can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(Constants.TAG, TAG+" user name : "+userName);
        Log.d(Constants.TAG, TAG+" password : "+password);
        mLoginProgress.setVisibility(View.VISIBLE);
        loginViewModel.login(new User(userName, password)).observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(LoginResult loginResult) {
                mLoginProgress.setVisibility(View.INVISIBLE);
                if (loginResult != null) {
                    Log.d(Constants.TAG, TAG+" Login status : "+loginResult.isSuccess());
                    if (loginResult.isSuccess()) {
                        preferences.saveLoginPreference(userName, password);
                        Log.d(Constants.TAG, TAG+" selectedSession : "+selectedSession);
                        preferences.saveSession(selectedSession);
                        Intent intent = new Intent(LoginActivity.this, HomeFragment.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, " Login failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(Constants.TAG, TAG+" Server error during login");
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedSession = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
