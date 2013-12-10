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
@Table(name = "equivalencia_aprovechamiento", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"eqa_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EquivalenciaAprovechamiento.findAll", query = "SELECT e FROM EquivalenciaAprovechamiento e"),
    @NamedQuery(name = "EquivalenciaAprovechamiento.findByEqaCodigo", query = "SELECT e FROM EquivalenciaAprovechamiento e WHERE e.eqaCodigo = :eqaCodigo"),
    @NamedQuery(name = "EquivalenciaAprovechamiento.findByEqaEscalacualitativa", query = "SELECT e FROM EquivalenciaAprovechamiento e WHERE e.eqaEscalacualitativa = :eqaEscalacualitativa"),
    @NamedQuery(name = "EquivalenciaAprovechamiento.findByEqaEscalacuantiva", query = "SELECT e FROM EquivalenciaAprovechamiento e WHERE e.eqaEscalacuantiva = :eqaEscalacuantiva")})
public class EquivalenciaAprovechamiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "eqa_codigo", nullable = false)
    private Integer eqaCodigo;
    @Size(max = 150)
    @Column(name = "eqa_escalacualitativa", length = 150)
    private String eqaEscalacualitativa;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "eqa_escalacuantiva", precision = 4, scale = 2)
    private BigDecimal eqaEscalacuantiva;
    @JoinColumn(name = "ins_codigo", referencedColumnName = "ins_codigo")
    @ManyToOne
    private Institucion insCodigo;

    public EquivalenciaAprovechamiento() {
    }

    public EquivalenciaAprovechamiento(Integer eqaCodigo) {
        this.eqaCodigo = eqaCodigo;
    }

    public Integer getEqaCodigo() {
        return eqaCodigo;
    }

    public void setEqaCodigo(Integer eqaCodigo) {
        this.eqaCodigo = eqaCodigo;
    }

    public String getEqaEscalacualitativa() {
        return eqaEscalacualitativa;
    }

    public void setEqaEscalacualitativa(String eqaEscalacualitativa) {
        this.eqaEscalacualitativa = eqaEscalacualitativa;
    }

    public BigDecimal getEqaEscalacuantiva() {
        return eqaEscalacuantiva;
    }

    public void setEqaEscalacuantiva(BigDecimal eqaEscalacuantiva) {
        this.eqaEscalacuantiva = eqaEscalacuantiva;
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
        hash += (eqaCodigo != null ? eqaCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquivalenciaAprovechamiento)) {
            return false;
        }
        EquivalenciaAprovechamiento other = (EquivalenciaAprovechamiento) object;
        if ((this.eqaCodigo == null && other.eqaCodigo != null) || (this.eqaCodigo != null && !this.eqaCodigo.equals(other.eqaCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.EquivalenciaAprovechamiento[ eqaCodigo=" + eqaCodigo + " ]";
    }
    
}
