/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.ui;

import com.ahid.kashkapay.entities.LearnType;
import com.ahid.kashkapay.entities.Organization;
import com.ahid.kashkapay.entities.Specialization;
import com.ahid.kashkapay.services.LearnTypeService;
import com.ahid.kashkapay.services.OrganizationService;
import com.ahid.kashkapay.services.SpecializationService;
import com.ahid.kashkapay.ui.models.LearnTypeModel;
import com.ahid.kashkapay.ui.models.OrganizationModel;
import com.ahid.kashkapay.ui.models.SpecializationModel;
import static com.ahid.kashkapay.utils.UIUtil.getCustomStyleForRootViews;
import static com.ahid.kashkapay.utils.UIUtil.showAlert;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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

    private TableView learnTypesTable = new TableView();
    private TableView specializationsTable = new TableView();
    private TableView organizationsTable = new TableView();

    private ContextMenu actionsContextMenu = new ContextMenu();
    
    // models for operate
    private LearnType learnType = null;
    private Specialization specialization = null;
    private Organization organization = null;

    public ReferenceDataTab(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.initUI();
    }

    private void initUI() {
        this.setText("Справочные данные");
        GridPane grid = new GridPane();
        ColumnConstraints ccLeft = new ColumnConstraints();
        ccLeft.setPercentWidth(20);
        ColumnConstraints ccCenter = new ColumnConstraints();
        ccCenter.setPercentWidth(40);
        ColumnConstraints ccRight = new ColumnConstraints();
        ccRight.setPercentWidth(40);
        grid.getColumnConstraints().addAll(ccLeft, ccCenter, ccRight);
        RowConstraints rc1 = new RowConstraints();
        rc1.setPrefHeight(20);
        RowConstraints rc2 = new RowConstraints();
        //rc2.setPrefHeight(primaryStage.getHeight());
        rc2.setPercentHeight(95);
        grid.getRowConstraints().add(rc1);
        grid.getRowConstraints().add(rc2);

        VBox leftContent = new VBox();
        leftContent.setStyle(getCustomStyleForRootViews());
        SplitPane leftSplitPane1 = new SplitPane(this.initAndGetLearnTypesTable(), new Pane());
        leftSplitPane1.setOrientation(Orientation.VERTICAL);
        leftSplitPane1.prefHeightProperty().bind(leftContent.heightProperty());
        leftContent.getChildren().add(leftSplitPane1);

        VBox centerContent = new VBox();
        centerContent.setStyle(getCustomStyleForRootViews());
        SplitPane centerSplitPane1 = new SplitPane(this.initAndGetSpecializationsTable(), new Pane());
        centerSplitPane1.setOrientation(Orientation.VERTICAL);
        centerSplitPane1.prefHeightProperty().bind(centerContent.heightProperty());
        centerContent.getChildren().add(centerSplitPane1);

        VBox rightContent = new VBox();
        rightContent.setStyle(getCustomStyleForRootViews());
        SplitPane rightSplitPane1 = new SplitPane(this.initAndGetOrganizationsTable(), new Pane());
        rightSplitPane1.setOrientation(Orientation.VERTICAL);
        rightSplitPane1.prefHeightProperty().bind(rightContent.heightProperty());
        rightContent.getChildren().add(rightSplitPane1);

        HBox leftHeader = new HBox();
        leftHeader.prefHeight(20);
        Text leftText = new Text("Виды обучения");
        leftText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        leftHeader.getChildren().add(leftText);
        leftHeader.setAlignment(Pos.CENTER);

        HBox centerHeader = new HBox();
        centerHeader.prefHeight(20);
        Text centerText = new Text("Специальности");
        centerText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        centerHeader.getChildren().add(centerText);
        centerHeader.setAlignment(Pos.CENTER);

        HBox rightHeader = new HBox();
        rightHeader.prefHeight(20);
        Text rightText = new Text("Организации");
        rightText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        rightHeader.getChildren().add(rightText);
        rightHeader.setAlignment(Pos.CENTER);

        grid.add(leftHeader, 0, 0);
        grid.add(centerHeader, 1, 0);
        grid.add(rightHeader, 2, 0);
        grid.add(leftContent, 0, 1);
        grid.add(centerContent, 1, 1);
        grid.add(rightContent, 2, 1);
        setContent(grid);

        this.createActionsContextMenu();
    }

    private TableView initAndGetLearnTypesTable() {
        TableColumn idCol = new TableColumn("Id");
        idCol.setMinWidth(100);
        idCol.setCellValueFactory(
                new PropertyValueFactory<LearnTypeModel, String>("id"));
        idCol.setVisible(false);

        TableColumn nameCol = new TableColumn("Наименование");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<LearnTypeModel, String>("name"));
        nameCol.prefWidthProperty().bind(this.learnTypesTable.widthProperty());
        nameCol.setResizable(false);

        this.fillLearnTypesTable();
        this.learnTypesTable.getColumns().addAll(idCol, nameCol);
        
        //this.addActionsContextMenuToTableView(this.learnTypesTable);
        this.learnTypesTable.setContextMenu(this.actionsContextMenu);

        return this.learnTypesTable;
    }

    private TableView initAndGetSpecializationsTable() {
        TableColumn idCol = new TableColumn("Id");
        idCol.setCellValueFactory(
                new PropertyValueFactory<SpecializationModel, String>("id"));
        idCol.setVisible(false);

        TableColumn nameCol = new TableColumn("Наименование");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<SpecializationModel, String>("name"));
        nameCol.prefWidthProperty().bind(this.specializationsTable.widthProperty());
        nameCol.setResizable(false);

        this.fillSpecializationsTable();
        this.specializationsTable.getColumns().addAll(idCol, nameCol);
        
        //this.addActionsContextMenuToTableView(this.specializationsTable);
        this.specializationsTable.setContextMenu(this.actionsContextMenu);
        
        return this.specializationsTable;
    }

    private TableView initAndGetOrganizationsTable() {
        TableColumn idCol = new TableColumn("Id");
        idCol.setCellValueFactory(
                new PropertyValueFactory<OrganizationModel, String>("id"));
        idCol.setVisible(false);

        TableColumn nameCol = new TableColumn("Наименование");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<OrganizationModel, String>("name"));
        nameCol.prefWidthProperty().bind(this.organizationsTable.widthProperty());
        nameCol.setResizable(false);

        this.fillOrganizationsTable();
        this.organizationsTable.getColumns().addAll(idCol, nameCol);
        
        //this.addActionsContextMenuToTableView(this.organizationsTable);
        this.organizationsTable.setContextMenu(this.actionsContextMenu);
        
        return this.organizationsTable;
    }

    private void fillLearnTypesTable() {
        this.learnTypesTable.setItems(FXCollections.observableArrayList(LearnTypeService.getAll()
                .stream().map(lt -> new LearnTypeModel(lt.getId(), lt.getName())).collect(Collectors.toList())));
    }

    private void fillSpecializationsTable() {
        this.specializationsTable.setItems(FXCollections.observableArrayList(SpecializationService.getAll()
                .stream().map(spec -> new SpecializationModel(spec.getId(), spec.getName())).collect(Collectors.toList())));
    }

    private void fillOrganizationsTable() {
        this.organizationsTable.setItems(FXCollections.observableArrayList(OrganizationService.getAll()
                .stream().map(org -> new OrganizationModel(org.getId(), org.getName())).collect(Collectors.toList())));
    }

    private void createActionsContextMenu() {
        MenuItem addItem = new MenuItem("Добавить");
        addItem.setOnAction(event -> this.addActionHandler(event));
        this.actionsContextMenu.getItems().add(addItem);
        MenuItem editItem = new MenuItem("Редактировать");
        editItem.setOnAction(event -> this.editActionHandler(event));
        this.actionsContextMenu.getItems().add(editItem);
        MenuItem removeItem = new MenuItem("Удалить");
        removeItem.setOnAction(event -> this.removeActionHandler(event));
        this.actionsContextMenu.getItems().add(removeItem);
    }

    private void addActionsContextMenuToTableView(TableView table) {
        table.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.SECONDARY) {
                    actionsContextMenu.show(table, t.getScreenX(), t.getScreenY());
                }
            }
        });
    }

    private void addActionHandler(ActionEvent event) {
        Object obj = event.getSource();
        showAlert(AlertType.INFORMATION, "This is information", "TEST");
    }

    private void editActionHandler(ActionEvent event) {
        showAlert(AlertType.INFORMATION, "This is information", "TEST");        
    }

    private void removeActionHandler(ActionEvent event) {
        showAlert(AlertType.INFORMATION, "This is information", "TEST");        
    }
}
