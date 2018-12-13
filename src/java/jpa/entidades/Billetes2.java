/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author oscarhsalvettig
 */
@Entity
@Table(name = "billetes2")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Billetes2.findAll", query = "SELECT b FROM Billetes2 b order by 1")
    , @NamedQuery(name = "Billetes2.findByDenominacion", query = "SELECT b FROM Billetes2 b WHERE b.denominacion = :denominacion")
    , @NamedQuery(name = "Billetes2.findByCantidad", query = "SELECT b FROM Billetes2 b WHERE b.cantidad = :cantidad")})
public class Billetes2 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull(message = "error: La Denominacion es requerida")
   
    @Column(name = "denominacion")
    private Integer denominacion;
    
    @NotNull(message = "error: La Cantidad es requerida")
    @Column(name = "cantidad")
    private Integer cantidad;

    public Billetes2() {
    }

    public Billetes2(Integer denominacion) {
        this.denominacion = denominacion;
    }

    public Integer getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(Integer denominacion) {
        this.denominacion = denominacion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (denominacion != null ? denominacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Billetes2)) {
            return false;
        }
        Billetes2 other = (Billetes2) object;
        if ((this.denominacion == null && other.denominacion != null) || (this.denominacion != null && !this.denominacion.equals(other.denominacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entidades.Billetes2[ denominacion=" + denominacion + " ]";
    }
    
}
