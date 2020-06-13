package com.vikram.school.ui.message;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MessageViewModel extends ViewModel {
    private LiveData<MessageResponse> messageResult;

    private MessageRepository messageRepository = new MessageRepository();

    public LiveData<MessageResponse> sendMessage(Message message) {
        messageResult = messageRepository.sendMessage(message);
        return messageResult;
    }
}