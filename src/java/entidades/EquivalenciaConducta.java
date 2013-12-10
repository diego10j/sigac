/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
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
@Table(name = "equivalencia_conducta", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"eqc_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EquivalenciaConducta.findAll", query = "SELECT e FROM EquivalenciaConducta e"),
    @NamedQuery(name = "EquivalenciaConducta.findByEqcCodigo", query = "SELECT e FROM EquivalenciaConducta e WHERE e.eqcCodigo = :eqcCodigo"),
    @NamedQuery(name = "EquivalenciaConducta.findByEqcAlterno", query = "SELECT e FROM EquivalenciaConducta e WHERE e.eqcAlterno = :eqcAlterno"),
    @NamedQuery(name = "EquivalenciaConducta.findByEqcEscala", query = "SELECT e FROM EquivalenciaConducta e WHERE e.eqcEscala = :eqcEscala"),
    @NamedQuery(name = "EquivalenciaConducta.findByEqcDescripcion", query = "SELECT e FROM EquivalenciaConducta e WHERE e.eqcDescripcion = :eqcDescripcion")})
public class EquivalenciaConducta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "eqc_codigo", nullable = false)
    private Integer eqcCodigo;
    @Size(max = 1)
    @Column(name = "eqc_alterno", length = 1)
    private String eqcAlterno;
    @Size(max = 100)
    @Column(name = "eqc_escala", length = 100)
    private String eqcEscala;
    @Size(max = 150)
    @Column(name = "eqc_descripcion", length = 150)
    private String eqcDescripcion;
    @JoinColumn(name = "ins_codigo", referencedColumnName = "ins_codigo")
    @ManyToOne
    private Institucion insCodigo;

    public EquivalenciaConducta() {
    }

    public EquivalenciaConducta(Integer eqcCodigo) {
        this.eqcCodigo = eqcCodigo;
    }

    public Integer getEqcCodigo() {
        return eqcCodigo;
    }

    public void setEqcCodigo(Integer eqcCodigo) {
        this.eqcCodigo = eqcCodigo;
    }

    public String getEqcAlterno() {
        return eqcAlterno;
    }

    public void setEqcAlterno(String eqcAlterno) {
        this.eqcAlterno = eqcAlterno;
    }

    public String getEqcEscala() {
        return eqcEscala;
    }

    public void setEqcEscala(String eqcEscala) {
        this.eqcEscala = eqcEscala;
    }

    public String getEqcDescripcion() {
        return eqcDescripcion;
    }

    public void setEqcDescripcion(String eqcDescripcion) {
        this.eqcDescripcion = eqcDescripcion;
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
        hash += (eqcCodigo != null ? eqcCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquivalenciaConducta)) {
            return false;
        }
        EquivalenciaConducta other = (EquivalenciaConducta) object;
        if ((this.eqcCodigo == null && other.eqcCodigo != null) || (this.eqcCodigo != null && !this.eqcCodigo.equals(other.eqcCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.EquivalenciaConducta[ eqcCodigo=" + eqcCodigo + " ]";
    }
    
}
