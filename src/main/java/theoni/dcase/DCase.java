package theoni.dcase;

import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import theoni.dcase.commands.*;
import theoni.dcase.listeners.EventListener;
import theoni.dcase.utils.*;
import lombok.Getter;

public class DCase extends PluginBase implements Listener {

    @Getter private static DCase instance;
    @Getter private LocationManager locationManager;
    @Getter private CaseManager caseManager;
    @Getter private RewardManager rewardManager;
    @Getter private Translation translation;

    public void onLoad() {
        this.saveDefaultConfig();
        this.saveResource("messages.yml");

        instance = this;
        locationManager = new LocationManager(this);
        caseManager = new CaseManager(this);
        rewardManager = new RewardManager(this);
        translation = new Translation(this);

    }

    public void onEnable() {
        PluginManager mgr = this.getServer().getPluginManager();
        if (mgr.getPlugin("LuckPerms") == null) {
            this.getLogger().warning("§cThe LuckPerms plugin was not found. Disabling the LuckPerms module...");
        }
        if (mgr.getPlugin("LlamaEconomy") == null && mgr.getPlugin("EconomyAPI") == null) {
            this.getLogger().warning("§cThe LlamaEconomy or EconomyAPI plugin was not found. Disabling module...");
        }
        this.getServer().getCommandMap().register("help", new CaseAdminCommand(this));
        this.getServer().getCommandMap().register("help", new CaseCommand(this));

        this.getServer().getPluginManager().registerEvents((Listener) new EventListener(this), (DCase) this);
    }
}
