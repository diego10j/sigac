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
@Table(name = "comportamientoparcial", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"com_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comportamientoparcial.findAll", query = "SELECT c FROM Comportamientoparcial c"),
    @NamedQuery(name = "Comportamientoparcial.findByComCodigo", query = "SELECT c FROM Comportamientoparcial c WHERE c.comCodigo = :comCodigo"),
    @NamedQuery(name = "Comportamientoparcial.findByComSemana1", query = "SELECT c FROM Comportamientoparcial c WHERE c.comSemana1 = :comSemana1"),
    @NamedQuery(name = "Comportamientoparcial.findByComSemana2", query = "SELECT c FROM Comportamientoparcial c WHERE c.comSemana2 = :comSemana2"),
    @NamedQuery(name = "Comportamientoparcial.findByComSemana3", query = "SELECT c FROM Comportamientoparcial c WHERE c.comSemana3 = :comSemana3"),
    @NamedQuery(name = "Comportamientoparcial.findByComSemana4", query = "SELECT c FROM Comportamientoparcial c WHERE c.comSemana4 = :comSemana4"),
    @NamedQuery(name = "Comportamientoparcial.findByComSemana5", query = "SELECT c FROM Comportamientoparcial c WHERE c.comSemana5 = :comSemana5"),
    @NamedQuery(name = "Comportamientoparcial.findByComSumatoria", query = "SELECT c FROM Comportamientoparcial c WHERE c.comSumatoria = :comSumatoria"),
    @NamedQuery(name = "Comportamientoparcial.findByComEquivalencia", query = "SELECT c FROM Comportamientoparcial c WHERE c.comEquivalencia = :comEquivalencia"),
    @NamedQuery(name = "Comportamientoparcial.findByComEqui", query = "SELECT c FROM Comportamientoparcial c WHERE c.comEqui = :comEqui")})
public class Comportamientoparcial implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "com_codigo", nullable = false)
    private Integer comCodigo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "com_semana1", precision = 4, scale = 2)
    private BigDecimal comSemana1;
    @Column(name = "com_semana2", precision = 4, scale = 2)
    private BigDecimal comSemana2;
    @Column(name = "com_semana3", precision = 4, scale = 2)
    private BigDecimal comSemana3;
    @Column(name = "com_semana4", precision = 4, scale = 2)
    private BigDecimal comSemana4;
    @Column(name = "com_semana5", precision = 4, scale = 2)
    private BigDecimal comSemana5;
    @Column(name = "com_sumatoria", precision = 4, scale = 2)
    private BigDecimal comSumatoria;
    @Size(max = 100)
    @Column(name = "com_equivalencia", length = 100)
    private String comEquivalencia;
    @Column(name = "com_equi")
    private Character comEqui;
    @JoinColumn(name = "mat_codigo", referencedColumnName = "mat_codigo")
    @ManyToOne
    private Matricula matCodigo;
    @JoinColumn(name = "for_codigo", referencedColumnName = "for_codigo")
    @ManyToOne
    private Formaevaluar forCodigo;
    @JoinColumn(name = "eva_codigo", referencedColumnName = "eva_codigo")
    @ManyToOne
    private Evaluarparcial evaCodigo;

    public Comportamientoparcial() {
    }

    public Comportamientoparcial(Integer comCodigo) {
        this.comCodigo = comCodigo;
    }

    public Integer getComCodigo() {
        return comCodigo;
    }

    public void setComCodigo(Integer comCodigo) {
        this.comCodigo = comCodigo;
    }

    public BigDecimal getComSemana1() {
        return comSemana1;
    }

    public void setComSemana1(BigDecimal comSemana1) {
        this.comSemana1 = comSemana1;
    }

    public BigDecimal getComSemana2() {
        return comSemana2;
    }

    public void setComSemana2(BigDecimal comSemana2) {
        this.comSemana2 = comSemana2;
    }

    public BigDecimal getComSemana3() {
        return comSemana3;
    }

    public void setComSemana3(BigDecimal comSemana3) {
        this.comSemana3 = comSemana3;
    }

    public BigDecimal getComSemana4() {
        return comSemana4;
    }

    public void setComSemana4(BigDecimal comSemana4) {
        this.comSemana4 = comSemana4;
    }

    public BigDecimal getComSemana5() {
        return comSemana5;
    }

    public void setComSemana5(BigDecimal comSemana5) {
        this.comSemana5 = comSemana5;
    }

    public BigDecimal getComSumatoria() {
        return comSumatoria;
    }

    public void setComSumatoria(BigDecimal comSumatoria) {
        this.comSumatoria = comSumatoria;
    }

    public String getComEquivalencia() {
        return comEquivalencia;
    }

    public void setComEquivalencia(String comEquivalencia) {
        this.comEquivalencia = comEquivalencia;
    }

    public Character getComEqui() {
        return comEqui;
    }

    public void setComEqui(Character comEqui) {
        this.comEqui = comEqui;
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

    public Evaluarparcial getEvaCodigo() {
        return evaCodigo;
    }

    public void setEvaCodigo(Evaluarparcial evaCodigo) {
        this.evaCodigo = evaCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (comCodigo != null ? comCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comportamientoparcial)) {
            return false;
        }
        Comportamientoparcial other = (Comportamientoparcial) object;
        if ((this.comCodigo == null && other.comCodigo != null) || (this.comCodigo != null && !this.comCodigo.equals(other.comCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Comportamientoparcial[ comCodigo=" + comCodigo + " ]";
    }
    
}
