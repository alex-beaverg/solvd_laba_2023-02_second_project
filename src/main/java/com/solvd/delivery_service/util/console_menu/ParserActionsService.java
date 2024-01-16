package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.domain.actions.parser_actions.*;

import static com.solvd.delivery_service.util.Printers.*;

public class ParserActionsService {
    private static ParserActionsService instance;
    private int numberOfParserActionsService;

    private ParserActionsService() {}

    public static ParserActionsService getInstance() {
        if (instance == null) {
            instance = new ParserActionsService();
        }
        return instance;
    }

    protected void assignParser(String title) {
        numberOfParserActionsService = Parser.valueOf(title).getNumber();
        PRINT2LN.info(String.format("RUNNING USING '%s' SERVICE", Parser.valueOf(title).getDescription()));
    }

    public IParserActions getParserActions() {
        IParserActions parserActions = null;
        switch (numberOfParserActionsService) {
            case (1) -> parserActions = new StaxXmlParserActions();
            case (2) -> parserActions = new JaxbXmlParserActions();
            case (3) -> parserActions = new JacksonJsonParserActions();
        }
        return parserActions;
    }
}