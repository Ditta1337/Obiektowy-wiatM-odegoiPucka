package MlodyPucekIndustries.model.maps;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.elements.Grass;
import MlodyPucekIndustries.model.elements.WorldElement;
import MlodyPucekIndustries.model.utils.AnimalTree;
import MlodyPucekIndustries.model.utils.MultipleHashMap;
import MlodyPucekIndustries.model.utils.RandomPositionGenerator;
import MlodyPucekIndustries.model.utils.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegularMap implements WorldMap {
    protected final int height;
    protected final int width;
    private final Vector2D jungleLowerLeft;
    private final Vector2D jungleUpperRight;
    private final int defaultAnimals;
    private final int defaultGrass;
    private final int animalEnergy;
    private final int genomeLength;
    private final AnimalTree animalTree = new AnimalTree();
    protected MultipleHashMap animals;
    protected HashMap<Vector2D, Grass> grasses = new HashMap<>();


    public RegularMap(int height,
                      int width,
                      int jungleHeight,
                      int jungleWidth,
                      int defaultAnimals,
                      int defaultGrass,
                      int animalEnergy,
                      int genomeLength) {
        this.height = height;
        this.width = width;
        this.jungleLowerLeft = new Vector2D((width - jungleWidth) / 2, (height - jungleHeight) / 2);
        this.jungleUpperRight = new Vector2D((width + jungleWidth) / 2, (height + jungleHeight) / 2);
        this.animals = new MultipleHashMap(height,width);
        this.defaultAnimals = defaultAnimals;
        this.defaultGrass = defaultGrass;
        this.animalEnergy = animalEnergy;
        this.genomeLength = genomeLength;
    }

    public void initiate() {
        placeDefaultAnimals();
        placeDefaultGrasses();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Vector2D getJungleLowerLeft() {
        return jungleLowerLeft;
    }

    public Vector2D getJungleUpperRight() {
        return jungleUpperRight;
    }

    public AnimalTree getAnimalTree(){
        return animalTree;
    }

    public void placeAnimal(Animal animal) {
        animals.put(animal);
    }

    public void placeGrass(Grass grass) {
        grasses.put(grass.getPosition(), grass);
    }


    private void placeDefaultGrasses() {
        RandomPositionGenerator generator = new RandomPositionGenerator(width - 1,
                height - 1,
                defaultGrass,
                this);
        for (Vector2D position : generator) {
            grasses.put(position, new Grass(position));
        }
    }

    private void placeDefaultAnimals() {
        RandomPositionGenerator generator = new RandomPositionGenerator(width - 1,
                height - 1,
                defaultAnimals,
                this);
        for (Vector2D position : generator) {
            Animal animal = new Animal(0, animalEnergy, generateGenome(genomeLength), position);
            placeAnimal(animal);
            animalTree.addAnimal(animal, null, null);
        }
    }

    public MultipleHashMap getAnimals() {
        return animals;
    }

    public HashMap<Vector2D, Grass> getGrasses() {
        return grasses;
    }

    private int[] generateGenome(int genomeLength) {
        int[] genome = new int[genomeLength];
        for (int i = 0; i < genomeLength; i++) {
            genome[i] = (int) (Math.random() * 8);
        }
        return genome;
    }


    public ArrayList<WorldElement> getElements() {
        ArrayList<WorldElement> elements = new ArrayList<>(animals.values());
        elements.addAll(grasses.values());
        return elements;
    }

    public boolean canMoveTo(Vector2D position) {
        return position.getY() >= 0 && position.getY() < height;
    }

    //counting form 0
    public Vector2D validPosition(Vector2D position) {
        if (position.getX() < 0) {
            position = new Vector2D(width -1, position.getY());
        }
        else if (position.getX() > width - 1) {
            position = new Vector2D(0, position.getY());
        }

        return position;
    }

    public boolean isOccupied(Vector2D position) {
        return !animals.get(position).isEmpty() || grasses.containsKey(position);
    }

    public WorldElement objectAt(Vector2D position) {
        if (!animals.get(position).isEmpty()) {
            return animals.get(position).get(0);
        } else return grasses.getOrDefault(position, null);
    }

    // TODO: zapytać czy moze zostac puste
    public void modifyTideState() {
        // has to be left empty in regular map
    }

}

// map manager z mapy


