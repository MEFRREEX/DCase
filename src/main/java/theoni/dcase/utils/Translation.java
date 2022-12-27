package theoni.dcase.utils;

import java.io.File;

import cn.nukkit.utils.Config;
import theoni.dcase.DCase;

public class Translation {
    
    private Config messages;

    public Translation(DCase main) {
        this.messages = new Config(new File(main.getDataFolder() + "/messages.yml"), Config.YAML);
    }

    public String get(String key, Object... replacements) {
        String message = messages.getString(key).replace("&", "§");

        int i = 0;
        for (Object replacement : replacements) {
            message = message.replace("[" + i + "]", String.valueOf(replacement));
            i++;
        }
        
        return message;
    }
}
