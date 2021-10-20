package hr.mikec.webstore.controller;

import hr.mikec.webstore.util.BaseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShoppingCartCommandController {

    private String command;
    private static List<String> allowedCommands;

    public ShoppingCartCommandController() throws BaseException {
        super();
        populateAllowedCommands();
    }

    private void populateAllowedCommands() {
        allowedCommands = new ArrayList();
        allowedCommands.add("ADD");
        allowedCommands.add("REMOVE");
        allowedCommands.add("CHECKOUT");
        allowedCommands.add("END");
    }

    public List<String> shoppingCartCommand(String command) throws BaseException {
        List<String> splitCommand = Arrays.asList(command.split("\\s* \\s*"));
        if(!allowedCommands.contains(splitCommand.get(0).toUpperCase())){
            throw new BaseException("\nIllegal command " + splitCommand.get(0)
                    + ". Allowed commands are ADD, REMOVE, CHECKOUT and END");
        }

        if(splitCommand.get(0).equalsIgnoreCase("ADD") && splitCommand.size()!=3){
            throw new BaseException("\nCommand must be in format ADD 1 1");
        }

        if(splitCommand.get(0).equalsIgnoreCase("REMOVE") && splitCommand.size()!=3){
            throw new BaseException("\nCommand must be in format REMOVE 1 1");
        }

        return splitCommand;
    }

}

