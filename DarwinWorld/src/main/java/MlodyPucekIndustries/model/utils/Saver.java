package MlodyPucekIndustries.model.utils;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class Saver {
    private final String saveDestination = "saves/MenuSaves";

    public void saveMenu(Save save) {
        Gson gson = new Gson();
        String json = gson.toJson(save);

        File directory = new File(saveDestination);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileWriter writer = new FileWriter(saveDestination + "/save" + getNumElementsInSaveFolder() + ".json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Save loadMenu(int saveNumber) {
        Gson gson = new Gson();
        String json = "";
        try {
            json = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(saveDestination + "/save" + saveNumber + ".json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gson.fromJson(json, Save.class);
    }

    public int getNumElementsInSaveFolder() {
        File folder = new File(saveDestination);
        if (folder.exists() && folder.isDirectory()) {
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles != null) {
                return listOfFiles.length;
            }
        }
        return 0;
    }

    public void saveSimulationStates(Statistics statistics) {
        // save csv with every field in statistics

    }
}
