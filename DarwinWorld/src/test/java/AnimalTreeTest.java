import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.utils.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTreeTest {
    @Test
    public void testAddAnimalTest() {
        AnimalTree tree = new AnimalTree();
        int[] genome = {1, 2};
        Animal animal1 = new Animal(0, 10, genome, new Vector2D(1, 1));
        Animal animal2 = new Animal(0, 10, genome, new Vector2D(1, 1));
        Animal child12 = new Animal(0, 10, genome, new Vector2D(1, 1));

        tree.addAnimal(animal1, null, null);
        tree.addAnimal(animal2, null, null);

        tree.addAnimal(child12, animal1, animal2);

        HashMap<Animal, AnimalNode> nodes = tree.getNodes();

        assertEquals(nodes.get(animal1).getChildren().size(), 1);
        assertEquals(nodes.get(animal2).getChildren().size(), 1);
        assertEquals(nodes.size(), 3);
        assertEquals(tree.getRoots().size(), 2);
    }

    @Test
    public void testRootChangeAtAnimalDeath(){
        AnimalTree tree = new AnimalTree();
        int[] genome = {1, 2};
        Animal animal1 = new Animal(0, 10, genome, new Vector2D(1, 1));
        Animal animal2 = new Animal(0, 10, genome, new Vector2D(1, 1));
        Animal child12 = new Animal(0, 10, genome, new Vector2D(1, 1));

        tree.addAnimal(animal1, null, null);
        tree.addAnimal(animal2, null, null);

        tree.addAnimal(child12, animal1, animal2);

        animal1.modifyEnergy(-10);
        tree.checkIfRootsAreAlive();

        assertEquals(tree.getRoots().get(0).getAnimal(), animal2);

        animal2.modifyEnergy(-10);
        tree.checkIfRootsAreAlive();
        assertEquals(tree.getRoots().get(0).getAnimal(), child12);
    }

    @Test
    public void testGetDescendantCount(){
        AnimalTree tree = new AnimalTree();
        int[] genome = {1, 2};
        Animal animal1r = new Animal(0, 10, genome, new Vector2D(1, 1));
        Animal animal2r = new Animal(0, 10, genome, new Vector2D(1, 1));
        Animal animal3 = new Animal(0, 10, genome, new Vector2D(1, 1));
        Animal animal4 = new Animal(0, 10, genome, new Vector2D(1, 1));
        Animal animal5 = new Animal(0, 10, genome, new Vector2D(1, 1));

        tree.addAnimal(animal1r, null, null);
        tree.addAnimal(animal2r, null, null);
        tree.addAnimal(animal3, animal1r, animal2r);
        tree.addAnimal(animal4, animal1r, animal2r);
        tree.addAnimal(animal5, animal3, animal4);

        assertEquals(tree.getDescendantCount(animal1r), 3);
        assertEquals(tree.getDescendantCount(animal2r), 3);
        assertEquals(tree.getDescendantCount(animal3), 1);
        assertEquals(tree.getDescendantCount(animal4), 1);
        assertEquals(tree.getDescendantCount(animal5), 0);
    }
}
