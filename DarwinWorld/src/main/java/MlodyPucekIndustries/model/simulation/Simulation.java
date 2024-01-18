package MlodyPucekIndustries.model.simulation;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.maps.MapManager;
import MlodyPucekIndustries.model.maps.WorldMap;
import MlodyPucekIndustries.model.ui.MapController;
import MlodyPucekIndustries.model.utils.Statistics;
import MlodyPucekIndustries.model.utils.Vector2D;
import javafx.application.Platform;

public class Simulation implements Runnable {
    private MapManager mapManager;
    private boolean isPaused = false;
    private boolean stoppedRunning = false;
    private WorldMap map;
    private MapController mapController;
    private Statistics statistics;
    private Animal observedAnimal = null;

    public Simulation (MapManager mapManager, WorldMap map, MapController mapController) {
        this.mapManager = mapManager;
        this.map = map;
        this.mapController = mapController;
        this.statistics = new Statistics(map);
        map.initiate();
        mapController.initiate(map);
    }

    public WorldMap getMap() {
        return map;
    }

    @Override
    public void run() {
        synchronized (this) {
            while (!isPaused && !stoppedRunning) {
                Platform.runLater(() -> mapController.drawMap(map));
                if (observedAnimal != null) {
                    Platform.runLater(() -> mapController.drawObservedAnimal(observedAnimal));
                }
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
            mapController.disablePauseButtons();
        } else {
            mapController.enablePauseButtons();
        }
    }

    public void stopRunning() {
        stoppedRunning = true;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setObservedAnimal(Vector2D position) {
        if (isPaused) {
            if (map.objectAt(position) instanceof Animal) {
                observedAnimal = map.getAnimals().get(position).get(0);
                mapController.drawObservedAnimal(observedAnimal);
            }
        }
    }
}
