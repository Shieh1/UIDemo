package com.zysm.curtain.uidemo;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zysm.curtain.mwidgetgroup.pickerview.TimePickerDialog;
import com.zysm.curtain.mwidgetgroup.pickerview.data.Type;
import com.zysm.curtain.mwidgetgroup.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DatePickDialogActivity extends AppCompatActivity implements OnDateSetListener{

    TimePickerDialog mDialogAll;
    TimePickerDialog mDialogYearMonth;
    TimePickerDialog mDialogYearMonthDay;
    TimePickerDialog mDialogMonthDayHourMinute;
    TimePickerDialog mDialogHourMinute;

    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @BindView(R.id.btn_all)
    Button btnAll;
    @BindView(R.id.btn_year_month_day)
    Button btnYearMonthDay;
    @BindView(R.id.btn_year_month)
    Button btnYearMonth;
    @BindView(R.id.btn_month_day_hour_minute)
    Button btnMonthDayHourMinute;
    @BindView(R.id.btn_hour_minute)
    Button btnHourMinute;
    @BindView(R.id.tv_time)
    TextView tvTime;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_pick_dialog);
        ButterKnife.bind(this);
        mContext = this;
        init();
    }

    private void init(){
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;

        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("Cancel")
                .setSureStringId("Sure")
                .setTitleStringId("TimePicker")
                .setYearText("Year")
                .setMonthText("Month")
                .setDayText("Day")
                .setHourText("Hour")
                .setMinuteText("Minute")
                .setCyclic(true)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(ContextCompat.getColor(mContext,R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(ContextCompat.getColor(mContext,R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(ContextCompat.getColor(mContext,R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(12)
                .build();
/**
        mDialogAll = new TimePickerDialog.Builder()
                .setMinMillseconds(System.currentTimeMillis())
                .setThemeColor(R.color.colorPrimary)
                .setWheelItemTextSize(16)
                .setCallBack(this)
                .build();**/

        mDialogYearMonth = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH)
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setCallBack(this)
                .build();
        mDialogYearMonthDay = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH_DAY)
                .setCallBack(this)
                .build();
        mDialogMonthDayHourMinute = new TimePickerDialog.Builder()
                .setType(Type.MONTH_DAY_HOUR_MIN)
                .setCallBack(this)
                .build();
        mDialogHourMinute = new TimePickerDialog.Builder()
                .setType(Type.HOURS_MINS)
                .setCallBack(this)
                .build();
    }


    @OnClick({R.id.btn_all,R.id.btn_year_month_day,
            R.id.btn_year_month,R.id.btn_month_day_hour_minute,
            R.id.btn_hour_minute,R.id.tv_time})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_all:
                if(!mDialogAll.isAdded())
                mDialogAll.show(getSupportFragmentManager(), "all");
                break;
            case R.id.btn_year_month:
                if(!mDialogYearMonth.isAdded())
                mDialogYearMonth.show(getSupportFragmentManager(), "year_month");
                break;
            case R.id.btn_year_month_day:
                if(!mDialogYearMonthDay.isAdded())
                mDialogYearMonthDay.show(getSupportFragmentManager(), "year_month_day");
                break;
            case R.id.btn_month_day_hour_minute:
                if(!mDialogMonthDayHourMinute.isAdded())
                mDialogMonthDayHourMinute.show(getSupportFragmentManager(), "month_day_hour_minute");
                break;
            case R.id.btn_hour_minute:
                if(!mDialogHourMinute.isAdded())
                mDialogHourMinute.show(getSupportFragmentManager(), "hour_minute");
                break;
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {

        String text = getDateToString(millseconds);
        tvTime.setText(""+text);
    }

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }
}
