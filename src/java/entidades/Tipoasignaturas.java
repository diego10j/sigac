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
@Table(name = "tipoasignaturas", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tip_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipoasignaturas.findAll", query = "SELECT t FROM Tipoasignaturas t"),
    @NamedQuery(name = "Tipoasignaturas.findByTipCodigo", query = "SELECT t FROM Tipoasignaturas t WHERE t.tipCodigo = :tipCodigo"),
    @NamedQuery(name = "Tipoasignaturas.findByTipNombre", query = "SELECT t FROM Tipoasignaturas t WHERE t.tipNombre = :tipNombre")})
public class Tipoasignaturas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "tip_codigo", nullable = false)
    private Integer tipCodigo;
    @Size(max = 200)
    @Column(name = "tip_nombre", length = 200)
    private String tipNombre;
    @OneToMany(mappedBy = "tipCodigo")
    private List<Asignaturas> asignaturasList;

    public Tipoasignaturas() {
    }

    public Tipoasignaturas(Integer tipCodigo) {
        this.tipCodigo = tipCodigo;
    }

    public Integer getTipCodigo() {
        return tipCodigo;
    }

    public void setTipCodigo(Integer tipCodigo) {
        this.tipCodigo = tipCodigo;
    }

    public String getTipNombre() {
        return tipNombre;
    }

    public void setTipNombre(String tipNombre) {
        this.tipNombre = tipNombre;
    }

    @XmlTransient
    public List<Asignaturas> getAsignaturasList() {
        return asignaturasList;
    }

    public void setAsignaturasList(List<Asignaturas> asignaturasList) {
        this.asignaturasList = asignaturasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipCodigo != null ? tipCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipoasignaturas)) {
            return false;
        }
        Tipoasignaturas other = (Tipoasignaturas) object;
        if ((this.tipCodigo == null && other.tipCodigo != null) || (this.tipCodigo != null && !this.tipCodigo.equals(other.tipCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Tipoasignaturas[ tipCodigo=" + tipCodigo + " ]";
    }
    
}
