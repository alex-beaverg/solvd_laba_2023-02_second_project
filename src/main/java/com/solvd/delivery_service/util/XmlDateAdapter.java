package com.solvd.delivery_service.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;

public class XmlDateAdapter extends XmlAdapter<String, LocalDate> {
    @Override
    public LocalDate unmarshal(String s) {
        return LocalDate.of(
                Integer.parseInt(s.split("-")[0]),
                Integer.parseInt(s.split("-")[1]),
                Integer.parseInt(s.split("-")[2]));
    }

    @Override
    public String marshal(LocalDate localDate) {
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int dayOfMonth = localDate.getDayOfMonth();
        return String.format("%d.%d.%d", dayOfMonth, month, year);
    }
}