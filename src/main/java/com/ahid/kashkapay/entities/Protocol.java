/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cccc
 */
@Entity
@Table(name = "protocols")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Protocol.findAll", query = "SELECT p FROM Protocol p order by p.protocolDate, p.protocolNumber")
    , @NamedQuery(name = "Protocol.existedCountByName", query = "SELECT count(p.id) FROM Protocol p WHERE p.protocolNumber = :protocolNumber and p.protocolDate = :protocolDate")
    , @NamedQuery(name = "Protocol.existedCountByNotIdAndName", query = "SELECT count(p.id) FROM Protocol p WHERE p.id != :id and p.protocolNumber = :protocolNumber and p.protocolDate = :protocolDate")
    , @NamedQuery(name = "Protocol.countByLearnType", query = "SELECT count(p.id) FROM Protocol p WHERE p.learnType.id = :id")
    , @NamedQuery(name = "Protocol.countBySpecialization", query = "SELECT count(p.id) FROM Protocol p WHERE p.specialization.id = :id")
    , @NamedQuery(name = "Protocol.countByOrganization", query = "SELECT count(p.id) FROM Protocol p WHERE p.organization.id = :id")
    , @NamedQuery(name = "Protocol.findById", query = "SELECT p FROM Protocol p WHERE p.id = :id")
    , @NamedQuery(name = "Protocol.findByProtocolNumber", query = "SELECT p FROM Protocol p WHERE p.protocolNumber = :protocolNumber")
    , @NamedQuery(name = "Protocol.findByProtocolDate", query = "SELECT p FROM Protocol p WHERE p.protocolDate = :protocolDate")
    , @NamedQuery(name = "Protocol.findByProtocolOwner", query = "SELECT p FROM Protocol p WHERE p.protocolOwner = :protocolOwner")})
public class Protocol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "protocol_number")
    private int protocolNumber;
    @Column(name = "protocol_date")
    private String protocolDate;
    @Column(name = "protocol_owner")
    private String protocolOwner;
    @OneToMany(mappedBy = "protocol")
    private List<Certificate> certificatesList;
    @JoinColumn(name = "organization", referencedColumnName = "id")
    @ManyToOne
    private Organization organization;
    @JoinColumn(name = "specialization", referencedColumnName = "id")
    @ManyToOne
    private Specialization specialization;
    @JoinColumn(name = "learn_type", referencedColumnName = "id")
    @ManyToOne
    private LearnType learnType;

    public Protocol() {
    }

    public Protocol(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(int protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public String getProtocolDate() {
        return protocolDate;
    }

    public void setProtocolDate(String protocolDate) {
        this.protocolDate = protocolDate;
    }

    public String getProtocolOwner() {
        return protocolOwner;
    }

    public void setProtocolOwner(String protocolOwner) {
        this.protocolOwner = protocolOwner;
    }

    @XmlTransient
    public List<Certificate> getCertificatesList() {
        return certificatesList;
    }

    public void setCertificatesList(List<Certificate> certificatesList) {
        this.certificatesList = certificatesList;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public LearnType getLearnType() {
        return learnType;
    }

    public void setLearnType(LearnType learnType) {
        this.learnType = learnType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Protocol)) {
            return false;
        }
        Protocol other = (Protocol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.crudapp.Protocol[ id=" + id + " ]";
    }
    
}
