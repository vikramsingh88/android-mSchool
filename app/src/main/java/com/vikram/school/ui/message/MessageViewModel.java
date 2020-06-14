package com.vikram.school.ui.message;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vikram.school.ui.message.list.ListMessageResponse;

public class MessageViewModel extends ViewModel {
    private LiveData<MessageResponse> messageResult;
    private LiveData<ListMessageResponse> listMessageResult;

    private MessageRepository messageRepository = new MessageRepository();

    public LiveData<MessageResponse> sendMessage(Message message) {
        messageResult = messageRepository.sendMessage(message);
        return messageResult;
    }

    public LiveData<ListMessageResponse> getMessages(String session) {
        listMessageResult = messageRepository.getMessages(session);
        return listMessageResult;
    }
}