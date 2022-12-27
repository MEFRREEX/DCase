package theoni.dcase.listeners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import theoni.dcase.DCase;
import theoni.dcase.LocationManager;

public class EventListener implements Listener {

    private DCase main;

    public EventListener(DCase main) {
        this.main = main;
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        LocationManager loc = main.getLocationManager();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (loc.getPlayers().contains(player)) {
                loc.set(block.getLocation());
                player.sendMessage(main.getTranslation().get("case-position-setted"));
                loc.getPlayers().remove(player);
                event.setCancelled();
                return;
            }
            if (block.getLocation().equals(loc.get())) {
                if (!main.getCaseManager().getOpening().contains(player)) {
                    if (main.getCaseManager().has(player, 1)) {
                        main.getCaseManager().open(player);
                    } else {
                        player.sendMessage(main.getTranslation().get("no-cases"));
                    }
                } else {
                    player.sendMessage(main.getTranslation().get("already-opening"));
                }
                event.setCancelled();
            }
        }
    }
}
