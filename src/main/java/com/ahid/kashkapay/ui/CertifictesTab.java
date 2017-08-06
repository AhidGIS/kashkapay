/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.HBox;

/**
 *
 * @author mejir
 */
public class CertifictesTab extends Tab {

    public CertifictesTab() {
        this.initUI();
    }

    private void initUI() {
            this.setText("Удостоверения");
            HBox hbox = new HBox();
            hbox.getChildren().add(new Label("Удостоверения"));
            hbox.setAlignment(Pos.CENTER);
            setContent(hbox);        
    }
    
    
}
