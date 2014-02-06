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
@Table(name = "docentes", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"doc_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Docentes.findAll", query = "SELECT d FROM Docentes d"),
    @NamedQuery(name = "Docentes.findByDocCodigo", query = "SELECT d FROM Docentes d WHERE d.docCodigo = :docCodigo"),
    @NamedQuery(name = "Docentes.findByDocCedula", query = "SELECT d FROM Docentes d WHERE d.docCedula = :docCedula"),
    @NamedQuery(name = "Docentes.findByDocFechanacimiento", query = "SELECT d FROM Docentes d WHERE d.docFechanacimiento = :docFechanacimiento"),
    @NamedQuery(name = "Docentes.findByDocNombres", query = "SELECT d FROM Docentes d WHERE d.docNombres = :docNombres"),
    @NamedQuery(name = "Docentes.findByDocDireccion", query = "SELECT d FROM Docentes d WHERE d.docDireccion = :docDireccion"),
    @NamedQuery(name = "Docentes.findByDocTelefono", query = "SELECT d FROM Docentes d WHERE d.docTelefono = :docTelefono"),
    @NamedQuery(name = "Docentes.findByDocCelular", query = "SELECT d FROM Docentes d WHERE d.docCelular = :docCelular"),
    @NamedQuery(name = "Docentes.findByDocEmail", query = "SELECT d FROM Docentes d WHERE d.docEmail = :docEmail"),
    @NamedQuery(name = "Docentes.findByDocHojavida", query = "SELECT d FROM Docentes d WHERE d.docHojavida = :docHojavida")})
public class Docentes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "doc_codigo", nullable = false)
    private Integer docCodigo;
    @Size(max = 10)
    @Column(name = "doc_cedula", length = 10)
    private String docCedula;
    @Column(name = "doc_fechanacimiento")
    @Temporal(TemporalType.DATE)
    private Date docFechanacimiento;
    @Size(max = 250)
    @Column(name = "doc_nombres", length = 250)
    private String docNombres;
    @Size(max = 250)
    @Column(name = "doc_direccion", length = 250)
    private String docDireccion;
    @Size(max = 100)
    @Column(name = "doc_telefono", length = 100)
    private String docTelefono;
    @Size(max = 100)
    @Column(name = "doc_celular", length = 100)
    private String docCelular;
    @Size(max = 200)
    @Column(name = "doc_email", length = 200)
    private String docEmail;
    @Size(max = 200)
    @Column(name = "doc_hojavida", length = 200)
    private String docHojavida;
    @JoinColumn(name = "ins_codigo", referencedColumnName = "ins_codigo")
    @ManyToOne
    private Institucion insCodigo;
    @OneToMany(mappedBy = "docCodigo")
    private List<Usuario> usuarioList;
    @OneToMany(mappedBy = "docCodigo")
    private List<CrearCurso> crearCursoList;
    @OneToMany(mappedBy = "docCodigo")
    private List<Distributivomxc> distributivomxcList;

    public Docentes() {
    }

    public Docentes(Integer docCodigo) {
        this.docCodigo = docCodigo;
    }

    public Integer getDocCodigo() {
        return docCodigo;
    }

    public void setDocCodigo(Integer docCodigo) {
        this.docCodigo = docCodigo;
    }

    public String getDocCedula() {
        return docCedula;
    }

    public void setDocCedula(String docCedula) {
        this.docCedula = docCedula;
    }

    public Date getDocFechanacimiento() {
        return docFechanacimiento;
    }

    public void setDocFechanacimiento(Date docFechanacimiento) {
        this.docFechanacimiento = docFechanacimiento;
    }

    public String getDocNombres() {
        return docNombres;
    }

    public void setDocNombres(String docNombres) {
        this.docNombres = docNombres;
    }

    public String getDocDireccion() {
        return docDireccion;
    }

    public void setDocDireccion(String docDireccion) {
        this.docDireccion = docDireccion;
    }

    public String getDocTelefono() {
        return docTelefono;
    }

    public void setDocTelefono(String docTelefono) {
        this.docTelefono = docTelefono;
    }

    public String getDocCelular() {
        return docCelular;
    }

    public void setDocCelular(String docCelular) {
        this.docCelular = docCelular;
    }

    public String getDocEmail() {
        return docEmail;
    }

    public void setDocEmail(String docEmail) {
        this.docEmail = docEmail;
    }

    public String getDocHojavida() {
        return docHojavida;
    }

    public void setDocHojavida(String docHojavida) {
        this.docHojavida = docHojavida;
    }

    public Institucion getInsCodigo() {
        return insCodigo;
    }

    public void setInsCodigo(Institucion insCodigo) {
        this.insCodigo = insCodigo;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @XmlTransient
    public List<CrearCurso> getCrearCursoList() {
        return crearCursoList;
    }

    public void setCrearCursoList(List<CrearCurso> crearCursoList) {
        this.crearCursoList = crearCursoList;
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
        hash += (docCodigo != null ? docCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Docentes)) {
            return false;
        }
        Docentes other = (Docentes) object;
        if ((this.docCodigo == null && other.docCodigo != null) || (this.docCodigo != null && !this.docCodigo.equals(other.docCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Docentes[ docCodigo=" + docCodigo + " ]";
    }
    
}
