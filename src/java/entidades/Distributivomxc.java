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
@Table(name = "distributivomxc", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"dis_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Distributivomxc.findAll", query = "SELECT d FROM Distributivomxc d"),
    @NamedQuery(name = "Distributivomxc.findByDisCodigo", query = "SELECT d FROM Distributivomxc d WHERE d.disCodigo = :disCodigo")})
public class Distributivomxc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "dis_codigo", nullable = false)
    private Integer disCodigo;
    @OneToMany(mappedBy = "disCodigo")
    private List<NotaDestrezaparcial> notaDestrezaparcialList;
    @JoinColumn(name = "doc_codigo", referencedColumnName = "doc_codigo")
    @ManyToOne
    private Docentes docCodigo;
    @JoinColumn(name = "cre_codigo", referencedColumnName = "cre_codigo")
    @ManyToOne
    private CrearCurso creCodigo;
    @JoinColumn(name = "asi_codigo", referencedColumnName = "asi_codigo")
    @ManyToOne
    private Asignaturas asiCodigo;

    public Distributivomxc() {
    }

    public Distributivomxc(Integer disCodigo) {
        this.disCodigo = disCodigo;
    }

    public Integer getDisCodigo() {
        return disCodigo;
    }

    public void setDisCodigo(Integer disCodigo) {
        this.disCodigo = disCodigo;
    }

    @XmlTransient
    public List<NotaDestrezaparcial> getNotaDestrezaparcialList() {
        return notaDestrezaparcialList;
    }

    public void setNotaDestrezaparcialList(List<NotaDestrezaparcial> notaDestrezaparcialList) {
        this.notaDestrezaparcialList = notaDestrezaparcialList;
    }

    public Docentes getDocCodigo() {
        return docCodigo;
    }

    public void setDocCodigo(Docentes docCodigo) {
        this.docCodigo = docCodigo;
    }

    public CrearCurso getCreCodigo() {
        return creCodigo;
    }

    public void setCreCodigo(CrearCurso creCodigo) {
        this.creCodigo = creCodigo;
    }

    public Asignaturas getAsiCodigo() {
        return asiCodigo;
    }

    public void setAsiCodigo(Asignaturas asiCodigo) {
        this.asiCodigo = asiCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (disCodigo != null ? disCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Distributivomxc)) {
            return false;
        }
        Distributivomxc other = (Distributivomxc) object;
        if ((this.disCodigo == null && other.disCodigo != null) || (this.disCodigo != null && !this.disCodigo.equals(other.disCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Distributivomxc[ disCodigo=" + disCodigo + " ]";
    }
    
}
