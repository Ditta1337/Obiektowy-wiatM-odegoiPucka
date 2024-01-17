package MlodyPucekIndustries.model.ui;

import MlodyPucekIndustries.model.elements.Animal;
import MlodyPucekIndustries.model.elements.Grass;
import MlodyPucekIndustries.model.elements.Water;
import MlodyPucekIndustries.model.elements.WorldElement;
import MlodyPucekIndustries.model.maps.WorldMap;
import MlodyPucekIndustries.model.simulation.Simulation;
import MlodyPucekIndustries.model.utils.Vector2D;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;


public class MapController {
    private Simulation simulation;
    private int mapHeight;

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    @FXML
    private GridPane mapGrid;

    public void drawMap(WorldMap map) {
        clearGrid();

        int width = map.getWidth();
        int height = map.getHeight();
        int cellSize = 900 / Math.max(width, height);
        GridPane.setHalignment(mapGrid, HPos.CENTER);

        // plot y axis
        for (int y = height - 1; y >= 0; y--) {
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
            Label label = new Label();
            label.setText(String.valueOf(y));
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
            mapGrid.add(label, 0, height - y - 1);
        }

        // plot x axis
        for (int x = 0; x < width; x++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
            Label label = new Label();
            label.setText(String.valueOf(x));
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
            mapGrid.add(label, x + 1, height);
        }

        // plot "y/x"
        mapGrid.getRowConstraints().add(new RowConstraints(cellSize)); // for the axis labels
        mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize)); // for the axis labels
        Label label = new Label();
        label.setText("y/x");
        GridPane.setHalignment(label, HPos.CENTER);
        GridPane.setValignment(label, VPos.CENTER);
        mapGrid.add(label, 0, height);


        ArrayList<WorldElement> elements = map.getElements();
        double maxDepth = 2;
        double maxEnergy = 500;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Rectangle rect = new Rectangle(cellSize, cellSize);
                rect.setFill(Color.LIGHTGREEN); // default color if no element

                for (WorldElement element : elements) {
                    Vector2D position = element.getPosition();
                    if (position.getX() == x && position.getY() == y) {
                        if (element instanceof Grass) {
                            rect.setFill(Color.GREEN);
                        } else if (element instanceof Water) {
                            double depth = ((Water) element).getDepth();
                            rect.setFill(Color.hsb(195, 1, -1.4 * depth + 1.7)); // Cyan color with brightness based on depth
                        } else if (element instanceof Animal) {
                            double energy = ((Animal) element).getEnergy();
                            // TODO: naprawić kolor zwierzaka bo energy bywa wieksze niz maxEnergy i wydupcla error
                            rect.setFill(new Color(1, 0, 0, 1 - energy / maxEnergy)); // Red color with opacity based on energy
                        }
                        break;
                    }
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
}
