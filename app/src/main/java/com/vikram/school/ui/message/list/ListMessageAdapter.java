package com.vikram.school.ui.message.list;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vikram.school.R;
import com.vikram.school.ui.message.Message;
import com.vikram.school.utility.ColorGenerator;
import com.vikram.school.utility.Constants;
import com.vikram.school.utility.Utility;

import java.util.List;

public class ListMessageAdapter extends RecyclerView.Adapter<ListMessageAdapter.ListMessageHolder>{
    private String TAG = "ListMessageAdapter";
    private List<Message> mListMessage;
    private final OnItemClickListener listener;
    private int previousColor;

    public interface OnItemClickListener {
        void onItemClick(String className, String monthlyFee, String examFee);
        void onItemLongClick(String id, String classTeacher, String className, String monthlyFee, String examFee);
    }

    public class ListMessageHolder extends RecyclerView.ViewHolder {
        private TextView txtSendTo;
        private TextView txtMessageBody;
        private TextView txtDate;
        private CardView row;

        public ListMessageHolder(View view) {
            super(view);
            txtSendTo = (TextView) view.findViewById(R.id.txt_send_to);
            txtMessageBody = (TextView) view.findViewById(R.id.txt_message);
            txtDate = (TextView) view.findViewById(R.id.txt_date);
            row = (CardView) view.findViewById(R.id.row);
        }
    }


    public ListMessageAdapter(List<Message> messageList, OnItemClickListener listener) {
        this.mListMessage = messageList;
        this.listener = listener;
    }

    @Override
    public ListMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_message_item, parent, false);

        return new ListMessageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListMessageHolder holder, int position) {
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getRandomColor();
        Log.d(Constants.TAG, TAG+" Current color "+color+" previous color "+previousColor);
        if (color == previousColor) {
            color = generator.getColor(mListMessage.get(position));
        }
        previousColor = color;
        holder.row.setBackgroundColor(color);
        final Message message = mListMessage.get(position);
        holder.txtSendTo.setText(message.getStdClass());
        holder.txtDate.setText(Utility.formatDate(message.getDate()));
        holder.txtMessageBody.setText(message.getMessage());
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListMessage == null ? 0 : mListMessage.size();
    }
}
