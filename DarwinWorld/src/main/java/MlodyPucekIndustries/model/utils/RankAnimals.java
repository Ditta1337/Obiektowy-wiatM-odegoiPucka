package MlodyPucekIndustries.model.utils;

import MlodyPucekIndustries.model.elements.Animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TODO : sorting to the second element Animals
public class RankAnimals {
    private boolean compareAnimals(Animal animal1, Animal animal2) {
        if (animal1.getEnergy() > animal2.getEnergy()) {
            return true;
        } else if (animal1.getEnergy() == animal2.getEnergy()) {
            if (animal1.getBirthTick() < animal2.getBirthTick()) {
                return true;
            } else if (animal1.getBirthTick() == animal2.getBirthTick()) {
                if (animal1.getChildren() > animal2.getChildren()) {
                    return true;
                } else if (animal1.getChildren() == animal2.getChildren()) {
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

    public Animal[] getDominantPair(List<Animal> animals) {
        Animal[] returnList = new Animal[2];
        for (int i = 0; i < 2; i++) {
            Animal maxAnimal = animals.get(i);
            for (int j = i; j < animals.size(); j++) {
                if (compareAnimals(maxAnimal, animals.get(j))) {
                    maxAnimal = animals.get(j);
                }
            }
            returnList[i] = maxAnimal;
        }
        return returnList;
    }
}

// czy robimy abstracta czy dziedziczymy po RegularMapie
// czy tick ma byc long???
