/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.ui.models;

import com.ahid.kashkapay.entities.LearnType;
import com.ahid.kashkapay.entities.Organization;
import com.ahid.kashkapay.entities.Protocol;
import com.ahid.kashkapay.entities.Specialization;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author cccc
 */
public class ProtocolModel {
    
    private final SimpleStringProperty id;
    private final SimpleStringProperty protocolNumber;
    private final SimpleStringProperty protocolDate;
    private final SimpleStringProperty protocolOwner;
    private final SimpleStringProperty learnTypeId;
    private final SimpleStringProperty learnTypeName;
    private final SimpleStringProperty orgId;
    private final SimpleStringProperty orgName;
    private final SimpleStringProperty specId;
    private final SimpleStringProperty specName;

    public ProtocolModel(String id, String protocolNumber, String protocolDate, String protocolOwner, 
            String learnTypeId, String learnTypeName, String orgId, String orgName, String specId, String specName) {
        this.id = new SimpleStringProperty(id);
        this.protocolNumber = new SimpleStringProperty(protocolNumber);
        this.protocolDate = new SimpleStringProperty(protocolDate);
        this.protocolOwner = new SimpleStringProperty(protocolOwner);
        this.learnTypeId = new SimpleStringProperty(learnTypeId);
        this.learnTypeName = new SimpleStringProperty(learnTypeName);
        this.orgId = new SimpleStringProperty(orgId);
        this.orgName = new SimpleStringProperty(orgName);
        this.specId = new SimpleStringProperty(specId);
        this.specName = new SimpleStringProperty(specName);
    }
    
    
    public Protocol toProtocol() {
        Protocol protocol = new Protocol();
        protocol.setId(this.getId());
        protocol.setProtocolNumber(this.getProtocolNumber());
        protocol.setProtocolDate(this.getProtocolDate());
        protocol.setProtocolOwner(this.getProtocolOwner());
        protocol.setLearnType(new LearnType(this.getLearnTypeId(), this.getLearnTypeName()));
        protocol.setOrganization(new Organization(this.getOrgId(), this.getOrgName()));
        protocol.setSpecialization(new Specialization(this.getSpecId(), this.getSpecName()));
        return protocol;
    }
     
    public static ProtocolModel fromProtocol(Protocol protocol) {
        return new ProtocolModel(protocol.getId(), 
                                protocol.getProtocolNumber(),
                                protocol.getProtocolDate(),
                                protocol.getProtocolOwner(),
                                protocol.getLearnType().getId(),
                                protocol.getLearnType().getName(),
                                protocol.getOrganization().getId(),
                                protocol.getOrganization().getName(),
                                protocol.getSpecialization().getId(),
                                protocol.getSpecialization().getName());
    }

    public String getId() {
        return id.get();
    }

    public String getProtocolNumber() {
        return protocolNumber.get();
    }

    public String getProtocolDate() {
        return protocolDate.get();
    }

    public String getProtocolOwner() {
        return protocolOwner.get();
    }

    public String getLearnTypeId() {
        return learnTypeId.get();
    }

    public String getLearnTypeName() {
        return learnTypeName.get();
    }

    public String getOrgId() {
        return orgId.get();
    }

    public String getOrgName() {
        return orgName.get();
    }

    public String getSpecId() {
        return specId.get();
    }

    public String getSpecName() {
        return specName.get();
    }
    
    public void setId(String id) {
        this.id.set(id);
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber.set(protocolNumber);
    }

    public void setProtocolDate(String protocolDate) {
        this.protocolDate.set(protocolDate);
    }

    public void setProtocolOwner(String protocolOwner) {
        this.protocolOwner.set(protocolOwner);
    }

    public void setLearnTypeId(String learnTypeId) {
        this.learnTypeId.set(learnTypeId);
    }

    public void setLearnTypeName(String learnTypeName) {
        this.learnTypeName.set(learnTypeName);
    }

    public void setOrgId(String orgId) {
        this.orgId.set(orgId);
    }

    public void setOrgName(String orgName) {
        this.orgName.set(orgName);
    }

    public void setSpecId(String specId) {
        this.specId.set(specId);
    }

    public void setSpecName(String specName) {
        this.specName.set(specName);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.protocolNumber);
        hash = 97 * hash + Objects.hashCode(this.protocolDate);
        hash = 97 * hash + Objects.hashCode(this.protocolOwner);
        hash = 97 * hash + Objects.hashCode(this.learnTypeId);
        hash = 97 * hash + Objects.hashCode(this.learnTypeName);
        hash = 97 * hash + Objects.hashCode(this.orgId);
        hash = 97 * hash + Objects.hashCode(this.orgName);
        hash = 97 * hash + Objects.hashCode(this.specId);
        hash = 97 * hash + Objects.hashCode(this.specName);
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
        final ProtocolModel other = (ProtocolModel) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.protocolNumber, other.protocolNumber)) {
            return false;
        }
        if (!Objects.equals(this.protocolDate, other.protocolDate)) {
            return false;
        }
        if (!Objects.equals(this.protocolOwner, other.protocolOwner)) {
            return false;
        }
        if (!Objects.equals(this.learnTypeId, other.learnTypeId)) {
            return false;
        }
        if (!Objects.equals(this.learnTypeName, other.learnTypeName)) {
            return false;
        }
        if (!Objects.equals(this.orgId, other.orgId)) {
            return false;
        }
        if (!Objects.equals(this.orgName, other.orgName)) {
            return false;
        }
        if (!Objects.equals(this.specId, other.specId)) {
            return false;
        }
        if (!Objects.equals(this.specName, other.specName)) {
            return false;
        }
        return true;
    }
    
        
}
