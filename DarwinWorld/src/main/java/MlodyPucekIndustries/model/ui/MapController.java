package MlodyPucekIndustries.model.ui;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.elements.Grass;
import MlodyPucekIndustries.model.elements.Water;
import MlodyPucekIndustries.model.elements.WorldElement;
import MlodyPucekIndustries.model.maps.WorldMap;
import MlodyPucekIndustries.model.simulation.Simulation;
import MlodyPucekIndustries.model.utils.Saver;
import MlodyPucekIndustries.model.utils.Statistics;
import MlodyPucekIndustries.model.utils.Vector2D;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MapController {
    private Simulation simulation;
    private Statistics statistics;
    private long tick = 0;
    private XYChart.Series<Number, Number> animalsSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> grassSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> emptyPlacesSeries = new XYChart.Series<>();
    private Rectangle[][] rectangles;
    private boolean jungleVisible = false;
    private boolean dominantAnimalsVisible = false;
    private boolean saveCsv = false;
    private Saver saver = new Saver();

    public void setSimulation(Simulation simulation, boolean saveCsv) {
        this.simulation = simulation;
        this.statistics = simulation.getStatistics();
        this.saveCsv = saveCsv;

        // Add series to chart
        chart.getData().add(animalsSeries);
        chart.getData().add(grassSeries);
        chart.getData().add(emptyPlacesSeries);

        // Optional: Set names for the series
        animalsSeries.setName("Animals");
        grassSeries.setName("Grass");
        emptyPlacesSeries.setName("Empty Places");

    }

    @FXML
    private GridPane mapGrid;

    @FXML
    private LineChart<Number, Number> chart;

    @FXML
    private Label mostPopularGenome;

    @FXML
    private Label averageEnergy;

    @FXML
    private Label averageChildren;

    @FXML
    private Label averageLifeSpan;

    @FXML
    private Label genomeDesc;

    @FXML
    private Label activeGenomeDesc;

    @FXML
    private Label energyDesc;

    @FXML
    private Label childrenDesc;

    @FXML
    private Label eatenGrassDesc;

    @FXML
    private Label descendantsDesc;

    @FXML
    private Label daysAliveDesc;

    @FXML
    private Label genome;

    @FXML
    private Label activeGenome;

    @FXML
    private Label energy;

    @FXML
    private Label children;

    @FXML
    private Label eatenGrass;

    @FXML
    private Label descendants;

    @FXML
    private Label daysAlive;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private Button showJungle;

    @FXML
    private Button showDominantAnimals;

    public void initiate(WorldMap map) {
        int height = map.getHeight();
        int width = map.getWidth();
        int cellSize = 800 / Math.max(width, height);
        rectangles = new Rectangle[width][height];


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final int finalX = x;
                final int finalY = y;

                Rectangle rect = new Rectangle(cellSize, cellSize);
                rect.setOnMouseClicked(event -> animalSelected(new Vector2D(finalX, finalY)));
                mapGrid.add(rect, x, y);
                rectangles[x][y] = rect;
            }
        }
    }

    public void drawMap(WorldMap map) {
        if (saveCsv) {
            saver.saveSimulationStatesTick(statistics, tick);
        }
        if(!jungleVisible && !dominantAnimalsVisible){
            tick++;
            updateChart();
        }
        jungleVisible = false;
        dominantAnimalsVisible = false;
        int width = map.getWidth();
        int height = map.getHeight();
        GridPane.setHalignment(mapGrid, HPos.CENTER);

        double maxEnergy = 1000;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                WorldElement element = map.objectAt(new Vector2D(x, y));
                Rectangle rect = rectangles[x][y];

                if (element instanceof Grass) {
                     rect.setFill(Color.GREEN);
                } else if (element instanceof Water) {
                    double depth = ((Water) element).getDepth();
                    rect.setFill(Color.hsb(195, 1, -1.4 * depth + 1.7)); // Cyan color with brightness based on depth
                } else if (element instanceof Animal) {
                    double energy = Math.min(Math.max(((Animal) element).getEnergy() / maxEnergy, 0.5), 1);
                    rect.setFill(new Color(1, 0, 0, energy));
                } else {
                    rect.setFill(Color.LIGHTGREEN);
                }

            }
        }
    }

