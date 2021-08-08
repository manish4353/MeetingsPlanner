package com.vinsol.meetingplanner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vinsol.meetingplanner.data.MeetingInfo;
import com.vinsol.meetingplanner.databinding.RecyclerItemBinding;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MeetingsRecyclerAdapter extends RecyclerView.Adapter<MeetingsRecyclerAdapter.MeetingsViewHolder> {

    private List<MeetingInfo> meetingsInfoList;
    private Context context;

    public MeetingsRecyclerAdapter(List<MeetingInfo> meetingsInfoList, Context context) {
        this.meetingsInfoList = meetingsInfoList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public MeetingsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        RecyclerItemBinding binding = RecyclerItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);

        return new MeetingsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MeetingsRecyclerAdapter.MeetingsViewHolder holder, int position) {
        MeetingInfo meetingInfo = meetingsInfoList.get(position);

        holder.itemBinding.meetingDesc.setText(meetingInfo.getDescription());
        holder.itemBinding.meetingStartTime.setText(get12Format(meetingInfo.getStartTime()));
        holder.itemBinding.meetingStopTime.setText(get12Format(meetingInfo.getEndTime()));
        if(holder.itemBinding.meetingAttendants != null) {
            String meetingParticipants = Arrays.toString(
                    meetingInfo.getParticipants().toArray(new String[0]));
            meetingParticipants = meetingParticipants.replace("[", "");
            meetingParticipants = meetingParticipants.replace("]", "");
            holder.itemBinding.meetingAttendants.setText(meetingParticipants);
        }
    }

    @Override
    public int getItemCount() {
        return meetingsInfoList.size();
    }

    public void updateData(List<MeetingInfo> meetingInfos) {
        this.meetingsInfoList = meetingInfos;
    }

    class MeetingsViewHolder extends RecyclerView.ViewHolder {

        private RecyclerItemBinding itemBinding;

        public MeetingsViewHolder(RecyclerItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    private String get12Format(String time){
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            final Date dateObj = sdf.parse(time);
            if(dateObj!=null) {
                time = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(dateObj);
            }
        } catch (final ParseException e) {
            e.printStackTrace();
        }

        return time;
    }
}
