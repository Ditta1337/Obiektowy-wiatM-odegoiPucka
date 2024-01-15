package MlodyPucekIndustries;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.maps.*;
import MlodyPucekIndustries.model.utils.AnimalNode;

import java.util.ArrayList;

public class World {
    public static void main(String[] args) {
        WaterMap map = new WaterMap(
                5,
                5,
                5,
                5,
                5,
                10,
                5,
                8,
                0.6,
                0.75);
//        RegularMap map = new RegularMap(
//                5,
//                5,
//                2,
//                2,
//                0,
//                0,
//                10,
//                8);
        MapManager mapManager = new MapManager(
                5,
                15,
                true,
                10,
                0,
                5,
                map);
        mapManager.start();
        map.getAnimalTree().printTree();
        ArrayList<AnimalNode> roots  = map.getAnimalTree().getRoots();
        for (Animal animal: map.getAnimalTree().getNodes().keySet()) {
            System.out.println("Animal: " + animal.hashCode() + " descendants: " + map.getAnimalTree().getDescendantCount(animal) + " energy: " + animal.getEnergy());
        }
        for (AnimalNode root : roots) {
            System.out.println("Root: " + root.getAnimal().hashCode() + " descendants: " + map.getAnimalTree().getDescendantCount(root.getAnimal()) + " energy: " + root.getEnergy());
        }
    }
}

// TODO: średnia dlugosc zycia dla martwych zwierzat
