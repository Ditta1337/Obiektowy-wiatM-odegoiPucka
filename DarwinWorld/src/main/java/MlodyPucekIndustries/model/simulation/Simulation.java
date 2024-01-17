package MlodyPucekIndustries.model.simulation;

import MlodyPucekIndustries.World;
import MlodyPucekIndustries.model.maps.MapManager;
import MlodyPucekIndustries.model.maps.WorldMap;
import MlodyPucekIndustries.model.observers.AnimalStatsDisplay;
import MlodyPucekIndustries.model.ui.MapController;
import MlodyPucekIndustries.model.utils.MapVisualizer;
import MlodyPucekIndustries.model.utils.Vector2D;
import javafx.application.Platform;

public class Simulation implements Runnable {
    private MapManager mapManager;
    private boolean isPaused = false;
    private boolean stoppedRunning = false;
    private WorldMap map;
    private MapController mapController;

    public Simulation (MapManager mapManager, WorldMap map, MapController mapController) {
        this.mapManager = mapManager;
        this.map = map;
        this.mapController = mapController;
        map.initiate();
    }

    @Override
    public void run() {
        synchronized (this) {
            MapVisualizer visualizer = new MapVisualizer(map);
            while (!isPaused && !stoppedRunning) {
                Platform.runLater(() -> mapController.drawMap(map));
                //System.out.println(visualizer.draw(new Vector2D(0, 0), new Vector2D(map.getWidth() - 1, map.getHeight() - 1)));
                mapManager.tickAnimalMove();
                mapManager.tickEnF();
                map.modifyTideState();
                mapManager.tickSpawnGrass();
                // TODO: checkIfRootsAreAlive po odpauzowaniu a nie po kazdym dniu
                mapManager.increaseTick();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void pauseUnpause() {
        isPaused = !isPaused;
        if (!isPaused) {
            map.getAnimalTree().checkIfRootsAreAlive();
            new Thread(this::run).start();
        }
    }

    public void stopRunning() {
        stoppedRunning = true;
    }


}
