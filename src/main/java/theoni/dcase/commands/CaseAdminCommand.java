package theoni.dcase.commands;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import theoni.dcase.DCase;
import theoni.dcase.utils.Translation;

public class CaseAdminCommand extends Command {

    private DCase main;
    private Translation translation;

    public CaseAdminCommand(DCase main) {
        super(main.getConfig().getString("commands.case-admin.name"));
        this.setDescription(main.getConfig().getString("commands.case-admin.description"));
        this.setAliases(main.getConfig().getStringList("commands.case-admin.aliases").toArray(new String[]{}));
        this.setUsage(main.getTranslation().get("usage", main.getConfig().getString("commands.case-admin.name")));

        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newEnum("actions", new CommandEnum("Actions", "add", "set", "take", "setcase", "help")),
                CommandParameter.newType("player", CommandParamType.TARGET)
        });

        this.main = main;
        this.translation = main.getTranslation();
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("case.admin")) {
            sender.sendMessage(translation.get("no-permission"));
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(getUsage());
            return false;
        }
        switch(args[0]) {
            case "add":
                if (args.length == 3) {
                    Player target = main.getServer().getPlayer(args[1]);
                    main.getCaseManager().add(target, Integer.parseInt(args[2]));
                    sender.sendMessage(translation.get("case-added", target.getName(), args[2]));
                    target.sendMessage(translation.get("case-added-player", sender.getName(), args[2]));
                } else {
                    sender.sendMessage(getUsage());
                }
                break;

            case "take":
                if (args.length == 3) {
                    Player target = main.getServer().getPlayer(args[1]);
                    main.getCaseManager().reduce(target, Integer.parseInt(args[2]));
                    sender.sendMessage(translation.get("case-taken", target.getName(), args[2]));
                    target.sendMessage(translation.get("case-taken-player", sender.getName(), args[2]));
                } else {
                    sender.sendMessage(getUsage());
                }
                break;

            case "set":
                if (args.length == 2) {
                    Player target = main.getServer().getPlayer(args[1]);
                    main.getCaseManager().set(target, Integer.parseInt(args[2]));
                    sender.sendMessage(translation.get("case-setted", target.getName(), args[2]));
                    target.sendMessage(translation.get("case-setted-player", sender.getName(), args[2]));
                } else {
                    sender.sendMessage(getUsage());
                }
                break;
            
            case "setcase":
                if (sender instanceof Player) {
                    List<Player> players = main.getLocationManager().getPlayers();
                    if (players.contains(sender)) {
                        players.remove(sender);
                    } else {
                        players.add((Player) sender);
                        sender.sendMessage(translation.get("case-position"));
                    }
                } else {
                    sender.sendMessage(translation.get("in-game"));
                }
                break;

            case "help":
                String cmd = main.getConfig().getString("commands.case-admin.name");
                sender.sendMessage(
                    "Help:\n" +
                    translation.get("help-add", cmd) + "\n" + 
                    translation.get("help-take", cmd) + "\n" +
                    translation.get("help-set", cmd) + "\n" + 
                    translation.get("help-help", cmd));
                break;

            default:
                sender.sendMessage(getUsage());
                break;
        }
        return false;
    }
    
}
