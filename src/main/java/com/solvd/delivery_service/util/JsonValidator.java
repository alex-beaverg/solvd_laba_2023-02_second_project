package com.solvd.delivery_service.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvd.delivery_service.util.custom_exceptions.JsonValidateException;

import java.io.File;
import java.io.IOException;

import static com.solvd.delivery_service.util.Printers.PRINTLN;

public class JsonValidator {
    public static void validate(File jsonFile) throws JsonValidateException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readTree(jsonFile);
            PRINTLN.info("[Info]: JSON file '" + jsonFile.getName() + "' is valid");
        } catch (JacksonException e) {
            throw new JsonValidateException("[JsonValidateException]: JSON file '" + jsonFile.getName() + "' is not valid!");
        } catch (IOException e) {
            throw new JsonValidateException("[XsdValidateException]: JSON file '" + jsonFile.getName() + "' is not exist!");
        }
    }
}