/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;

/**
 *
 * @author mejir
 */
public class ProtocolsTab extends Tab {

    public ProtocolsTab() {
        this.initUI();
    }

    private void initUI() {
            this.setText("Протокола");
            HBox hbox = new HBox();
            hbox.getChildren().add(new Label("Протоколы"));
            hbox.setAlignment(Pos.CENTER);
            setContent(hbox);
    }
    
    
}
