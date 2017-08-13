/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.ui;

import com.ahid.kashkapay.entities.LearnType;
import com.ahid.kashkapay.entities.Organization;
import com.ahid.kashkapay.entities.Specialization;
import com.ahid.kashkapay.exceptions.DuplicateException;
import com.ahid.kashkapay.exceptions.ReferencesExistsException;
import com.ahid.kashkapay.services.LearnTypeService;
import com.ahid.kashkapay.services.OrganizationService;
import com.ahid.kashkapay.services.SpecializationService;
import com.ahid.kashkapay.ui.models.LearnTypeModel;
import com.ahid.kashkapay.ui.models.OrganizationModel;
import com.ahid.kashkapay.ui.models.SpecializationModel;
import static com.ahid.kashkapay.utils.UIUtil.getCustomStyleForRootViews;
import static com.ahid.kashkapay.utils.UIUtil.getMenuItemAddIcon;
import static com.ahid.kashkapay.utils.UIUtil.getMenuItemDeleteIcon;
import static com.ahid.kashkapay.utils.UIUtil.getMenuItemEditIcon;
import static com.ahid.kashkapay.utils.UIUtil.showConfirmation;
import static com.ahid.kashkapay.utils.UIUtil.showError;
import static com.ahid.kashkapay.utils.UIUtil.showInfo;
import static com.ahid.kashkapay.utils.UIUtil.showWarning;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
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

    private final Stage primaryStage;
    private final ProtocolsTab protocolsView;

    private final TableView learnTypesTable = new TableView();
    private final TableView specializationsTable = new TableView();
    private final TableView organizationsTable = new TableView();

    private TextField learnTypeNameTextEdit;
    private TextField specializationNameTextEdit;
    private TextField organizationNameTextEdit;
    private Button learnTypeSaveButton;
    private Button specializationSaveButton;
    private Button organizationSaveButton;
    private Button learnTypeCancelButton;
    private Button specializationCancelButton;
    private Button organizationCancelButton;

    // models for operate
    private LearnType learnType = null;
    private Specialization specialization = null;
    private Organization organization = null;

    public ReferenceDataTab(Stage primaryStage, ProtocolsTab protocolsView) {
        this.primaryStage = primaryStage;
        this.protocolsView = protocolsView;
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
        SplitPane leftSplitPane1 = new SplitPane(this.initAndGetLearnTypesTable(), this.getLearnTypesEditorPane());
        leftSplitPane1.setOrientation(Orientation.VERTICAL);
        new Thread(() -> {
            Platform.runLater(() -> {
                leftSplitPane1.setDividerPositions(0.7f, 0.3f);
            });
        }).start();
        leftSplitPane1.prefHeightProperty().bind(leftContent.heightProperty());
        leftContent.getChildren().add(leftSplitPane1);

        VBox centerContent = new VBox();
        centerContent.setStyle(getCustomStyleForRootViews());
        SplitPane centerSplitPane1 = new SplitPane(this.initAndGetSpecializationsTable(), this.getSpecializationEditorPane());
        centerSplitPane1.setOrientation(Orientation.VERTICAL);
        new Thread(() -> {
            Platform.runLater(() -> {
                centerSplitPane1.setDividerPositions(0.7f, 0.3f);
            });
        }).start();
        centerSplitPane1.prefHeightProperty().bind(centerContent.heightProperty());
        centerContent.getChildren().add(centerSplitPane1);

        VBox rightContent = new VBox();
        rightContent.setStyle(getCustomStyleForRootViews());
        SplitPane rightSplitPane1 = new SplitPane(this.initAndGetOrganizationsTable(), this.getOrganizatonEditorPane());
        rightSplitPane1.setOrientation(Orientation.VERTICAL);
        new Thread(() -> {
            Platform.runLater(() -> {
                rightSplitPane1.setDividerPositions(0.7f, 0.3f);
            });
        }).start();
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
        this.learnTypesTable.setContextMenu(this.createLearnTypeContextMenu());

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
        this.specializationsTable.setContextMenu(this.createSpecializationContextMenu());

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
        this.organizationsTable.setContextMenu(this.createOrganizationContextMenu());

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

    private Pane getLearnTypesEditorPane() {
        learnTypeNameTextEdit = new TextField();
        learnTypeNameTextEdit.setPromptText("Введите наименование");
        learnTypeSaveButton = new Button("Сохранить");
        learnTypeSaveButton.setOnAction(event -> {
            try {
                if (learnType.getName().equals(learnTypeNameTextEdit.getText().trim())) {
                    showWarning("Изменения не обнаружены");
                    return;
                }

                if (showConfirmation("Вы подтверждаете сохранение вида обучения?").get() == ButtonType.OK) {
                    learnType.setName(learnTypeNameTextEdit.getText().trim());
                    String id = learnType.getId();
                    LearnTypeService.save(learnType);
                    cancelLearnTypeSave();
                    fillLearnTypesTable();
                    showInfo("Вид обучения успешно сохранен");
                    
                    protocolsView.refreshLearnTypes();
                    protocolsView.fillProtocolsTable();
                }
            } catch (DuplicateException de) {
                showError(de.getMessage());
            }
        });
        learnTypeCancelButton = new Button("Отменить");
        learnTypeCancelButton.setOnAction(event -> {
            cancelLearnTypeSave();
        });
        HBox hb = new HBox();
        hb.setSpacing(10);
        hb.getChildren().addAll(learnTypeSaveButton, learnTypeCancelButton);
        VBox vb = new VBox();
        vb.setPadding(new Insets(10, 10, 10, 10));
        vb.getChildren().addAll(learnTypeNameTextEdit, hb);
        vb.setSpacing(10);

        this.setInactiveLearnTypeEditors();
        return vb;
    }

    private Pane getSpecializationEditorPane() {
        specializationNameTextEdit = new TextField();
        specializationNameTextEdit.setPromptText("Введите наименование");
        specializationSaveButton = new Button("Сохранить");
        specializationSaveButton.setOnAction(event -> {
            try {
                if (specialization.getName().equals(specializationNameTextEdit.getText().trim())) {
                    showWarning("Изменения не обнаружены");
                    return;
                }

                if (showConfirmation("Вы подтверждаете сохранение специальности?").get() == ButtonType.OK) {
                    specialization.setName(specializationNameTextEdit.getText().trim());
                    String id = specialization.getId();
                    SpecializationService.save(specialization);
                    cancelSpecializationSave();
                    fillSpecializationsTable();
                    showInfo("Специальность успешно сохранена");
                    
                    protocolsView.refreshSpecializations();
                    protocolsView.fillProtocolsTable();
                }
            } catch (DuplicateException de) {
                showError(de.getMessage());
            }
        });
        specializationCancelButton = new Button("Отменить");
        specializationCancelButton.setOnAction(event -> {
            cancelSpecializationSave();
        });
        HBox hb = new HBox();
        hb.setSpacing(10);
        hb.getChildren().addAll(specializationSaveButton, specializationCancelButton);
        VBox vb = new VBox();
        vb.setPadding(new Insets(10, 10, 10, 10));
        vb.getChildren().addAll(specializationNameTextEdit, hb);
        vb.setSpacing(10);

        this.setInactiveSpecializationEditors();
        return vb;
    }

    private Pane getOrganizatonEditorPane() {
        organizationNameTextEdit = new TextField();
        organizationNameTextEdit.setPromptText("Введите наименование");
        organizationSaveButton = new Button("Сохранить");
        organizationSaveButton.setOnAction(event -> {
            try {
                if (organization.getName().equals(organizationNameTextEdit.getText().trim())) {
                    showWarning("Изменения не обнаружены");
                    return;
                }

                if (showConfirmation("Вы подтверждаете сохранение организации?").get() == ButtonType.OK) {
                    organization.setName(organizationNameTextEdit.getText().trim());
                    String id = organization.getId();
                    OrganizationService.save(organization);
                    cancelOrganizationSave();
                    fillOrganizationsTable();
                    showInfo("Организация успешно сохранена");
                    
                    protocolsView.refreshOrganizations();
                    protocolsView.fillProtocolsTable();
                }
            } catch (DuplicateException de) {
                showError(de.getMessage());
            }
        });
        organizationCancelButton = new Button("Отменить");
        organizationCancelButton.setOnAction(event -> {
            cancelOrganizationSave();
        });
        HBox hb = new HBox();
        hb.setSpacing(10);
        hb.getChildren().addAll(organizationSaveButton, organizationCancelButton);
        VBox vb = new VBox();
        vb.setPadding(new Insets(10, 10, 10, 10));
        vb.getChildren().addAll(organizationNameTextEdit, hb);
        vb.setSpacing(10);

        this.setInactiveOrganizationEditors();
        return vb;
    }

    private void setInactiveLearnTypeEditors() {
        learnTypeNameTextEdit.setDisable(true);
        learnTypeSaveButton.setDisable(true);
        learnTypeCancelButton.setDisable(true);
    }

    private void setActiveLearnTypeEditors() {
        learnTypeNameTextEdit.setDisable(false);
        learnTypeSaveButton.setDisable(false);
        learnTypeCancelButton.setDisable(false);
    }

    private void setInactiveSpecializationEditors() {
        specializationNameTextEdit.setDisable(true);
        specializationSaveButton.setDisable(true);
        specializationCancelButton.setDisable(true);
    }

    private void setActiveSpecializationEditors() {
        specializationNameTextEdit.setDisable(false);
        specializationSaveButton.setDisable(false);
        specializationCancelButton.setDisable(false);
    }

    private void setInactiveOrganizationEditors() {
        organizationNameTextEdit.setDisable(true);
        organizationSaveButton.setDisable(true);
        organizationCancelButton.setDisable(true);
    }

    private void setActiveOrganizationEditors() {
        organizationNameTextEdit.setDisable(false);
        organizationSaveButton.setDisable(false);
        organizationCancelButton.setDisable(false);
    }

    private ContextMenu createLearnTypeContextMenu() {
        ContextMenu cm = new ContextMenu();
        MenuItem addItem = new MenuItem("Добавить");
        addItem.setGraphic(new ImageView(getMenuItemAddIcon()));
        addItem.setOnAction(event -> {
            this.learnType = new LearnType();
            this.learnType.setName("");
            this.setActiveLearnTypeEditors();
        });
        cm.getItems().add(addItem);
        MenuItem editItem = new MenuItem("Редактировать");
        editItem.setGraphic(new ImageView(getMenuItemEditIcon()));
        editItem.setOnAction(event -> {
            LearnTypeModel model = (LearnTypeModel) this.learnTypesTable.getSelectionModel().getSelectedItem();
            this.learnType = model.toLearnType();
            this.learnTypeNameTextEdit.setText(this.learnType.getName());
            this.setActiveLearnTypeEditors();
        });
        cm.getItems().add(editItem);
        MenuItem removeItem = new MenuItem("Удалить");
        removeItem.setGraphic(new ImageView(getMenuItemDeleteIcon()));
        removeItem.setOnAction(event -> {
            try {
                cancelLearnTypeSave();
                if (showConfirmation("Вы подтверждаете удаление вида обучения?").get() == ButtonType.OK) {
                    LearnTypeModel ltm = (LearnTypeModel) this.learnTypesTable.getSelectionModel().getSelectedItem();
                    LearnTypeService.delete(ltm.toLearnType());
                    learnTypesTable.getItems().remove(ltm);
                    showInfo("Вид обучения успешно удален");
                    protocolsView.refreshLearnTypes();
                }
            } catch (ReferencesExistsException ex) {
                showError(ex.getMessage());
            }
        });
        cm.getItems().add(removeItem);
        return cm;
    }

    private ContextMenu createSpecializationContextMenu() {
        ContextMenu cm = new ContextMenu();
        MenuItem addItem = new MenuItem("Добавить");
        addItem.setGraphic(new ImageView(getMenuItemAddIcon()));
        addItem.setOnAction(event -> {
            this.specialization = new Specialization();
            this.specialization.setName("");
            this.setActiveSpecializationEditors();
        });
        cm.getItems().add(addItem);
        MenuItem editItem = new MenuItem("Редактировать");
        editItem.setGraphic(new ImageView(getMenuItemEditIcon()));
        editItem.setOnAction(event -> {
            SpecializationModel model = (SpecializationModel) this.specializationsTable.getSelectionModel().getSelectedItem();
            this.specialization = model.toSpecialization();
            this.specializationNameTextEdit.setText(this.specialization.getName());
            this.setActiveSpecializationEditors();
        });
        cm.getItems().add(editItem);
        MenuItem removeItem = new MenuItem("Удалить");
        removeItem.setGraphic(new ImageView(getMenuItemDeleteIcon()));
        removeItem.setOnAction(event -> {
            cancelSpecializationSave();
            try {
                if (showConfirmation("Вы подтверждаете удаление специальности?").get() == ButtonType.OK) {
                    SpecializationModel model = (SpecializationModel) this.specializationsTable.getSelectionModel().getSelectedItem();
                    SpecializationService.delete(model.toSpecialization());
                    specializationsTable.getItems().remove(model);
                    showInfo("Специальность успешно удалена");
                    protocolsView.refreshSpecializations();
                }
            } catch (ReferencesExistsException ex) {
                showError(ex.getMessage());
            }
        });
        cm.getItems().add(removeItem);
        return cm;
    }

    private ContextMenu createOrganizationContextMenu() {
        ContextMenu cm = new ContextMenu();
        MenuItem addItem = new MenuItem("Добавить");
        addItem.setGraphic(new ImageView(getMenuItemAddIcon()));
        addItem.setOnAction(event -> {
            this.organization = new Organization();
            this.organization.setName("");
            this.setActiveOrganizationEditors();
        });
        cm.getItems().add(addItem);
        MenuItem editItem = new MenuItem("Редактировать");
        editItem.setGraphic(new ImageView(getMenuItemEditIcon()));
        editItem.setOnAction(event -> {
            OrganizationModel model = (OrganizationModel) this.organizationsTable.getSelectionModel().getSelectedItem();
            this.organization = model.toOrganization();
            this.organizationNameTextEdit.setText(this.organization.getName());
            this.setActiveOrganizationEditors();
        });
        cm.getItems().add(editItem);
        MenuItem removeItem = new MenuItem("Удалить");
        removeItem.setGraphic(new ImageView(getMenuItemDeleteIcon()));
        removeItem.setOnAction(event -> {
            try {
                cancelOrganizationSave();
                if (showConfirmation("Вы подтверждаете удаление организации?").get() == ButtonType.OK) {
                    OrganizationModel model = (OrganizationModel) this.organizationsTable.getSelectionModel().getSelectedItem();
                    OrganizationService.delete(model.toOrganization());
                    organizationsTable.getItems().remove(model);
                    showInfo("Организация успешно удалена");
                    protocolsView.refreshOrganizations();
                }
            } catch (ReferencesExistsException ex) {
                showError(ex.getMessage());
            }
        });
        cm.getItems().add(removeItem);
        return cm;
    }

    private void cancelLearnTypeSave() {
        this.learnType = null;
        this.learnTypeNameTextEdit.clear();
        this.setInactiveLearnTypeEditors();
    }

    private void cancelSpecializationSave() {
        this.specialization = null;
        this.specializationNameTextEdit.clear();
        this.setInactiveSpecializationEditors();
    }

    private void cancelOrganizationSave() {
        this.organization = null;
        this.organizationNameTextEdit.clear();
        this.setInactiveOrganizationEditors();
    }
}
