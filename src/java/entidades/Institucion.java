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
@Table(name = "institucion", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ins_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Institucion.findAll", query = "SELECT i FROM Institucion i"),
    @NamedQuery(name = "Institucion.findByInsCodigo", query = "SELECT i FROM Institucion i WHERE i.insCodigo = :insCodigo"),
    @NamedQuery(name = "Institucion.findByInsNombre", query = "SELECT i FROM Institucion i WHERE i.insNombre = :insNombre"),
    @NamedQuery(name = "Institucion.findByInsDireccion", query = "SELECT i FROM Institucion i WHERE i.insDireccion = :insDireccion"),
    @NamedQuery(name = "Institucion.findByInsTelefono", query = "SELECT i FROM Institucion i WHERE i.insTelefono = :insTelefono"),
    @NamedQuery(name = "Institucion.findByInsCorreo", query = "SELECT i FROM Institucion i WHERE i.insCorreo = :insCorreo"),
    @NamedQuery(name = "Institucion.findByInsTipoescuela", query = "SELECT i FROM Institucion i WHERE i.insTipoescuela = :insTipoescuela"),
    @NamedQuery(name = "Institucion.findByInsRector", query = "SELECT i FROM Institucion i WHERE i.insRector = :insRector"),
    @NamedQuery(name = "Institucion.findByInsLogo", query = "SELECT i FROM Institucion i WHERE i.insLogo = :insLogo")})
public class Institucion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ins_codigo", nullable = false)
    private Integer insCodigo;
    @Size(max = 150)
    @Column(name = "ins_nombre", length = 150)
    private String insNombre;
    @Size(max = 200)
    @Column(name = "ins_direccion", length = 200)
    private String insDireccion;
    @Size(max = 30)
    @Column(name = "ins_telefono", length = 30)
    private String insTelefono;
    @Size(max = 250)
    @Column(name = "ins_correo", length = 250)
    private String insCorreo;
    @Size(max = 150)
    @Column(name = "ins_tipoescuela", length = 150)
    private String insTipoescuela;
    @Size(max = 200)
    @Column(name = "ins_rector", length = 200)
    private String insRector;
    @Size(max = 150)
    @Column(name = "ins_logo", length = 150)
    private String insLogo;
    @OneToMany(mappedBy = "insCodigo")
    private List<Docentes> docentesList;
    @OneToMany(mappedBy = "insCodigo")
    private List<Cursos> cursosList;
    @OneToMany(mappedBy = "insCodigo")
    private List<Usuario> usuarioList;
    @OneToMany(mappedBy = "insCodigo")
    private List<Alumnos> alumnosList;
    @OneToMany(mappedBy = "insCodigo")
    private List<EquivalenciaConducta> equivalenciaConductaList;
    @OneToMany(mappedBy = "insCodigo")
    private List<EquivalenciaAprovechamiento> equivalenciaAprovechamientoList;
    @OneToMany(mappedBy = "insCodigo")
    private List<Asignaturas> asignaturasList;
    @OneToMany(mappedBy = "insCodigo")
    private List<PeriodoLectivo> periodoLectivoList;
    @OneToMany(mappedBy = "insCodigo")
    private List<Roles> rolesList;
    @OneToMany(mappedBy = "insCodigo")
    private List<Pantalla> pantallaList;

    public Institucion() {
    }

    public Institucion(Integer insCodigo) {
        this.insCodigo = insCodigo;
    }

    public Integer getInsCodigo() {
        return insCodigo;
    }

    public void setInsCodigo(Integer insCodigo) {
        this.insCodigo = insCodigo;
    }

    public String getInsNombre() {
        return insNombre;
    }

    public void setInsNombre(String insNombre) {
        this.insNombre = insNombre;
    }

    public String getInsDireccion() {
        return insDireccion;
    }

    public void setInsDireccion(String insDireccion) {
        this.insDireccion = insDireccion;
    }

    public String getInsTelefono() {
        return insTelefono;
    }

    public void setInsTelefono(String insTelefono) {
        this.insTelefono = insTelefono;
    }

    public String getInsCorreo() {
        return insCorreo;
    }

    public void setInsCorreo(String insCorreo) {
        this.insCorreo = insCorreo;
    }

    public String getInsTipoescuela() {
        return insTipoescuela;
    }

    public void setInsTipoescuela(String insTipoescuela) {
        this.insTipoescuela = insTipoescuela;
    }

    public String getInsRector() {
        return insRector;
    }

    public void setInsRector(String insRector) {
        this.insRector = insRector;
    }

    public String getInsLogo() {
        return insLogo;
    }

    public void setInsLogo(String insLogo) {
        this.insLogo = insLogo;
    }

    @XmlTransient
    public List<Docentes> getDocentesList() {
        return docentesList;
    }

    public void setDocentesList(List<Docentes> docentesList) {
        this.docentesList = docentesList;
    }

    @XmlTransient
    public List<Cursos> getCursosList() {
        return cursosList;
    }

    public void setCursosList(List<Cursos> cursosList) {
        this.cursosList = cursosList;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @XmlTransient
    public List<Alumnos> getAlumnosList() {
        return alumnosList;
    }

    public void setAlumnosList(List<Alumnos> alumnosList) {
        this.alumnosList = alumnosList;
    }

    @XmlTransient
    public List<EquivalenciaConducta> getEquivalenciaConductaList() {
        return equivalenciaConductaList;
    }

    public void setEquivalenciaConductaList(List<EquivalenciaConducta> equivalenciaConductaList) {
        this.equivalenciaConductaList = equivalenciaConductaList;
    }

    @XmlTransient
    public List<EquivalenciaAprovechamiento> getEquivalenciaAprovechamientoList() {
        return equivalenciaAprovechamientoList;
    }

    public void setEquivalenciaAprovechamientoList(List<EquivalenciaAprovechamiento> equivalenciaAprovechamientoList) {
        this.equivalenciaAprovechamientoList = equivalenciaAprovechamientoList;
    }

    @XmlTransient
    public List<Asignaturas> getAsignaturasList() {
        return asignaturasList;
    }

    public void setAsignaturasList(List<Asignaturas> asignaturasList) {
        this.asignaturasList = asignaturasList;
    }

    @XmlTransient
    public List<PeriodoLectivo> getPeriodoLectivoList() {
        return periodoLectivoList;
    }

    public void setPeriodoLectivoList(List<PeriodoLectivo> periodoLectivoList) {
        this.periodoLectivoList = periodoLectivoList;
    }

    @XmlTransient
    public List<Roles> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<Roles> rolesList) {
        this.rolesList = rolesList;
    }

    @XmlTransient
    public List<Pantalla> getPantallaList() {
        return pantallaList;
    }

    public void setPantallaList(List<Pantalla> pantallaList) {
        this.pantallaList = pantallaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (insCodigo != null ? insCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Institucion)) {
            return false;
        }
        Institucion other = (Institucion) object;
        if ((this.insCodigo == null && other.insCodigo != null) || (this.insCodigo != null && !this.insCodigo.equals(other.insCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Institucion[ insCodigo=" + insCodigo + " ]";
    }
    
}
