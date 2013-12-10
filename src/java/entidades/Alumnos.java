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
@Table(name = "alumnos", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"alu_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alumnos.findAll", query = "SELECT a FROM Alumnos a"),
    @NamedQuery(name = "Alumnos.findByAluCodigo", query = "SELECT a FROM Alumnos a WHERE a.aluCodigo = :aluCodigo"),
    @NamedQuery(name = "Alumnos.findByAluCedula", query = "SELECT a FROM Alumnos a WHERE a.aluCedula = :aluCedula"),
    @NamedQuery(name = "Alumnos.findByAluNombres", query = "SELECT a FROM Alumnos a WHERE a.aluNombres = :aluNombres"),
    @NamedQuery(name = "Alumnos.findByAluApellidos", query = "SELECT a FROM Alumnos a WHERE a.aluApellidos = :aluApellidos"),
    @NamedQuery(name = "Alumnos.findByAluSexo", query = "SELECT a FROM Alumnos a WHERE a.aluSexo = :aluSexo"),
    @NamedQuery(name = "Alumnos.findByAluDireccion", query = "SELECT a FROM Alumnos a WHERE a.aluDireccion = :aluDireccion"),
    @NamedQuery(name = "Alumnos.findByAluFechanacimiento", query = "SELECT a FROM Alumnos a WHERE a.aluFechanacimiento = :aluFechanacimiento"),
    @NamedQuery(name = "Alumnos.findByAluLugarnacimiento", query = "SELECT a FROM Alumnos a WHERE a.aluLugarnacimiento = :aluLugarnacimiento"),
    @NamedQuery(name = "Alumnos.findByAluPadre", query = "SELECT a FROM Alumnos a WHERE a.aluPadre = :aluPadre"),
    @NamedQuery(name = "Alumnos.findByAluPrfpadre", query = "SELECT a FROM Alumnos a WHERE a.aluPrfpadre = :aluPrfpadre"),
    @NamedQuery(name = "Alumnos.findByAluMadre", query = "SELECT a FROM Alumnos a WHERE a.aluMadre = :aluMadre"),
    @NamedQuery(name = "Alumnos.findByAluPrfmadre", query = "SELECT a FROM Alumnos a WHERE a.aluPrfmadre = :aluPrfmadre")})
public class Alumnos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "alu_codigo", nullable = false)
    private Integer aluCodigo;
    @Size(max = 20)
    @Column(name = "alu_cedula", length = 20)
    private String aluCedula;
    @Size(max = 250)
    @Column(name = "alu_nombres", length = 250)
    private String aluNombres;
    @Size(max = 250)
    @Column(name = "alu_apellidos", length = 250)
    private String aluApellidos;
    @Column(name = "alu_sexo")
    private Character aluSexo;
    @Size(max = 250)
    @Column(name = "alu_direccion", length = 250)
    private String aluDireccion;
    @Column(name = "alu_fechanacimiento")
    @Temporal(TemporalType.DATE)
    private Date aluFechanacimiento;
    @Size(max = 150)
    @Column(name = "alu_lugarnacimiento", length = 150)
    private String aluLugarnacimiento;
    @Size(max = 200)
    @Column(name = "alu_padre", length = 200)
    private String aluPadre;
    @Size(max = 150)
    @Column(name = "alu_prfpadre", length = 150)
    private String aluPrfpadre;
    @Size(max = 200)
    @Column(name = "alu_madre", length = 200)
    private String aluMadre;
    @Size(max = 150)
    @Column(name = "alu_prfmadre", length = 150)
    private String aluPrfmadre;
    @JoinColumn(name = "ins_codigo", referencedColumnName = "ins_codigo")
    @ManyToOne
    private Institucion insCodigo;
    @OneToMany(mappedBy = "aluCodigo")
    private List<Matricula> matriculaList;
    @OneToMany(mappedBy = "aluCodigo")
    private List<Representante> representanteList;

    public Alumnos() {
    }

    public Alumnos(Integer aluCodigo) {
        this.aluCodigo = aluCodigo;
    }

    public Integer getAluCodigo() {
        return aluCodigo;
    }

    public void setAluCodigo(Integer aluCodigo) {
        this.aluCodigo = aluCodigo;
    }

    public String getAluCedula() {
        return aluCedula;
    }

    public void setAluCedula(String aluCedula) {
        this.aluCedula = aluCedula;
    }

    public String getAluNombres() {
        return aluNombres;
    }

    public void setAluNombres(String aluNombres) {
        this.aluNombres = aluNombres;
    }

    public String getAluApellidos() {
        return aluApellidos;
    }

    public void setAluApellidos(String aluApellidos) {
        this.aluApellidos = aluApellidos;
    }

    public Character getAluSexo() {
        return aluSexo;
    }

    public void setAluSexo(Character aluSexo) {
        this.aluSexo = aluSexo;
    }

    public String getAluDireccion() {
        return aluDireccion;
    }

    public void setAluDireccion(String aluDireccion) {
        this.aluDireccion = aluDireccion;
    }

    public Date getAluFechanacimiento() {
        return aluFechanacimiento;
    }

    public void setAluFechanacimiento(Date aluFechanacimiento) {
        this.aluFechanacimiento = aluFechanacimiento;
    }

    public String getAluLugarnacimiento() {
        return aluLugarnacimiento;
    }

    public void setAluLugarnacimiento(String aluLugarnacimiento) {
        this.aluLugarnacimiento = aluLugarnacimiento;
    }

    public String getAluPadre() {
        return aluPadre;
    }

    public void setAluPadre(String aluPadre) {
        this.aluPadre = aluPadre;
    }

    public String getAluPrfpadre() {
        return aluPrfpadre;
    }

    public void setAluPrfpadre(String aluPrfpadre) {
        this.aluPrfpadre = aluPrfpadre;
    }

    public String getAluMadre() {
        return aluMadre;
    }

    public void setAluMadre(String aluMadre) {
        this.aluMadre = aluMadre;
    }

    public String getAluPrfmadre() {
        return aluPrfmadre;
    }

    public void setAluPrfmadre(String aluPrfmadre) {
        this.aluPrfmadre = aluPrfmadre;
    }

    public Institucion getInsCodigo() {
        return insCodigo;
    }

    public void setInsCodigo(Institucion insCodigo) {
        this.insCodigo = insCodigo;
    }

    @XmlTransient
    public List<Matricula> getMatriculaList() {
        return matriculaList;
    }

    public void setMatriculaList(List<Matricula> matriculaList) {
        this.matriculaList = matriculaList;
    }

    @XmlTransient
    public List<Representante> getRepresentanteList() {
        return representanteList;
    }

    public void setRepresentanteList(List<Representante> representanteList) {
        this.representanteList = representanteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aluCodigo != null ? aluCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alumnos)) {
            return false;
        }
        Alumnos other = (Alumnos) object;
        if ((this.aluCodigo == null && other.aluCodigo != null) || (this.aluCodigo != null && !this.aluCodigo.equals(other.aluCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Alumnos[ aluCodigo=" + aluCodigo + " ]";
    }
    
}
