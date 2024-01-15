package MlodyPucekIndustries.model.maps;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.elements.Water;
import MlodyPucekIndustries.model.elements.WorldElement;
import MlodyPucekIndustries.model.utils.MapDirection;
import MlodyPucekIndustries.model.utils.PerlinNoise2D;
import MlodyPucekIndustries.model.utils.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;

public class WaterMap extends RegularMap {
    HashMap<Vector2D, Water> waters = new HashMap<>();
    private final double grassEdge;
    private final double deepEdge;
    private double tideState;
    private double deltaTide;

    public WaterMap(int height,
                    int width,
                    int jungleHeight,
                    int jungleWidth,
                    int defaultAnimals,
                    int defaultGrass,
                    int animalEnergy,
                    int genomeLength,
                    double grassEdge,
                    double deepEdge) {
        super(height, width, jungleHeight, jungleWidth, defaultAnimals, defaultGrass, animalEnergy, genomeLength);
        this.grassEdge = grassEdge;
        this.deepEdge = deepEdge;
        this.tideState = deepEdge;
        this.deltaTide = (1 - grassEdge) / 10;
    }

    @Override
    public void initiate() {
        placeWaters();
        super.initiate();
    }

    private void placeWaters() {
        PerlinNoise2D perlinNoise2D = new PerlinNoise2D(width, height);
        double[][] waterLevels = perlinNoise2D.getWaterLevels(grassEdge, deepEdge);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height ; j++) {
                double waterLevel = waterLevels[i][j];
                if(waterLevel > grassEdge){
                    waters.put(new Vector2D(i,j), new Water(new Vector2D(i,j), waterLevel));
                }
            }
        }
    }

    @Override
    public boolean canMoveTo(Vector2D position) {
        if (!super.canMoveTo(position)) {
            return false;
        }
        position = super.validPosition(position);
        if (waters.containsKey(position)) {
            return !(waters.get(position).getDepth() > tideState);
        }
        return true;
    }

    @Override
    public void modifyTideState() {
        tideState += deltaTide;
        if (deltaTide < 0) {
            removeSunkenGrasses();
            moveOrDie();
        }

        if (tideState < grassEdge){
            tideState = grassEdge;
            deltaTide = -deltaTide;
        }

        if (tideState > 0.9){
            tideState = 0.9;
            deltaTide = -deltaTide;
        }
    }

    private void removeSunkenGrasses() {
        for (Vector2D waterPosition : waters.keySet()) {
            // TODO: check if >= tideState or > tideState
            if (grasses.containsKey(waterPosition) && waters.get(waterPosition).getDepth() >= tideState) {
                grasses.remove(waterPosition);
            }
        }
    }

    private void moveOrDie() {
        for (Vector2D waterPosition : waters.keySet()) {
            boolean animalMoved = false;
            ArrayList<Animal> animalsToRemove = new ArrayList<>();
            if (animals.containsKey(waterPosition)) {
                for (Animal animal : animals.get(waterPosition)) {
                    int randomIndex = (int) (Math.random() * 8);
                    for (int i = 0; i < 8 && !animalMoved; i++) {
                        Vector2D newPosition = animal.getPosition().add(MapDirection.values()[randomIndex].toUnitVector());
                        newPosition = validPosition(newPosition);
                        if (canMoveTo(newPosition)) {
                            animal.setPosition(newPosition);
                            animalsToRemove.add(animal);
                            animals.put(animal);
                            animalMoved = true;
                        }
                        randomIndex = (randomIndex + 1) % 8;
                    }
                    if (!animalMoved) {
                        animalsToRemove.add(animal);
                    }
                }
                for (Animal animal : animalsToRemove) {
                    animals.remove(animal, waterPosition);
                }
            }
        }
    }

    @Override
    public WorldElement objectAt(Vector2D position) {
        if (waters.containsKey(position) && waters.get(position).getDepth() >= tideState) {
            return waters.get(position);
        }
        return super.objectAt(position);
    }

    @Override
    public boolean isOccupied(Vector2D position) {
        return super.isOccupied(position) || waters.containsKey(position) && waters.get(position).getDepth() >= tideState;
    }

}
