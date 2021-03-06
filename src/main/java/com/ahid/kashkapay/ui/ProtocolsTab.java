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
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Callback;
import static com.ahid.kashkapay.utils.CommonUtil.getDBDateFormat;
import static com.ahid.kashkapay.utils.CommonUtil.getPdfFontPath;
import static com.ahid.kashkapay.utils.CommonUtil.getUIDateFormat;
import static com.ahid.kashkapay.utils.UIUtil.getMenuItemPdfIcon;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

/**
 *
 * @author mejir
 */
public class ProtocolsTab extends Tab {

    private Protocol protocolForOperate;

    private Map<String, String> filters;

    private ObservableList<LearnType> learnTypes;
    private ObservableList<Organization> organizations;
    private ObservableList<Specialization> specializations;

    private final Stage primaryStage;
    private final CertificatesTab certificatesView;
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

    private Button filterBtn;
    private Button resetFiltersBtn;
    private TextField tfCurrentYear;
    private TextField tfFilterProtocolNumber;
    private TextField tfFilterProtocolOwner;
    private ComboBox cbFilterLearnType;
    private ComboBox cbFilterOrganization;
    private ComboBox cbFilterSpecialization;

    public ProtocolsTab(Stage primaryStage, CertificatesTab certificatesTab) {
        this.primaryStage = primaryStage;
        this.filters = new HashMap<>();
        this.certificatesView = certificatesTab;
        this.initUI();
    }

    private void initUI() {
        this.setText("Протокола");

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
        this.initDSsForComboItems();
    }

