import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.utils.Vector2D;
import org.junit.jupiter.api.Test;
import MlodyPucekIndustries.model.utils.AnimalTree;
import static org.junit.jupiter.api.Assertions.*;

public class AnimalTreeTest {
    @Test
    public void addAnimalTest() {
        AnimalTree tree = new AnimalTree();
        int[] genome = {1, 2, 3, 4, 5, 6, 7, 8};
        Animal animal1 = new Animal(0, 10, genome, new Vector2D(1, 1));
        Animal animal2 = new Animal(0, 10, genome, new Vector2D(1, 1));

        tree.addAnimal(animal1, null, null);
        tree.addAnimal(animal2, null, null);


        assertEquals(tree.getNodes().size(), 2);
        assertEquals(tree.getRoots().size(), 2);
        }
}
