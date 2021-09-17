package de.shiirroo.manhunt.command;

import java.util.ArrayList;
import java.util.List;

public class CommandBuilder{

    private final String name;
    private boolean isNeedOP = false;
    private final List<CommandBuilder> sCBList = new ArrayList<>();


    public CommandBuilder(String name){
        this.name = name;
    }

    public CommandBuilder(String name, Boolean OP){
        this.name = name;
        this.isNeedOP = OP;
    }

    public boolean hasSubCommands(){
        return this.sCBList.size() > 0;
    }


    public List<String> getSubCommandListAsString(Boolean isOP){
        List<String> subCommand = new ArrayList<>();
        for(CommandBuilder subCommandBuilder :this.sCBList){
            if(subCommandBuilder.isNeedOP == isOP || isOP)
                subCommand.add(subCommandBuilder.getCommandName());
        }
        return subCommand;
    }


    public String getCommandName() {
        return this.name;
    }


    public CommandBuilder getSubCommand(String command, Boolean isOP) {
        for(CommandBuilder subCommandBuilder :this.sCBList){
            if(subCommandBuilder.getCommandName().equalsIgnoreCase(command))
                if(subCommandBuilder.isNeedOP == isOP || isOP)
                    return subCommandBuilder;
        }
        return null;
    }



    public void addSubCommandBuilder(CommandBuilder subCommand){
        this.sCBList.add(subCommand);
    }


}
