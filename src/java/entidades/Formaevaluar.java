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
@Table(name = "formaevaluar", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"for_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Formaevaluar.findAll", query = "SELECT f FROM Formaevaluar f"),
    @NamedQuery(name = "Formaevaluar.findByForCodigo", query = "SELECT f FROM Formaevaluar f WHERE f.forCodigo = :forCodigo"),
    @NamedQuery(name = "Formaevaluar.findByForNombre", query = "SELECT f FROM Formaevaluar f WHERE f.forNombre = :forNombre")})
public class Formaevaluar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "for_codigo", nullable = false)
    private Integer forCodigo;
    @Size(max = 150)
    @Column(name = "for_nombre", length = 150)
    private String forNombre;
    @OneToMany(mappedBy = "forCodigo")
    private List<InformeQuimestre> informeQuimestreList;

    public Formaevaluar() {
    }

    public Formaevaluar(Integer forCodigo) {
        this.forCodigo = forCodigo;
    }

    public Integer getForCodigo() {
        return forCodigo;
    }

    public void setForCodigo(Integer forCodigo) {
        this.forCodigo = forCodigo;
    }

    public String getForNombre() {
        return forNombre;
    }

    public void setForNombre(String forNombre) {
        this.forNombre = forNombre;
    }

    @XmlTransient
    public List<InformeQuimestre> getInformeQuimestreList() {
        return informeQuimestreList;
    }

    public void setInformeQuimestreList(List<InformeQuimestre> informeQuimestreList) {
        this.informeQuimestreList = informeQuimestreList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (forCodigo != null ? forCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Formaevaluar)) {
            return false;
        }
        Formaevaluar other = (Formaevaluar) object;
        if ((this.forCodigo == null && other.forCodigo != null) || (this.forCodigo != null && !this.forCodigo.equals(other.forCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Formaevaluar[ forCodigo=" + forCodigo + " ]";
    }
    
}
