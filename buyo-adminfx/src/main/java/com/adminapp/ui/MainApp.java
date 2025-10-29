package com.adminapp.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/adminapp/ui/MainView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Sistema Administrativo (JavaFX)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
