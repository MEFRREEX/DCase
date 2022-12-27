package theoni.dcase;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import me.onebone.economyapi.EconomyAPI;
import net.lldv.llamaeconomy.LlamaEconomy;
import net.lldv.llamaeconomy.components.api.API;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

import java.util.List;
import java.util.Random;

public class RewardManager {
    
    private Config config;
    private DCase main;
    
    public RewardManager(DCase main) {
        this.main = main;
        this.config = main.getConfig();
    }


    public String get() {
        List<String> list = config.getStringList("rewards");
        Random random = new Random();
        int randomIndex = random.nextInt(list.size());
        String rand_value = list.get(randomIndex);
        return rand_value;
    }

    public void set(Player player) {
        String reward = this.get();
        String[] s = reward.split(":");
        if (s[0].equals("group")) {
            this.setGroup(player, s[1]);
            
        } else if (s[0].equals("money")) {
            this.addMoney(player, Integer.parseInt(s[1]));
            
        } else if (s[0].equals("case")) {
            main.getCaseManager().add(player, Integer.parseInt(s[1]));

        } else if (s[0].equals("command")) {
            if (s[1].equals("player")) {
                main.getServer().dispatchCommand(player, s[2]);
            } else if (s[1].equals("console")) {
                main.getServer().dispatchCommand(main.getServer().getConsoleSender(), s[2]);
            }
        }
    }

    public void addMoney(Player player, int count) {
        PluginManager mgr = main.getServer().getPluginManager();
        if (mgr.getPlugin("LlamaEconomy") != null) {
            API leco = LlamaEconomy.getAPI();
            leco.addMoney(player, count);

        }
        if (mgr.getPlugin("EconomyAPI") != null) {
            EconomyAPI eapi = EconomyAPI.getInstance();
            eapi.addMoney(player, count);
        }
    }

    public String getMonetaryUnit() {
        PluginManager mgr = main.getServer().getPluginManager();
        if (mgr.getPlugin("LlamaEconomy") != null) {
            API leco = LlamaEconomy.getAPI();
            return leco.getMonetaryUnit();

        } else if (mgr.getPlugin("EconomyAPI") != null) {
            EconomyAPI eapi = EconomyAPI.getInstance();
            return eapi.getMonetaryUnit();
        }
        return "";
    }

    public void setGroup(Player player, String group) {
        PluginManager mgr = main.getServer().getPluginManager();
        if (mgr.getPlugin("LuckPerms") != null) {
            LuckPerms api = LuckPermsProvider.get();
            api.getUserManager().getUser(player.getUniqueId()).setPrimaryGroup(group);
        }
    }
}
