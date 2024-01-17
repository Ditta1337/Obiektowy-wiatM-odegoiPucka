package MlodyPucekIndustries.model.utils;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.maps.WorldMap;

import java.util.HashMap;

public class Statistics {
    private long cumulativeLifeSpan = 0;
    private final WorldMap map;

    public Statistics(WorldMap map) {
        this.map = map;
    }

    public int getAnimalsCount() {
        return map.getAnimals().values().size();
    }

    public int getGrassesCount() {
        return map.getGrasses().size();
    }

    public int getEmptyPlaces(){
        int emptyPlaces = 0;
        for(int i = 0; i < map.getWidth(); i++){
            for(int j = 0; j < map.getHeight(); j++){
                if(!map.isOccupied(new Vector2D(i, j))){
                    emptyPlaces++;
                }
            }
        }
        return emptyPlaces;
    }

    public int getAverageEnergy() {
        int sum = 0;
        for (Animal animal : map.getAnimals().values()) {
            sum += animal.getEnergy();
        }
        try {
            return sum / getAnimalsCount();
        }
        catch (ArithmeticException e){
            return 0;
        }
    }

    public double getAverageChildrenCount() {
        long sum = 0;
        for (Animal animal : map.getAnimals().values()) {
            sum += animal.getChildren();
        }
        try {
            return  Math.floor(((double) sum / getAnimalsCount()) * 100) / 100;
        }
        catch (ArithmeticException e){
            return 0;
        }
    }

    public double getAverageLifeSpan() {
        return map.getAverageLifeSpan();
    }

    public int[] getDominantGenome(){
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
        return mostPopularGenome;
    }
}
