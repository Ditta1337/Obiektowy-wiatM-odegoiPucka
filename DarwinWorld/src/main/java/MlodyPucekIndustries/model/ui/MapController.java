package MlodyPucekIndustries.model.ui;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.elements.Grass;
import MlodyPucekIndustries.model.elements.Water;
import MlodyPucekIndustries.model.elements.WorldElement;
import MlodyPucekIndustries.model.maps.WorldMap;
import MlodyPucekIndustries.model.simulation.Simulation;
import MlodyPucekIndustries.model.utils.Statistics;
import MlodyPucekIndustries.model.utils.Vector2D;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;


public class MapController {
    private Simulation simulation;
    private Statistics statistics;
    private int mapHeight;
    private long tick = 0;
    private XYChart.Series<Number, Number> animalsSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> grassSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> emptyPlacesSeries = new XYChart.Series<>();

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        this.statistics = simulation.getStatistics();
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

    public void drawMap(WorldMap map) {
        clearGrid();
        updateChart();
        tick++;
        int width = map.getWidth();
        int height = map.getHeight();
        int cellSize = 800 / Math.max(width, height);
        GridPane.setHalignment(mapGrid, HPos.CENTER);


        double maxEnergy = 1000;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Rectangle rect = new Rectangle(cellSize, cellSize);
                WorldElement element = map.objectAt(new Vector2D(x, y));

                if (element instanceof Grass) {
                    rect.setFill(Color.GREEN);
                } else if (element instanceof Water) {
                    double depth = ((Water) element).getDepth();
                    rect.setFill(Color.hsb(195, 1, -1.4 * depth + 1.7)); // Cyan color with brightness based on depth
                } else if (element instanceof Animal) {
                    double energy = ((Animal) element).getEnergy();
                    rect.setFill(new Color(1, 0, 0, 1 - energy / maxEnergy));
                } else {
                    rect.setFill(Color.LIGHTGREEN);
                }
                mapGrid.add(rect, x + 1, height - y - 1);

            }
        }
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

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


        // Add data to series. Assuming 'time' as X-axis.
        // Replace 'time' with appropriate variable from your code.
        animalsSeries.getData().add(new XYChart.Data<>(tick, numAnimals));
        grassSeries.getData().add(new XYChart.Data<>(tick, numGrass));
        emptyPlacesSeries.getData().add(new XYChart.Data<>(tick, emptyPlaces));

        if (tick > 100) {
            tick = -1;
            animalsSeries.getData().clear();
            grassSeries.getData().clear();
            emptyPlacesSeries.getData().clear();
        }

        ObservableList<XYChart.Series<Number, Number>> chartData = FXCollections.observableArrayList(chart.getData());
        chart.setData(FXCollections.observableArrayList());
        chart.setData(chartData);

    }




}
