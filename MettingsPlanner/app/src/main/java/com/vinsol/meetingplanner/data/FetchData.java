package com.vinsol.meetingplanner.data;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.vinsol.meetingplanner.adapter.MeetingsRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FetchData {

    private static final String STRING_REP = "@@@";

    public void get(Context context, String date, MeetingsRecyclerAdapter meetingsRecyclerAdapter
            , String startTime, String endTime){
        if(date == null || date.isEmpty()){
            return;
        }
        List<MeetingInfo> meetingInfos = new ArrayList<>();

        String url = "https://fathomless-shelf-5846.herokuapp.com/api/schedule?date=\""+STRING_REP+"\"";
        url = url.replaceAll(STRING_REP, date);
        Logger log = Logger.getLogger(context.getClass().getName());
        log.log(Level.INFO, "URL: " +url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            MeetingInfo meetingInfo = new MeetingInfo(
                                    obj.getString("start_time"),
                                    obj.getString("end_time"),
                                    obj.getString("description"),
                                    getListOfParticipants(obj.getJSONArray("participants"))
                            );
                            log.log(Level.INFO, meetingInfo.toString());

                            meetingInfos.add(meetingInfo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if(meetingsRecyclerAdapter != null) {
                        meetingsRecyclerAdapter.updateData(meetingInfos);
                        meetingsRecyclerAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Checking Slot Availability", Toast.LENGTH_SHORT).show();
                        checkAvailability(context, meetingInfos, startTime, endTime);
                    }
                }, error -> {
                    Toast.makeText(context, "Error in fetching data: " + error.getMessage(), Toast.LENGTH_LONG).show();
                });

        MySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }


    private void checkAvailability(Context context, List<MeetingInfo> meetingInfos, String startTime, String endTime){
        boolean checkAvailability = true;
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
        try{
            Date time1 = timeFormat.parse(startTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            calendar1.add(Calendar.MINUTE, 1);

            Date time2 = timeFormat.parse(endTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.MINUTE, -1);

            for (MeetingInfo meetingInfo : meetingInfos) {
                if(!checkAvailability){
                    break;
                }
                checkAvailability = checkAvailability &&
                    checkTimeAvailable(context, meetingInfo.getStartTime(), meetingInfo.getEndTime(),
                            timeFormat.format(calendar1.getTime())) &&
                    checkTimeAvailable(context, meetingInfo.getStartTime(), meetingInfo.getEndTime(),
                            timeFormat.format(calendar2.getTime()));
            }
        } catch (ParseException e){
            e.printStackTrace();
        }

        if(checkAvailability){
            Toast.makeText(context, "Slot available", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Slot not available", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkTimeAvailable(Context context, String time1Text, String time2Text, String timeToCheck){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        try {
            Date time1 = timeFormat.parse(time1Text);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);

            Date time2 = timeFormat.parse(time2Text);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);

            Date d = new SimpleDateFormat("hh:mm aa").parse(timeToCheck);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);

            Date x = calendar3.getTime();

            return x.before(calendar1.getTime()) || x.after(calendar2.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    private List<String> getListOfParticipants(JSONArray participants) throws JSONException {
        List<String> participantsList = new ArrayList<>();
        for (int i = 0; i < participants.length(); i++) {
            participantsList.add(participants.getString(i));
        }
        return participantsList;
    }
}
