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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "asignaturas", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"asi_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Asignaturas.findAll", query = "SELECT a FROM Asignaturas a"),
    @NamedQuery(name = "Asignaturas.findByAsiCodigo", query = "SELECT a FROM Asignaturas a WHERE a.asiCodigo = :asiCodigo"),
    @NamedQuery(name = "Asignaturas.findByAsiNombre", query = "SELECT a FROM Asignaturas a WHERE a.asiNombre = :asiNombre"),
    @NamedQuery(name = "Asignaturas.findByAsiObservaciones", query = "SELECT a FROM Asignaturas a WHERE a.asiObservaciones = :asiObservaciones")})
public class Asignaturas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "asi_codigo", nullable = false)
    private Integer asiCodigo;
    @Size(max = 200)
    @Column(name = "asi_nombre", length = 200)
    private String asiNombre;
    @Size(max = 250)
    @Column(name = "asi_observaciones", length = 250)
    private String asiObservaciones;
    @OneToMany(mappedBy = "asiCodigo")
    private List<NotaDestrezaparcial> notaDestrezaparcialList;
    @JoinColumn(name = "tip_codigo", referencedColumnName = "tip_codigo")
    @ManyToOne
    private Tipoasignaturas tipCodigo;
    @JoinColumn(name = "ins_codigo", referencedColumnName = "ins_codigo")
    @ManyToOne
    private Institucion insCodigo;
    @OneToMany(mappedBy = "asiAsiCodigo")
    private List<Asignaturas> asignaturasList;
    @JoinColumn(name = "asi_asi_codigo", referencedColumnName = "asi_codigo")
    @ManyToOne
    private Asignaturas asiAsiCodigo;
    @OneToMany(mappedBy = "asiCodigo")
    private List<Distributivomxc> distributivomxcList;

    public Asignaturas() {
    }

    public Asignaturas(Integer asiCodigo) {
        this.asiCodigo = asiCodigo;
    }

    public Integer getAsiCodigo() {
        return asiCodigo;
    }

    public void setAsiCodigo(Integer asiCodigo) {
        this.asiCodigo = asiCodigo;
    }

    public String getAsiNombre() {
        return asiNombre;
    }

    public void setAsiNombre(String asiNombre) {
        this.asiNombre = asiNombre;
    }

    public String getAsiObservaciones() {
        return asiObservaciones;
    }

    public void setAsiObservaciones(String asiObservaciones) {
        this.asiObservaciones = asiObservaciones;
    }

    @XmlTransient
    public List<NotaDestrezaparcial> getNotaDestrezaparcialList() {
        return notaDestrezaparcialList;
    }

    public void setNotaDestrezaparcialList(List<NotaDestrezaparcial> notaDestrezaparcialList) {
        this.notaDestrezaparcialList = notaDestrezaparcialList;
    }

    public Tipoasignaturas getTipCodigo() {
        return tipCodigo;
    }

    public void setTipCodigo(Tipoasignaturas tipCodigo) {
        this.tipCodigo = tipCodigo;
    }

    public Institucion getInsCodigo() {
        return insCodigo;
    }

    public void setInsCodigo(Institucion insCodigo) {
        this.insCodigo = insCodigo;
    }

    @XmlTransient
    public List<Asignaturas> getAsignaturasList() {
        return asignaturasList;
    }

    public void setAsignaturasList(List<Asignaturas> asignaturasList) {
        this.asignaturasList = asignaturasList;
    }

    public Asignaturas getAsiAsiCodigo() {
        return asiAsiCodigo;
    }

    public void setAsiAsiCodigo(Asignaturas asiAsiCodigo) {
        this.asiAsiCodigo = asiAsiCodigo;
    }

    @XmlTransient
    public List<Distributivomxc> getDistributivomxcList() {
        return distributivomxcList;
    }

    public void setDistributivomxcList(List<Distributivomxc> distributivomxcList) {
        this.distributivomxcList = distributivomxcList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asiCodigo != null ? asiCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Asignaturas)) {
            return false;
        }
        Asignaturas other = (Asignaturas) object;
        if ((this.asiCodigo == null && other.asiCodigo != null) || (this.asiCodigo != null && !this.asiCodigo.equals(other.asiCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Asignaturas[ asiCodigo=" + asiCodigo + " ]";
    }
    
}
