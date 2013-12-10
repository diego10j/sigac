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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Diego
 */
@Entity
@Table(name = "permisos", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"prm_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permisos.findAll", query = "SELECT p FROM Permisos p"),
    @NamedQuery(name = "Permisos.findByPrmCodigo", query = "SELECT p FROM Permisos p WHERE p.prmCodigo = :prmCodigo")})
public class Permisos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "prm_codigo", nullable = false)
    private Integer prmCodigo;
    @JoinColumn(name = "rol_codigo", referencedColumnName = "rol_codigo")
    @ManyToOne
    private Roles rolCodigo;
    @JoinColumn(name = "pan_codigo", referencedColumnName = "pan_codigo")
    @ManyToOne
    private Pantalla panCodigo;

    public Permisos() {
    }

    public Permisos(Integer prmCodigo) {
        this.prmCodigo = prmCodigo;
    }

    public Integer getPrmCodigo() {
        return prmCodigo;
    }

    public void setPrmCodigo(Integer prmCodigo) {
        this.prmCodigo = prmCodigo;
    }

    public Roles getRolCodigo() {
        return rolCodigo;
    }

    public void setRolCodigo(Roles rolCodigo) {
        this.rolCodigo = rolCodigo;
    }

    public Pantalla getPanCodigo() {
        return panCodigo;
    }

    public void setPanCodigo(Pantalla panCodigo) {
        this.panCodigo = panCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prmCodigo != null ? prmCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permisos)) {
            return false;
        }
        Permisos other = (Permisos) object;
        if ((this.prmCodigo == null && other.prmCodigo != null) || (this.prmCodigo != null && !this.prmCodigo.equals(other.prmCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Permisos[ prmCodigo=" + prmCodigo + " ]";
    }
    
}
