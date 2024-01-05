package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.domain.actions.IParserActions;
import com.solvd.delivery_service.domain.actions.JaxbXmlParserActions;
import com.solvd.delivery_service.domain.actions.StaxXmlParserActions;

public class ParserService {
    private static ParserService instance;
    private boolean isStaxParserService;

    private ParserService() {}

    public static ParserService getInstance() {
        if (instance == null) {
            instance = new ParserService();
            instance.isStaxParserService = true;
        }
        return instance;
    }

    protected void assignStaxParserService() {
        isStaxParserService = true;
    }

    protected void assignJaxbParserService() {
        isStaxParserService = false;
    }

    public boolean isStaxParserService() {
        return isStaxParserService;
    }

    public IParserActions getParser() {
        if (isStaxParserService) {
            return new StaxXmlParserActions();
        } else {
            return new JaxbXmlParserActions();
        }
    }
}
