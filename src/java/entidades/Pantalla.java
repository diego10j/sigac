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
@Table(name = "pantalla", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"pan_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pantalla.findAll", query = "SELECT p FROM Pantalla p"),
    @NamedQuery(name = "Pantalla.findByPanCodigo", query = "SELECT p FROM Pantalla p WHERE p.panCodigo = :panCodigo"),
    @NamedQuery(name = "Pantalla.findByPanNombre", query = "SELECT p FROM Pantalla p WHERE p.panNombre = :panNombre")})
public class Pantalla implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "pan_codigo", nullable = false)
    private Integer panCodigo;
    @Size(max = 150)
    @Column(name = "pan_nombre", length = 150)
    private String panNombre;
    @OneToMany(mappedBy = "panCodigo")
    private List<Permisos> permisosList;
    @OneToMany(mappedBy = "panPanCodigo")
    private List<Pantalla> pantallaList;
    @JoinColumn(name = "pan_pan_codigo", referencedColumnName = "pan_codigo")
    @ManyToOne
    private Pantalla panPanCodigo;
    @JoinColumn(name = "ins_codigo", referencedColumnName = "ins_codigo")
    @ManyToOne
    private Institucion insCodigo;

    public Pantalla() {
    }

    public Pantalla(Integer panCodigo) {
        this.panCodigo = panCodigo;
    }

    public Integer getPanCodigo() {
        return panCodigo;
    }

    public void setPanCodigo(Integer panCodigo) {
        this.panCodigo = panCodigo;
    }

    public String getPanNombre() {
        return panNombre;
    }

    public void setPanNombre(String panNombre) {
        this.panNombre = panNombre;
    }

    @XmlTransient
    public List<Permisos> getPermisosList() {
        return permisosList;
    }

    public void setPermisosList(List<Permisos> permisosList) {
        this.permisosList = permisosList;
    }

    @XmlTransient
    public List<Pantalla> getPantallaList() {
        return pantallaList;
    }

    public void setPantallaList(List<Pantalla> pantallaList) {
        this.pantallaList = pantallaList;
    }

    public Pantalla getPanPanCodigo() {
        return panPanCodigo;
    }

    public void setPanPanCodigo(Pantalla panPanCodigo) {
        this.panPanCodigo = panPanCodigo;
    }

    public Institucion getInsCodigo() {
        return insCodigo;
    }

    public void setInsCodigo(Institucion insCodigo) {
        this.insCodigo = insCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (panCodigo != null ? panCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pantalla)) {
            return false;
        }
        Pantalla other = (Pantalla) object;
        if ((this.panCodigo == null && other.panCodigo != null) || (this.panCodigo != null && !this.panCodigo.equals(other.panCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Pantalla[ panCodigo=" + panCodigo + " ]";
    }
    
}
