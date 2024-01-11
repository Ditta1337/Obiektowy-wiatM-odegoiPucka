package MlodyPucekIndustries.model.maps;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.elements.Grass;
import MlodyPucekIndustries.model.utils.MapVisualizer;
import MlodyPucekIndustries.model.utils.MultipleHashMap;
import MlodyPucekIndustries.model.utils.Vector2D;

import java.util.HashMap;
import java.util.List;

import static MlodyPucekIndustries.model.utils.RankAnimals.getDominantPair;

public class MapManager {
    private final MultipleHashMap animals;
    private final HashMap<Vector2D, Grass> grasses;
    private final Vector2D[] positions;
    private final Vector2D[] junglePositions;
    private final Vector2D[] steppePositions;
    private final WorldMap map;
    private final int fedThreshold;
    private final int grassEnergy;
    private final boolean mutationVariation;
    private long tick = 0;

    public MapManager(int defaultGrassEnergy,
                      int fedThreshold,
                      boolean mutationVariation,
                      WorldMap map) {
        this.grassEnergy = defaultGrassEnergy;
        this.map = map;
        this.animals = map.getAnimals();
        this.grasses = map.getGrasses();
        this.fedThreshold = fedThreshold;
        this.positions = generatePositions();
        this.mutationVariation = mutationVariation;
        this.junglePositions = generateJungle();
        this.steppePositions = generateSteppe();
    }

    public void start() {
        map.initiate();
        MapVisualizer visualizer = new MapVisualizer(map);
        for (int i = 0; i < 100; i++) {
            System.out.println(visualizer.draw(new Vector2D(0, 0), new Vector2D(map.getWidth() - 1, map.getHeight() - 1)));
            tickAnimalMove();
            tickEnF();
            map.modifyTideState();
            tickSpawnGrass();
            increaseTick();
        }
    }

    private Vector2D[] generatePositions() {
        Vector2D upperRight = new Vector2D(map.getWidth(), map.getHeight());
        Vector2D[] positions = new Vector2D[upperRight.getX() * upperRight.getY()];
        for (int i = 0; i < upperRight.getX(); i++) {
            for (int j = 0; j < upperRight.getY(); j++) {
                positions[i * upperRight.getY() + j] = new Vector2D(i, j);
            }
        }
        return positions;
    }

    private Vector2D[] generateJungle() {
        Vector2D jungleLowerLeft = map.getJungleLowerLeft();
        Vector2D jungleUpperRight = map.getJungleUpperRight();
        Vector2D[] positionsInJungle = new Vector2D[(jungleUpperRight.getX() - jungleLowerLeft.getX()) * (jungleUpperRight.getY() - jungleLowerLeft.getY())];
        for (Vector2D position : positions) {
            if (position.follows(jungleLowerLeft) && position.getX() < jungleUpperRight.getX() && position.getY() < jungleUpperRight.getY()) {
                positionsInJungle[(position.getX() - jungleLowerLeft.getX()) * (jungleUpperRight.getY() - jungleLowerLeft.getY()) + position.getY() - jungleLowerLeft.getY()] = position;
            }
        }
        return positionsInJungle;
    }

    private Vector2D[] generateSteppe(){
        Vector2D[] positionsInSteppe = new Vector2D[positions.length - junglePositions.length];
        int i = 0;
        int j = 0;
        for (Vector2D position : positions) {
            if(j < junglePositions.length && position.equals(junglePositions[j])){
                j++;
            }
            else{
                positionsInSteppe[i] = position;
                i++;
            }
        }
        return positionsInSteppe;
    }

    private void move(Animal animal) {
        Vector2D oldPosition = animal.getPosition();
        animal.move(tick, map);
        Vector2D newPosition = animal.getPosition();
        if (oldPosition.equals(newPosition)) {
            return;
        }

        animals.remove(animal, oldPosition);
        animals.put(animal);
    }

    public void eatGrass(Animal animal, Vector2D grassPosition){
        grasses.remove(grassPosition);
        animal.modifyEnergy(grassEnergy);
    }

    public void tickAnimalMove(){
        for(Animal animal: animals.values()) {
            move(animal);
            if (animal.getEnergy() <= 0) {
                animals.remove(animal, animal.getPosition());
                System.out.println("Died on " + animal.getPosition());
            }
        }
    }

    private void reproduce(Animal animal1, Animal animal2){
        int[] genome1 = animal1.getGenome();
        int[] genome2 = animal2.getGenome();
        int genomeLength = genome1.length;
        int animal1Energy = animal1.getEnergy();
        int animal2Energy = animal2.getEnergy();
        int[] newGenome = new int[genomeLength];
        int split = (int) (animal1Energy / (animal1Energy + animal2Energy)) * genomeLength;
        int side = (int) (Math.random() * 2);

        if (side != 0) {
            split = genomeLength - split;
        }

        for (int i = 0; i < split; i++) {
            newGenome[i] = genome1[i];
        }

        for (int i = split; i < genomeLength; i++) {
            newGenome[i] = genome2[i];
        }

        if (mutationVariation) {
            int firstIndex = (int) (Math.random() * genomeLength);
            int secondIndex = (int) (Math.random() * genomeLength);
            int temp = newGenome[firstIndex];
            newGenome[firstIndex] = newGenome[secondIndex];
            newGenome[secondIndex] = temp;
        }
        else {
            // TODO: Zapytac o to ile genow ma byc losowane -> parametr
            newGenome[(int) (Math.random() * genomeLength)] = (int) (Math.random() * 8);
        }


        animals.put(new Animal(tick, (int) (0.33 * animal1Energy) + (int) (0.33 * animal2Energy), newGenome, animal1.getPosition()));

        animal1.modifyEnergy((int) (-0.33 * animal1Energy));
        animal2.modifyEnergy((int) (-0.33 * animal2Energy));
        animal1.addChild();
        animal2.addChild();

        System.out.println("Reproduction on " + animal1.getPosition());
    }

    public void tickEnF(){
        for (Vector2D position : positions) {
            if (animals.containsKey(position)) {
                List<Animal> animalsAtPosition = animals.get(position);
                if (animalsAtPosition.size() == 1 && grasses.containsKey(position)) {
                    eatGrass(animalsAtPosition.get(0), position);
                } else if (animalsAtPosition.size() > 1) {
                    Animal[] dominantPair = getDominantPair(animalsAtPosition);
                    if (grasses.containsKey(position)) {
                        eatGrass(dominantPair[0], position);
                    }
                    if (dominantPair[0].getEnergy() >= fedThreshold && dominantPair[1].getEnergy() >= fedThreshold) {
                        reproduce(dominantPair[0], dominantPair[1]);
                    }

                }

            }
        }
    }

    // todo: sprawdzic czy jest woda na tym polu
    public void tickSpawnGrass(){
        for(Vector2D position: steppePositions){
            if(!grasses.containsKey(position) && Math.random() < 0.2){
                map.placeGrass(new Grass(position));
            }
        }
        for(Vector2D position: junglePositions){
            if(!grasses.containsKey(position) && Math.random() < 0.8){
                map.placeGrass(new Grass(position));
            }
        }
    }

    public void increaseTick(){
        tick += 1;
    }

}