package com.vikram.school.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    private LiveData<LoginResult> loginResult;
    private LoginRepository loginRepository = new LoginRepository();

    public LiveData<LoginResult> login(User user) {
        loginResult = loginRepository.login(user);
        return loginResult;
    }
}
