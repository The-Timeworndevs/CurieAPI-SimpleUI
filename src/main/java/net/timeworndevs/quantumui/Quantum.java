package net.timeworndevs.quantumui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.timeworndevs.quantumui.client.NewRadiationHudOverlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Quantum implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MOD_ID = "quantumui";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier locate(String name) {
        return new Identifier(MOD_ID, name);
    }



    /*public Identifier getFabricId() {
        return new Identifier(Quantum.MOD_ID, "radiation_data");
    }
    public void reload(ResourceManager manager) {
        Map<Identifier, Resource> data = manager.findResources("radiation_data", path -> path.toString().endsWith(".json"));

        BiConsumer<Identifier, Resource> read = (i, resource) -> {
            Quantum.LOGGER.info(i + resource.toString());
        };
        data.forEach(read);
    }*/
    public static HashMap<String, JsonObject> new_radiation_types = new HashMap<>();

    @Override
    public void onInitialize() {
        JsonParser parser = new JsonParser();
        new_radiation_types.put("alpha", (JsonObject) parser.parse("{\"name\": \"alpha\",\"color\": \"0.086f 0.2f 0.32f 1f\"}"));
        new_radiation_types.put("beta", (JsonObject) parser.parse("{\"name\": \"beta\",\"color\": \"0.22f 0.32f 0.21f 1f\"}"));
        new_radiation_types.put("gamma", (JsonObject) parser.parse("{\"name\": \"gamma\",\"color\": \"0.32f 0.15f 0.15f 1f\"}"));
        Set<String> files;
        try {
            Files.createDirectories(Paths.get(FabricLoader.getInstance().getConfigDir() + "/curie"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (Stream<Path> stream = Files.list(Paths.get(FabricLoader.getInstance().getConfigDir() + "/curie"))) {
            files = stream.filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LOGGER.error(String.valueOf(files));

        for (int i=0; i<files.size(); i++) {
            File file = new File(FabricLoader.getInstance().getConfigDir() + "/curie/" + files.toArray()[i]);
            if (file.exists()) {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(file));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                JsonObject json = JsonParser.parseReader(br).getAsJsonObject();

                if (json.has("radiation_types")) {
                    for (JsonElement ele: json.get("radiation_types").getAsJsonArray()) {
                        new_radiation_types.put(ele.getAsJsonObject().get("name").getAsString(), ele.getAsJsonObject());
                    }
                }
            }
        }


        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        LOGGER.info("Computing wave-functions...");
        NewRadiationHudOverlay.init();
        LOGGER.info(String.valueOf(new_radiation_types));
        //ModRecipes.registerRecipes();
        LOGGER.info("Testing radiation...");

        LOGGER.info("Wormhole established!");
    }
}