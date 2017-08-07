/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.utils;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author cccc
 */
public final class UIUtil {

    public static String getCustomStyleForRootViews() {
        return "-fx-border-color: blue;\n"
                + "-fx-border-insets: 5;\n"
                + "-fx-border-width: 3;\n";
    }

    public static Optional<ButtonType> showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle("");
        alert.setHeaderText(title);
        alert.getDialogPane().setContentText(content);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(getMainIcon());
        return alert.showAndWait();
    }

    public static void showError(String content) {
        showAlert(AlertType.ERROR, "Ошибка", content);
    }

    public static void showWarning(String content) {
        showAlert(AlertType.WARNING, "Предупреждение", content);
    }

    public static void showInfo(String content) {
        showAlert(AlertType.INFORMATION, "Информация", content);
    }

    public static Optional<ButtonType> showConfirmation(String content) {
        return showAlert(AlertType.CONFIRMATION, "Диалог подтверждения", content);
    }
    
    public static Image getIconByName(String name) {
        return new Image("/icons/" + name);
    }
    
    public static Image getMainIcon() {
        return getIconByName("logo.png");
    }
    
    public static Image getMenuItemEditIcon() {
        return getIconByName("edit.png");
    }
    
    public static Image getMenuItemAddIcon() {
        return getIconByName("add.png");
    }
    
    public static Image getMenuItemDeleteIcon() {
        return getIconByName("delete.png");
    }
}
