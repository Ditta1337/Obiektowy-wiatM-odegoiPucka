package MlodyPucekIndustries.model.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.*;

public class MenuMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("MlodyPucekIndustries/model/ui/menu.fxml"));
        BorderPane viewRoot = loader.load();
        primaryStage.setScene(new Scene(viewRoot, 1000, 1000));
        primaryStage.setTitle("Menu");
        primaryStage.show();
    }
}
