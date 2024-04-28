package com.example.ultrahang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> implements
        View.OnClickListener {
    private ArrayList<Appointment> mAppointmentData;
    private Context mContext;
    private int lastPosition = -1;

    public AppointmentAdapter(ArrayList<Appointment> mAppointmentData, Context mContext) {
        this.mAppointmentData = mAppointmentData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.appointment_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.ViewHolder viewHolder, int i) {
        Appointment currentAppointment = mAppointmentData.get(i);
        viewHolder.bindTo(currentAppointment);

        if(viewHolder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            viewHolder.itemView.startAnimation(animation);
            lastPosition = viewHolder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mAppointmentData.size();
    }

    @Override
    public void onClick(View v) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mDateText;
        private TextView mCreatedAtTime;
        private TextView mUpdatedAtTime;

        ViewHolder(View itemView) {
            super(itemView);
            mDateText = itemView.findViewById(R.id.date);
            mCreatedAtTime = itemView.findViewById(R.id.created_at_time);
            mUpdatedAtTime = itemView.findViewById(R.id.updated_at_time);
        }

        void bindTo(Appointment currentItem){
            mDateText.setText(currentItem.getDate());
            mCreatedAtTime.setText(currentItem.getCreated_at());
            mUpdatedAtTime.setText(currentItem.getUpdated_at());
            //itemView.findViewById(R.id.delete).setOnClickListener(view -> ((AppointmentActivity)mContext).deleteAppointment(currentItem));
        }
    }

}
