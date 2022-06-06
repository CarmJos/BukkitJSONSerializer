# BukkitJSONSerializer

[![version](https://img.shields.io/github/v/release/CarmJos/BukkitJSONSerializer)](https://github.com/CarmJos/BukkitJSONSerializer/releases)
[![License](https://img.shields.io/github/license/CarmJos/BukkitJSONSerializer)](https://opensource.org/licenses/MIT)
[![workflow](https://github.com/CarmJos/BukkitJSONSerializer/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/BukkitJSONSerializer/actions/workflows/maven.yml)
[![CodeFactor](https://www.codefactor.io/repository/github/carmjos/BukkitJSONSerializer/badge)](https://www.codefactor.io/repository/github/carmjos/BukkitJSONSerializer)
![CodeSize](https://img.shields.io/github/languages/code-size/CarmJos/BukkitJSONSerializer)
![](https://visitor-badge.glitch.me/badge?page_id=BukkitJSONSerializer.readme)

A JSON serialize/deserialize util for bukkit's ConfigurationSerialization.

## Usage

### Basic usage

We cloud use `BukkitJSONSerializer#serializeToJSON(ConfigurationSerializable)` to serialize a object to JSON.

```jave
        Location location = new Location(Bukkit.getWorlds().get(0), -100.5, 100, 105.5);
        String serialized = BukkitJSONSerializer.serializeToJSON(location);
        // serialized -> {"world":"world","x":-100.5,"y":100,"z":105.5,"yaw":0.0,"pitch":0.0}
```

When we need to read the object, just use `BukkitJSONSerializer#deserializeSON(json,typeClass)` to deserialize the JSON
string.

```java
        Location deserialized=BukkitJSONSerializer.deserializeJSON(serialized,Location.class);
```

Or use `BukkitJSONSerializer#deserializeSON(json,typeClass,defaultValue)` if we need a default value.

### JSONSerializable class
This project provided an interface `JSONSerializable` which provided a default method to serialize itself to JSON.

```java
public interface JSONSerializable extends ConfigurationSerializable {

    default @NotNull String serializeToJSON() {
        return BukkitJSONSerializer.serializeToJSON(this);
    }

}
```

## Dependency Usage

<details>
<summary>Maven dependency</summary>

```xml

<project>
    <repositories>

        <repository>
            <!--Using central repository-->
            <id>maven</id>
            <name>Maven Central</name>
            <url>https://repo1.maven.org/maven2</url>
        </repository>

        <repository>
            <!--Using github repository-->
            <id>BukkitJSONSerializer</id>
            <url>https://raw.githubusercontent.com/CarmJos/BukkitJSONSerializer/repo/</url>
        </repository>

    </repositories>

    <dependencies>

        <dependency>
            <groupId>cc.carm.lib</groupId>
            <artifactId>bukkitjsonserializer</artifactId>
            <version>[LATEST RELEASE]</version>
            <scope>compile</scope>
        </dependency>

    </dependencies>

</project>
```

</details>

<details>
<summary>Gradle dependency</summary>

```groovy
repositories {

    mavenCentral() // Using central repository.

    // Using github repositories.
    maven { url 'https://raw.githubusercontent.com/CarmJos/BukkitJSONSerializer/repo/' }

}

dependencies {
    api "cc.carm.lib:bukkitjsonserializer:[LATEST RELEASE]"
}
```

</details>

## Open Source License.

The project using [GNU LESSER GENERAL PUBLIC LICENSE](https://www.gnu.org/licenses/lgpl-3.0.html) .