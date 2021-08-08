package com.vinsol.meetingplanner.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeetingInfo {

    private String startTime;
    private String endTime;
    private String description;
    private List<String> participants;

    public MeetingInfo(String startTime, String endTime, String description, List<String> participants) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.participants = participants;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getParticipants() {
        if(participants == null){
            participants = new ArrayList<>();
        }
        return participants;
    }

    @Override
    public String toString() {
        return "MeetingInfo{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", description='" + description + '\'' +
                ", participants=" + Arrays.toString(getParticipants().toArray(new String[0])) +
                '}';
    }
}
