package hr.mikec.webstore.controller;

import hr.mikec.webstore.util.BaseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductCommandController{

    private String command;
    private static List<String> allowedCommands;

    public ProductCommandController() throws BaseException {
        super();
        populateAllowedCommands();
    }

    private void populateAllowedCommands() {
        allowedCommands = new ArrayList();
        allowedCommands.add("ADD");
        allowedCommands.add("END");
    }

    public List<String> productCommand(String command) throws BaseException {
        List<String> splitCommand = Arrays.asList(command.split("\\s* \\s*"));
        if(!allowedCommands.contains(splitCommand.get(0).toUpperCase())){
            throw new BaseException("\nIllegal command " + splitCommand.get(0) + ". Allowed commands are ADD and END");
        }

        if(splitCommand.get(0).equalsIgnoreCase("ADD") && splitCommand.size()!=5){
            throw new BaseException("\nCommand must be in format ADD 1 Product 1 5.00");
        }

        return splitCommand;
    }

}

