package MlodyPucekIndustries.model.maps;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.elements.Grass;
import MlodyPucekIndustries.model.utils.MultipleHashMap;
import MlodyPucekIndustries.model.utils.Vector2d;

import java.util.HashMap;
import java.util.List;

import static MlodyPucekIndustries.model.utils.RankAnimals.getDominantPair;

public class RegularMapManager {
    private final MultipleHashMap animals;
    private final HashMap<Vector2d, Grass> grasses;
    private final Vector2d[] positions;
    private final Vector2d[] junglePositions;
    private final Vector2d[] steppePositions;
    private final WorldMap map;
    private final int fedThreshold;
    private final int grassEnergy;
    private final boolean mutationVariation;
    private long tick = 0;

    public RegularMapManager(int defaultGrassEnergy,
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

    private Vector2d[] generatePositions() {
        Vector2d upperRight = map.getUpperRight();
        Vector2d[] positions = new Vector2d[upperRight.getX() * upperRight.getY()];
        for (int i = 0; i < upperRight.getX(); i++) {
            for (int j = 0; j < upperRight.getY(); j++) {
                positions[i * upperRight.getY() + j] = new Vector2d(i, j);
            }
        }
        return positions;
    }

    private Vector2d[] generateJungle() {
        Vector2d jungleLowerLeft = map.getJungleLowerLeft();
        Vector2d jungleUpperRight = map.getJungleUpperRight();
        Vector2d[] positionsInJungle = new Vector2d[(jungleUpperRight.getX() - jungleLowerLeft.getX()) * (jungleUpperRight.getY() - jungleLowerLeft.getY())];
        for (Vector2d position : positions) {
            if (position.follows(jungleLowerLeft) && position.getX() < jungleUpperRight.getX() && position.getY() < jungleUpperRight.getY()) {
                positionsInJungle[(position.getX() - jungleLowerLeft.getX()) * (jungleUpperRight.getY() - jungleLowerLeft.getY()) + position.getY() - jungleLowerLeft.getY()] = position;
            }
        }
        return positionsInJungle;
    }

    public Vector2d[] generateSteppe(){
        Vector2d[] positionsInSteppe = new Vector2d[positions.length - junglePositions.length];
        int i = 0;
        int j = 0;
        for (Vector2d position : positions) {
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
        Vector2d oldPosition = animal.getPosition();
        animal.move(tick, map);
        Vector2d newPosition = animal.getPosition();
        if (oldPosition.equals(newPosition)) {
            return;
        }

        animals.remove(animal, oldPosition);
        animals.put(animal);
    }

    public void eatGrass(Animal animal, Vector2d grassPosition){
        grasses.remove(grassPosition);
        animal.modifyEnergy(grassEnergy);
    }

    public void tickAnimalMove(){
        for(Animal animal: animals.values()) {
            move(animal);
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
            // TODO: Zapytac Lazarza o to ile genow ma byc losowane
            newGenome[(int) (Math.random() * genomeLength)] = (int) (Math.random() * 8);
        }


        animals.put(new Animal(tick, (int) (0.33 * animal1Energy) + (int) (0.33 * animal2Energy), newGenome, animal1.getPosition()));

        animal1.modifyEnergy((int) (-0.33 * animal1Energy));
        animal2.modifyEnergy((int) (-0.33 * animal2Energy));
        animal1.addChild();
        animal2.addChild();

        System.out.println("Seggz on " + animal1.getPosition());
    }

    public void tickEnF(){
        for (Vector2d position : positions) {
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

    public void tickSpawnGrass(){
        for(Vector2d position: steppePositions){
            if(!grasses.containsKey(position) && Math.random() < 0.2){
                map.placeGrass(new Grass(position));
            }
        }
        for(Vector2d position: junglePositions){
            if(!grasses.containsKey(position) && Math.random() < 0.8){
                map.placeGrass(new Grass(position));
            }
        }
    }

    public void increaseTick(){
        tick += 1;
    }

}