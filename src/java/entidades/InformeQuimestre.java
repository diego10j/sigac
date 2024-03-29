/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Diego
 */
@Entity
@Table(name = "informe_quimestre", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"inf_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InformeQuimestre.findAll", query = "SELECT i FROM InformeQuimestre i"),
    @NamedQuery(name = "InformeQuimestre.findByInfCodigo", query = "SELECT i FROM InformeQuimestre i WHERE i.infCodigo = :infCodigo"),
    @NamedQuery(name = "InformeQuimestre.findByInfEqv80", query = "SELECT i FROM InformeQuimestre i WHERE i.infEqv80 = :infEqv80"),
    @NamedQuery(name = "InformeQuimestre.findByInfExa20", query = "SELECT i FROM InformeQuimestre i WHERE i.infExa20 = :infExa20"),
    @NamedQuery(name = "InformeQuimestre.findByInfNota", query = "SELECT i FROM InformeQuimestre i WHERE i.infNota = :infNota"),
    @NamedQuery(name = "InformeQuimestre.findByInfEqvquimestre", query = "SELECT i FROM InformeQuimestre i WHERE i.infEqvquimestre = :infEqvquimestre"),
    @NamedQuery(name = "InformeQuimestre.findByInfExamen", query = "SELECT i FROM InformeQuimestre i WHERE i.infExamen = :infExamen"),
    @NamedQuery(name = "InformeQuimestre.findByInfSumatoria", query = "SELECT i FROM InformeQuimestre i WHERE i.infSumatoria = :infSumatoria")})
public class InformeQuimestre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "inf_codigo", nullable = false)
    private Integer infCodigo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "inf_eqv80", precision = 4, scale = 2)
    private BigDecimal infEqv80;
    @Column(name = "inf_exa20", precision = 4, scale = 2)
    private BigDecimal infExa20;
    @Column(name = "inf_nota", precision = 4, scale = 2)
    private BigDecimal infNota;
    @Size(max = 150)
    @Column(name = "inf_eqvquimestre", length = 150)
    private String infEqvquimestre;
    @Column(name = "inf_examen", precision = 4, scale = 2)
    private BigDecimal infExamen;
    @Column(name = "inf_sumatoria", precision = 4, scale = 2)
    private BigDecimal infSumatoria;
    @JoinColumn(name = "mat_codigo", referencedColumnName = "mat_codigo")
    @ManyToOne
    private Matricula matCodigo;
    @JoinColumn(name = "for_codigo", referencedColumnName = "for_codigo")
    @ManyToOne
    private Formaevaluar forCodigo;
    @JoinColumn(name = "dis_codigo", referencedColumnName = "dis_codigo")
    @ManyToOne
    private Distributivomxc disCodigo;

    public InformeQuimestre() {
    }

    public InformeQuimestre(Integer infCodigo) {
        this.infCodigo = infCodigo;
    }

    public Integer getInfCodigo() {
        return infCodigo;
    }

    public void setInfCodigo(Integer infCodigo) {
        this.infCodigo = infCodigo;
    }

    public BigDecimal getInfEqv80() {
        return infEqv80;
    }

    public void setInfEqv80(BigDecimal infEqv80) {
        this.infEqv80 = infEqv80;
    }

    public BigDecimal getInfExa20() {
        return infExa20;
    }

    public void setInfExa20(BigDecimal infExa20) {
        this.infExa20 = infExa20;
    }

    public BigDecimal getInfNota() {
        return infNota;
    }

    public void setInfNota(BigDecimal infNota) {
        this.infNota = infNota;
    }

    public String getInfEqvquimestre() {
        return infEqvquimestre;
    }

    public void setInfEqvquimestre(String infEqvquimestre) {
        this.infEqvquimestre = infEqvquimestre;
    }

    public BigDecimal getInfExamen() {
        return infExamen;
    }

    public void setInfExamen(BigDecimal infExamen) {
        this.infExamen = infExamen;
    }

    public BigDecimal getInfSumatoria() {
        return infSumatoria;
    }

    public void setInfSumatoria(BigDecimal infSumatoria) {
        this.infSumatoria = infSumatoria;
    }

    public Matricula getMatCodigo() {
        return matCodigo;
    }

    public void setMatCodigo(Matricula matCodigo) {
        this.matCodigo = matCodigo;
    }

    public Formaevaluar getForCodigo() {
        return forCodigo;
    }

    public void setForCodigo(Formaevaluar forCodigo) {
        this.forCodigo = forCodigo;
    }

    public Distributivomxc getDisCodigo() {
        return disCodigo;
    }

    public void setDisCodigo(Distributivomxc disCodigo) {
        this.disCodigo = disCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (infCodigo != null ? infCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InformeQuimestre)) {
            return false;
        }
        InformeQuimestre other = (InformeQuimestre) object;
        if ((this.infCodigo == null && other.infCodigo != null) || (this.infCodigo != null && !this.infCodigo.equals(other.infCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.InformeQuimestre[ infCodigo=" + infCodigo + " ]";
    }
    
}
