/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.ui;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author mejir
 */
public class ReferenceDataTab extends Tab {
    
    private Stage primaryStage;

    public ReferenceDataTab(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.initUI();
    }

    private void initUI() {
        this.setText("Справочные данные");
        GridPane grid = new GridPane();
        ColumnConstraints ccLeft = new ColumnConstraints();
        ccLeft.setPercentWidth(33);
        ColumnConstraints ccCenter = new ColumnConstraints();
        ccCenter.setPercentWidth(34);
        ColumnConstraints ccRight = new ColumnConstraints();
        ccRight.setPercentWidth(33);
        grid.getColumnConstraints().addAll(ccLeft, ccCenter, ccRight);
        RowConstraints rc1 = new RowConstraints();
        rc1.setPrefHeight(20);
        RowConstraints rc2 = new RowConstraints();
        //rc2.setPrefHeight(primaryStage.getHeight());
        rc2.setPercentHeight(95);
        System.out.println(rc2.getPrefHeight());
        grid.getRowConstraints().add(rc1);
        grid.getRowConstraints().add(rc2);

        String cssLayout = "-fx-border-color: blue;\n"
                + "-fx-border-insets: 5;\n"
                + "-fx-border-width: 3;\n";

        VBox leftContent = new VBox();
        leftContent.setStyle(cssLayout);
        SplitPane splitPane1 = new SplitPane(new Pane(), new Pane());
        splitPane1.setOrientation(Orientation.VERTICAL);
        splitPane1.prefHeightProperty().bind(leftContent.heightProperty());
        leftContent.getChildren().add(splitPane1);

        VBox centerContent = new VBox();
        centerContent.setStyle(cssLayout);

        VBox rightContent = new VBox();
        rightContent.setStyle(cssLayout);
        
        HBox leftHeader = new HBox();
        leftHeader.prefHeight(20);
        Text leftText = new Text("Виды обучения");
        leftText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        leftHeader.getChildren().add(leftText);
        
        HBox centerHeader = new HBox();
        centerHeader.prefHeight(20);
        Text centerText = new Text("Специальности");
        centerText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        centerHeader.getChildren().add(centerText);
        
        HBox rightHeader = new HBox();
        rightHeader.prefHeight(20);
        Text rightText = new Text("Организации");
        rightText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        rightHeader.getChildren().add(rightText);
        
        grid.add(leftHeader, 0, 0);
        grid.add(centerHeader, 1, 0);
        grid.add(rightHeader, 2, 0);
        grid.add(leftContent, 0, 1);
        grid.add(centerContent, 1, 1);
        grid.add(rightContent, 2, 1);
        setContent(grid);
    }
}
