package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.domain.actions.IParserActions;
import com.solvd.delivery_service.domain.actions.JaxbXmlParserActions;
import com.solvd.delivery_service.domain.actions.StaxXmlParserActions;

public class ParserActionsService {
    private static ParserActionsService instance;
    private boolean isStaxXmlParserActionsService;

    private ParserActionsService() {}

    public static ParserActionsService getInstance() {
        if (instance == null) {
            instance = new ParserActionsService();
        }
        return instance;
    }

    protected void assignStaxXmlParserActionsService() {
        isStaxXmlParserActionsService = true;
    }

    protected void assignJaxbXmlParserActionsService() {
        isStaxXmlParserActionsService = false;
    }

    public IParserActions getParserActions() {
        if (isStaxXmlParserActionsService) {
            return new StaxXmlParserActions();
        } else {
            return new JaxbXmlParserActions();
        }
    }
}
