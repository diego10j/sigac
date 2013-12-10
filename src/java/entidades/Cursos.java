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
@Table(name = "cursos", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"cur_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cursos.findAll", query = "SELECT c FROM Cursos c"),
    @NamedQuery(name = "Cursos.findByCurCodigo", query = "SELECT c FROM Cursos c WHERE c.curCodigo = :curCodigo"),
    @NamedQuery(name = "Cursos.findByCurAnio", query = "SELECT c FROM Cursos c WHERE c.curAnio = :curAnio"),
    @NamedQuery(name = "Cursos.findByCurObservaciones", query = "SELECT c FROM Cursos c WHERE c.curObservaciones = :curObservaciones")})
public class Cursos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cur_codigo", nullable = false)
    private Integer curCodigo;
    @Size(max = 150)
    @Column(name = "cur_anio", length = 150)
    private String curAnio;
    @Size(max = 250)
    @Column(name = "cur_observaciones", length = 250)
    private String curObservaciones;
    @JoinColumn(name = "ins_codigo", referencedColumnName = "ins_codigo")
    @ManyToOne
    private Institucion insCodigo;
    @OneToMany(mappedBy = "curCodigo")
    private List<CrearCurso> crearCursoList;

    public Cursos() {
    }

    public Cursos(Integer curCodigo) {
        this.curCodigo = curCodigo;
    }

    public Integer getCurCodigo() {
        return curCodigo;
    }

    public void setCurCodigo(Integer curCodigo) {
        this.curCodigo = curCodigo;
    }

    public String getCurAnio() {
        return curAnio;
    }

    public void setCurAnio(String curAnio) {
        this.curAnio = curAnio;
    }

    public String getCurObservaciones() {
        return curObservaciones;
    }

    public void setCurObservaciones(String curObservaciones) {
        this.curObservaciones = curObservaciones;
    }

    public Institucion getInsCodigo() {
        return insCodigo;
    }

    public void setInsCodigo(Institucion insCodigo) {
        this.insCodigo = insCodigo;
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
        hash += (curCodigo != null ? curCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cursos)) {
            return false;
        }
        Cursos other = (Cursos) object;
        if ((this.curCodigo == null && other.curCodigo != null) || (this.curCodigo != null && !this.curCodigo.equals(other.curCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Cursos[ curCodigo=" + curCodigo + " ]";
    }
    
}
