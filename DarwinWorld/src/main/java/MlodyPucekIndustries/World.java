package MlodyPucekIndustries;

import MlodyPucekIndustries.model.maps.RegularMap;
import MlodyPucekIndustries.model.maps.WaterMap;
import MlodyPucekIndustries.model.maps.WorldMap;
import MlodyPucekIndustries.model.utils.Save;
import MlodyPucekIndustries.model.utils.Saver;

public class World {
    public static void main(String[] args) {
        //WorldMap map = new WaterMap(20, 20, 10, 10, 10, 100, 32, 1, 0.6, 0.8);
        Save save = new Save(20, 20, 10, 10, 10, 100, 32, 1, 3, 50, 10, 10, 100, 10, true, "WaterMap");
        Saver saver = new Saver();
        saver.saveMenu(save);

    }
}

// TODO: średnia dlugosc zycia dla martwych zwierzat
