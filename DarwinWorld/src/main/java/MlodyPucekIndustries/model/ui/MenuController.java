package MlodyPucekIndustries.model.ui;

import MlodyPucekIndustries.model.elements.Water;
import MlodyPucekIndustries.model.maps.MapManager;
import MlodyPucekIndustries.model.maps.RegularMap;
import MlodyPucekIndustries.model.maps.WaterMap;
import MlodyPucekIndustries.model.maps.WorldMap;
import MlodyPucekIndustries.model.simulation.Simulation;
import MlodyPucekIndustries.model.simulation.SimulationEngine;
import MlodyPucekIndustries.model.utils.Save;
import MlodyPucekIndustries.model.utils.Saver;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static java.util.Collections.min;

public class MenuController implements Initializable{
    @FXML
    private Spinner<Integer> height;
    @FXML
    private Spinner<Integer> width;
    @FXML
    private Spinner<Integer> jungleHeight;
    @FXML
    private Spinner<Integer> jungleWidth;
    @FXML
    private Spinner<Integer> initialAnimal;
    @FXML
    private Spinner<Integer> startAnimalEnergy;
    @FXML
    private Spinner<Integer> genomeLength;
    @FXML
    private Slider minMutation;
    @FXML
    private Label minMutationShow;
    @FXML
    private Spinner<Integer> energySharePercentage;
    @FXML
    private Spinner<Integer> initialGrassNumber;
    @FXML
    private Spinner<Integer> grassEnergy;
    @FXML
    private Spinner<Integer> fedThreshold;
    @FXML
    private Slider maxMutation;
    @FXML
    private Label maxMutationShow;
    @FXML
    private Spinner<Integer> grassSpawn;
    @FXML
    private ChoiceBox mapChoice;
    @FXML
    private CheckBox switchGenome;
    @FXML
    private ChoiceBox saveChoice;

