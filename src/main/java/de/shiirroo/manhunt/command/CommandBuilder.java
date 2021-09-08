package de.shiirroo.manhunt.command;

import java.util.ArrayList;
import java.util.List;

public class CommandBuilder{

    private String name;
    private boolean isNeedOP = false;
    private List<CommandBuilder> sCBList = new ArrayList<CommandBuilder>();


    public CommandBuilder(String name){
        this.name = name;
    }

    public CommandBuilder(String name, Boolean OP){
        this.name = name;
        this.isNeedOP = OP;

    }

    public boolean hasSubCommands(){
        if(this.sCBList.size() > 0)
            return true;
        return false;
    }


    public List<String> getSubCommandListAsString(Boolean isOP){
        List<String> subCommand = new ArrayList<>();
        for(CommandBuilder subCommandBuilder :this.sCBList){
            if(subCommandBuilder.isNeedOP == isOP || isOP == true)
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
                if(subCommandBuilder.isNeedOP == isOP || isOP == true)
                    return subCommandBuilder;
        }
        return null;
    }



    public void addSubCommandBuilder(CommandBuilder subCommand){
        this.sCBList.add(subCommand);
    }


}
