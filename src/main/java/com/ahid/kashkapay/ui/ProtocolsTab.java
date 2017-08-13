/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.ui;

import com.ahid.kashkapay.entities.LearnType;
import com.ahid.kashkapay.entities.Organization;
import com.ahid.kashkapay.entities.Protocol;
import com.ahid.kashkapay.entities.Specialization;
import com.ahid.kashkapay.exceptions.DuplicateException;
import com.ahid.kashkapay.exceptions.ReferencesExistsException;
import com.ahid.kashkapay.services.LearnTypeService;
import com.ahid.kashkapay.services.OrganizationService;
import com.ahid.kashkapay.services.ProtocolService;
import com.ahid.kashkapay.services.SpecializationService;
import com.ahid.kashkapay.ui.models.ProtocolModel;
import static com.ahid.kashkapay.utils.UIUtil.getCustomStyleForRootViews;
import static com.ahid.kashkapay.utils.UIUtil.getMenuItemAddIcon;
import static com.ahid.kashkapay.utils.UIUtil.getMenuItemDeleteIcon;
import static com.ahid.kashkapay.utils.UIUtil.getMenuItemEditIcon;
import static com.ahid.kashkapay.utils.UIUtil.showConfirmation;
import static com.ahid.kashkapay.utils.UIUtil.showError;
import static com.ahid.kashkapay.utils.UIUtil.showInfo;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
import static com.ahid.kashkapay.utils.CommonUtil.getCommonDateFormat;

/**
 *
 * @author mejir
 */
public class ProtocolsTab extends Tab {

    private Protocol protocolForOperate;
    private ObservableList<LearnType> learnTypes;
    private ObservableList<Organization> organizations;
    private ObservableList<Specialization> specializations;

    private final Stage primaryStage;
    private final TableView protocolsTable = new TableView();
    private VBox editorView;
    private Button saveBtn;
    private Button cancelBtn;
    private TextField tfProtocolNumber;
    private TextField tfProtocolOwner;
    private ComboBox cbOrganization;
    private ComboBox cbLearnType;
    private ComboBox cbSpecialization;
    private DatePicker dpProtocolDate;

    public ProtocolsTab(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.initUI();
    }

    private void initUI() {
        this.setText("Протокола");

        this.initDSsForComboItems();

        SplitPane content = new SplitPane();
        content.setStyle(getCustomStyleForRootViews());
        content.setOrientation(Orientation.HORIZONTAL);
        content.getItems().addAll(this.initAndGetViewContent(), this.initAndGetEditContent());

        new Thread(() -> {
            Platform.runLater(() -> {
                content.setDividerPositions(0.75f, 0.25f);
            });
        }).start();

        this.setContent(content);
    }

    private SplitPane initAndGetViewContent() {
        SplitPane viewContent = new SplitPane(this.initAndGetFilterContent(), this.initAndGetTableContent());
        viewContent.setOrientation(Orientation.VERTICAL);
        new Thread(() -> {
            Platform.runLater(() -> {
                viewContent.setDividerPositions(0.20f, 0.80f);
            });
        }).start();
        return viewContent;
    }

