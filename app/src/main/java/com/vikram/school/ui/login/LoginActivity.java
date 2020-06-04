package com.vikram.school.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.vikram.school.MainActivity;
import com.vikram.school.R;
import com.vikram.school.ui.home.HomeFragment;
import com.vikram.school.utility.Constants;
import com.vikram.school.utility.PreferenceManager;

public class LoginActivity extends AppCompatActivity {
    private String TAG = "LoginActivity";
    private EditText editUserName;
    private EditText editPassword;
    private CheckBox checkBox;
    private Button btnLogin;

    private LoginViewModel loginViewModel;
    private PreferenceManager preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = PreferenceManager.instance();
        editUserName = findViewById(R.id.edit_user_name);
        editPassword = findViewById(R.id.edit_password);
        checkBox = findViewById(R.id.check_box);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAttempt();
            }
        });

        //get ViewModel using ViewModelProvider and then fetch data
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
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
        loginViewModel.login(new User(userName, password)).observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(LoginResult loginResult) {
                if (loginResult != null) {
                    Log.d(Constants.TAG, TAG+" Login status : "+loginResult.isSuccess());
                    if (loginResult.isSuccess()) {
                        preferences.saveLoginPreference(userName, password);
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
}
