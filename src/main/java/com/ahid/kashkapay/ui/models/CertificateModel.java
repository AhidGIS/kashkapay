/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.ui.models;

import com.ahid.kashkapay.entities.Certificate;
import com.ahid.kashkapay.entities.Organization;
import com.ahid.kashkapay.entities.Protocol;
import java.util.Objects;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author cccc
 */
public class CertificateModel {
    
    private final SimpleStringProperty id;
    private final SimpleIntegerProperty certificateNumber;
    private final SimpleStringProperty certificateDate;
    private final SimpleStringProperty certificateOwner;
    private final SimpleStringProperty certificateOwnerBirthDate;
    private final SimpleStringProperty protocolId;
    private final SimpleIntegerProperty protocolNumber;
    private final SimpleStringProperty protocolDate;
    private final SimpleStringProperty learnTypeId;
    private final SimpleStringProperty learnTypeName;
    private final SimpleStringProperty orgId;
    private final SimpleStringProperty orgName;
    private final SimpleStringProperty specId;
    private final SimpleStringProperty specName;

    public CertificateModel(String id, int certificateNumber, String certificateDate, String certificateOwner, 
            String certificateOwnerBirthDate, String protocolId, int protocolNumber, String protocolDate, 
            String learnTypeId, String learnTypeName, String orgId, String orgName, String specId, 
            String specName) {
        this.id = new SimpleStringProperty(id);
        this.certificateNumber = new SimpleIntegerProperty(certificateNumber);
        this.certificateDate = new SimpleStringProperty(certificateDate);
        this.certificateOwner = new SimpleStringProperty(certificateOwner);
        this.certificateOwnerBirthDate = new SimpleStringProperty(certificateOwnerBirthDate);
        this.protocolId = new SimpleStringProperty(protocolId);
        this.protocolNumber = new SimpleIntegerProperty(protocolNumber);
        this.protocolDate = new SimpleStringProperty(protocolDate);
        this.learnTypeId = new SimpleStringProperty(learnTypeId);
        this.learnTypeName = new SimpleStringProperty(learnTypeName);
        this.orgId = new SimpleStringProperty(orgId);
        this.orgName = new SimpleStringProperty(orgName);
        this.specId = new SimpleStringProperty(specId);
        this.specName = new SimpleStringProperty(specName);
    }
    
    public Certificate toCertificate() {
        Certificate certificate = new Certificate();
        certificate.setId(this.getId());
        certificate.setCertificateNumber(this.getCertificateNumber());
        certificate.setCertificateDate(this.getCertificateDate());
        certificate.setFullname(this.getCertificateOwner());
        certificate.setBirthDate(this.getCertificateOwnerBirthDate());
        Protocol protocol = new Protocol();
        protocol.setId(this.getProtocolId());
        //protocol.setProtocolNumber(this.getProtocolNumber());
        //protocol.setProtocolDate(this.getProtocolDate());
        certificate.setProtocol(protocol);
        Organization organization = new Organization();
        organization.setId(this.getOrgId());
        certificate.setOrganization(organization);
        return certificate;
    }
     
    public static CertificateModel fromCertificate(Certificate certificate) {
        return new CertificateModel(certificate.getId(), certificate.getCertificateNumber(),
                                    certificate.getCertificateDate(), certificate.getFullname(),
                                    certificate.getBirthDate(), certificate.getProtocol().getId(),
                                    certificate.getProtocol().getProtocolNumber(), certificate.getProtocol().getProtocolDate(),
                                    certificate.getProtocol().getLearnType().getId(), certificate.getProtocol().getLearnType().getName(),
                                    certificate.getOrganization() == null ? null : certificate.getOrganization().getId(), 
                                    certificate.getOrganization() == null ? null : certificate.getOrganization().getName(),
                                    certificate.getProtocol().getSpecialization().getId(), certificate.getProtocol().getSpecialization().getName());
    }

    public String getId() {
        return id.get();
    }

    public int getCertificateNumber() {
        return certificateNumber.get();
    }

    public String getCertificateDate() {
        return certificateDate.get();
    }

    public String getCertificateOwner() {
        return certificateOwner.get();
    }

    public String getCertificateOwnerBirthDate() {
        return certificateOwnerBirthDate.get();
    }

    public String getProtocolId() {
        return protocolId.get();
    }

    public int getProtocolNumber() {
        return protocolNumber.get();
    }

    public String getProtocolDate() {
        return protocolDate.get();
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
    
    public void setId(String value) {
        id.set(value);
    }

    public void setCertificateNumber(int value) {
        certificateNumber.set(value);
    }

    public void setCertificateDate(String value) {
        certificateDate.set(value);
    }

    public void setCertificateOwner(String value) {
        certificateOwner.set(value);
    }

    public void setCertificateOwnerBirthDate(String value) {
        certificateOwnerBirthDate.set(value);
    }

    public void setProtocolId(String value) {
        protocolId.set(value);
    }

    public void setProtocolNumber(int value) {
        protocolNumber.set(value);
    }

    public void setProtocolDate(String value) {
        protocolDate.set(value);
    }

    public void setLearnTypeId(String value) {
        learnTypeId.set(value);
    }

    public void setLearnTypeName(String value) {
        learnTypeName.set(value);
    }

    public void setOrgId(String value) {
        orgId.set(value);
    }

    public void setOrgName(String value) {
        orgName.set(value);
    }

    public void setSpecId(String value) {
        specId.set(value);
    }

    public void setSpecName(String value) {
        specName.set(value);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.certificateNumber);
        hash = 17 * hash + Objects.hashCode(this.certificateDate);
        hash = 17 * hash + Objects.hashCode(this.certificateOwner);
        hash = 17 * hash + Objects.hashCode(this.certificateOwnerBirthDate);
        hash = 17 * hash + Objects.hashCode(this.protocolId);
        hash = 17 * hash + Objects.hashCode(this.protocolNumber);
        hash = 17 * hash + Objects.hashCode(this.protocolDate);
        hash = 17 * hash + Objects.hashCode(this.learnTypeId);
        hash = 17 * hash + Objects.hashCode(this.learnTypeName);
        hash = 17 * hash + Objects.hashCode(this.orgId);
        hash = 17 * hash + Objects.hashCode(this.orgName);
        hash = 17 * hash + Objects.hashCode(this.specId);
        hash = 17 * hash + Objects.hashCode(this.specName);
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
        final CertificateModel other = (CertificateModel) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.certificateNumber, other.certificateNumber)) {
            return false;
        }
        if (!Objects.equals(this.certificateDate, other.certificateDate)) {
            return false;
        }
        if (!Objects.equals(this.certificateOwner, other.certificateOwner)) {
            return false;
        }
        if (!Objects.equals(this.certificateOwnerBirthDate, other.certificateOwnerBirthDate)) {
            return false;
        }
        if (!Objects.equals(this.protocolId, other.protocolId)) {
            return false;
        }
        if (!Objects.equals(this.protocolNumber, other.protocolNumber)) {
            return false;
        }
        if (!Objects.equals(this.protocolDate, other.protocolDate)) {
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
