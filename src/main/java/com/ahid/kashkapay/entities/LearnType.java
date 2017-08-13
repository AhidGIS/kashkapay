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
@Table(name = "learn_types")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LearnType.findAll", query = "SELECT l FROM LearnType l order by L.name")
    , @NamedQuery(name = "LearnType.existedCountByName", query = "SELECT count(l.id) FROM LearnType l WHERE UPPER(l.name) = UPPER(:name)")
    , @NamedQuery(name = "LearnType.existedCountByNotIdAndName", query = "SELECT count(l.id) FROM LearnType l WHERE l.id != :id and UPPER(l.name) = UPPER(:name)")
    , @NamedQuery(name = "LearnType.findById", query = "SELECT l FROM LearnType l WHERE l.id = :id")
    , @NamedQuery(name = "LearnType.findByName", query = "SELECT l FROM LearnType l WHERE l.name = :name")})
public class LearnType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "learnType")
    private List<Protocol> protocolsList;

    public LearnType() {
    }

    public LearnType(String id) {
        this.id = id;
    }
    
    public LearnType(String id, String name) {
        this.id = id;
        this.name = name;
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
        if (!(object instanceof LearnType)) {
            return false;
        }
        LearnType other = (LearnType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.crudapp.LearnType[ id=" + id + " ]";
    }
    
}
