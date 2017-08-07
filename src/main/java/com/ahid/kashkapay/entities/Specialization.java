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
@Table(name = "specializations")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Specialization.findAll", query = "SELECT s FROM Specialization s")
    , @NamedQuery(name = "Specialization.existedCountByName", query = "SELECT count(s.id) FROM Specialization s WHERE s.name = :name")
    , @NamedQuery(name = "Specialization.existedCountByNotIdAndName", query = "SELECT count(s.id) FROM Specialization s WHERE s.id != :id and s.name = :name")
    , @NamedQuery(name = "Specialization.findById", query = "SELECT s FROM Specialization s WHERE s.id = :id")
    , @NamedQuery(name = "Specialization.findByName", query = "SELECT s FROM Specialization s WHERE s.name = :name")})
public class Specialization implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "specialization")
    private List<Protocol> protocolsList;

    public Specialization() {
    }

    public Specialization(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Protocol> getProtocolsList() {
        return protocolsList;
    }

    public void setProtocolsList(List<Protocol> protocolsList) {
        this.protocolsList = protocolsList;
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
        if (!(object instanceof Specialization)) {
            return false;
        }
        Specialization other = (Specialization) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.crudapp.Specialization[ id=" + id + " ]";
    }
    
}
