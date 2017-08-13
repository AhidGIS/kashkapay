package com.ahid.kashkapay;

import com.ahid.kashkapay.services.EntityManagerFactoryHolder;
import com.ahid.kashkapay.ui.CertifictesTab;
import com.ahid.kashkapay.ui.ProtocolsTab;
import com.ahid.kashkapay.ui.ReferenceDataTab;
import static com.ahid.kashkapay.utils.UIUtil.getMainIcon;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static String[] arguments;

    private void initDB() {
        Map props = new HashMap();
        String pathToDbFile = arguments.length == 0 ? "data.db" : arguments[0];
        props.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + pathToDbFile);
        props.put("javax.persistence.jdbc.driver", "org.sqlite.JDBC");
        EntityManagerFactoryHolder.init(props);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(Locale.getDefault());
        this.initDB();
//        
//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
//        
//        Scene scene = new Scene(root);
//        scene.getStylesheets().add("/styles/Styles.css");
//        
//        stage.setTitle("JavaFX and Maven");
//        stage.setScene(scene);
//        stage.show();

        primaryStage.setTitle("Kashkapay");
        primaryStage.setMaximized(true);
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(1000);
        primaryStage.getIcons().add(getMainIcon());

        Group root = new Group();
        Scene scene = new Scene(root);

        BorderPane borderPane = new BorderPane();
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        borderPane.setCenter(tabPane);

        ProtocolsTab protocolsTab = new ProtocolsTab(primaryStage);
        ReferenceDataTab referencesTab = new ReferenceDataTab(primaryStage, protocolsTab);
        CertifictesTab certificatesTab = new CertifictesTab();

        tabPane.getTabs().add(referencesTab);
        tabPane.getTabs().add(protocolsTab);
        tabPane.getTabs().add(certificatesTab);
        tabPane.getSelectionModel().selectedItemProperty().addListener((tp, oldTab, newTab) -> {
            if (oldTab.getClass().equals(ProtocolsTab.class) && !newTab.getClass().equals(ProtocolsTab.class)) {
                ProtocolsTab pt = (ProtocolsTab) oldTab;
                pt.setInactiveEditorView();
                pt.disableCurrentYear();
            }
        });

        root.getChildren().add(borderPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        arguments = args;
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        EntityManagerFactoryHolder.destroy();
    }

}
