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
@Table(name = "representante", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"rep_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Representante.findAll", query = "SELECT r FROM Representante r"),
    @NamedQuery(name = "Representante.findByRepCodigo", query = "SELECT r FROM Representante r WHERE r.repCodigo = :repCodigo"),
    @NamedQuery(name = "Representante.findByRepNombres", query = "SELECT r FROM Representante r WHERE r.repNombres = :repNombres"),
    @NamedQuery(name = "Representante.findByRepParentesco", query = "SELECT r FROM Representante r WHERE r.repParentesco = :repParentesco"),
    @NamedQuery(name = "Representante.findByRepTelefono", query = "SELECT r FROM Representante r WHERE r.repTelefono = :repTelefono"),
    @NamedQuery(name = "Representante.findByRepCelular", query = "SELECT r FROM Representante r WHERE r.repCelular = :repCelular")})
public class Representante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "rep_codigo", nullable = false)
    private Integer repCodigo;
    @Size(max = 250)
    @Column(name = "rep_nombres", length = 250)
    private String repNombres;
    @Size(max = 100)
    @Column(name = "rep_parentesco", length = 100)
    private String repParentesco;
    @Size(max = 100)
    @Column(name = "rep_telefono", length = 100)
    private String repTelefono;
    @Size(max = 250)
    @Column(name = "rep_celular", length = 250)
    private String repCelular;
    @JoinColumn(name = "alu_codigo", referencedColumnName = "alu_codigo")
    @ManyToOne
    private Alumnos aluCodigo;

    public Representante() {
    }

    public Representante(Integer repCodigo) {
        this.repCodigo = repCodigo;
    }

    public Integer getRepCodigo() {
        return repCodigo;
    }

    public void setRepCodigo(Integer repCodigo) {
        this.repCodigo = repCodigo;
    }

    public String getRepNombres() {
        return repNombres;
    }

    public void setRepNombres(String repNombres) {
        this.repNombres = repNombres;
    }

    public String getRepParentesco() {
        return repParentesco;
    }

    public void setRepParentesco(String repParentesco) {
        this.repParentesco = repParentesco;
    }

    public String getRepTelefono() {
        return repTelefono;
    }

    public void setRepTelefono(String repTelefono) {
        this.repTelefono = repTelefono;
    }

    public String getRepCelular() {
        return repCelular;
    }

    public void setRepCelular(String repCelular) {
        this.repCelular = repCelular;
    }

    public Alumnos getAluCodigo() {
        return aluCodigo;
    }

    public void setAluCodigo(Alumnos aluCodigo) {
        this.aluCodigo = aluCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (repCodigo != null ? repCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Representante)) {
            return false;
        }
        Representante other = (Representante) object;
        if ((this.repCodigo == null && other.repCodigo != null) || (this.repCodigo != null && !this.repCodigo.equals(other.repCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Representante[ repCodigo=" + repCodigo + " ]";
    }
    
}
