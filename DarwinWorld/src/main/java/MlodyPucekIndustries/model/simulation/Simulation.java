package MlodyPucekIndustries.model.simulation;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.maps.MapManager;
import MlodyPucekIndustries.model.maps.WorldMap;
import MlodyPucekIndustries.model.ui.MapController;
import MlodyPucekIndustries.model.utils.Statistics;
import MlodyPucekIndustries.model.utils.Vector2D;
import javafx.application.Platform;

public class Simulation implements Runnable {
    private final MapManager mapManager;
    private boolean isPaused = false;
    private boolean stoppedRunning = false;
    private final WorldMap map;
    private final MapController mapController;
    private final Statistics statistics;
    private Animal observedAnimal;

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
            while (!stoppedRunning) {
                mapManager.tickAnimalMove();
                mapManager.tickEnF();
                map.customMapFeature();
                mapManager.tickSpawnGrass();
                mapManager.increaseTick();
                statistics.updateStats();
                Platform.runLater(() -> mapController.drawMap(map));
                if (observedAnimal != null) {
                    Platform.runLater(() -> mapController.drawObservedAnimal(observedAnimal));
                }
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isPaused) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void pauseUnpause() {
        isPaused = !isPaused;
        if (!isPaused) {
            synchronized (this) {
                notify();
            }
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
