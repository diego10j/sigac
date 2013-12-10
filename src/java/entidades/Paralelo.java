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
@Table(name = "paralelo", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"par_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Paralelo.findAll", query = "SELECT p FROM Paralelo p"),
    @NamedQuery(name = "Paralelo.findByParCodigo", query = "SELECT p FROM Paralelo p WHERE p.parCodigo = :parCodigo"),
    @NamedQuery(name = "Paralelo.findByParNombre", query = "SELECT p FROM Paralelo p WHERE p.parNombre = :parNombre")})
public class Paralelo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "par_codigo", nullable = false)
    private Integer parCodigo;
    @Size(max = 150)
    @Column(name = "par_nombre", length = 150)
    private String parNombre;
    @OneToMany(mappedBy = "parCodigo")
    private List<CrearCurso> crearCursoList;

    public Paralelo() {
    }

    public Paralelo(Integer parCodigo) {
        this.parCodigo = parCodigo;
    }

    public Integer getParCodigo() {
        return parCodigo;
    }

    public void setParCodigo(Integer parCodigo) {
        this.parCodigo = parCodigo;
    }

    public String getParNombre() {
        return parNombre;
    }

    public void setParNombre(String parNombre) {
        this.parNombre = parNombre;
    }

    @XmlTransient
    public List<CrearCurso> getCrearCursoList() {
        return crearCursoList;
    }

    public void setCrearCursoList(List<CrearCurso> crearCursoList) {
        this.crearCursoList = crearCursoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (parCodigo != null ? parCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paralelo)) {
            return false;
        }
        Paralelo other = (Paralelo) object;
        if ((this.parCodigo == null && other.parCodigo != null) || (this.parCodigo != null && !this.parCodigo.equals(other.parCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Paralelo[ parCodigo=" + parCodigo + " ]";
    }
    
}
