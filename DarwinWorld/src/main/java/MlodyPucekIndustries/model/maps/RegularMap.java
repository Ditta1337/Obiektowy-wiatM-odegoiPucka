package MlodyPucekIndustries.model.maps;

import MlodyPucekIndustries.World;
import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.elements.Grass;
import MlodyPucekIndustries.model.elements.WorldElement;
import MlodyPucekIndustries.model.utils.MapDirection;
import MlodyPucekIndustries.model.utils.RandomPositionGenerator;
import MlodyPucekIndustries.model.utils.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegularMap implements WorldMap{
    private final Vector2d upperRight;
    private final Vector2d jungleLowerLeft;
    private final Vector2d jungleUpperRight;
    private HashMap<Vector2d, Animal> animals = new HashMap<>();
    private HashMap<Vector2d, Grass> grasses = new HashMap<>();
    private final int defaultAnimals;
    private final int defaultGrass;
    private final int defaultEnergy;
    private long tick = 0;
    private final int genomeLength;

    public RegularMap(int height,
                      int width,
                      int jungleHeight,
                      int jungleWidth,
                      int defaultGrass,
                      int defaultAnimals,
                      int defaultEnergy,
                      int genomeLength) {
        this.jungleLowerLeft = new Vector2d((width - jungleWidth) / 2, (height - jungleHeight) / 2);
        this.jungleUpperRight = new Vector2d((width + jungleWidth) / 2, (height + jungleHeight) / 2);
        this.upperRight = new Vector2d(width, height);
        this.defaultGrass = defaultGrass;
        this.defaultAnimals = defaultAnimals;
        this.defaultEnergy = defaultEnergy;
        this.genomeLength = genomeLength;
        placeDefaultGrasses();
        placeDefaultAnimals();

    }

    @Override
    public void placeAnimal(Animal animal) {
        animals.put(animal.getPosition(), animal);
    }

    // IF this ends up in abstract class, change from public to private
    public void placeDefaultGrasses() {
        RandomPositionGenerator generator = new RandomPositionGenerator(upperRight.getX() - 1,
                upperRight.getY() - 1,
                defaultGrass,
                this);
        for (Vector2d position : generator) {
            grasses.put(position, new Grass(position));
        }
    }

    public void placeDefaultAnimals() {
        RandomPositionGenerator generator = new RandomPositionGenerator(upperRight.getX() - 1,
                upperRight.getY() - 1,
                defaultAnimals,
                this);
        for (Vector2d position : generator) {
            placeAnimal(new Animal(0, defaultEnergy, generateGenome(genomeLength), position));
        }
    }


    public void move(Animal animal, int tick) {
        Vector2d oldPosition = animal.getPosition();
        animal.move(tick, this);
        Vector2d newPosition = animal.getPosition();
        if (oldPosition.equals(newPosition)) {
            return;
        }
        animals.remove(oldPosition);
        animals.put(newPosition, animal);
    }

    private int[] generateGenome(int genomeLength) {
        int[] genome = new int[genomeLength];
        for (int i = 0; i < genomeLength; i++) {
            genome[i] = (int) (Math.random() * 8);
        }
        return genome;
    }


    public List<WorldElement> getElements() {
        List<WorldElement> elements = new ArrayList<>(animals.values());
        elements.addAll(grasses.values());
        return elements;
    }

    public boolean canMoveTo(Vector2d position) {
        return position.getY() >= 0 && position.getY() < upperRight.getY();
    }

    // TODO: counting form 0
    public Vector2d validPosition(Vector2d position) {
        if (position.getX() < 0) {
            position = new Vector2d(upperRight.getX() -1, position.getY());
        }
        else if (position.getX() >= upperRight.getX() - 1) {
            position = new Vector2d(0, position.getY());
        }

        return position;
    }

    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position) || grasses.containsKey(position);
    }

    public WorldElement objectAt(Vector2d position) {
        if (animals.containsKey(position)) {
            return animals.get(position);
        } else return grasses.getOrDefault(position, null);
    }

}
