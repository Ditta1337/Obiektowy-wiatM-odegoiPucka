package MlodyPucekIndustries.model.utils;

import MlodyPucekIndustries.model.maps.WorldMap;

public class Save {
    private int width;
    private int height;
    private int jungleWidth;
    private int jungleHeight;
    private int initialAnimal;
    private int startAnimalEnergy;
    private int genomeLength;
    private int minMutation;
    private int maxMutation;
    private int energySharePercentage;
    private int initialGrassNumber;
    private int grassEnergy;
    private int fedThreshold;
    private int grassSpawn;
    private String mapChoice;
    private boolean switchGenome;

    public Save(int width, int height, int jungleWidth, int jungleHeight, int initialAnimal, int startAnimalEnergy, int genomeLength, int minMutation, int maxMutation, int energySharePercentage, int initialGrassNumber, int grassEnergy, int fedThreshold, int grassSpawn, boolean switchGenome, String mapChoice) {
        this.width = width;
        this.height = height;
        this.jungleWidth = jungleWidth;
        this.jungleHeight = jungleHeight;
        this.initialAnimal = initialAnimal;
        this.startAnimalEnergy = startAnimalEnergy;
        this.genomeLength = genomeLength;
        this.minMutation = minMutation;
        this.maxMutation = maxMutation;
        this.energySharePercentage = energySharePercentage;
        this.initialGrassNumber = initialGrassNumber;
        this.grassEnergy = grassEnergy;
        this.fedThreshold = fedThreshold;
        this.grassSpawn = grassSpawn;
        this.switchGenome = switchGenome;
        this.mapChoice = mapChoice;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getJungleWidth() {
        return jungleWidth;
    }

    public int getJungleHeight() {
        return jungleHeight;
    }

    public int getInitialAnimal() {
        return initialAnimal;
    }

    public int getStartAnimalEnergy() {
        return startAnimalEnergy;
    }

    public int getGenomeLength() {
        return genomeLength;
    }

    public int getMinMutation() {
        return minMutation;
    }

    public int getMaxMutation() {
        return maxMutation;
    }

    public int getEnergySharePercentage() {
        return energySharePercentage;
    }

    public int getInitialGrassNumber() {
        return initialGrassNumber;
    }

    public int getGrassEnergy() {
        return grassEnergy;
    }

    public int getFedThreshold() {
        return fedThreshold;
    }

    public int getGrassSpawn() {
        return grassSpawn;
    }

    public String getMapChoice() {
        return mapChoice;
    }

    public boolean isSwitchGenome() {
        return switchGenome;
    }
}