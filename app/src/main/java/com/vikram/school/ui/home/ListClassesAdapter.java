package com.vikram.school.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vikram.school.R;
import com.vikram.school.ui.slideshow.Classes;
import com.vikram.school.utility.ColorGenerator;
import com.vikram.school.utility.Constants;

import java.util.List;

public class ListClassesAdapter extends RecyclerView.Adapter<ListClassesAdapter.ListClassesHolder>{
    private String TAG = "ListClassesAdapter";
    private List<Classes> mListClasses;
    private final OnItemClickListener listener;
    private int previousColor;

    public interface OnItemClickListener {
        void onItemClick(String className, String monthlyFee, String examFee);
        void onItemLongClick(String id, String classTeacher, String className, String monthlyFee, String examFee);
    }

    public class ListClassesHolder extends RecyclerView.ViewHolder {
        private TextView txtClassTeacherName;
        private TextView txtClassName;
        private TextView txtClassFees;
        private TextView txtClassExamFees;
        private CardView row;

        public ListClassesHolder(View view) {
            super(view);
            txtClassName = (TextView) view.findViewById(R.id.txt_class_name);
            txtClassTeacherName = (TextView) view.findViewById(R.id.txt_class_teacher_name);
            txtClassFees = (TextView) view.findViewById(R.id.txt_class_fees);
            txtClassExamFees = (TextView) view.findViewById(R.id.txt_class_exam_fees);
            row = (CardView) view.findViewById(R.id.row);
        }
    }


    public ListClassesAdapter(List<Classes> classesList, OnItemClickListener listener) {
        this.mListClasses = classesList;
        this.listener = listener;
    }

    @Override
    public ListClassesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_classes_item, parent, false);

        return new ListClassesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListClassesHolder holder, int position) {
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(mListClasses.get(position));
        Log.d(Constants.TAG, TAG+" Current color "+color+" previous color "+previousColor);
        if (color == previousColor) {
            color = generator.getColor(mListClasses.get(position));
        }
        previousColor = color;
        holder.row.setBackgroundColor(color);
        final Classes classes = mListClasses.get(position);
        holder.txtClassName.setText(classes.getClassName());
        holder.txtClassTeacherName.setText(classes.getClassTeacherName());
        holder.txtClassFees.setText(classes.getClassFees());
        holder.txtClassExamFees.setText(classes.getClassExamFees());
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(classes.getClassName(), classes.getClassFees(), classes.getClassExamFees());
            }
        });

        holder.row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onItemLongClick(classes.get_id(), classes.getClassTeacherName(), classes.getClassName(), classes.getClassFees(), classes.getClassExamFees());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListClasses == null ? 0 : mListClasses.size();
    }
}
