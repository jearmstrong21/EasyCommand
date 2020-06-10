package p0nki.easycommand;

import java.util.List;

public class RealCommandCog {

    private final String name;
    private final List<RealCommand> commands;

    public RealCommandCog(String name, List<RealCommand> commands) {
        this.name = name;
        this.commands = commands;
    }

    public String getName() {
        return name;
    }

    public List<RealCommand> getCommands() {
        return commands;
    }
}