//    private void clearGrid() {
//        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
//        mapGrid.getColumnConstraints().clear();
//        mapGrid.getRowConstraints().clear();
//    }

    public void onStartStopClicked() {
        simulation.pauseUnpause();
    }

    private void updateChart(){
        int numAnimals = statistics.getAnimalsCount();
        int numGrass = statistics.getGrassesCount();
        int emptyPlaces = statistics.getEmptyPlaces();
        int[] mostPopularGenome = statistics.getDominantGenome();
        int averageEnergy = statistics.getAverageEnergy();
        double averageChildren = statistics.getAverageChildrenCount();
        double averageLifeSpan = statistics.getAverageLifeSpan();

        System.out.println("Animals: " + numAnimals);
        System.out.println("Grass: " + numGrass);
        System.out.println("Empty places: " + emptyPlaces);
        System.out.println("Most popular genome: " + Arrays.toString(mostPopularGenome));
        System.out.println("Average energy: " + averageEnergy);
        System.out.println("Average children: " + averageChildren);
        System.out.println("Average life span: " + averageLifeSpan);

        this.mostPopularGenome.setText(Arrays.toString(mostPopularGenome));
        this.averageEnergy.setText(String.valueOf(averageEnergy));
        this.averageChildren.setText(String.valueOf(averageChildren));
        this.averageLifeSpan.setText(String.valueOf(averageLifeSpan));

        animalsSeries.getData().add(new XYChart.Data<>(tick, numAnimals));
        grassSeries.getData().add(new XYChart.Data<>(tick, numGrass));
        emptyPlacesSeries.getData().add(new XYChart.Data<>(tick, emptyPlaces));

        if (tick % 100 == 0) {
            animalsSeries.getData().clear();
            grassSeries.getData().clear();
            emptyPlacesSeries.getData().clear();
            xAxis.setLowerBound(tick);
            xAxis.setUpperBound(tick + 100);

        }

        ObservableList<XYChart.Series<Number, Number>> chartData = FXCollections.observableArrayList(chart.getData());
        chart.setData(FXCollections.observableArrayList());
        chart.setData(chartData);

    }

    // TODO: dodac @Override do map
    // zmienic z modifyTideState na customMapMethod

    private void animalSelected(Vector2D position) {
        simulation.setObservedAnimal(position);
    }


    public void drawObservedAnimal(Animal animal) {
        genomeDesc.setText("Genome: ");
        activeGenomeDesc.setText("Active genome: ");
        energyDesc.setText("Energy: ");
        childrenDesc.setText("Children: ");
        eatenGrassDesc.setText("Eaten grass: ");
        descendantsDesc.setText("Descendants: ");
        daysAliveDesc.setText("Days alive: ");
        genome.setText(Arrays.toString(animal.getGenome()));
        activeGenome.setText(String.valueOf(animal.getActiveGenome()));
        energy.setText(String.valueOf(animal.getEnergy()));
        children.setText(String.valueOf(animal.getChildrenNum()));
        eatenGrass.setText(String.valueOf(animal.getEatenGrass()));
        descendants.setText(String.valueOf(animal.getDescendants()));
       if (animal.getEnergy() > 0) {
           daysAlive.setText(String.valueOf(animal.getAge()));
       } else {
           daysAliveDesc.setText("Days alive:");
           daysAlive.setText(String.valueOf(animal.getAge()));
           energyDesc.setText("Died at day:");
           energy.setText(String.valueOf(animal.getBirthTick() + animal.getAge()));
       }
    }

    public void enablePauseButtons() {
        showJungle.setDisable(false);
        showDominantAnimals.setDisable(false);
        showJungle.setVisible(true);
        showDominantAnimals.setVisible(true);
    }

    public void disablePauseButtons() {
        showJungle.setDisable(true);
        showDominantAnimals.setDisable(true);
        showJungle.setVisible(false);
        showDominantAnimals.setVisible(false);
    }

    public void onShowJungleClicked() {
        WorldMap map = simulation.getMap();
        if(!jungleVisible) {
            Vector2D jungleLowerLeft = map.getJungleLowerLeft();
            Vector2D jungleUpperRight = map.getJungleUpperRight();
            for (int i = jungleLowerLeft.getX(); i < jungleUpperRight.getX(); i++) {
                for (int j = jungleLowerLeft.getY(); j < jungleUpperRight.getY(); j++) {
                    rectangles[i][j].setFill(Color.YELLOW);
                }
            }
            jungleVisible = true;
        } else{
            drawMap(map);
        }
    }

    public void onShowDominantAnimalsClicked() {
        WorldMap map = simulation.getMap();
        if(!dominantAnimalsVisible) {
            int[] dominantGenome = statistics.getDominantGenome();
            for (Animal animal : map.getAnimals().values()) {
                if (Arrays.equals(animal.getGenome(), dominantGenome)) {
                    rectangles[animal.getPosition().getX()][animal.getPosition().getY()].setFill(Color.BLACK);
                }
            }
            dominantAnimalsVisible = true;
        } else{
            drawMap(simulation.getMap());
        }
    }

}
