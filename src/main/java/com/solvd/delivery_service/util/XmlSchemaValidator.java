package com.solvd.delivery_service.util;

import com.solvd.delivery_service.util.custom_exceptions.XsdValidateException;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

import static com.solvd.delivery_service.util.Printers.*;

public class XmlSchemaValidator {
    public static void validate(File xmlFile, File xsdFile) throws XsdValidateException {
        String xmlName = xmlFile.getName();
        String xsdName = xsdFile.getName();
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlFile));
            PRINTLN.info(String.format("[Info]: XML file '%s' matches XSD file '%s'", xmlName, xsdName));
        } catch (SAXException e) {
            throw new XsdValidateException(String.format("[XsdValidateException]: XML file '%s' is not valid!", xmlName));
        } catch (IOException e) {
            throw new XsdValidateException(String.format("[XsdValidateException]: XML file '%s' (XSD file '%s') was not found!", xmlName, xsdName));
        }
    }
}