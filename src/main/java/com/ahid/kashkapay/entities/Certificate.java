/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cccc
 */
@Entity
@Table(name = "certificates")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Certificate.findAll", query = "SELECT c FROM Certificate c")
    , @NamedQuery(name = "Certificate.existedCountByName", query = "SELECT count(c.id) FROM Certificate c WHERE c.certificateNumber = :certificateNumber and c.certificateDate = :certificateDate")
    , @NamedQuery(name = "Certificate.existedCountByNotIdAndName", query = "SELECT count(c.id) FROM Certificate c WHERE c.id != :id and c.certificateNumber = :certificateNumber and c.certificateDate = :certificateDate")
    , @NamedQuery(name = "Certificate.countByProtocol", query = "SELECT count(c.id) FROM Certificate c WHERE c.protocol.id = :id")
    , @NamedQuery(name = "Certificate.findById", query = "SELECT c FROM Certificate c WHERE c.id = :id")
    , @NamedQuery(name = "Certificate.findByCertificateNumber", query = "SELECT c FROM Certificate c WHERE c.certificateNumber = :certificateNumber")
    , @NamedQuery(name = "Certificate.findByCertificateDate", query = "SELECT c FROM Certificate c WHERE c.certificateDate = :certificateDate")
    , @NamedQuery(name = "Certificate.findByFullname", query = "SELECT c FROM Certificate c WHERE c.fullname = :fullname")
    , @NamedQuery(name = "Certificate.findByBirthDate", query = "SELECT c FROM Certificate c WHERE c.birthDate = :birthDate")})
public class Certificate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "certificate_number")
    private String certificateNumber;
    @Column(name = "certificate_date")
    private String certificateDate;
    @Column(name = "fullname")
    private String fullname;
    @Column(name = "birth_date")
    private String birthDate;
    @JoinColumn(name = "protocol_id", referencedColumnName = "id")
    @ManyToOne
    private Protocol protocol;

    public Certificate() {
    }

    public Certificate(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getCertificateDate() {
        return certificateDate;
    }

    public void setCertificateDate(String certificateDate) {
        this.certificateDate = certificateDate;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
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
        if (!(object instanceof Certificate)) {
            return false;
        }
        Certificate other = (Certificate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.crudapp.Certificate[ id=" + id + " ]";
    }
    
}
