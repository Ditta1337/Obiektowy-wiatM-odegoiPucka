package MlodyPucekIndustries.model.utils;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class Saver {
    private final String saveDestinationMenu = "saves/MenuSaves";
    private final String saveDestinationSimulations = "saves/SimulationSaves";
    private long simulationNumber = 0;

    public void saveMenu(Save save) {
        Gson gson = new Gson();
        String json = gson.toJson(save);

        File directory = new File(saveDestinationMenu);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileWriter writer = new FileWriter(saveDestinationMenu + "/save" + getNumElementsInSaveFolder() + ".json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Save loadMenu(int saveNumber) {
        Gson gson = new Gson();
        String json = "";
        try {
            json = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(saveDestinationMenu + "/save" + saveNumber + ".json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gson.fromJson(json, Save.class);
    }

    public int getNumElementsInSaveFolder() {
        File folder = new File(saveDestinationMenu);
        if (folder.exists() && folder.isDirectory()) {
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles != null) {
                return listOfFiles.length;
            }
        }
        return 0;
    }

    public void saveSimulationStatesTick(Statistics statistics, long tick) {
        File directory = new File(saveDestinationSimulations);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        if (tick == 0) {
            simulationNumber = directory.list().length + 1;
        }

        try (FileWriter writer = new FileWriter(saveDestinationSimulations + "/simulation" + simulationNumber + ".csv", true)) {
            if (tick == 0) {
                writer.write("Tick,AnimalsCount,GrassesCount,EmptyPlaces,AverageEnergy,AverageChildrenCount,AverageLifeSpan,DominantGenome\n");
            }
            writer.write(tick + ",");
            writer.write(statistics.getAnimalsCount() + ",");
            writer.write(statistics.getGrassesCount() + ",");
            writer.write(statistics.getEmptyPlaces() + ",");
            writer.write(statistics.getAverageEnergy() + ",");
            writer.write(statistics.getAverageChildrenCount() + ",");
            writer.write(statistics.getAverageLifeSpan() + ",");
            int[] dominantGenome = statistics.getDominantGenome();
            for (int i = 0; i < dominantGenome.length; i++) {
                writer.write(dominantGenome[i] + (i < dominantGenome.length - 1 ? "," : "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
