/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.ui.models;

import com.ahid.kashkapay.entities.Organization;
import com.ahid.kashkapay.entities.Specialization;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author cccc
 */
public class SpecializationModel {
    
    private final SimpleStringProperty id;
    private final SimpleStringProperty name;

    public SpecializationModel(String id, String name) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.id);
        hash = 19 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SpecializationModel other = (SpecializationModel) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    
    public Specialization toSpecialization() {
        Specialization spec = new Specialization();
        spec.setId(this.getId());
        spec.setName(this.getName());
        return spec;
    }
 
    public static SpecializationModel fromSpecialization(Specialization spec) {
        return new SpecializationModel(spec.getId(), spec.getName());
    }   
}
