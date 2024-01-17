package MlodyPucekIndustries.model.simulation;

import MlodyPucekIndustries.model.maps.MapManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class SimulationEngine {
    public static void runAsyncWithPool(Simulation simulation) {
        // TODO: zerknac na to po czwartku
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(simulation);
    }
}

