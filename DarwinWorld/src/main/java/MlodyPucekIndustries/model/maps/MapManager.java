package MlodyPucekIndustries.model.maps;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.elements.Grass;
import MlodyPucekIndustries.model.utils.*;
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
    private final int grassSpawnNumber;
    private final int minMutationNumber;
    private final int maxMutationNumber;
    private final double energySharePercentage;

    public MapManager(int defaultGrassEnergy,
                      int fedThreshold,
                      boolean mutationVariation,
                      int grassSpawnNumber,
                      int minMutationNumber,
                      int maxMutationNumber,
                      double energySharePercentage,
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
        this.grassSpawnNumber = grassSpawnNumber;
        this.minMutationNumber = minMutationNumber;
        this.maxMutationNumber = maxMutationNumber;
        this.energySharePercentage = energySharePercentage;
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
            if (animal.getEnergy() <= 0) {
                animals.remove(animal, animal.getPosition());
                // System.out.println("Died on " + animal.getPosition());
                map.addDeadAnimal(animal.getAge());
                animal.die();
            } else {
                move(animal);
            }
        }
    }

    public void reproduce(Animal animal1, Animal animal2){
        int[] genome1 = animal1.getGenome();
        int[] genome2 = animal2.getGenome();
        int genomeLength = genome1.length;
        int animal1Energy = animal1.getEnergy();
        int animal2Energy = animal2.getEnergy();
        int[] newGenome = new int[genomeLength];
        // System.out.println("Reproduction on " + animal1.getPosition() + " with energies: " + animal1Energy + " and " + animal2Energy);
        int split = animal1Energy / (animal1Energy + animal2Energy) * genomeLength;
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

        int mutationNumber = (int) (Math.random() * (maxMutationNumber - minMutationNumber)) + minMutationNumber;
        if (mutationVariation) {
            for (int i = 0; i < mutationNumber; i++) {
                int firstIndex = (int) (Math.random() * genomeLength);
                int secondIndex = (int) (Math.random() * genomeLength);
                int temp = newGenome[firstIndex];
                newGenome[firstIndex] = newGenome[secondIndex];
                newGenome[secondIndex] = temp;
            }
        }
        else {
            for (int i = 0; i < mutationNumber; i++) {
                newGenome[(int) (Math.random() * genomeLength)] = (int) (Math.random() * 8);
            }
        }

        Animal child = new Animal(tick, (int) (energySharePercentage * animal1Energy) + (int) (energySharePercentage * animal2Energy), newGenome, animal1.getPosition());
        animals.put(child);

        animal1.modifyEnergy((int) (-energySharePercentage * animal1Energy));
        animal2.modifyEnergy((int) (-energySharePercentage * animal2Energy));
        animal1.addChild(child);
        animal2.addChild(child);
        child.addParent(animal1);
        child.addParent(animal2);
    }

    public void tickEnF(){
        for (Vector2D position : positions) {
            if (animals.containsKey(position)) {
                List<Animal> animalsAtPosition = animals.get(position);
                if (animalsAtPosition.size() == 1 && grasses.containsKey(position)) {
                    animalsAtPosition.get(0).addEatenGrass();
                    eatGrass(animalsAtPosition.get(0), position);
                } else if (animalsAtPosition.size() > 1) {
                    Animal[] dominantPair = getDominantPair(animalsAtPosition);
                    if (grasses.containsKey(position)) {
                        dominantPair[0].addEatenGrass();
                        eatGrass(dominantPair[0], position);
                    }
                    if (dominantPair[0].getEnergy() >= fedThreshold && dominantPair[1].getEnergy() >= fedThreshold) {
                        reproduce(dominantPair[0], dominantPair[1]);
                    }

                }

            }
        }
    }

    private void fillLeftOverGrass(Vector2D[] region,
                                   int[] permutation,
                                   int index,
                                   int leftOverGrass){
        for (int i = 0; i < leftOverGrass; i++) {
            if (index < region.length) {
                Vector2D position = region[permutation[index]];
                index++;
                if (map.canMoveTo(position) && !grasses.containsKey(position)) {
                    grasses.put(position, new Grass(position));
                }
            }
            else {
                break;
            }
        }
    }

    private int placeGrassInRandomPosition(Vector2D[] region1,
                                           Vector2D[] region2,
                                           int[] permutation1,
                                           int[] permutation2,
                                           int index1,
                                           int index2,
                                           int placedGrass) {
        boolean placed = false;
        while(!placed) {
            if (index1 < region1.length) {
                Vector2D position = region1[permutation1[index1]];
                index1++;
                if (map.canMoveTo(position) && !grasses.containsKey(position)) {
                    grasses.put(position, new Grass(position));
                    placed = true;
                }
            }
            else{
                fillLeftOverGrass(region2, permutation2, index2, grassSpawnNumber - placedGrass);
                return -1;
            }
        }
        return index1;
    }


    public void tickSpawnGrass(){
        int[] steppePermutation = RandomPositionGenerator.permutationGenerator(steppePositions.length);
        int[] junglePermutation = RandomPositionGenerator.permutationGenerator(junglePositions.length);
        int steppeIndex = 0;
        int jungleIndex = 0;
        for (int i = 0; i < grassSpawnNumber; i++){
            if (Math.random() < 0.2) {
                    int newI = placeGrassInRandomPosition(steppePositions,
                            junglePositions,
                            steppePermutation,
                            junglePermutation,
                            steppeIndex,
                            jungleIndex,
                            i);
                    if (newI >= 0) {
                        steppeIndex = newI;
                    }
                    else{
                        return;
                    }
                }
            else {
                int newI = placeGrassInRandomPosition(junglePositions,
                        steppePositions,
                        junglePermutation,
                        steppePermutation,
                        jungleIndex,
                        steppeIndex,
                        i);
                if (newI >= 0) {
                    jungleIndex = newI;
                }
                else{
                    return;
                }
            }
        }
    }

    public void increaseTick(){
        tick += 1;
    }

}