package com.example.coursemate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    // Chuyển đổi định dạng ngày tháng từ "yyyy-MM-dd" sang "dd-MM-yyyy"
    public String convertDateFormat(String originalDate) {
        if (originalDate == null || originalDate.isEmpty()) return "";
        String originalFormat = "yyyy-MM-dd";
        String targetFormat = "dd-MM-yyyy";
        SimpleDateFormat originalDateFormat = new SimpleDateFormat(originalFormat);
        SimpleDateFormat targetDateFormat = new SimpleDateFormat(targetFormat);
        String formattedDate = "";
        try {
            Date date = originalDateFormat.parse(originalDate);
            formattedDate = targetDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    // Chuyển đổi định dạng ngày tháng từ "dd-MM-yyyy" sang "yyyy-MM-dd"
    public String reverseConvertDateFormat(String formattedDate) {
        if (formattedDate == null || formattedDate.isEmpty()) return "";
        String originalFormat = "dd-MM-yyyy";
        String targetFormat = "yyyy-MM-dd";
        SimpleDateFormat originalDateFormat = new SimpleDateFormat(originalFormat);
        SimpleDateFormat targetDateFormat = new SimpleDateFormat(targetFormat);
        String convertedDate = "";
        try {
            Date date = originalDateFormat.parse(formattedDate);
            convertedDate = targetDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
}
