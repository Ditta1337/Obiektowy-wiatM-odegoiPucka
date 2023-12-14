package MlodyPucekIndustries.model.maps;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.elements.Grass;
import MlodyPucekIndustries.model.utils.RandomPositionGenerator;
import MlodyPucekIndustries.model.utils.Vector2d;

import java.util.HashMap;

public class RegularMap implements WorldMap{
    private final Vector2d upperRight;
    private final Vector2d jungleLowerLeft;
    private final Vector2d jungleUpperRight;
    private HashMap<Vector2d, Animal> animals = new HashMap<>();
    private HashMap<Vector2d, Grass> grasses = new HashMap<>();
    private final int defaultGrass;

    public RegularMap(int height, int width, int jungleHeight, int jungleWidth, int defaultGrass) throws IllegalArgumentException {
        this.jungleLowerLeft = new Vector2d((width - jungleWidth) / 2, (height - jungleHeight) / 2);
        this.jungleUpperRight = new Vector2d((width + jungleWidth) / 2, (height + jungleHeight) / 2);
        this.upperRight = new Vector2d(width, height);
        this.defaultGrass = defaultGrass;
    }

    @Override
    public void placeAnimal(Animal animal) {
        animals.put(animal.getPosition(), animal);
    }

    public void placeDefaultGrass() {
        RandomPositionGenerator generator = new RandomPositionGenerator(upperRight.getX(),
                upperRight.getY(),
                defaultGrass,
                this);
        for (Vector2d position : generator) {
            grasses.put(position, new Grass(position));
        }
    }

}
