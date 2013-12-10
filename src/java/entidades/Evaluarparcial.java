/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Diego
 */
@Entity
@Table(name = "evaluarparcial", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"eva_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Evaluarparcial.findAll", query = "SELECT e FROM Evaluarparcial e"),
    @NamedQuery(name = "Evaluarparcial.findByEvaCodigo", query = "SELECT e FROM Evaluarparcial e WHERE e.evaCodigo = :evaCodigo"),
    @NamedQuery(name = "Evaluarparcial.findByEvaNombre", query = "SELECT e FROM Evaluarparcial e WHERE e.evaNombre = :evaNombre")})
public class Evaluarparcial implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "eva_codigo", nullable = false)
    private Integer evaCodigo;
    @Size(max = 150)
    @Column(name = "eva_nombre", length = 150)
    private String evaNombre;
    @OneToMany(mappedBy = "evaCodigo")
    private List<NotaDestrezaparcial> notaDestrezaparcialList;
    @OneToMany(mappedBy = "evaCodigo")
    private List<Comportamientoparcial> comportamientoparcialList;
    @OneToMany(mappedBy = "evaCodigo")
    private List<Registroasistencia> registroasistenciaList;

    public Evaluarparcial() {
    }

    public Evaluarparcial(Integer evaCodigo) {
        this.evaCodigo = evaCodigo;
    }

    public Integer getEvaCodigo() {
        return evaCodigo;
    }

    public void setEvaCodigo(Integer evaCodigo) {
        this.evaCodigo = evaCodigo;
    }

    public String getEvaNombre() {
        return evaNombre;
    }

    public void setEvaNombre(String evaNombre) {
        this.evaNombre = evaNombre;
    }

    @XmlTransient
    public List<NotaDestrezaparcial> getNotaDestrezaparcialList() {
        return notaDestrezaparcialList;
    }

    public void setNotaDestrezaparcialList(List<NotaDestrezaparcial> notaDestrezaparcialList) {
        this.notaDestrezaparcialList = notaDestrezaparcialList;
    }

    @XmlTransient
    public List<Comportamientoparcial> getComportamientoparcialList() {
        return comportamientoparcialList;
    }

    public void setComportamientoparcialList(List<Comportamientoparcial> comportamientoparcialList) {
        this.comportamientoparcialList = comportamientoparcialList;
    }

    @XmlTransient
    public List<Registroasistencia> getRegistroasistenciaList() {
        return registroasistenciaList;
    }

    public void setRegistroasistenciaList(List<Registroasistencia> registroasistenciaList) {
        this.registroasistenciaList = registroasistenciaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evaCodigo != null ? evaCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Evaluarparcial)) {
            return false;
        }
        Evaluarparcial other = (Evaluarparcial) object;
        if ((this.evaCodigo == null && other.evaCodigo != null) || (this.evaCodigo != null && !this.evaCodigo.equals(other.evaCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Evaluarparcial[ evaCodigo=" + evaCodigo + " ]";
    }
    
}
