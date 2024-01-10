package com.solvd.delivery_service.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;

public class JsonDateAdapter extends JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext context) throws IOException {
        return LocalDate.parse(p.readValueAs(String.class));
    }

    public String serialize(LocalDate localDate) {
        return String.format("%d.%d.%d", localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
    }
}
