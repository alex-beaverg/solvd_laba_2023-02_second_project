package com.solvd.delivery_service.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvd.delivery_service.util.custom_exceptions.JsonValidateException;

import java.io.File;
import java.io.IOException;

import static com.solvd.delivery_service.util.Printers.PRINTLN;

public class JsonReader {
    public static <T> T validateAndReadValue(File jsonFile, Class<T> clazz) throws JsonValidateException {
        T value;
        String fileName = jsonFile.getName();
        ObjectMapper mapper = new ObjectMapper();
        try {
            value = mapper.readValue(jsonFile, clazz);
            PRINTLN.info(String.format("[Info]: JSON file '%s' is valid", fileName));
        } catch (JacksonException e) {
            throw new JsonValidateException(String.format("[JsonValidateException]: JSON file '%s' is not valid!", fileName));
        } catch (IOException e) {
            throw new JsonValidateException(String.format("[XsdValidateException]: JSON file '%s' was not found!", fileName));
        }
        return value;
    }
}