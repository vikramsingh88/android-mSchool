package com.vikram.school.ui.addfee;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class AddFeeViewModel extends ViewModel {

    private LiveData<AddFeeResponse> addFeeResult;
    private AddFeeRepository addFeeRepository = new AddFeeRepository();

    public LiveData<AddFeeResponse> addFee(Fee fee) {
        addFeeResult = addFeeRepository.addFee(fee);
        return addFeeResult;
    }
}
