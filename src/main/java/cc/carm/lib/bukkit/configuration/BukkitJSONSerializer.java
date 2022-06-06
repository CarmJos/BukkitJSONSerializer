package cc.carm.lib.bukkit.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * BukkitJSONSerializer, a lightweight JSON serializer for {@link ConfigurationSerializable} objects.
 * <ul>
 *   <li>serialize by {@link #serializeToJSON(ConfigurationSerializable)}</li>
 *   <li>deserialize by {@link #deserializeJSON(String, Class, ConfigurationSerializable)}</li>
 * </ul>
 *
 * @author CarmJos
 * @since 1.0.0
 */
public class BukkitJSONSerializer {

    public static final String TYPE_KEY = ConfigurationSerialization.SERIALIZED_TYPE_KEY;
    public static final BukkitJSONSerializer INSTANCE = BukkitJSONSerializer.create();

    public static @NotNull BukkitJSONSerializer create() {
        return create(new GsonBuilder().disableHtmlEscaping().create(), new JsonParser());
    }

    public static @NotNull BukkitJSONSerializer create(@NotNull Gson gson, @NotNull JsonParser parser) {
        return new BukkitJSONSerializer(gson, parser);
    }

    public static @NotNull BukkitJSONSerializer get() {
        return BukkitJSONSerializer.INSTANCE;
    }

    protected final @NotNull Gson gson;
    protected final @NotNull JsonParser parser;

    public BukkitJSONSerializer(@NotNull Gson gson, @NotNull JsonParser parser) {
        this.gson = gson;
        this.parser = parser;
    }

    /**
     * Serializes a {@link ConfigurationSerializable} object to Map.
     *
     * @param value object to serialize.
     * @param <T>   {@link ConfigurationSerializable} object type.
     * @return Map containing serialized data
     */
    public <T extends ConfigurationSerializable> Map<String, Object> serializeToMap(T value) {
        Map<String, Object> values = new LinkedHashMap<>();
        // First, put tye type key;
        values.put(TYPE_KEY, ConfigurationSerialization.getAlias(value.getClass()));
        // Then, put the serialized value
        value.serialize().forEach((key, sub) -> {
            if (sub instanceof ConfigurationSerializable) {
                values.put(key, serializeToMap((ConfigurationSerializable) sub));
            } else {
                values.put(key, sub);
            }
        });
        return values;
    }

    /**
     * Serializes a {@link ConfigurationSerializable} object to JSON.
     *
     * @param value object to serialize.
     * @param <T>   {@link ConfigurationSerializable} object type.
     * @return JSON string containing serialized data.
     */
    public <T extends ConfigurationSerializable> String serializeToJSON(T value) {
        return gson.toJson(serializeToMap(value));
    }

    /**
     * Deserializes a {@link ConfigurationSerializable} object from JSON.
     *
     * @param json JSON string to deserialize.
     * @return Deserialized object.
     */
    @Contract("null->null")
    public Object deserializeJSON(@Nullable String json) {
        return deserializeJSON(json, (ConfigurationSerializable) null);
    }

    /**
     * Deserializes a {@link ConfigurationSerializable} object from JSON.
     *
     * @param json         JSON string to deserialize.
     * @param defaultValue default value to return if json is null or failed to deserialize.
     * @return Deserialized object.
     */
    @Contract("_,!null->!null; null,null->null")
    public ConfigurationSerializable deserializeJSON(@Nullable String json,
                                                     @Nullable ConfigurationSerializable defaultValue) {
        if (json == null) return defaultValue;
        Map<String, Object> args = jsonToMap(json);

        return Optional.ofNullable((String) args.get(TYPE_KEY))
                .map(ConfigurationSerialization::getClassByAlias)
                .map(clazz -> ConfigurationSerialization.deserializeObject(args, clazz))
                .orElse(defaultValue);
    }

    /**
     * Deserializes a {@link ConfigurationSerializable} object from Map.
     *
     * @param json      JSON string to deserialize.
     * @param typeClazz type of object.
     * @param <T>       {@link ConfigurationSerializable} object type.
     * @return Deserialized object.
     */
    public <T extends ConfigurationSerializable> @Nullable T deserializeJSON(@Nullable String json,
                                                                             @NotNull Class<T> typeClazz) {
        return deserializeJSON(json, typeClazz, null);
    }

    /**
     * Deserializes a {@link ConfigurationSerializable} object from Map.
     *
     * @param json         JSON string to deserialize.
     * @param typeClazz    type of object.
     * @param defaultValue default value to return if json is null or failed to deserialize.
     * @param <T>          {@link ConfigurationSerializable} object type.
     * @return Deserialized object.
     */
    @Contract("_,_,!null->!null; null,_,null->null")
    public <T extends ConfigurationSerializable> T deserializeJSON(@Nullable String json,
                                                                   @NotNull Class<T> typeClazz,
                                                                   @Nullable T defaultValue) {
        Object value = deserializeJSON(json, defaultValue);
        if (!typeClazz.isInstance(value)) return defaultValue;

        return typeClazz.cast(value);
    }

    protected Map<String, Object> jsonToMap(String json) {
        return jsonToMap(parser.parse(json).getAsJsonObject());
    }

    protected Map<String, Object> jsonToMap(JsonObject object) {
        return parseMap(gson.fromJson(object, Map.class));
    }

    protected Map<String, Object> parseMap(Map<?, ?> map) {
        Map<String, Object> args = new LinkedHashMap<>();
        map.forEach((k, v) -> {
            String key = (String) k;
            if (v instanceof Map<?, ?>) {
                Map<String, Object> sub = parseMap((Map<?, ?>) v);
                if (sub.containsKey(TYPE_KEY)) {
                    args.put(key, ConfigurationSerialization.deserializeObject(sub));
                } else {
                    args.put(key, sub);
                }
            } else {
                args.put(key, v);
            }
        });
        return args;
    }


}
