package com.example.ultrahang;

import android.content.Context;
import android.media.RouteListingPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReserveAppointmentAdapter extends RecyclerView.Adapter<ReserveAppointmentAdapter.ViewHolder> implements
        View.OnClickListener {
    private ArrayList<Appointment> mAppointmentData;
    private Context mContext;
    private ItemClickListener itemClickListener;
    private int selectedPosition = -1;

    public ReserveAppointmentAdapter(ArrayList<Appointment> mAppointmentData, Context mContext, ItemClickListener listener) {
        this.mAppointmentData = mAppointmentData;
        this.mContext = mContext;
        this.itemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mAppointmentData.size();
    }

    @Override
    public void onClick(View v) {
    }

    @NonNull
    @Override
    public ViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservable_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.radioButton.setText((CharSequence) mAppointmentData.get(position));
        holder.radioButton.setChecked(position == selectedPosition);
        holder.radioButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(
                            CompoundButton compoundButton,
                            boolean b)
                    {
                        if (b) {
                            selectedPosition = holder.getAdapterPosition();
                            itemClickListener.onClick(holder.radioButton.getText().toString());
                        }
                    }
                });
    }

    @Override public long getItemId(int position)
    {
        return position;
    }
    @Override public int getItemViewType(int position)
    {
        return position;
    }

public class ViewHolder extends RecyclerView.ViewHolder {
    RadioButton radioButton;

    public ViewHolder(@NonNull View itemView)
    {
        super(itemView);
        radioButton = itemView.findViewById(R.id.radio_button);
    }
    void bindTo(Appointment currentItem){
        radioButton.setText(currentItem.getDate());
    }
}
}