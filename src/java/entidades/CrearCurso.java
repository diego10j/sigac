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
@Table(name = "crear_curso", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"cre_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CrearCurso.findAll", query = "SELECT c FROM CrearCurso c"),
    @NamedQuery(name = "CrearCurso.findByCreCodigo", query = "SELECT c FROM CrearCurso c WHERE c.creCodigo = :creCodigo"),
    @NamedQuery(name = "CrearCurso.findByCreObservacion", query = "SELECT c FROM CrearCurso c WHERE c.creObservacion = :creObservacion")})
public class CrearCurso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cre_codigo", nullable = false)
    private Integer creCodigo;
    @Size(max = 250)
    @Column(name = "cre_observacion", length = 250)
    private String creObservacion;
    @OneToMany(mappedBy = "creCodigo")
    private List<Matricula> matriculaList;
    @JoinColumn(name = "per_codigo", referencedColumnName = "per_codigo")
    @ManyToOne
    private PeriodoLectivo perCodigo;
    @JoinColumn(name = "par_codigo", referencedColumnName = "par_codigo")
    @ManyToOne
    private Paralelo parCodigo;
    @JoinColumn(name = "doc_codigo", referencedColumnName = "doc_codigo")
    @ManyToOne
    private Docentes docCodigo;
    @JoinColumn(name = "cur_codigo", referencedColumnName = "cur_codigo")
    @ManyToOne
    private Cursos curCodigo;
    @OneToMany(mappedBy = "creCodigo")
    private List<Distributivomxc> distributivomxcList;

    public CrearCurso() {
    }

    public CrearCurso(Integer creCodigo) {
        this.creCodigo = creCodigo;
    }

    public Integer getCreCodigo() {
        return creCodigo;
    }

    public void setCreCodigo(Integer creCodigo) {
        this.creCodigo = creCodigo;
    }

    public String getCreObservacion() {
        return creObservacion;
    }

    public void setCreObservacion(String creObservacion) {
        this.creObservacion = creObservacion;
    }

    @XmlTransient
    public List<Matricula> getMatriculaList() {
        return matriculaList;
    }

    public void setMatriculaList(List<Matricula> matriculaList) {
        this.matriculaList = matriculaList;
    }

    public PeriodoLectivo getPerCodigo() {
        return perCodigo;
    }

    public void setPerCodigo(PeriodoLectivo perCodigo) {
        this.perCodigo = perCodigo;
    }

    public Paralelo getParCodigo() {
        return parCodigo;
    }

    public void setParCodigo(Paralelo parCodigo) {
        this.parCodigo = parCodigo;
    }

    public Docentes getDocCodigo() {
        return docCodigo;
    }

    public void setDocCodigo(Docentes docCodigo) {
        this.docCodigo = docCodigo;
    }

    public Cursos getCurCodigo() {
        return curCodigo;
    }

    public void setCurCodigo(Cursos curCodigo) {
        this.curCodigo = curCodigo;
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
        hash += (creCodigo != null ? creCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CrearCurso)) {
            return false;
        }
        CrearCurso other = (CrearCurso) object;
        if ((this.creCodigo == null && other.creCodigo != null) || (this.creCodigo != null && !this.creCodigo.equals(other.creCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.CrearCurso[ creCodigo=" + creCodigo + " ]";
    }
    
}
