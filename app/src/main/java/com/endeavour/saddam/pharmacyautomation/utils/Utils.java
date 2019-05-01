package com.endeavour.saddam.pharmacyautomation.utils;

import android.text.TextUtils;
import android.widget.EditText;

import com.endeavour.saddam.pharmacyautomation.db.dto.StatsDTO;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {

    public static boolean isEmpty(String data) {
        return data.trim().isEmpty();
    }

    public static boolean isNumber(String data) {
        return !data.isEmpty() && TextUtils.isDigitsOnly(data.trim());
    }

    public static boolean isDate(String date) {
        try {
            Constants.DATE_FORMAT.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static int getNumber(EditText editText) {
        return Integer.parseInt(editText.getText().toString().trim());
    }

    public static Long getDate(EditText editText) throws ParseException {
        return Constants.DATE_FORMAT.parse(editText.getText().toString().trim()).getTime();
    }

    public static String formatDate(long date) {
        return Constants.DATE_FORMAT.format(new Date(date));
    }

    public static String formatTime(Date time) {
        return Constants.TIME_FORMAT.format(time);
    }

    public static String[] getArrayOfString(String data) {
        return Constants.GSON.fromJson(data, String[].class);
    }

    public static String formatMonth(int month) {
        return Constants.FORMAT_SYMBOLS.getMonths()[month - 1];
    }

    public static List<StatsDTO> dummyData() {
        List<StatsDTO> statsDTOS = new ArrayList<>();
        for (int year = 2018; year >= 2017; year--) {
            for (int month = 12; month >= 1; month--) {
                for (int day = 30; day >= 1; day--) {
                    StatsDTO statsDTO = new StatsDTO();
                    statsDTO.setDay(day);
                    statsDTO.setMonth(month);
                    statsDTO.setYear(year);
                    statsDTO.setValue((float) (Math.random() * (100 + 1)));
                    statsDTOS.add(statsDTO);
                }
            }
        }
        return statsDTOS;
    }

    public static String formatLabel(int year, int month) {
        return formatMonth(month) + ", " + year;
    }

    public static float total(BarData data) {
        float sum = 0;
        for (IBarDataSet iBarDataSet : data.getDataSets()) {
            for (int i = 0; i < iBarDataSet.getEntryCount(); i++) {
                sum += iBarDataSet.getEntryForIndex(i).getY();
            }
        }
        return sum;
    }
}
