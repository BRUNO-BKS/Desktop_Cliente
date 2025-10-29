package com.adminapp.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.CheckBox;
import javafx.scene.Scene;

public class MainController {
    @FXML private TabPane tabPane;
    @FXML private CheckBox darkToggle;

    @FXML
    private void toggleTheme() {
        Scene scene = tabPane.getScene();
        if (scene == null) return;
        String dark = getClass().getResource("/com/adminapp/ui/dark.css").toExternalForm();
        if (darkToggle.isSelected()) {
            if (!scene.getStylesheets().contains(dark)) scene.getStylesheets().add(dark);
        } else {
            scene.getStylesheets().remove(dark);
        }
    }
}
