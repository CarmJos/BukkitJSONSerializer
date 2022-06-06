package cc.carm.lib.bukkit.configuration;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public interface JSONSerializable extends ConfigurationSerializable {

    /**
     * Serialize this object into a JSON string.
     *
     * @return JSON string contains serialized data.
     */
    default @NotNull String serializeToJSON() {
        return BukkitJSONSerializer.get().serializeToJSON(this);
    }

}
