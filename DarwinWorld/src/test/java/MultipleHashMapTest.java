import MlodyPucekIndustries.model.utils.MultipleHashMap;
import MlodyPucekIndustries.model.utils.Vector2D;
import org.junit.jupiter.api.Test;
import MlodyPucekIndustries.model.elements.Animal;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class MultipleHashMapTest {
    int[] exampleGenome1 = {1,1,1};
    int[] exampleGenome2 = {2,2,2};

    Vector2D examplePosition1 = new Vector2D(1,1);

    @Test
    public void testSimplePutAndGet() {
        MultipleHashMap animalsMap = new MultipleHashMap(3,3);
        Animal animal = new Animal(1,1,exampleGenome1,examplePosition1);
        animalsMap.put(animal);
        assertEquals(animal,(animalsMap.get(examplePosition1).get(0)));
    }

    @Test
    public void testMoreThanOnePutAndGet() {
        MultipleHashMap animalsMap = new MultipleHashMap(3,3);
        Animal animal1 = new Animal(1,1,exampleGenome1,examplePosition1);
        Animal animal2 = new Animal(1,1,exampleGenome2, examplePosition1);
        animalsMap.put(animal1);
        animalsMap.put(animal2);
        ArrayList<Animal> arrayList = animalsMap.get(examplePosition1);
        assertEquals(arrayList.size(), 2);
        assertEquals(animal1,arrayList.get(0));
        assertEquals(animal2,arrayList.get(1));
    }

    @Test
    public void testRemove() {
        MultipleHashMap animalsMap = new MultipleHashMap(2,2);
        Animal animal = new Animal(1,1,exampleGenome1,examplePosition1);
        animalsMap.put(animal);
        assertEquals(animalsMap.get(examplePosition1).size(), 1);
        animalsMap.remove(animal,examplePosition1);
        assertEquals(animalsMap.get(examplePosition1).size(), 0);
    }

    @Test
    public void testConstainsKey() {
        MultipleHashMap animalsMap = new MultipleHashMap(2,2);
        Animal animal = new Animal(1,1,exampleGenome1,examplePosition1);
        animalsMap.put(animal);
        assertFalse(animalsMap.containsKey(new Vector2D(0,0)));
        assertFalse(animalsMap.containsKey(new Vector2D(1,0)));
        assertFalse(animalsMap.containsKey(new Vector2D(0,1)));
        assertTrue(animalsMap.containsKey(new Vector2D(1,1)));
    }

    @Test
    public void testGetValues() {
//         have you heard the tragedy of Darth Plagueis The Wise?
//         I thought not. It's not a story the Jedi would tell you.
//
//         It's a Sith legend. Darth Plagueis was a Dark Lord of the Sith,
//
//         so powerful and so wise he could use the Force to influence the midichlorians to create life...
//         He had such a knowledge of the dark side that he could even keep the ones he cared about from dying.
//         The dark side of the Force is a pathway to many abilities some consider to be unnatural.
//         He became so powerful... the only thing he was afraid of was losing his power, which eventually, of course, he did.
//         Unfortunately, he taught his apprentice everything he knew, then his apprentice killed him in his sleep.
//         Ironic. He could save others from death, but not himself.
//         Is it possible to learn this power? Not from a Jedi.
//         I have brought peace, freedom, justice, and security to my new Empire.
//         Your new empire?
//
//         Don't try it.
//         You underestimate my power!
//         Don't try it.
//         AAAAAAAAAAAAAAAAAAAAA  AHAHAHAHAHAHAH WTF
//         AAAAAAAAAAAAAAAAAAAAA  lmao co ten copilot pisze
//         AAAAAAAAAAAAAAAAAAAAA
//         AAAAAAAAAAAAAAAAAAAAA
//         AAAAAAAAAAAAAAAAAAAAA
//         AAAAAAAAAAAAAAAAAAAAA
//         AAAAAAAAAAAAAAAAAAAAA
//
//
        MultipleHashMap animalsMap = new MultipleHashMap(2, 2);
        Vector2D[] positions = {new Vector2D(0, 0),
                                new Vector2D(0, 0),
                                new Vector2D(1, 0),
                                new Vector2D(1, 0),
                                new Vector2D(0, 1),
                                new Vector2D(0, 1),
                                new Vector2D(1, 1),
                                new Vector2D(1, 1)};

        ArrayList<Animal> animals = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            Animal animal = new Animal(i, 1, exampleGenome1, positions[i]);
            animalsMap.put(animal);
            animals.add(animal);
        }
        ArrayList<Animal> values = animalsMap.values();
        assertEquals(values.size(), 8);
        for (int i = 0; i < 8; i++) {
            assertTrue(values.contains(animals.get(i)));
        }
    }

    @Test
    public void testContainsKey() {
        MultipleHashMap animalsMap = new MultipleHashMap(2, 2);
        Vector2D[] positions = {new Vector2D(0, 0),
                new Vector2D(0, 0),
                new Vector2D(1, 0),
                new Vector2D(1, 0),
                new Vector2D(0, 1),
                new Vector2D(0, 1),
                new Vector2D(1, 1),
                new Vector2D(1, 1)};

        for (int i = 0; i < 8; i++) {
            Animal animal = new Animal(i, 1, exampleGenome1, positions[i]);
            animalsMap.put(animal);
        }

        for (int i = 0; i < 8; i++) {
            assertTrue(animalsMap.containsKey(positions[i]));
        }

    }
}
