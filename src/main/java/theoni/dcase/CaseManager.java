package theoni.dcase;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CaseManager {
    
    private Config data;
    private Config config;
    private DCase main;
    @Getter private List<Player> opening;
    
    public CaseManager(DCase main) {
        this.main = main;
        this.config = main.getConfig();
        this.data = new Config(new File(main.getDataFolder() + "/data/data.yml"), Config.YAML);
        this.opening = new ArrayList<>();
    }


    public void add(Player player, int count) {
        int c = data.getInt(player.getUniqueId().toString());
        data.set(player.getUniqueId().toString(), c + count);
        data.save();
    }

    public void reduce(Player player, int count) {
        int c = data.getInt(player.getUniqueId().toString());
        data.set(player.getUniqueId().toString(), c - count);
        data.save();
    }

    public void set(Player player, int count) {
        data.set(player.getUniqueId().toString(),count);
        data.save();
    }

    public void clear(Player player) {
        data.remove(player.getUniqueId().toString());
        data.save();
    }

    public int get(Player player) {
        int c = data.getInt(player.getUniqueId().toString());
        return c;
    }

    public boolean has(Player player, int count) {
        if (get(player) >= count) return true;
        return false;
    }
    
    public void pay(Player from, Player to, int count) {
        if (has(from, count)) {
            reduce(from, count);
            add(to, count);
        }
    }

    public void open(Player player) {
        if (has(player, 1)) {
            reduce(player, 1);
            player.sendTitle(main.getTranslation().get("title-title-opening"), main.getTranslation().get("title-subtitle-opening"));
            opening.add(player);
            (new Timer()).schedule(new TimerTask() {
                public void run() {
                    RewardManager rm = main.getRewardManager();
                    String reward = rm.get();
                    String[] s = reward.split(":");

                    if (s[0].equals("group")) {
                        rm.setGroup(player, s[1]);
                        if (player.isOnline()) {
                            player.sendTitle(main.getTranslation().get("title-title-opened", 
                                    config.getString("reward-placeholders.group").replace("[0]", s[1])), main.getTranslation().get("title-subtitle-opened", 
                                    config.getString("reward-placeholders.group").replace("[0]", s[1])));
                            main.getServer().broadcastMessage(main.getTranslation().get("broadcast", player.getName(), 
                                    config.getString("reward-placeholders.group").replace("[0]", s[1])));
                        }
                    } else if (s[0].equals("money")) {
                        rm.addMoney(player, Integer.parseInt(s[1]));
                        if (player.isOnline()) {
                            player.sendTitle(main.getTranslation().get("title-title-opened", 
                                    config.getString("reward-placeholders.money").replace("[0]", s[1]).replace("[1]", rm.getMonetaryUnit())), main.getTranslation().get("title-subtitle-opened", 
                                    config.getString("reward-placeholders.money").replace("[0]", s[1]).replace("[1]", rm.getMonetaryUnit())));
                            main.getServer().broadcastMessage(main.getTranslation().get("broadcast", player.getName(), 
                                    config.getString("reward-placeholders.money").replace("[0]", s[1]).replace("[1]", rm.getMonetaryUnit())));
                        }
                    } else if (s[0].equals("case")) {
                        add(player, Integer.parseInt(s[1]));
                        if (player.isOnline()) {
                            player.sendTitle(main.getTranslation().get("title-title-opened", 
                                    config.getString("reward-placeholders.cases").replace("[0]", s[1])), main.getTranslation().get("title-subtitle-opened", 
                                    config.getString("reward-placeholders.cases").replace("[0]", s[1])));
                            main.getServer().broadcastMessage(main.getTranslation().get("broadcast", player.getName(), 
                                    config.getString("reward-placeholders.cases").replace("[0]", s[1])));
                        }
                    
                    } else if (s[0].equals("command")) {
                        if (player.isOnline()) {
                            if (s[1].equals("player")) {
                                main.getServer().dispatchCommand(player, s[2].replace("%player%", player.getName()));
                            } else if (s[1].equals("console")) {
                                main.getServer().dispatchCommand(main.getServer().getConsoleSender(), s[2].replace("%player%", player.getName()));
                            }
                        }
                    }
                    opening.remove(player);
                }
            }, 1000 * 2);
            main.getRewardManager().set(player);
        }
    }
}
