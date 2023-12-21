package MlodyPucekIndustries.model.maps;

import MlodyPucekIndustries.World;
import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.elements.Grass;
import MlodyPucekIndustries.model.elements.WorldElement;
import MlodyPucekIndustries.model.utils.MapDirection;
import MlodyPucekIndustries.model.utils.MultipleHashMap;
import MlodyPucekIndustries.model.utils.RandomPositionGenerator;
import MlodyPucekIndustries.model.utils.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegularMap implements WorldMap{
    private final Vector2d upperRight;
    private final Vector2d jungleLowerLeft;
    private final Vector2d jungleUpperRight;
    private final int defaultAnimals;
    private final int defaultGrass;
    private final int animalEnergy;
    private final int genomeLength;
    private MultipleHashMap animals;
    private HashMap<Vector2d, Grass> grasses = new HashMap<>();


    public RegularMap(int height,
                      int width,
                      int jungleHeight,
                      int jungleWidth,
                      int defaultAnimals,
                      int defaultGrass,
                      int animalEnergy,
                      int genomeLength) {
        this.jungleLowerLeft = new Vector2d((width - jungleWidth) / 2, (height - jungleHeight) / 2);
        this.jungleUpperRight = new Vector2d((width + jungleWidth) / 2, (height + jungleHeight) / 2);
        this.upperRight = new Vector2d(width, height);
        this.animals = new MultipleHashMap(height,width);
        this.defaultAnimals = defaultAnimals;
        this.defaultGrass = defaultGrass;
        this.animalEnergy = animalEnergy;
        this.genomeLength = genomeLength;

        placeDefaultGrasses();
        placeDefaultAnimals();
    }

    public Vector2d getJungleLowerLeft() {
        return jungleLowerLeft;
    }

    public Vector2d getJungleUpperRight() {
        return jungleUpperRight;
    }

    public Vector2d getUpperRight() {
        return upperRight;
    }

    @Override
    public void placeAnimal(Animal animal) {
        animals.put(animal);
    }

    @Override
    public void placeGrass(Grass grass) {
        grasses.put(grass.getPosition(), grass);
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
            placeAnimal(new Animal(0, animalEnergy, generateGenome(genomeLength), position));
        }
    }

    public MultipleHashMap getAnimals() {
        return animals;
    }

    public HashMap<Vector2d, Grass> getGrasses() {
        return grasses;
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
        return !animals.get(position).isEmpty() || grasses.containsKey(position);
    }

    public WorldElement objectAt(Vector2d position) {
        if (!animals.get(position).isEmpty()) {
            return animals.get(position).get(0);
        } else return grasses.getOrDefault(position, null);
    }

}

// map manager z mapy


