package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.domain.actions.IParserActions;
import com.solvd.delivery_service.domain.actions.JacksonJsonParserActions;
import com.solvd.delivery_service.domain.actions.JaxbXmlParserActions;
import com.solvd.delivery_service.domain.actions.StaxXmlParserActions;

import static com.solvd.delivery_service.util.Printers.*;

public class ParserActionsService {
    private static ParserActionsService instance;
    private int numberOfActionService;

    private ParserActionsService() {}

    public static ParserActionsService getInstance() {
        if (instance == null) {
            instance = new ParserActionsService();
        }
        return instance;
    }

    protected void assignStaxXmlParserActionsService() {
        PRINT2LN.info("RUNNING USING 'STAX (XML) PARSER' SERVICE");
        numberOfActionService = 0;
    }

    protected void assignJaxbXmlParserActionsService() {
        PRINT2LN.info("RUNNING USING 'JAXB (XML) PARSER' SERVICE");
        numberOfActionService = 1;
    }

    protected void assignJacksonJsonParserActionsService() {
        PRINT2LN.info("RUNNING USING 'JACKSON (JSON) PROCESSOR' SERVICE");
        numberOfActionService = 2;
    }

    public IParserActions getParserActions() {
        if (numberOfActionService == 0) {
            return new StaxXmlParserActions();
        }
        if (numberOfActionService == 1) {
            return new JaxbXmlParserActions();
        }
        if (numberOfActionService == 2) {
            return new JacksonJsonParserActions();
        }
        return null;
    }
}
