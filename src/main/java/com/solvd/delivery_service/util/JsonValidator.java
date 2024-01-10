package com.solvd.delivery_service.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvd.delivery_service.util.custom_exceptions.JsonValidateException;

import java.io.File;
import java.io.IOException;

import static com.solvd.delivery_service.util.Printers.PRINTLN;

public class JsonValidator {
    public static <T> T validateAndReadValue(File jsonFile, Class<T> clazz) throws JsonValidateException {
        T value;
        ObjectMapper mapper = new ObjectMapper();
        try {
            value = mapper.readValue(jsonFile, clazz);
            PRINTLN.info("[Info]: JSON file '" + jsonFile.getName() + "' is valid");
        } catch (JacksonException e) {
            throw new JsonValidateException("[JsonValidateException]: JSON file '" + jsonFile.getName() + "' is not valid!");
        } catch (IOException e) {
            throw new JsonValidateException("[XsdValidateException]: JSON file '" + jsonFile.getName() + "' is not exist!");
        }
        return value;
    }
}