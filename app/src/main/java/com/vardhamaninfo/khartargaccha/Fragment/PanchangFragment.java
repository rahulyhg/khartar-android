package com.vardhamaninfo.khartargaccha.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.vardhamaninfo.khartargaccha.Activity.PanchangDetailActivity;
import com.vardhamaninfo.khartargaccha.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class PanchangFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {

    MaterialCalendarView calender;
    String formattedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_panchang,container,false);

        calender=(MaterialCalendarView) view.findViewById(R.id.calendarView);
        calender.setOnDateChangedListener(this);
        calender.setOnMonthChangedListener(this);
        calender.setDateSelected(Calendar.getInstance().getTime(),true);

        Log.e("Calendar.getInstance().getTime()", Calendar.getInstance().getTime()+"");

        //calender.setCurrentDate(CalendarDay.from(2017, 03, 22), true);;

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.navigation_menu_panchang);


        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
        getSelectedDatesString();

        Intent i=new Intent(getActivity(), PanchangDetailActivity.class);
        i.putExtra("date",formattedDate);
        startActivity(i);

        Log.d("dateSelected",""+getSelectedDatesString());

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        // getSupportActionBar().setTitle(FORMATTER.format(date.getDate()));


    }



    private String getSelectedDatesString() {
        CalendarDay date = calender.getSelectedDate();
        String daz=date+"";
        Log.d("date", "" + daz);

       /* String ddate= String.valueOf(date.getCalendar());
        Log.d("ddate",ddate);
        //FORMATTER.format()*/

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(date.getDate());
        Log.d("format",formattedDate);

        if (date == null) {
            return "No Selection";
        }
        return formattedDate;
    }
}
