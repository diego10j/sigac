/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Diego
 */
@Entity
@Table(name = "usuario", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usu_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByUsuCodigo", query = "SELECT u FROM Usuario u WHERE u.usuCodigo = :usuCodigo"),
    @NamedQuery(name = "Usuario.findByUsuNombre", query = "SELECT u FROM Usuario u WHERE u.usuNombre = :usuNombre"),
    @NamedQuery(name = "Usuario.findByUsuNick", query = "SELECT u FROM Usuario u WHERE u.usuNick = :usuNick"),
    @NamedQuery(name = "Usuario.findByUsuClave", query = "SELECT u FROM Usuario u WHERE u.usuClave = :usuClave"),
    @NamedQuery(name = "Usuario.findByUsuFechacreacion", query = "SELECT u FROM Usuario u WHERE u.usuFechacreacion = :usuFechacreacion")})
public class Usuario implements Serializable {
    @JoinColumn(name = "doc_codigo", referencedColumnName = "doc_codigo")
    @ManyToOne
    private Docentes docCodigo;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "usu_codigo", nullable = false)
    private Integer usuCodigo;
    @Size(max = 150)
    @Column(name = "usu_nombre", length = 150)
    private String usuNombre;
    @Size(max = 150)
    @Column(name = "usu_nick", length = 150)
    private String usuNick;
    @Size(max = 100)
    @Column(name = "usu_clave", length = 100)
    private String usuClave;
    @Column(name = "usu_fechacreacion")
    @Temporal(TemporalType.DATE)
    private Date usuFechacreacion;
    @JoinColumn(name = "rol_codigo", referencedColumnName = "rol_codigo")
    @ManyToOne
    private Roles rolCodigo;
    @JoinColumn(name = "ins_codigo", referencedColumnName = "ins_codigo")
    @ManyToOne
    private Institucion insCodigo;

    public Usuario() {
    }

    public Usuario(Integer usuCodigo) {
        this.usuCodigo = usuCodigo;
    }

    public Integer getUsuCodigo() {
        return usuCodigo;
    }

    public void setUsuCodigo(Integer usuCodigo) {
        this.usuCodigo = usuCodigo;
    }

    public String getUsuNombre() {
        return usuNombre;
    }

    public void setUsuNombre(String usuNombre) {
        this.usuNombre = usuNombre;
    }

    public String getUsuNick() {
        return usuNick;
    }

    public void setUsuNick(String usuNick) {
        this.usuNick = usuNick;
    }

    public String getUsuClave() {
        return usuClave;
    }

    public void setUsuClave(String usuClave) {
        this.usuClave = usuClave;
    }

    public Date getUsuFechacreacion() {
        return usuFechacreacion;
    }

    public void setUsuFechacreacion(Date usuFechacreacion) {
        this.usuFechacreacion = usuFechacreacion;
    }

    public Roles getRolCodigo() {
        return rolCodigo;
    }

    public void setRolCodigo(Roles rolCodigo) {
        this.rolCodigo = rolCodigo;
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
        hash += (usuCodigo != null ? usuCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.usuCodigo == null && other.usuCodigo != null) || (this.usuCodigo != null && !this.usuCodigo.equals(other.usuCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Usuario[ usuCodigo=" + usuCodigo + " ]";
    }

    public Docentes getDocCodigo() {
        return docCodigo;
    }

    public void setDocCodigo(Docentes docCodigo) {
        this.docCodigo = docCodigo;
    }
    
}
