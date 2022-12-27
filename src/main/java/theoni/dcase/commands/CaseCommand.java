package theoni.dcase.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import theoni.dcase.DCase;
import theoni.dcase.utils.Translation;

public class CaseCommand extends Command {

    private DCase main;
    private Translation translation;

    public CaseCommand(DCase main) {
        super(main.getConfig().getString("commands.case.name"));
        this.setDescription(main.getConfig().getString("commands.case.description"));
        this.setAliases(main.getConfig().getStringList("commands.case.aliases").toArray(new String[]{}));
        this.setUsage(main.getTranslation().get("usage", main.getConfig().getString("commands.case.name")));

        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newEnum("action", new CommandEnum("Action", "open", "pay", "cases", "balance","help")),
                CommandParameter.newType("player", CommandParamType.TARGET)
        });

        this.main = main;
        this.translation = main.getTranslation();
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("dcase.player")) {
            sender.sendMessage(translation.get("no-permission"));
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(translation.get("in-game"));
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(getUsage());
            return false;
        }

        Player player = (Player) sender;
        switch(args[0]) {
            case "open":
                if (!main.getCaseManager().getOpening().contains(player)) {
                    if (main.getCaseManager().has(player, 1)) {
                        main.getCaseManager().open(player);
                    } else {
                        sender.sendMessage(translation.get("no-cases"));
                    }
                } else {
                    sender.sendMessage(translation.get("already-opening"));
                }
                break;

            case "pay":
                if (args.length == 2) {
                    Player target = main.getServer().getPlayer(args[1]);
                    int count = Integer.parseInt(args[2]);
                    if (main.getCaseManager().has(player, count)) {
                        main.getCaseManager().pay(target, player, count);
                        sender.sendMessage(translation.get("case-pay", target.getName(), count));
                        target.sendMessage(translation.get("case-pay-player", sender.getName(), count));
                    } else {
                        sender.sendMessage(translation.get("dont-have-that-many-cases", target.getName(), count));
                    }
                } else {
                    sender.sendMessage(getUsage());
                }
                break;
            
            case "mycases":
            case "balance":
            case "cases":
                player.sendMessage(translation.get("case-balance", main.getCaseManager().get(player)));
                break;

            case "help":
                String cmd = main.getConfig().getString("commands.case.name");
                sender.sendMessage(
                    "Help:\n" +
                    translation.get("help-open", cmd) + "\n" + 
                    translation.get("help-pay", cmd) + "\n" +
                    translation.get("help-cases", cmd) + "\n" +
                    translation.get("help-help", cmd));
                break;

            default:
                sender.sendMessage(getUsage());
                break;
        }
        return false;
    }
    
}