    private SpinnerValueFactory<Integer> valueFactory = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set every spinner to int
        height.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 500, 10));
        jungleHeight.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 5));
        initialAnimal.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10));
        startAnimalEnergy.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999999999, 10));
        genomeLength.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 10));
        energySharePercentage.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 30));
        width.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 500, 10));
        jungleWidth.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 5));
        initialGrassNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 25));
        grassEnergy.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999999999, 5));
        fedThreshold.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999999999, 20));
        grassSpawn.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 25));

        height.valueProperty().addListener((observable, oldValue, newValue) -> onHeightChanged());
        width.valueProperty().addListener((observable, oldValue, newValue) -> onWidthChanged());
        maxMutation.valueProperty().addListener((observable, oldValue, newValue) -> setMaxMutationSliderValue());
        minMutation.valueProperty().addListener((observable, oldValue, newValue) -> setMinMutationSliderValue());
        genomeLength.valueProperty().addListener((observable, oldValue, newValue) -> setMaxMutationValue());
        maxMutation.valueProperty().addListener((observable, oldValue, newValue) -> setMinMutationValue());
        initiateSaveChoiceBoxes();
    }

    private void initiateSaveChoiceBoxes() {
        Saver saver = new Saver();
        int numberOfSaves = saver.getNumElementsInSaveFolder();
        for (int i=0; i<numberOfSaves; i++) {
            saveChoice.getItems().add(i);
        }
        saveChoice.setValue(0);
    }

    private void onHeightChanged(){
        System.out.println("Height changed");
        if (height.getValue() < 10) {
            height.getValueFactory().setValue(10);
        }
        int value = min(List.of(height.getValue(),jungleHeight.getValue()));
        valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,height.getValue(),value);
        jungleHeight.setValueFactory(valueFactory);
        setValidMapDependentValues();
    }

    private void onWidthChanged(){
        System.out.println("Width changed");
        if (width.getValue() < 10) {
            width.getValueFactory().setValue(10);
        }
        int value = min(List.of(width.getValue(),jungleWidth.getValue()));
        valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,width.getValue(),value);
        jungleWidth.setValueFactory(valueFactory);
        setValidMapDependentValues();
    }

    private void setValidMapDependentValues() {
        int area = width.getValue() * height.getValue();
        int animalNumber = min(List.of(initialAnimal.getValue(), area));
        int grassNumber = min(List.of(initialGrassNumber.getValue(), area));
        int grassSpawnNumber = min(List.of(grassSpawn.getValue(), area));
        initialAnimal.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, area, animalNumber));
        initialGrassNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, area, grassNumber));
        grassSpawn.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, area, grassSpawnNumber));
    }

    private void setMaxMutationSliderValue() {
        int value = (int) maxMutation.getValue();
        maxMutationShow.setText(String.valueOf(value));
    }

    private void setMinMutationSliderValue() {
        int value = (int) minMutation.getValue();
        minMutationShow.setText(String.valueOf(value));
    }

    private void setMaxMutationValue() {
        int maxValue = Math.max(genomeLength.getValue(), 1);
        maxMutation.setMax(maxValue);
    }

    private void setMinMutationValue() {
        int minValue = (int) maxMutation.getValue();
        minMutation.setMax(minValue);
    }

    public void onStartButtonClicked() throws Exception {
        System.out.println("Start button clicked");
        int startAnimalEnergyValue = startAnimalEnergy.getValue().intValue();
        System.out.println(startAnimalEnergyValue);
        System.out.println("Height: " + height.getValue());
        WorldMap map = null;
        if (mapChoice.getValue().equals("RegularMap")) {
            map = new RegularMap(
                    height.getValue(),
                    width.getValue(),
                    jungleHeight.getValue(),
                    jungleWidth.getValue(),
                    initialAnimal.getValue(),
                    initialGrassNumber.getValue(),
                    startAnimalEnergy.getValue(),
                    genomeLength.getValue()
            );
        } else {
            map = new WaterMap(
                    height.getValue(),
                    width.getValue(),
                    jungleHeight.getValue(),
                    jungleWidth.getValue(),
                    initialAnimal.getValue(),
                    initialGrassNumber.getValue(),
                    startAnimalEnergy.getValue(),
                    genomeLength.getValue(),
                    0.6,
                    0.75
            );
        }
        MapManager mapManager = new MapManager(
                grassEnergy.getValue(),
                fedThreshold.getValue(),
                switchGenome.isSelected(),
                grassSpawn.getValue(),
                (int) Math.round(minMutation.getValue()),
                (int) Math.round(maxMutation.getValue()),
                energySharePercentage.getValue() / 100.0,
                map
        );

        // TODO: try catch dla złych wartości

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("MlodyPucekIndustries/model/ui/map.fxml"));
        BorderPane viewRoot = loader.load();
        MapController mapController = loader.getController();

        Simulation simulation = new Simulation(mapManager, map, mapController);

        Stage stage = new Stage();
        stage.setScene(new Scene(viewRoot));
        stage.setTitle("Simulation window");

        // set prefHeight and prefWidth to fullscreen
        stage.setMaximized(true);

        Thread simulationThread = new Thread(simulation);
        mapController.setSimulation(simulation);
        simulationThread.start();

        stage.setOnCloseRequest(e -> {
            // stop the thread with simulation
            simulation.stopRunning();
        });

        stage.show();
    }

    public void onSaveButtonClicked() {
        System.out.println("Save button clicked");
        Save save = new Save(
                width.getValue(),
                height.getValue(),
                jungleHeight.getValue(),
                jungleWidth.getValue(),
                initialAnimal.getValue(),
                startAnimalEnergy.getValue(),
                genomeLength.getValue(),
                (int) Math.round(minMutation.getValue()),
                (int) Math.round(maxMutation.getValue()),
                energySharePercentage.getValue(),
                initialGrassNumber.getValue(),
                grassEnergy.getValue(),
                fedThreshold.getValue(),
                grassSpawn.getValue(),
                switchGenome.isSelected(),
                mapChoice.getValue().toString()
        );
        Saver saver = new Saver();
        saver.saveMenu(save);
        saveChoice.getItems().add(saveChoice.getItems().size());
    }

    public void onLoadButtonClicked() {
        System.out.println("Load button clicked");

        Saver saver = new Saver();
        Save save = saver.loadMenu((int) saveChoice.getValue());
        width.getValueFactory().setValue(save.getWidth());
        height.getValueFactory().setValue(save.getHeight());
        jungleWidth.getValueFactory().setValue(save.getJungleWidth());
        jungleHeight.getValueFactory().setValue(save.getJungleHeight());
        initialAnimal.getValueFactory().setValue(save.getInitialAnimal());
        startAnimalEnergy.getValueFactory().setValue(save.getStartAnimalEnergy());
        genomeLength.getValueFactory().setValue(save.getGenomeLength());
        minMutation.setValue(save.getMinMutation());
        maxMutation.setValue(save.getMaxMutation());
        energySharePercentage.getValueFactory().setValue(save.getEnergySharePercentage());
        initialGrassNumber.getValueFactory().setValue(save.getInitialGrassNumber());
        grassEnergy.getValueFactory().setValue(save.getGrassEnergy());
        fedThreshold.getValueFactory().setValue(save.getFedThreshold());
        grassSpawn.getValueFactory().setValue(save.getGrassSpawn());
        switchGenome.setSelected(save.isSwitchGenome());
        mapChoice.setValue(save.getMapChoice());

        setMaxMutationSliderValue();
        setMinMutationSliderValue();

        setMaxMutationValue();
        setMinMutationValue();

        setValidMapDependentValues();
    }
}
