/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "periodo_lectivo", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"per_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PeriodoLectivo.findAll", query = "SELECT p FROM PeriodoLectivo p"),
    @NamedQuery(name = "PeriodoLectivo.findByPerCodigo", query = "SELECT p FROM PeriodoLectivo p WHERE p.perCodigo = :perCodigo"),
    @NamedQuery(name = "PeriodoLectivo.findByPerNombre", query = "SELECT p FROM PeriodoLectivo p WHERE p.perNombre = :perNombre"),
    @NamedQuery(name = "PeriodoLectivo.findByPerFechainicio", query = "SELECT p FROM PeriodoLectivo p WHERE p.perFechainicio = :perFechainicio"),
    @NamedQuery(name = "PeriodoLectivo.findByPerFechafin", query = "SELECT p FROM PeriodoLectivo p WHERE p.perFechafin = :perFechafin"),
    @NamedQuery(name = "PeriodoLectivo.findByPerActivo", query = "SELECT p FROM PeriodoLectivo p WHERE p.perActivo = :perActivo")})
public class PeriodoLectivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "per_codigo", nullable = false)
    private Integer perCodigo;
    @Size(max = 250)
    @Column(name = "per_nombre", length = 250)
    private String perNombre;
    @Column(name = "per_fechainicio")
    @Temporal(TemporalType.DATE)
    private Date perFechainicio;
    @Column(name = "per_fechafin")
    @Temporal(TemporalType.DATE)
    private Date perFechafin;
    @Column(name = "per_activo")
    private Boolean perActivo;
    @JoinColumn(name = "ins_codigo", referencedColumnName = "ins_codigo")
    @ManyToOne
    private Institucion insCodigo;
    @OneToMany(mappedBy = "perCodigo")
    private List<CrearCurso> crearCursoList;

    public PeriodoLectivo() {
    }

    public PeriodoLectivo(Integer perCodigo) {
        this.perCodigo = perCodigo;
    }

    public Integer getPerCodigo() {
        return perCodigo;
    }

    public void setPerCodigo(Integer perCodigo) {
        this.perCodigo = perCodigo;
    }

    public String getPerNombre() {
        return perNombre;
    }

    public void setPerNombre(String perNombre) {
        this.perNombre = perNombre;
    }

    public Date getPerFechainicio() {
        return perFechainicio;
    }

    public void setPerFechainicio(Date perFechainicio) {
        this.perFechainicio = perFechainicio;
    }

    public Date getPerFechafin() {
        return perFechafin;
    }

    public void setPerFechafin(Date perFechafin) {
        this.perFechafin = perFechafin;
    }

    public Boolean getPerActivo() {
        return perActivo;
    }

    public void setPerActivo(Boolean perActivo) {
        this.perActivo = perActivo;
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
        hash += (perCodigo != null ? perCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PeriodoLectivo)) {
            return false;
        }
        PeriodoLectivo other = (PeriodoLectivo) object;
        if ((this.perCodigo == null && other.perCodigo != null) || (this.perCodigo != null && !this.perCodigo.equals(other.perCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.PeriodoLectivo[ perCodigo=" + perCodigo + " ]";
    }
    
}