    private Pane initAndGetEditContent() {
        editorView = new VBox();
        editorView.setPadding(new Insets(10, 10, 10, 10));
        editorView.setSpacing(10);

        this.tfProtocolNumber = new TextField();
        this.tfProtocolNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if ((newValue == null || !newValue.equals(oldValue)) && protocolForOperate != null) {
                protocolForOperate.setProtocolNumber(newValue);
                validate();
            }
        });

        this.tfProtocolOwner = new TextField();
        this.tfProtocolOwner.textProperty().addListener((observable, oldValue, newValue) -> {
            if ((newValue == null || !newValue.equals(oldValue)) && protocolForOperate != null) {
                protocolForOperate.setProtocolOwner(newValue);
                validate();
            }
        });

        this.cbOrganization = new ComboBox();
        this.cbOrganization.setConverter(new StringConverter<Organization>() {

            @Override
            public String toString(Organization object) {
                return object.getName();
            }

            @Override
            public Organization fromString(String string) {
                return organizations.stream().filter((Organization item) -> item.getName().equals(string)).findFirst().orElse(null);
            }
        });
        this.cbOrganization.valueProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
            if ((newValue == null || !newValue.equals(oldValue)) && protocolForOperate != null) {
                protocolForOperate.setOrganization((Organization) newValue);
                validate();
            }
        });

        this.cbLearnType = new ComboBox();
        this.cbLearnType.setConverter(new StringConverter<LearnType>() {

            @Override
            public String toString(LearnType object) {
                return object.getName();
            }

            @Override
            public LearnType fromString(String string) {
                return learnTypes.stream().filter(item -> item.getName().equals(string)).findFirst().orElse(null);
            }
        });
        this.cbLearnType.valueProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
            if ((newValue == null || !newValue.equals(oldValue)) && protocolForOperate != null) {
                protocolForOperate.setLearnType((LearnType) newValue);
                validate();
            }
        });

        this.cbSpecialization = new ComboBox();
        this.cbSpecialization.setConverter(new StringConverter<Specialization>() {

            @Override
            public String toString(Specialization object) {
                return object.getName();
            }

            @Override
            public Specialization fromString(String string) {
                return specializations.stream().filter(item -> item.getName().equals(string)).findFirst().orElse(null);
            }
        });
        this.cbSpecialization.valueProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
            if ((newValue == null || !newValue.equals(oldValue)) && protocolForOperate != null) {
                protocolForOperate.setSpecialization((Specialization) newValue);
                validate();
            }
        });

        this.dpProtocolDate = new DatePicker();
        this.dpProtocolDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ((newValue == null || !newValue.equals(oldValue)) && protocolForOperate != null) {
                protocolForOperate.setProtocolDate(newValue.format(DateTimeFormatter.ofPattern(getCommonDateFormat())));
                validate();
            }
        });

        this.saveBtn = new Button("Сохранить");
        this.saveBtn.setOnAction(event -> {
            try {
                if (showConfirmation("Вы подтверждаете сохранение протокола?").get() == ButtonType.OK) {
                    ProtocolService.save(protocolForOperate);
                    setInactiveEditorView();
                    fillProtocolsTable();
                    showInfo("Протокол успешно сохранен");
                }
            } catch (DuplicateException de) {
                showError(de.getMessage());
            }
        });

        this.cancelBtn = new Button("Отменить");
        this.cancelBtn.setOnAction(event -> {
            setInactiveEditorView();
        });

        HBox hbPtotocolNumber = new HBox();
        hbPtotocolNumber.setSpacing(10);
        Label lbPtotocolNumber = new Label("Номер протокола");
        lbPtotocolNumber.setPrefWidth(150);
        hbPtotocolNumber.getChildren().addAll(lbPtotocolNumber, this.tfProtocolNumber);
        this.tfProtocolNumber.prefWidthProperty().bind(hbPtotocolNumber.widthProperty().subtract(lbPtotocolNumber.widthProperty()));

        HBox hbProtocolOwner = new HBox();
        hbProtocolOwner.setSpacing(10);
        Label lbProtocolOwner = new Label("ФИО");
        lbProtocolOwner.setPrefWidth(150);
        hbProtocolOwner.getChildren().addAll(lbProtocolOwner, this.tfProtocolOwner);
        this.tfProtocolOwner.prefWidthProperty().bind(hbProtocolOwner.widthProperty().subtract(lbProtocolOwner.widthProperty()));

        HBox hbOrganization = new HBox();
        hbOrganization.setSpacing(10);
        Label lbOrganization = new Label("Цех (орг.)");
        lbOrganization.setPrefWidth(150);
        hbOrganization.getChildren().addAll(lbOrganization, this.cbOrganization);
        this.cbOrganization.prefWidthProperty().bind(hbOrganization.widthProperty().subtract(lbOrganization.widthProperty()));

        HBox hbLearnType = new HBox();
        hbLearnType.setSpacing(10);
        Label lbLearnType = new Label("Вид обучения");
        lbLearnType.setPrefWidth(150);
        hbLearnType.getChildren().addAll(lbLearnType, this.cbLearnType);
        this.cbLearnType.prefWidthProperty().bind(hbLearnType.widthProperty().subtract(lbLearnType.widthProperty()));

        HBox hbSpecialization = new HBox();
        hbSpecialization.setSpacing(10);
        Label lbSpecialization = new Label("Профессия");
        lbSpecialization.setPrefWidth(150);
        hbSpecialization.getChildren().addAll(lbSpecialization, this.cbSpecialization);
        this.cbSpecialization.prefWidthProperty().bind(hbSpecialization.widthProperty().subtract(lbSpecialization.widthProperty()));

        HBox hbProtocolDate = new HBox();
        hbProtocolDate.setSpacing(10);
        Label lbProtocolDate = new Label("Дата протокола");
        lbProtocolDate.setPrefWidth(150);
        hbProtocolDate.getChildren().addAll(lbProtocolDate, this.dpProtocolDate);
        this.dpProtocolDate.prefWidthProperty().bind(hbProtocolDate.widthProperty().subtract(lbProtocolDate.widthProperty()));

        HBox buttons = new HBox();
        buttons.setSpacing(10);
        buttons.getChildren().addAll(this.saveBtn, this.cancelBtn);

        editorView.getChildren().addAll(hbPtotocolNumber, hbProtocolOwner, hbOrganization,
                hbLearnType, hbSpecialization, hbProtocolDate, buttons);
        this.setInactiveEditorView();
        return editorView;
    }

    private Pane initAndGetFilterContent() {
        return new HBox();
    }

    private TableView initAndGetTableContent() {
        TableColumn idCol = new TableColumn("Id");
        idCol.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("id"));
        idCol.setVisible(false);

        TableColumn protocolNumber = new TableColumn("Номер протокола");
        protocolNumber.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("protocolNumber"));
        protocolNumber.setResizable(true);

        TableColumn protocolOwner = new TableColumn("ФИО");
        protocolOwner.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("protocolOwner"));
        protocolOwner.setResizable(true);

        TableColumn orgId = new TableColumn("org");
        orgId.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("orgId"));
        orgId.setVisible(false);

        TableColumn orgName = new TableColumn("Цех (организация)");
        orgName.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("orgName"));
        orgName.setResizable(true);

        TableColumn learnTypeId = new TableColumn("learnType");
        learnTypeId.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("learnTypeId"));
        learnTypeId.setVisible(false);

        TableColumn learnTypeName = new TableColumn("Вид обучения");
        learnTypeName.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("learnTypeName"));
        learnTypeName.setResizable(true);

        TableColumn specId = new TableColumn("spec");
        specId.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("specId"));
        specId.setVisible(false);

        TableColumn specName = new TableColumn("Профессия (специальность)");
        specName.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("specName"));
        specName.setResizable(true);

        TableColumn protocolDate = new TableColumn("Дата протокола");
        protocolDate.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("protocolDate"));
        protocolDate.setResizable(true);

        this.protocolsTable.getColumns().addAll(idCol, protocolNumber, protocolOwner, orgId, orgName,
                learnTypeId, learnTypeName, specId, specName, protocolDate);

        this.protocolsTable.setContextMenu(this.createContextMenu());

        //this.protocolsTable.setColumnResizePolicy((param) -> true);
        this.fillProtocolsTable();
        return this.protocolsTable;
    }

    void fillProtocolsTable() {
        this.protocolsTable.setItems(FXCollections.observableArrayList(ProtocolService.getAll()
                .stream().map(protocol -> ProtocolModel.fromProtocol(protocol)).collect(Collectors.toList())));
    }

    private ContextMenu createContextMenu() {
        ContextMenu cm = new ContextMenu();
        MenuItem addItem = new MenuItem("Добавить");
        addItem.setGraphic(new ImageView(getMenuItemAddIcon()));
        addItem.setOnAction(event -> {
            this.protocolForOperate = new Protocol();
            this.setActiveEditorView();
        });
        cm.getItems().add(addItem);
        MenuItem editItem = new MenuItem("Редактировать");
        editItem.setGraphic(new ImageView(getMenuItemEditIcon()));
        editItem.setOnAction(event -> {
            ProtocolModel model = (ProtocolModel) this.protocolsTable.getSelectionModel().getSelectedItem();
            this.protocolForOperate = model.toProtocol();
            this.setActiveEditorView();
        });
        cm.getItems().add(editItem);
        MenuItem removeItem = new MenuItem("Удалить");
        removeItem.setGraphic(new ImageView(getMenuItemDeleteIcon()));
        removeItem.setOnAction(event -> {
            try {
                if (showConfirmation("Вы подтверждаете удаление протокола?").get() == ButtonType.OK) {
                    ProtocolModel model = (ProtocolModel) this.protocolsTable.getSelectionModel().getSelectedItem();
                    ProtocolService.delete(model.toProtocol());
                    protocolsTable.getItems().remove(model);
                    showInfo("Протокол успешно удален");
                }
            } catch (ReferencesExistsException ex) {
                showError(ex.getMessage());
            }
        });
        cm.getItems().add(removeItem);
        return cm;
    }

    public void setInactiveEditorView() {
        this.editorView.setDisable(true);
        this.saveBtn.setDisable(true);
        this.protocolForOperate = null;
        this.tfProtocolNumber.setText(null);
        this.tfProtocolOwner.setText(null);
        this.cbOrganization.setValue(null);
        this.cbLearnType.setValue(null);
        this.cbSpecialization.setValue(null);
        this.dpProtocolDate.setValue(null);
    }

    private void setActiveEditorView() {
        this.editorView.setDisable(false);
        this.tfProtocolNumber.setText(this.protocolForOperate.getProtocolNumber());
        this.tfProtocolOwner.setText(this.protocolForOperate.getProtocolOwner());
        this.cbOrganization.setItems(this.organizations);
        this.cbOrganization.setValue(this.protocolForOperate.getOrganization());
        this.cbLearnType.setItems(this.learnTypes);
        this.cbLearnType.setValue(this.protocolForOperate.getLearnType());
        this.cbSpecialization.setItems(this.specializations);
        this.cbSpecialization.setValue(this.protocolForOperate.getSpecialization());
        if (this.protocolForOperate.getProtocolDate() != null) {
            this.dpProtocolDate.setValue(LocalDate.parse(this.protocolForOperate.getProtocolDate(), DateTimeFormatter.ofPattern(getCommonDateFormat())));
        }
    }

    private void initDSsForComboItems() {
        this.refreshLearnTypes();
        this.refreshOrganizations();
        this.refreshSpecializations();
    }

    void refreshLearnTypes() {
        new Thread(() -> {
            Platform.runLater(() -> {
                learnTypes = FXCollections.observableArrayList(LearnTypeService.getAll());
                cbLearnType.getItems().clear();
                cbLearnType.setItems(this.learnTypes);
            });
        }).start();
    }

    void refreshOrganizations() {
        new Thread(() -> {
            Platform.runLater(() -> {
                organizations = FXCollections.observableArrayList(OrganizationService.getAll());
                cbOrganization.getItems().clear();
                cbOrganization.setItems(this.organizations);
            });
        }).start();
    }

    void refreshSpecializations() {
        new Thread(() -> {
            Platform.runLater(() -> {
                specializations = FXCollections.observableArrayList(SpecializationService.getAll());
                cbSpecialization.getItems().clear();
                cbSpecialization.setItems(this.specializations);
            });
        }).start();
    }

    private void validate() {
        if (this.protocolForOperate.getLearnType() != null && this.protocolForOperate.getLearnType().getId() != null
                && this.protocolForOperate.getOrganization() != null && this.protocolForOperate.getOrganization().getId() != null
                && this.protocolForOperate.getSpecialization()!= null && this.protocolForOperate.getSpecialization().getId() != null
                && this.protocolForOperate.getProtocolNumber() != null && !"".equals(this.protocolForOperate.getProtocolNumber()) 
                && this.protocolForOperate.getProtocolOwner()!= null && !"".equals(this.protocolForOperate.getProtocolOwner()) 
                && this.protocolForOperate.getProtocolDate()!= null && !"".equals(this.protocolForOperate.getProtocolDate())) {
            this.saveBtn.setDisable(false);
        }
    }
}
