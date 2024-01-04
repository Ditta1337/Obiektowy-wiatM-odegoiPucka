package MlodyPucekIndustries;

import MlodyPucekIndustries.model.maps.*;

public class World {
    public static void main(String[] args) {
        WaterMap waterMap = new WaterMap(
                30,
                30,
                5,
                5,
                50,
                5,
                10,
                8,
                0.6,
                0.75);
        MapManager mapManager = new MapManager(4, 50, true, waterMap);
        mapManager.start();
    }
}
