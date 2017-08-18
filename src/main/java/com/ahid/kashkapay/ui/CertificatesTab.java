/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.ui;

import com.ahid.kashkapay.entities.Certificate;
import com.ahid.kashkapay.entities.LearnType;
import com.ahid.kashkapay.entities.Organization;
import com.ahid.kashkapay.entities.Protocol;
import com.ahid.kashkapay.entities.Specialization;
import com.ahid.kashkapay.exceptions.DuplicateException;
import com.ahid.kashkapay.exceptions.ReferencesExistsException;
import com.ahid.kashkapay.services.CertificateService;
import com.ahid.kashkapay.services.LearnTypeService;
import com.ahid.kashkapay.services.OrganizationService;
import com.ahid.kashkapay.services.ProtocolService;
import com.ahid.kashkapay.services.SpecializationService;
import com.ahid.kashkapay.ui.models.CertificateModel;
import com.ahid.kashkapay.ui.models.ProtocolModel;
import static com.ahid.kashkapay.utils.CommonUtil.getDBDateFormat;
import static com.ahid.kashkapay.utils.CommonUtil.getUIDateFormat;
import static com.ahid.kashkapay.utils.UIUtil.getCustomStyleForRootViews;
import static com.ahid.kashkapay.utils.UIUtil.getMenuItemAddIcon;
import static com.ahid.kashkapay.utils.UIUtil.getMenuItemDeleteIcon;
import static com.ahid.kashkapay.utils.UIUtil.getMenuItemEditIcon;
import static com.ahid.kashkapay.utils.UIUtil.showConfirmation;
import static com.ahid.kashkapay.utils.UIUtil.showError;
import static com.ahid.kashkapay.utils.UIUtil.showInfo;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author mejir
 */
public class CertificatesTab extends Tab {

    private Certificate certificateForOperate;

    private Map<String, String> filters;

    private ObservableList<LearnType> learnTypes;
    private ObservableList<Organization> organizations;
    private ObservableList<Specialization> specializations;
    private ObservableList<Protocol> protocols;

    private final Stage primaryStage;
    private final TableView certificatesTable = new TableView();
    private VBox editorView;
    private Button saveBtn;
    private Button cancelBtn;
    private TextField tfCertificateNumber;
    private TextField tfCertificateOwner;
    private TextField tfCertificateOwnerBirthDate;
    private DatePicker dpCertificateDate;
    private ComboBox cbProtocol;

    private Button filterBtn;
    private Button resetFiltersBtn;
    private TextField tfCurrentYear;
    private TextField tfFilterCertificateNumber;
    private TextField tfFilterCertificateOwner;
    private TextField tfFilterCertificateOwnerBirthDate;
    private ComboBox cbFilterLearnType;
    private ComboBox cbFilterOrganization;
    private ComboBox cbFilterSpecialization;
    private ComboBox cbFilterProtocol;

    public CertificatesTab(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.filters = new HashMap<>();
        this.initUI();
    }

