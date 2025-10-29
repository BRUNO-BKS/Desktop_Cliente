package com.buyo.adminfx.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlUrl = MainApp.class.getResource("MainView.fxml");
        if (fxmlUrl == null) {
            fxmlUrl = MainApp.class.getResource("/com/buyo/adminfx/ui/MainView.fxml");
        }
        if (fxmlUrl == null) {
            String userDir = System.getProperty("user.dir");
            Path candidate1 = Paths.get(userDir, "buyo-adminfx", "src", "main", "resources", "com", "buyo", "adminfx", "ui", "MainView.fxml");
            Path candidate2 = Paths.get(userDir, "src", "main", "resources", "com", "buyo", "adminfx", "ui", "MainView.fxml");
            Path existing = Files.exists(candidate1) ? candidate1 : (Files.exists(candidate2) ? candidate2 : null);
            if (existing != null) {
                fxmlUrl = existing.toUri().toURL();
            }
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setTitle("Buyo AdminFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
