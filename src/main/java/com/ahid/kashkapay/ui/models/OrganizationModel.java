/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.ui.models;

import com.ahid.kashkapay.entities.LearnType;
import com.ahid.kashkapay.entities.Organization;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author cccc
 */
public class OrganizationModel {
    
    private final SimpleStringProperty id;
    private final SimpleStringProperty name;

    public OrganizationModel(String id, String name) {
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
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.name);
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
        final OrganizationModel other = (OrganizationModel) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    
    public Organization toOrganization() {
        Organization org = new Organization();
        org.setId(this.getId());
        org.setName(this.getName());
        return org;
    }
     
    public static OrganizationModel fromOrganization(Organization org) {
        return new OrganizationModel(org.getId(), org.getName());
    }
}
