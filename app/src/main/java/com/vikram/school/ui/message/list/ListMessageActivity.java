package com.vikram.school.ui.message.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.vikram.school.R;
import com.vikram.school.ui.message.Message;
import com.vikram.school.ui.message.MessageViewModel;
import com.vikram.school.utility.Constants;
import com.vikram.school.utility.LineDividerItemDecorator;
import com.vikram.school.utility.PreferenceManager;

import java.util.List;

public class ListMessageActivity extends AppCompatActivity {

    private String TAG = "ListMessageActivity";
    private RecyclerView mMessageRecyclerView;
    private ListMessageAdapter mListMessageAdapter;
    private MessageViewModel messageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_message);
        setTitle(getString(R.string.all_sent_message)+" "+PreferenceManager.instance().getSession());

        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.list_messages);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mMessageRecyclerView.setLayoutManager(mLayoutManager);
        mMessageRecyclerView.addItemDecoration(new LineDividerItemDecorator(this));

        messageViewModel.getMessages(PreferenceManager.instance().getSession()).observe(this, new Observer<ListMessageResponse>() {
            @Override
            public void onChanged(ListMessageResponse messages) {
                updateMessageList(messages);
            }
        });
    }

    private void updateMessageList(ListMessageResponse messages) {
        if(messages != null) {
            if (messages.isSuccess()) {
                List<Message> listMessage = messages.getMsgs();
                mListMessageAdapter = new ListMessageAdapter(listMessage, null);
                mMessageRecyclerView.setAdapter(mListMessageAdapter);
            } else {
                Log.d(Constants.TAG, TAG+" No message available");
                Toast.makeText(ListMessageActivity.this, "Error in getting messages", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(Constants.TAG, TAG+" Error in getting messages");
            Toast.makeText(ListMessageActivity.this, "Error in getting messages", Toast.LENGTH_SHORT).show();
        }
    }
}
