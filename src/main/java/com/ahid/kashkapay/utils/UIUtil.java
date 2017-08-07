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

    public static void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Диалог пожтверждения");
        alert.setHeaderText(null);
        alert.setContentText(content);

        return alert.showAndWait();
    }
}
