package theoni.dcase;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import cn.nukkit.utils.Config;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocationManager {
    
    private DCase main;
    private Config location;
    @Getter private List<Player> players;

    public LocationManager(DCase main) {
        this.main = main;
        this.location = new Config(new File(main.getDataFolder() + "/data/location.yml"), Config.YAML);
        this.players = new ArrayList<>();
    }

    public void set(Location pos) {
        location.set("location.x", pos.getX());
        location.set("location.y", pos.getY());
        location.set("location.z", pos.getZ());
        location.set("location.level", pos.getLevel().getName());
        location.save();
    }

    public Location get() {
        Location loc = new Location(
            location.getDouble("location.x"), location.getDouble("location.y"), location.getDouble("location.z"),
            main.getServer().getLevelByName(location.getString("location.level")));
        return loc;
    }

    public void teleport(Player player) {
        player.teleport(get());
    }
}
