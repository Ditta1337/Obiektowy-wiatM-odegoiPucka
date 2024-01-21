package MlodyPucekIndustries.model.utils;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.maps.WorldMap;

import java.util.HashMap;

public class Statistics {
    private long cumulativeLifeSpan = 0;
    private final WorldMap map;
    private int emptyPlaces = 0;
    private int animalCount = 0;
    private int grassCount = 0;
    private int averageEnergy = 0;
    private double averageChildrenCount = 0;
    private double averageLifeSpan = 0;
    private int[] dominantGenome;



    public Statistics(WorldMap map) {
        this.map = map;
        this.dominantGenome = new int[map.getGenomeLength()];
    }

    public int getEmptyPlaces() {
        return emptyPlaces;
    }

    public int getAnimalCount() {
        return animalCount;
    }

    public int getGrassCount() {
        return grassCount;
    }

    public int getAverageEnergy() {
        return averageEnergy;
    }

    public double getAverageChildrenCount() {
        return averageChildrenCount;
    }

    public double getAverageLifeSpan() {
        return averageLifeSpan;
    }

    public int[] getDominantGenome() {
        return dominantGenome;
    }

    public void updateStats() {
        updateAverageLifeSpan();
        updateAnimalsCount();
        updateGrassesCount();
        updateEmptyPlaces();
        updateAverageEnergy();
        updateAverageChildrenCount();
        updateDominantGenome();
    }

    private void updateAnimalsCount() {
        this.animalCount = map.getAnimals().values().size();
    }

    private void updateGrassesCount() {
        this.grassCount = map.getGrasses().size();
    }

    private void updateEmptyPlaces(){
        int emptyPlaces = 0;
        for(int i = 0; i < map.getWidth(); i++){
            for(int j = 0; j < map.getHeight(); j++){
                if(!map.isOccupied(new Vector2D(i, j))){
                    emptyPlaces++;
                }
            }
        }
        this.emptyPlaces = emptyPlaces;
    }

    private void updateAverageEnergy() {
        int sum = 0;
        for (Animal animal : map.getAnimals().values()) {
            sum += animal.getEnergy();
        }
        try {
            this.averageEnergy = sum / animalCount;
        }
        catch (ArithmeticException e){
               this.averageEnergy = 0;
        }
    }

    private void updateAverageChildrenCount() {
        long sum = 0;
        for (Animal animal : map.getAnimals().values()) {
            sum += animal.getChildrenNum();
        }
        try {
             this.averageChildrenCount = Math.floor(((double) sum / animalCount) * 100) / 100;
        }
        catch (ArithmeticException e){
            this.averageChildrenCount = 0;
        }
    }

    private void updateAverageLifeSpan() {
        this.averageLifeSpan = map.getAverageLifeSpan();
    }

    private void updateDominantGenome(){
        HashMap<int[], Integer> genomes = new HashMap<>();
        for(Animal animal : map.getAnimals().values()){
            int[] genome = animal.getGenome();
            if(genomes.containsKey(genome)){
                genomes.put(genome, genomes.get(genome) + 1);
            }
            else{
                genomes.put(genome, 1);
            }
        }
        int[] mostPopularGenome = new int[map.getGenomeLength()];
        int max = 0;
        for(int[] genome : genomes.keySet()){
            if(genomes.get(genome) > max){
                max = genomes.get(genome);
                mostPopularGenome = genome;
            }
        }
        this.dominantGenome = mostPopularGenome;
    }
}
