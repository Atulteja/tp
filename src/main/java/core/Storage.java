package core;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import java.util.logging.Logger;
import java.util.logging.Level;
import programme.ProgrammeList;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

/**
 * Represents the storage system for saving and loading tasks.
 * The <code>Storage</code> class handles reading from and writing to the file specified by the user.
 */
public class Storage {
    private final String path;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Storage(String path) {
        this.path = path;
    }

    public JsonObject loadProgrammeList() {
        JsonObject jsonObject = load();
        if(jsonObject == null || !jsonObject.has("programmeList")) {
            logger.log(Level.INFO, "No programme list found.");
            return new JsonObject();
        }
        logger.log(Level.INFO, "Programme list Loaded");
        return jsonObject.getAsJsonObject("programmeList");
    }

    public JsonObject loadHistory() {
        JsonObject jsonObject = load();
        if(jsonObject == null || !jsonObject.has("history")) {
            logger.log(Level.INFO, "No history found.");
            return new JsonObject();
        }
        logger.log(Level.INFO, "History Loaded");
        return jsonObject.getAsJsonObject("history");
    }

    private JsonObject load() {
        logger.info("Attempting to load data from file: " + path);
        try (FileReader reader = new FileReader(path)){
            JsonElement element = JsonParser.parseReader(reader);
            if(element == null || element.isJsonNull()) {
                logger.info("No data found");
                return new JsonObject();
            }
            logger.info("Data successfully loaded from file");
            return element.getAsJsonObject();
        } catch(IOException e){
            logger.log(Level.WARNING, "Failed to load data from file: " + path, e);
            throw new RuntimeException("Failed to load data due to: " + e.getMessage());
        }
    }

    public void save(ProgrammeList programmeList, History history) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        createDirIfNotExists();
        createFileIfNotExists();

        assert programmeList != null : "programmeList must not be null";
        assert history != null : "history must not be null";

        JsonObject jsonObject = createJSON(programmeList, history);
        logger.info("JsonObject containing programme list and history created.");

        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(jsonObject, writer);
            logger.info("Data successfully saved to file.");
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to save data to file: " + path, e);
            throw new IOException("Failed to save data due to: " + e.getMessage());
        }
    }

    private JsonObject createJSON(ProgrammeList programmeList, History history) {
        JsonObject jsonObject = new JsonObject();

        assert programmeList != null : "programmeList must not be null";
        assert history != null : "history must not be null";

        jsonObject.add("programmeList", programmeList.toJson());
        logger.info("Programme list converted to JsonObject.");
        jsonObject.add("history", history.toJson());
        logger.info("History converted to JsonObject.");
        return jsonObject;
    }

    private void createDirIfNotExists() throws IOException {
        File dir = new File(path).getParentFile();

        if (dir == null || dir.exists()){
            logger.log(Level.INFO, "Directory exists");
            return;
        }

        boolean isSuccess = dir.mkdirs();

        if (!isSuccess){
            logger.log(Level.WARNING, "Failed to create directory.");
            throw new IOException("Failed to create directory: " + dir.getAbsolutePath());
        }
        logger.log(Level.INFO, "Directory created");
    }

    private void createFileIfNotExists() throws IOException {
        File file = new File(path);
        if (file.exists()) {
            logger.log(Level.INFO, "File exists");
            return;
        }

        boolean isSuccess = file.createNewFile();

        if (!isSuccess) {
            logger.log(Level.WARNING, "Failed to create file.");
            throw new IOException("Failed to create file: " + file.getAbsolutePath());
        }
        logger.log(Level.INFO, "File created");
    }
}
