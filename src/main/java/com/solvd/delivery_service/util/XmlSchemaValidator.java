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
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlFile));
            PRINTLN.info("[Info]: XML file '" + xmlFile.getName() + "' matches XSD file '" + xsdFile.getName() + "'");
        } catch (SAXException e) {
            throw new XsdValidateException("[XsdValidateException]: XML file '" + xmlFile.getName() + "' is not valid!");
        } catch (IOException e) {
            throw new XsdValidateException("[XsdValidateException]: XML file '" + xmlFile.getName() +
                    "' (XSD file '" + xsdFile.getName() + "') is not exist!");
        }
    }
}