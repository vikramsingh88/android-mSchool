package com.vikram.school.ui.student.liststudent;

import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.vikram.school.R;
import com.vikram.school.ui.student.Student;
import com.vikram.school.utility.ColorGenerator;
import com.vikram.school.utility.Constants;
import com.vikram.school.utility.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ListStudentAdapter extends RecyclerView.Adapter<ListStudentAdapter.ListStudentHolder>{
    private String TAG = "ListStudentAdapter";
    private List<Student> mListStudents;
    private final OnItemClickListener listener;
    private int previousColor;

    public interface OnItemClickListener {
        void onItemClick(Student student);
        void onItemLongClick(Student student);
    }

    public class ListStudentHolder extends RecyclerView.ViewHolder {
        private TextView txtStudentName;
        private TextView txtFatherName;
        private TextView txtAddress;
        private TextView txtStudentClass;
        private TextView txtClassTeacher;
        private TextView txtDate;
        private TextView txtSession;
        private CardView row;
        private CircularImageView imageView;
        private TextView txtMobile;
        private TextView txtTransport;

        public ListStudentHolder(View view) {
            super(view);
            txtStudentName =(TextView) view.findViewById(R.id.txt_student_name);
            txtFatherName =(TextView) view.findViewById(R.id.txt_father_name);
            txtAddress = (TextView) view.findViewById(R.id.txt_address);
            txtStudentClass = (TextView) view.findViewById(R.id.txt_student_class);
            txtClassTeacher = (TextView) view.findViewById(R.id.txt_class_teacher);
            txtDate = (TextView) view.findViewById(R.id.txt_date);
            txtSession = (TextView) view.findViewById(R.id.txt_session);
            row = (CardView) view.findViewById(R.id.row);
            imageView = (CircularImageView) view.findViewById(R.id.img_profile);
            txtMobile = (TextView) view.findViewById(R.id.txt_mobile);
            txtTransport = (TextView) view.findViewById(R.id.txt_transport);
        }
    }


    public ListStudentAdapter(List<Student> studentsList, OnItemClickListener listener) {
        this.mListStudents = studentsList;
        this.listener = listener;
    }

    @Override
    public ListStudentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_student_item, parent, false);

        return new ListStudentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListStudentHolder holder, int position) {
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(mListStudents.get(position));
        Log.d(Constants.TAG, TAG+" Current color "+color+" previous color "+previousColor);
        if (color == previousColor) {
            color = generator.getColor(mListStudents.get(position));
        }
        previousColor = color;
        holder.row.setBackgroundColor(color);
        final Student student = mListStudents.get(position);
        holder.txtStudentName.setText(student.getStudentName());
        holder.txtFatherName.setText(student.getFatherName());
        holder.txtAddress.setText(student.getAddress());
        holder.txtStudentClass.setText(student.getStudentClass());
        holder.txtClassTeacher.setText(student.getClassTeacher());
        holder.txtDate.setText(Utility.formatDate(student.getDate(), Utility.datePattern));
        holder.txtSession.setText(student.getSession());
        if (student.getMobile() == null || student.getMobile().isEmpty()) {
            holder.txtMobile.setText("Not available");
        } else {
            holder.txtMobile.setText(student.getMobile());
        }
        if (student.isTransport()) {
            holder.txtTransport.setText("Yes");
        } else {
            holder.txtTransport.setText("No");
        }
        Log.d(Constants.TAG, TAG+" image data : "+student.getImage());
        if(student.getImage() != null) {
            byte[] imageAsBytes = Base64.decode(student.getImage(), Base64.DEFAULT);
            holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(student);
            }
        });

        holder.row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onItemLongClick(student);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListStudents == null ? 0 : mListStudents.size();
    }
}

