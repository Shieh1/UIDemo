package com.zysm.curtain.mwidgetgroup.pickerview.listener;

import com.zysm.curtain.mwidgetgroup.pickerview.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by jzxiang on 16/4/20.
 */
public interface OnDateSetListener {

    void onDateSet(TimePickerDialog timePickerView, long millseconds);
}
