package MlodyPucekIndustries.model.utils;

import MlodyPucekIndustries.model.elements.Animal;

import java.util.List;
import java.util.Random;

public class RankAnimals {
    private static boolean compareAnimals(Animal animal1, Animal animal2) {
        if (animal1.getEnergy() > animal2.getEnergy()) {
            return true;
        } else if (animal1.getEnergy() == animal2.getEnergy()) {
            if (animal1.getBirthTick() < animal2.getBirthTick()) {
                return true;
            } else if (animal1.getBirthTick() == animal2.getBirthTick()) {
                if (animal1.getChildrenNum() > animal2.getChildrenNum()) {
                    return true;
                } else if (animal1.getChildrenNum() == animal2.getChildrenNum()) {
                    Random random = new Random();
                    return random.nextBoolean();
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static Animal[] getDominantPair(List<Animal> animals) {
        Animal[] dominantPair = new Animal[2];
        dominantPair[0] = animals.get(0);
        dominantPair[1] = animals.get(1);
        for (int i = 2; i < animals.size(); i++) {
            if (compareAnimals(animals.get(i), dominantPair[0])) {
                dominantPair[1] = dominantPair[0];
                dominantPair[0] = animals.get(i);
            } else if (compareAnimals(animals.get(i), dominantPair[1])) {
                dominantPair[1] = animals.get(i);
            }
        }
        return dominantPair;
    }
}