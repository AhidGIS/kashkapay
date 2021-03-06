package com.ahid.kashkapay;

import com.ahid.kashkapay.services.LearnTypeService;
import com.ahid.kashkapay.entities.LearnType;
import com.ahid.kashkapay.utils.CommonUtil;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class FXMLController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText(LearnTypeService.getAll().stream().map(lt -> lt.getId() + ", " + lt.getName()).collect(Collectors.joining("\n")));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LearnTypeService.getAll().forEach(lt -> {
            System.out.println(lt.getId() + ", " + lt.getName());
        });
        
        /*LearnType learnType = new LearnType();
        learnType.setId(CommonUtil.uniqueString());
        learnType.setName("Обучение");
        LearnTypeService.create(learnType);*/
        
        LearnTypeService.getAll().forEach(lt -> {
            System.out.println(lt.getId() + ", " + lt.getName());
        });
    }    
}