    private void initUI() {
        this.setText("Удостоверения");

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

        this.tfCertificateNumber = new TextField();
        this.tfCertificateNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if ((newValue == null || !newValue.equals(oldValue)) && certificateForOperate != null) {
                certificateForOperate.setCertificateNumber(newValue);
                validate();
            }
        });

        this.tfCertificateOwner = new TextField();
        this.tfCertificateOwner.textProperty().addListener((observable, oldValue, newValue) -> {
            if ((newValue == null || !newValue.equals(oldValue)) && certificateForOperate != null) {
                certificateForOperate.setFullname(newValue);
                validate();
            }
        });
        
        this.tfCertificateOwnerBirthDate = new TextField();
        this.tfCertificateOwnerBirthDate.textProperty().addListener((observable, oldValue, newValue) -> {
            if ((newValue == null || !newValue.equals(oldValue)) && certificateForOperate != null) {
                certificateForOperate.setBirthDate(newValue);
                validate();
            }
        });

        this.cbProtocol = new ComboBox();
        this.cbProtocol.setConverter(new StringConverter<Protocol>() {

            @Override
            public String toString(Protocol object) {
                return object.getProtocolNumber();
            }

            @Override
            public Protocol fromString(String string) {
                return protocols.stream().filter(item -> item.getProtocolNumber().equals(string)).findFirst().orElse(null);
            }
        });
        this.cbProtocol.valueProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
            if ((newValue == null || !newValue.equals(oldValue)) && certificateForOperate != null) {
                certificateForOperate.setProtocol((Protocol) newValue);
                validate();
            }
        });

        this.dpCertificateDate = new DatePicker();
        this.dpCertificateDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ((newValue == null || !newValue.equals(oldValue)) && certificateForOperate != null) {
                certificateForOperate.setCertificateDate(newValue.format(DateTimeFormatter.ofPattern(getDBDateFormat())));
                validate();
            }
        });

        this.saveBtn = new Button("Сохранить");
        this.saveBtn.setOnAction(event -> {
            try {
                if (showConfirmation("Вы подтверждаете сохранение сертификата?").get() == ButtonType.OK) {
                    CertificateService.save(certificateForOperate);
                    setInactiveEditorView();
                    fillCertificatesTable();
                    showInfo("Сертификат успешно сохранен");
                }
            } catch (DuplicateException de) {
                showError(de.getMessage());
            }
        });

        this.cancelBtn = new Button("Отменить");
        this.cancelBtn.setOnAction(event -> {
            setInactiveEditorView();
        });

        HBox hbCertificateNumber = new HBox();
        hbCertificateNumber.setSpacing(10);
        Label lbCertificateNumber = new Label("Номер сертификата");
        lbCertificateNumber.setPrefWidth(150);
        hbCertificateNumber.getChildren().addAll(lbCertificateNumber, this.tfCertificateNumber);
        this.tfCertificateNumber.prefWidthProperty().bind(hbCertificateNumber.widthProperty().subtract(lbCertificateNumber.widthProperty()));

        HBox hbCertificatelOwner = new HBox();
        hbCertificatelOwner.setSpacing(10);
        Label lbCertificateOwner = new Label("ФИО");
        lbCertificateOwner.setPrefWidth(150);
        hbCertificatelOwner.getChildren().addAll(lbCertificateOwner, this.tfCertificateOwner);
        this.tfCertificateOwner.prefWidthProperty().bind(hbCertificatelOwner.widthProperty().subtract(lbCertificateOwner.widthProperty()));

        HBox hbCertificatelOwnerBirthDate = new HBox();
        hbCertificatelOwnerBirthDate.setSpacing(10);
        Label lbCertificateOwnerBirthDate = new Label("Год рождения");
        lbCertificateOwnerBirthDate.setPrefWidth(150);
        hbCertificatelOwnerBirthDate.getChildren().addAll(lbCertificateOwnerBirthDate, this.tfCertificateOwnerBirthDate);
        this.tfCertificateOwnerBirthDate.prefWidthProperty().bind(hbCertificatelOwnerBirthDate.widthProperty().subtract(lbCertificateOwnerBirthDate.widthProperty()));

        HBox hbProtocol = new HBox();
        hbProtocol.setSpacing(10);
        Label lbProtocol = new Label("Протокол");
        lbProtocol.setPrefWidth(150);
        hbProtocol.getChildren().addAll(lbProtocol, this.cbProtocol);
        this.cbProtocol.prefWidthProperty().bind(hbProtocol.widthProperty().subtract(lbProtocol.widthProperty()));

        HBox hbCertificateDate = new HBox();
        hbCertificateDate.setSpacing(10);
        Label lbCertificateDate = new Label("Дата сертификата");
        lbCertificateDate.setPrefWidth(150);
        hbCertificateDate.getChildren().addAll(lbCertificateDate, this.dpCertificateDate);
        this.dpCertificateDate.prefWidthProperty().bind(hbCertificateDate.widthProperty().subtract(lbCertificateDate.widthProperty()));

        HBox buttons = new HBox();
        buttons.setSpacing(10);
        buttons.getChildren().addAll(this.saveBtn, this.cancelBtn);

        editorView.getChildren().addAll(hbCertificateNumber, hbCertificatelOwner, hbCertificatelOwnerBirthDate, hbProtocol, hbCertificateDate, buttons);
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
        this.tfCurrentYear.setPrefWidth(50);
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
                    refreshProtocols();
                    fillCertificatesTable();
                }
            }
        });
        this.tfCurrentYear.setStyle("-fx-background-color: #48D1CC; -fx-margin: 0 0 0 100");

        this.tfFilterCertificateNumber = new TextField();
        this.tfFilterCertificateNumber.setPromptText("Номер сертификата");
        this.tfFilterCertificateNumber.setPrefWidth(80);

        this.tfFilterCertificateOwner = new TextField();
        this.tfFilterCertificateOwner.setPromptText("ФИО");
        this.tfFilterCertificateOwner.setPrefWidth(80);
        
        this.tfFilterCertificateOwnerBirthDate = new TextField();
        this.tfFilterCertificateOwnerBirthDate.setPromptText("Год рождения");
        this.tfFilterCertificateOwnerBirthDate.setPrefWidth(60);

        this.cbFilterLearnType = new ComboBox();
        this.cbFilterLearnType.setPromptText("Вид обучения");
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

        this.cbFilterProtocol = new ComboBox();
        this.cbFilterProtocol.setPromptText("Протокол");
        this.cbFilterProtocol.setConverter(new StringConverter<Protocol>() {

            @Override
            public String toString(Protocol object) {
                return object.getProtocolNumber();
            }

            @Override
            public Protocol fromString(String string) {
                return protocols.stream().filter(item -> item.getProtocolNumber().equals(string)).findFirst().orElse(null);
            }
        });

        this.resetFiltersBtn = new Button("Сбросить");
        this.resetFiltersBtn.setOnAction(e -> {
            disableCurrentYear();

            tfFilterCertificateNumber.setText(null);
            filters.remove("certificate_number");

            tfFilterCertificateOwner.setText(null);
            filters.remove("fullname");

            tfFilterCertificateOwnerBirthDate.setText(null);
            filters.remove("birth_date");
            
            cbFilterProtocol.setValue(null);
            filters.remove("protocol_id");

            cbFilterLearnType.setValue(null);
            filters.remove("learn_type_id");

            cbFilterOrganization.setValue(null);
            filters.remove("organization_id");

            cbFilterSpecialization.setValue(null);
            filters.remove("specialization_id");
        });

        this.filterBtn = new Button("Обновить");
        this.filterBtn.setOnAction(e -> {
            disableCurrentYear();
            filters.put("current_year", tfCurrentYear.getText());

            if (!"".equals(tfFilterCertificateNumber.getText()) && tfFilterCertificateNumber.getText() != null) {
                filters.put("certificate_number", tfFilterCertificateNumber.getText());
            }

            if (!"".equals(tfFilterCertificateOwner.getText()) && tfFilterCertificateOwner.getText() != null) {
                filters.put("fullname", tfFilterCertificateOwner.getText());
            }

            if (!"".equals(tfFilterCertificateOwnerBirthDate.getText()) && tfFilterCertificateOwnerBirthDate.getText() != null) {
                filters.put("birth_date", tfFilterCertificateOwnerBirthDate.getText());
            }

            if (cbFilterProtocol.getValue() != null) {
                filters.put("protocol_id", ((Protocol) cbFilterProtocol.getValue()).getId());
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

            fillCertificatesTable();
        });

        filtersHb.getChildren().addAll(this.tfCurrentYear, this.tfFilterCertificateNumber, 
                this.tfFilterCertificateOwner, this.tfFilterCertificateOwnerBirthDate, this.cbFilterProtocol,
                this.cbFilterLearnType, this.cbFilterOrganization, this.cbFilterSpecialization, 
                this.resetFiltersBtn, this.filterBtn);
        return filtersHb;
    }

    private TableView initAndGetTableContent() {
        TableColumn idCol = new TableColumn("Id");
        idCol.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("id"));
        idCol.setVisible(false);

        TableColumn certificateNumber = new TableColumn("Номер сертификата");
        certificateNumber.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("certificateNumber"));
        certificateNumber.setResizable(true);

        TableColumn certificateOwner = new TableColumn("ФИО");
        certificateOwner.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("certificateOwner"));
        certificateOwner.setResizable(true);
        
        TableColumn certificateOwnerBirthDate = new TableColumn("Год рождения");
        certificateOwnerBirthDate.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("certificateOwnerBirthDate"));
        certificateOwnerBirthDate.setResizable(true);

        TableColumn orgId = new TableColumn("org");
        orgId.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("orgId"));
        orgId.setVisible(false);

        TableColumn learnTypeId = new TableColumn("learnType");
        learnTypeId.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("learnTypeId"));
        learnTypeId.setVisible(false);

        TableColumn specId = new TableColumn("spec");
        specId.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("specId"));
        specId.setVisible(false);

        TableColumn orgName = new TableColumn("Цех (организация)");
        orgName.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("orgName"));
        orgName.setResizable(true);

        TableColumn learnTypeName = new TableColumn("Вид обучения");
        learnTypeName.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("learnTypeName"));
        learnTypeName.setResizable(true);

        TableColumn specName = new TableColumn("Профессия (специальность)");
        specName.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("specName"));
        specName.setResizable(true);

        TableColumn protocolId = new TableColumn("protocol");
        protocolId.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("protocolId"));
        protocolId.setVisible(false);
        
        TableColumn protocolNumber = new TableColumn("Протокол");
        protocolNumber.setCellValueFactory(
                new PropertyValueFactory<ProtocolModel, String>("protocolNumber"));
        protocolNumber.setResizable(true);

        TableColumn certificateDate = new TableColumn("Дата сертификата");
        certificateDate.setResizable(true);
        certificateDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CertificateModel, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures cdf) {
                SimpleStringProperty property = new SimpleStringProperty();
                LocalDate ld = LocalDate.parse(((CertificateModel) cdf.getValue()).getCertificateDate(), DateTimeFormatter.ofPattern(getDBDateFormat()));
                property.setValue(ld.format(DateTimeFormatter.ofPattern(getUIDateFormat())));
                return property;
            }
        });

        this.certificatesTable.getColumns().addAll(idCol, certificateNumber, certificateOwner, certificateOwnerBirthDate, learnTypeId, learnTypeName,
                orgId, orgName, specId, specName, protocolId, protocolNumber, certificateDate);

        this.certificatesTable.setContextMenu(this.createContextMenu());

        //this.certificatesTable.setColumnResizePolicy((param) -> true);
        this.fillCertificatesTable();
        this.certificatesTable.setTooltip(new Tooltip("Для выполнения операции нажмите правую кнопку мыши"));
        return this.certificatesTable;
    }

    void fillCertificatesTable() {
        if (this.filters.isEmpty()) {
            this.certificatesTable.setItems(FXCollections.observableArrayList(CertificateService.getAll()
                    .stream().map(certificate -> CertificateModel.fromCertificate(certificate)).collect(Collectors.toList())));
        } else {
            this.certificatesTable.setItems(FXCollections.observableArrayList(CertificateService.getFiltered(this.filters)
                    .stream().map(certificate -> CertificateModel.fromCertificate(certificate)).collect(Collectors.toList())));
        }
    }

    private ContextMenu createContextMenu() {
        ContextMenu cm = new ContextMenu();
        MenuItem addItem = new MenuItem("Добавить");
        addItem.setGraphic(new ImageView(getMenuItemAddIcon()));
        addItem.setOnAction(event -> {
            certificateForOperate = new Certificate();
            this.setActiveEditorView();
        });
        cm.getItems().add(addItem);
        MenuItem editItem = new MenuItem("Редактировать");
        editItem.setGraphic(new ImageView(getMenuItemEditIcon()));
        editItem.setOnAction(event -> {
            CertificateModel model = (CertificateModel) this.certificatesTable.getSelectionModel().getSelectedItem();
            certificateForOperate = model.toCertificate();
            this.setActiveEditorView();
        });
        cm.getItems().add(editItem);
        MenuItem removeItem = new MenuItem("Удалить");
        removeItem.setGraphic(new ImageView(getMenuItemDeleteIcon()));
        removeItem.setOnAction(event -> {
            try {
                if (showConfirmation("Вы подтверждаете удаление сертификата?").get() == ButtonType.OK) {
                    CertificateModel model = (CertificateModel) this.certificatesTable.getSelectionModel().getSelectedItem();
                    CertificateService.delete(model.toCertificate());
                    certificatesTable.getItems().remove(model);
                    showInfo("Сертификат успешно удален");
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
        this.certificateForOperate = null;
        this.tfCertificateNumber.setText(null);
        this.tfCertificateOwner.setText(null);
        this.tfCertificateOwnerBirthDate.setText(null);
        this.cbProtocol.setValue(null);
        this.dpCertificateDate.setValue(null);
    }

    private void setActiveEditorView() {
        this.editorView.setDisable(false);
        this.tfCertificateNumber.setText(this.certificateForOperate.getCertificateNumber());
        this.tfCertificateOwner.setText(this.certificateForOperate.getFullname());
        this.tfCertificateOwnerBirthDate.setText(this.certificateForOperate.getBirthDate());
        this.cbProtocol.setItems(this.protocols);
        this.cbProtocol.setValue(this.certificateForOperate.getProtocol());
        if (this.certificateForOperate.getCertificateDate()!= null) {
            this.dpCertificateDate.setValue(LocalDate.parse(this.certificateForOperate.getCertificateDate(), DateTimeFormatter.ofPattern(getDBDateFormat())));
        }
    }

    private void initDSsForComboItems() {
        this.refreshLearnTypes();
        this.refreshOrganizations();
        this.refreshSpecializations();
        this.refreshProtocols();
    }

    void refreshLearnTypes() {
        new Thread(() -> {
            Platform.runLater(() -> {
                learnTypes = FXCollections.observableArrayList(LearnTypeService.getAll());
                cbFilterLearnType.getItems().clear();
                cbFilterLearnType.setItems(learnTypes);
            });
        }).start();
    }

    void refreshOrganizations() {
        new Thread(() -> {
            Platform.runLater(() -> {
                organizations = FXCollections.observableArrayList(OrganizationService.getAll());
                cbFilterOrganization.getItems().clear();
                cbFilterOrganization.setItems(organizations);
            });
        }).start();
    }

    void refreshSpecializations() {
        new Thread(() -> {
            Platform.runLater(() -> {
                specializations = FXCollections.observableArrayList(SpecializationService.getAll());
                cbFilterSpecialization.getItems().clear();
                cbFilterSpecialization.setItems(specializations);
            });
        }).start();
    }
    
    void refreshProtocols() {
        new Thread(() -> {
            Platform.runLater(() -> {
                protocols = FXCollections.observableArrayList(ProtocolService.getAllForYear(tfCurrentYear.getText()));
                cbProtocol.getItems().clear();
                cbProtocol.setItems(protocols);
                cbFilterProtocol.getItems().clear();
                cbFilterProtocol.setItems(protocols);
            });
        }).start();
    }

    private void validate() {
        if (this.certificateForOperate.getProtocol()!= null && this.certificateForOperate.getProtocol().getId() != null
                && this.certificateForOperate.getCertificateNumber()!= null && !"".equals(this.certificateForOperate.getCertificateNumber())
                && this.certificateForOperate.getFullname()!= null && !"".equals(this.certificateForOperate.getFullname())
                && this.certificateForOperate.getCertificateDate()!= null && !"".equals(this.certificateForOperate.getCertificateDate())
                && this.certificateForOperate.getBirthDate()!= null && !"".equals(this.certificateForOperate.getBirthDate())) {
            this.saveBtn.setDisable(false);
        }
    }

    public void disableCurrentYear() {
        tfCurrentYear.setEditable(false);
    }
    
}
