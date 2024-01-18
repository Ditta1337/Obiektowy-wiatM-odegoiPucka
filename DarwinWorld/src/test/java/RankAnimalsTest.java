import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.utils.RankAnimals;
import MlodyPucekIndustries.model.utils.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RankAnimalsTest {

    @Test
    void testGetDominantPair() {
        ArrayList<Animal> animals = new ArrayList<>();
        int[] genome = {1, 2};
        Animal animal1 = new Animal(0, 1, genome, new Vector2D(1, 1));
        Animal animal2 = new Animal(0, 2, genome, new Vector2D(1, 1));
        Animal animal3 = new Animal(0, 3, genome, new Vector2D(1, 1));
        Animal animal4 = new Animal(0, 4, genome, new Vector2D(1, 1));

        animals.add(animal1);
        animals.add(animal2);
        animals.add(animal3);
        animals.add(animal4);

        Animal[] dominantPair = RankAnimals.getDominantPair(animals);

        assertEquals(dominantPair[0], animal4);
        assertEquals(dominantPair[1], animal3);
    }
}