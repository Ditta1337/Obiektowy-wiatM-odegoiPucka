package MlodyPucekIndustries.model.simulation;

import MlodyPucekIndustries.model.maps.MapManager;
import MlodyPucekIndustries.model.maps.WorldMap;
import MlodyPucekIndustries.model.ui.MapController;
import MlodyPucekIndustries.model.utils.Statistics;
import javafx.application.Platform;

public class Simulation implements Runnable {
    private MapManager mapManager;
    private boolean isPaused = false;
    private boolean stoppedRunning = false;
    private WorldMap map;
    private MapController mapController;
    private Statistics statistics;

    public Simulation (MapManager mapManager, WorldMap map, MapController mapController) {
        this.mapManager = mapManager;
        this.map = map;
        this.mapController = mapController;
        this.statistics = new Statistics(map);
        map.initiate();
    }

    @Override
    public void run() {
        synchronized (this) {
            while (!isPaused && !stoppedRunning) {
                Platform.runLater(() -> mapController.drawMap(map));
                mapManager.tickAnimalMove();
                mapManager.tickEnF();
                map.modifyTideState();
                mapManager.tickSpawnGrass();
                mapManager.increaseTick();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void pauseUnpause() {
        isPaused = !isPaused;
        if (!isPaused) {
            new Thread(this::run).start();
        }
    }

    public void stopRunning() {
        stoppedRunning = true;
    }

    public Statistics getStatistics() {
        return statistics;
    }
}
