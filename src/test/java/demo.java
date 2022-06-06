import cc.carm.lib.bukkit.configuration.BukkitJSONSerializer;
import cc.carm.lib.bukkit.configuration.JSONSerializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class demo {

    public static void demoUsage() {

        Location location = new Location(Bukkit.getWorlds().get(0), -100.5, 100, 105.5);

        String serialized = BukkitJSONSerializer.serializeToJSON(location);
        // serialized -> {"world":"world","x":-100.5,"y":100,"z":105.5,"yaw":0.0,"pitch":0.0}

        Location deserialized = BukkitJSONSerializer.deserializeJSON(serialized, Location.class);

    }

    public static final class SomeValue implements JSONSerializable {

        long time;
        UUID uuid;

        public SomeValue(long time, UUID uuid) {
            this.time = time;
            this.uuid = uuid;
        }

        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("time", time);
            map.put("uuid", uuid.toString());
            return map;
        }

        public static SomeValue deserialize(Map<String, Object> map) {
            return new SomeValue(
                    (long) map.get("time"),
                    UUID.fromString((String) map.get("uuid"))
            );
        }

    }

}