    private SplitPane initAndGetViewContent() {
        SplitPane viewContent = new SplitPane(this.initAndGetFilterContent(), this.initAndGetTableContent());
        viewContent.setOrientation(Orientation.VERTICAL);
        new Thread(() -> {
            Platform.runLater(() -> {
                viewContent.setDividerPositions(0.1f, 0.9f);
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
                if (!newValue.matches("\\d+")) {
                    protocolForOperate.setProtocolNumber(Integer.valueOf(oldValue));
                    this.tfProtocolNumber.setText(oldValue);
                } else {
                    protocolForOperate.setProtocolNumber(Integer.valueOf(newValue));
                    this.tfProtocolNumber.setText(newValue);
                }
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
                protocolForOperate.setProtocolDate(newValue.format(DateTimeFormatter.ofPattern(getDBDateFormat())));
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

                    certificatesView.refreshProtocols();
                    certificatesView.fillCertificatesTable();
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
        HBox filtersHb = new HBox();
        filtersHb.setPadding(new Insets(10, 10, 10, 10));
        filtersHb.setSpacing(10);

        String currentYear = String.valueOf(LocalDate.now().getYear());
        this.filters.put("current_year", currentYear);
        this.tfCurrentYear = new TextField(currentYear);
        this.tfCurrentYear.setTooltip(new Tooltip("Для смены года нажмите дважды. Потом для завершения нажмите Enter"));
        this.tfCurrentYear.setMinWidth(45);
        this.tfCurrentYear.setMaxWidth(45);
        this.disableCurrentYear();
        this.tfCurrentYear.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        tfCurrentYear.setEditable(true);
                    }
                }
            }
        });
        this.tfCurrentYear.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    disableCurrentYear();
                    filters.put("current_year", tfCurrentYear.getText());
                    fillProtocolsTable();
                }
            }
        });
        this.tfCurrentYear.setStyle("-fx-background-color: #48D1CC; -fx-margin: 0 0 0 100");

        this.tfFilterProtocolNumber = new TextField();
        this.tfFilterProtocolNumber.setPromptText("Номер протокола");
        this.tfFilterProtocolNumber.setMinWidth(120);
        this.tfFilterProtocolNumber.setMaxWidth(120);

        this.tfFilterProtocolOwner = new TextField();
        this.tfFilterProtocolOwner.setPromptText("ФИО");
        this.tfFilterProtocolOwner.setMinWidth(120);
        this.tfFilterProtocolOwner.setMaxWidth(120);

        this.cbFilterLearnType = new ComboBox();
        this.cbFilterLearnType.setPromptText("Вид обучения");
        this.cbFilterLearnType.setPrefWidth(120);
        this.cbFilterLearnType.setConverter(new StringConverter<LearnType>() {

            @Override
            public String toString(LearnType object) {
                return object.getName();
            }

            @Override
            public LearnType fromString(String string) {
                return learnTypes.stream().filter(item -> item.getName().equals(string)).findFirst().orElse(null);
            }
        });

        this.cbFilterOrganization = new ComboBox();
        this.cbFilterOrganization.setPromptText("Цех (орг.)");
        this.cbFilterOrganization.setPrefWidth(120);
        this.cbFilterOrganization.setConverter(new StringConverter<Organization>() {

            @Override
            public String toString(Organization object) {
                return object.getName();
            }

            @Override
            public Organization fromString(String string) {
                return organizations.stream().filter(item -> item.getName().equals(string)).findFirst().orElse(null);
            }
        });

        this.cbFilterSpecialization = new ComboBox();
        this.cbFilterSpecialization.setPromptText("Профессия");
        this.cbFilterSpecialization.setPrefWidth(150);
        this.cbFilterSpecialization.setConverter(new StringConverter<Specialization>() {

            @Override
            public String toString(Specialization object) {
                return object.getName();
            }

            @Override
            public Specialization fromString(String string) {
                return specializations.stream().filter(item -> item.getName().equals(string)).findFirst().orElse(null);
            }
        });

        this.resetFiltersBtn = new Button("Сбросить");
        this.resetFiltersBtn.setMinWidth(100);
        this.resetFiltersBtn.setMaxWidth(100);
        this.resetFiltersBtn.setOnAction(e -> {
            disableCurrentYear();

            tfFilterProtocolNumber.setText(null);
            filters.remove("protocol_number");

            tfFilterProtocolOwner.setText(null);
            filters.remove("protocol_owner");

            cbFilterLearnType.setValue(null);
            filters.remove("learn_type_id");

            cbFilterOrganization.setValue(null);
            filters.remove("organization_id");

            cbFilterSpecialization.setValue(null);
            filters.remove("specialization_id");
        });

        this.filterBtn = new Button("Обновить");
        this.filterBtn.setMinWidth(100);
        this.filterBtn.setMaxWidth(100);
        this.filterBtn.setOnAction(e -> {
            disableCurrentYear();
            filters.put("current_year", tfCurrentYear.getText());

            if (!"".equals(tfFilterProtocolNumber.getText()) && tfFilterProtocolNumber.getText() != null) {
                filters.put("protocol_number", tfFilterProtocolNumber.getText());
            }

            if (!"".equals(tfFilterProtocolOwner.getText()) && tfFilterProtocolOwner.getText() != null) {
                filters.put("protocol_owner", tfFilterProtocolOwner.getText());
            }

            if (cbFilterLearnType.getValue() != null) {
                filters.put("learn_type_id", ((LearnType) cbFilterLearnType.getValue()).getId());
            }

            if (cbFilterOrganization.getValue() != null) {
                filters.put("organization_id", ((Organization) cbFilterOrganization.getValue()).getId());
            }

            if (cbFilterSpecialization.getValue() != null) {
                filters.put("specialization_id", ((Specialization) cbFilterSpecialization.getValue()).getId());
            }

            fillProtocolsTable();
        });

        filtersHb.getChildren().addAll(this.tfCurrentYear, this.tfFilterProtocolNumber, this.tfFilterProtocolOwner,
                this.cbFilterLearnType, this.cbFilterOrganization, this.cbFilterSpecialization, this.resetFiltersBtn, this.filterBtn);
        return filtersHb;
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

        TableColumn learnTypeId = new TableColumn("learnType");
        learnTypeId.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("learnTypeId"));
        learnTypeId.setVisible(false);

        TableColumn orgName = new TableColumn("Цех (организация)");
        orgName.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("orgName"));
        orgName.setResizable(true);

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
//        protocolDate.setCellValueFactory(
//                new PropertyValueFactory<ProtocolModel, String>("protocolDate"));
        protocolDate.setResizable(true);
        protocolDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProtocolModel, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures cdf) {
                SimpleStringProperty property = new SimpleStringProperty();
                LocalDate ld = LocalDate.parse(((ProtocolModel) cdf.getValue()).getProtocolDate(), DateTimeFormatter.ofPattern(getDBDateFormat()));
                property.setValue(ld.format(DateTimeFormatter.ofPattern(getUIDateFormat())));
                return property;
            }
        });

        this.protocolsTable.getColumns().addAll(idCol, protocolNumber, protocolOwner, learnTypeId, learnTypeName,
                orgId, orgName, specId, specName, protocolDate);

        this.protocolsTable.setContextMenu(this.createContextMenu());

        //this.protocolsTable.setColumnResizePolicy((param) -> true);
        this.fillProtocolsTable();
        this.protocolsTable.setTooltip(new Tooltip("Для выполнения операции нажмите правую кнопку мыши"));
        return this.protocolsTable;
    }

    void fillProtocolsTable() {
        if (this.filters.isEmpty()) {
            this.protocolsTable.setItems(FXCollections.observableArrayList(ProtocolService.getAll()
                    .stream().map(protocol -> ProtocolModel.fromProtocol(protocol)).collect(Collectors.toList())));
        } else {
            this.protocolsTable.setItems(FXCollections.observableArrayList(ProtocolService.getFiltered(this.filters)
                    .stream().map(protocol -> ProtocolModel.fromProtocol(protocol)).collect(Collectors.toList())));
        }
    }

    private ContextMenu createContextMenu() {
        ContextMenu cm = new ContextMenu();
        MenuItem addItem = new MenuItem("Добавить");
        addItem.setGraphic(new ImageView(getMenuItemAddIcon()));
        addItem.setOnAction(event -> {
            protocolForOperate = new Protocol();
            this.setActiveEditorView();
        });
        cm.getItems().add(addItem);

        MenuItem editItem = new MenuItem("Редактировать");
        editItem.setGraphic(new ImageView(getMenuItemEditIcon()));
        editItem.setOnAction(event -> {
            ProtocolModel model = (ProtocolModel) this.protocolsTable.getSelectionModel().getSelectedItem();
            protocolForOperate = model.toProtocol();
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

                    certificatesView.refreshProtocols();
                }
            } catch (ReferencesExistsException ex) {
                showError(ex.getMessage());
            }
        });
        cm.getItems().add(removeItem);
        
        SeparatorMenuItem sep = new SeparatorMenuItem();
        cm.getItems().add(sep);

        MenuItem reportItem = new MenuItem("Отчет в PDF");
        reportItem.setGraphic(new ImageView(getMenuItemPdfIcon()));
        reportItem.setOnAction(event -> {
            saveToPdf();
        });
        cm.getItems().add(reportItem);

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
        this.tfProtocolNumber.setText(String.valueOf(this.protocolForOperate.getProtocolNumber()));
        this.tfProtocolOwner.setText(this.protocolForOperate.getProtocolOwner());
        this.cbOrganization.setItems(this.organizations);
        this.cbOrganization.setValue(this.protocolForOperate.getOrganization());
        this.cbLearnType.setItems(this.learnTypes);
        this.cbLearnType.setValue(this.protocolForOperate.getLearnType());
        this.cbSpecialization.setItems(this.specializations);
        this.cbSpecialization.setValue(this.protocolForOperate.getSpecialization());
        if (this.protocolForOperate.getProtocolDate() != null) {
            this.dpProtocolDate.setValue(LocalDate.parse(this.protocolForOperate.getProtocolDate(), DateTimeFormatter.ofPattern(getDBDateFormat())));
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
                cbLearnType.setItems(learnTypes);
                cbFilterLearnType.getItems().clear();
                cbFilterLearnType.setItems(learnTypes);
            });
        }).start();
    }

    void refreshOrganizations() {
        new Thread(() -> {
            Platform.runLater(() -> {
                organizations = FXCollections.observableArrayList(OrganizationService.getAll());
                cbOrganization.getItems().clear();
                cbOrganization.setItems(organizations);
                cbFilterOrganization.getItems().clear();
                cbFilterOrganization.setItems(organizations);
            });
        }).start();
    }

    void refreshSpecializations() {
        new Thread(() -> {
            Platform.runLater(() -> {
                specializations = FXCollections.observableArrayList(SpecializationService.getAll());
                cbSpecialization.getItems().clear();
                cbSpecialization.setItems(specializations);
                cbFilterSpecialization.getItems().clear();
                cbFilterSpecialization.setItems(specializations);
            });
        }).start();
    }

    private void validate() {
        if (this.protocolForOperate.getLearnType() != null && this.protocolForOperate.getLearnType().getId() != null
                && this.protocolForOperate.getOrganization() != null && this.protocolForOperate.getOrganization().getId() != null
                && this.protocolForOperate.getSpecialization() != null && this.protocolForOperate.getSpecialization().getId() != null
                && this.protocolForOperate.getProtocolNumber() != 0
                && this.protocolForOperate.getProtocolOwner() != null && !"".equals(this.protocolForOperate.getProtocolOwner())
                && this.protocolForOperate.getProtocolDate() != null && !"".equals(this.protocolForOperate.getProtocolDate())) {
            this.saveBtn.setDisable(false);
        }
    }

    public void disableCurrentYear() {
        tfCurrentYear.setEditable(false);
    }

    private void saveToPdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить в PDF-файл");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF", "*.pdf")
        );
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try {
                generatePdfFromTableViewAndWriteToFile(file, protocolsTable);
            } catch (DocumentException | IOException ex) {
            }
        }
    }

    private void generatePdfFromTableViewAndWriteToFile(File file, TableView protocolsTable) throws IOException, DocumentException {
        String[] headers = new String[]{"№ протокола", "ФИО", "Цех (организация)", "Вид обучения", "Профессия", "Дата протокола"};

        Document document = new Document(PageSize.LETTER.rotate());

        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            BaseFont bf = BaseFont.createFont(getPdfFontPath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf, 12, Font.BOLD);
            Paragraph caption = new Paragraph("Протокола за " + filters.get("current_year") + " год", font);
            caption.setAlignment(Element.ALIGN_CENTER);
            document.add(caption);
            document.add(new Paragraph());

            Font fontHeader = new Font(bf, 10, Font.BOLD);
            Font fontRow = new Font(bf, 10, Font.NORMAL);

            PdfPTable table = new PdfPTable(headers.length);
            for (String header : headers) {
                PdfPCell cell = new PdfPCell();
                cell.setGrayFill(0.9f);
                cell.setPhrase(new Phrase(header.toUpperCase(), fontHeader));
                table.addCell(cell);
            }
            table.completeRow();

            protocolsTable.getItems().forEach(item -> {
                ProtocolModel protocol = (ProtocolModel) item;

                Phrase phrase1 = new Phrase(String.valueOf(protocol.getProtocolNumber()), fontRow);
                table.addCell(new PdfPCell(phrase1));
                Phrase phrase2 = new Phrase(protocol.getProtocolOwner(), fontRow);
                table.addCell(new PdfPCell(phrase2));
                Phrase phrase3 = new Phrase(protocol.getOrgName(), fontRow);
                table.addCell(new PdfPCell(phrase3));
                Phrase phrase4 = new Phrase(protocol.getLearnTypeName(), fontRow);
                table.addCell(new PdfPCell(phrase4));
                Phrase phrase5 = new Phrase(protocol.getSpecName(), fontRow);
                table.addCell(new PdfPCell(phrase5));
                
                Phrase phrase6 = new Phrase(LocalDate.parse(protocol.getProtocolDate(), DateTimeFormatter.ofPattern(getDBDateFormat())).format(DateTimeFormatter.ofPattern(getUIDateFormat())), fontRow);
                table.addCell(new PdfPCell(phrase6));

                table.completeRow();
            });

            document.addTitle("PDF Table Demo");
            document.add(table);
        } catch (DocumentException e) {
        } finally {
            document.close();
        }
    }
}
