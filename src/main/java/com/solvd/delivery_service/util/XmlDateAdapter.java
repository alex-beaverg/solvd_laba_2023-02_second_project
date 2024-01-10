package com.solvd.delivery_service.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class XmlDateAdapter extends XmlAdapter<String, LocalDate> {
    @Override
    public LocalDate unmarshal(String s) {
        List<Integer> dateList = Arrays.stream(s.split("-")).map(Integer::parseInt).toList();
        return LocalDate.of(dateList.get(0), dateList.get(1), dateList.get(2));
    }

    @Override
    public String marshal(LocalDate localDate) {
        return String.format("%d.%d.%d", localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
    }
}