/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Diego
 */
@Entity
@Table(name = "matricula", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"mat_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Matricula.findAll", query = "SELECT m FROM Matricula m"),
    @NamedQuery(name = "Matricula.findByMatCodigo", query = "SELECT m FROM Matricula m WHERE m.matCodigo = :matCodigo"),
    @NamedQuery(name = "Matricula.findByMatPromediogeneral", query = "SELECT m FROM Matricula m WHERE m.matPromediogeneral = :matPromediogeneral"),
    @NamedQuery(name = "Matricula.findByMatDisciplinageneral", query = "SELECT m FROM Matricula m WHERE m.matDisciplinageneral = :matDisciplinageneral"),
    @NamedQuery(name = "Matricula.findByMatAprobado", query = "SELECT m FROM Matricula m WHERE m.matAprobado = :matAprobado"),
    @NamedQuery(name = "Matricula.findByMatActivo", query = "SELECT m FROM Matricula m WHERE m.matActivo = :matActivo")})
public class Matricula implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "mat_codigo", nullable = false)
    private Integer matCodigo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "mat_promediogeneral", precision = 4, scale = 2)
    private BigDecimal matPromediogeneral;
    @Column(name = "mat_disciplinageneral")
    private Character matDisciplinageneral;
    @Column(name = "mat_aprobado")
    private Boolean matAprobado;
    @Column(name = "mat_activo")
    private Boolean matActivo;
    @OneToMany(mappedBy = "matCodigo")
    private List<NotaDestrezaparcial> notaDestrezaparcialList;
    @OneToMany(mappedBy = "matCodigo")
    private List<InformeQuimestre> informeQuimestreList;
    @JoinColumn(name = "cre_codigo", referencedColumnName = "cre_codigo")
    @ManyToOne
    private CrearCurso creCodigo;
    @JoinColumn(name = "alu_codigo", referencedColumnName = "alu_codigo")
    @ManyToOne
    private Alumnos aluCodigo;
    @OneToMany(mappedBy = "matCodigo")
    private List<Comportamientoparcial> comportamientoparcialList;
    @OneToMany(mappedBy = "matCodigo")
    private List<Registroasistencia> registroasistenciaList;

    public Matricula() {
    }

    public Matricula(Integer matCodigo) {
        this.matCodigo = matCodigo;
    }

    public Integer getMatCodigo() {
        return matCodigo;
    }

    public void setMatCodigo(Integer matCodigo) {
        this.matCodigo = matCodigo;
    }

    public BigDecimal getMatPromediogeneral() {
        return matPromediogeneral;
    }

    public void setMatPromediogeneral(BigDecimal matPromediogeneral) {
        this.matPromediogeneral = matPromediogeneral;
    }

    public Character getMatDisciplinageneral() {
        return matDisciplinageneral;
    }

    public void setMatDisciplinageneral(Character matDisciplinageneral) {
        this.matDisciplinageneral = matDisciplinageneral;
    }

    public Boolean getMatAprobado() {
        return matAprobado;
    }

    public void setMatAprobado(Boolean matAprobado) {
        this.matAprobado = matAprobado;
    }

    public Boolean getMatActivo() {
        return matActivo;
    }

    public void setMatActivo(Boolean matActivo) {
        this.matActivo = matActivo;
    }

    @XmlTransient
    public List<NotaDestrezaparcial> getNotaDestrezaparcialList() {
        return notaDestrezaparcialList;
    }

    public void setNotaDestrezaparcialList(List<NotaDestrezaparcial> notaDestrezaparcialList) {
        this.notaDestrezaparcialList = notaDestrezaparcialList;
    }

    @XmlTransient
    public List<InformeQuimestre> getInformeQuimestreList() {
        return informeQuimestreList;
    }

    public void setInformeQuimestreList(List<InformeQuimestre> informeQuimestreList) {
        this.informeQuimestreList = informeQuimestreList;
    }

    public CrearCurso getCreCodigo() {
        return creCodigo;
    }

    public void setCreCodigo(CrearCurso creCodigo) {
        this.creCodigo = creCodigo;
    }

    public Alumnos getAluCodigo() {
        return aluCodigo;
    }

    public void setAluCodigo(Alumnos aluCodigo) {
        this.aluCodigo = aluCodigo;
    }

    @XmlTransient
    public List<Comportamientoparcial> getComportamientoparcialList() {
        return comportamientoparcialList;
    }

    public void setComportamientoparcialList(List<Comportamientoparcial> comportamientoparcialList) {
        this.comportamientoparcialList = comportamientoparcialList;
    }

    @XmlTransient
    public List<Registroasistencia> getRegistroasistenciaList() {
        return registroasistenciaList;
    }

    public void setRegistroasistenciaList(List<Registroasistencia> registroasistenciaList) {
        this.registroasistenciaList = registroasistenciaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matCodigo != null ? matCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Matricula)) {
            return false;
        }
        Matricula other = (Matricula) object;
        if ((this.matCodigo == null && other.matCodigo != null) || (this.matCodigo != null && !this.matCodigo.equals(other.matCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Matricula[ matCodigo=" + matCodigo + " ]";
    }
    
}
