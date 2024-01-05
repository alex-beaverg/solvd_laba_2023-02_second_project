package com.solvd.delivery_service.util;

import com.solvd.delivery_service.util.custom_exceptions.XsdException;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class XmlSchemaValidator {
    public static void validate(File xmlFile, File xsdFile) throws XsdException {
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlFile));
        } catch (IOException | SAXException e) {
            throw new XsdException("[XsdException]: XML file is not valid!");
        }
    }
}
