package com.vikram.school.ui.listfee;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vikram.school.R;
import com.vikram.school.ui.addfee.Fee;
import com.vikram.school.utility.ColorGenerator;
import com.vikram.school.utility.Constants;

import java.util.List;

public class ListFeesAdapter extends RecyclerView.Adapter<ListFeesAdapter.ListFeeHolder>{
    private String TAG = "ListFeesAdapter";
    private List<Fee> mListFees;
    private final OnItemClickListener listener;
    private int previousColor;
    private String studentName;

    public interface OnItemClickListener {
        void onItemClick(Fee fee);
    }

    public class ListFeeHolder extends RecyclerView.ViewHolder {
        private TextView txtStudentName;
        private TextView txtFeesType;
        private TextView txtAmount;
        private TextView txtMonth;
        private TextView txtSubmittedDate;
        private CardView row;

        public ListFeeHolder(View view) {
            super(view);
            txtStudentName =(TextView) view.findViewById(R.id.txt_student_name);
            txtFeesType =(TextView) view.findViewById(R.id.txt_fees_type);
            txtAmount = (TextView) view.findViewById(R.id.txt_amount);
            txtMonth = (TextView) view.findViewById(R.id.txt_month);
            txtSubmittedDate = (TextView) view.findViewById(R.id.txt_date);
            row = (CardView) view.findViewById(R.id.row);
        }
    }


    public ListFeesAdapter(List<Fee> studentsFeesList,String studentName, OnItemClickListener listener) {
        this.mListFees = studentsFeesList;
        this.studentName = studentName;
        this.listener = listener;
    }

    @Override
    public ListFeeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_student_fee_item, parent, false);

        return new ListFeeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListFeeHolder holder, int position) {
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(mListFees.get(position));
        Log.d(Constants.TAG, TAG+" Current color "+color+" previous color "+previousColor);
        if (color == previousColor) {
            color = generator.getColor(mListFees.get(position));
        }
        previousColor = color;
        holder.row.setBackgroundColor(color);
        final Fee fee = mListFees.get(position);
        holder.txtStudentName.setText(studentName);
        holder.txtFeesType.setText(fee.getFeeType());
        holder.txtAmount.setText(fee.getAmount());
        holder.txtMonth.setText(fee.getMonth());
        holder.txtSubmittedDate.setText(fee.getDate());
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(fee);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListFees == null ? 0 : mListFees.size();
    }
}
