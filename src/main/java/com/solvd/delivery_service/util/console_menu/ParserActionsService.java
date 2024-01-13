package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.domain.actions.*;

import static com.solvd.delivery_service.util.Printers.*;

public class ParserActionsService {
    private static ParserActionsService instance;
    private int numberOfActionsService;

    private ParserActionsService() {}

    public static ParserActionsService getInstance() {
        if (instance == null) {
            instance = new ParserActionsService();
        }
        return instance;
    }

    protected void assignParser(String title) {
        numberOfActionsService = Parser.valueOf(title).getNumber();
        PRINT2LN.info(String.format("RUNNING USING '%s' SERVICE", Parser.valueOf(title).getDescription()));
    }

    public IParserActions getParserActions() {
        if (numberOfActionsService == 1) {
            return new StaxXmlParserActions();
        }
        if (numberOfActionsService == 2) {
            return new JaxbXmlParserActions();
        }
        if (numberOfActionsService == 3) {
            return new JacksonJsonParserActions();
        }
        return null;
    }
}