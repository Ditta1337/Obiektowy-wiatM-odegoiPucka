package MlodyPucekIndustries.model.observers;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.maps.WorldMap;

import java.util.Arrays;

public class AnimalStatsDisplay {

    public static void animalChanged(WorldMap map, Animal animal) {
        System.out.println("--------------------------------");
        System.out.println(Arrays.toString(animal.getGenome()));
        System.out.println("Active genome: " + animal.getActiveGenome());
        System.out.println("Energy: " + animal.getEnergy());
        System.out.println("Children: " + animal.getChildren());
        System.out.println("Eaten grass: " + animal.getEatenGrass());
        System.out.println("Descendants: " + map.getAnimalTree().getDescendantCount(animal));
        if (animal.getEnergy() > 0) {
            System.out.println("Days alive: " + animal.getAge());
        }
        else {
            System.out.println("Died at day: " + animal.getAge() + animal.getBirthTick());
        }
        System.out.println("--------------------------------");

    }
}
