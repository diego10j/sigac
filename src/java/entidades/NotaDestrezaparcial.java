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
@Table(name = "nota_destrezaparcial", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"not_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotaDestrezaparcial.findAll", query = "SELECT n FROM NotaDestrezaparcial n"),
    @NamedQuery(name = "NotaDestrezaparcial.findByNotCodigo", query = "SELECT n FROM NotaDestrezaparcial n WHERE n.notCodigo = :notCodigo"),
    @NamedQuery(name = "NotaDestrezaparcial.findByNotTrabajos", query = "SELECT n FROM NotaDestrezaparcial n WHERE n.notTrabajos = :notTrabajos"),
    @NamedQuery(name = "NotaDestrezaparcial.findByNotActividadindividual", query = "SELECT n FROM NotaDestrezaparcial n WHERE n.notActividadindividual = :notActividadindividual"),
    @NamedQuery(name = "NotaDestrezaparcial.findByNotActividadgrupal", query = "SELECT n FROM NotaDestrezaparcial n WHERE n.notActividadgrupal = :notActividadgrupal"),
    @NamedQuery(name = "NotaDestrezaparcial.findByNotLecciones", query = "SELECT n FROM NotaDestrezaparcial n WHERE n.notLecciones = :notLecciones"),
    @NamedQuery(name = "NotaDestrezaparcial.findByNotEvaluacionsumativa", query = "SELECT n FROM NotaDestrezaparcial n WHERE n.notEvaluacionsumativa = :notEvaluacionsumativa"),
    @NamedQuery(name = "NotaDestrezaparcial.findByNotTotal", query = "SELECT n FROM NotaDestrezaparcial n WHERE n.notTotal = :notTotal"),
    @NamedQuery(name = "NotaDestrezaparcial.findByNotPrimerparcial", query = "SELECT n FROM NotaDestrezaparcial n WHERE n.notPrimerparcial = :notPrimerparcial"),
    @NamedQuery(name = "NotaDestrezaparcial.findByNotEqvdestreza", query = "SELECT n FROM NotaDestrezaparcial n WHERE n.notEqvdestreza = :notEqvdestreza"),
    @NamedQuery(name = "NotaDestrezaparcial.findByNotObservacion", query = "SELECT n FROM NotaDestrezaparcial n WHERE n.notObservacion = :notObservacion")})
public class NotaDestrezaparcial implements Serializable {
    @JoinColumn(name = "mat_codigo", referencedColumnName = "mat_codigo")
    @ManyToOne
    private Matricula matCodigo;
    @JoinColumn(name = "for_codigo", referencedColumnName = "for_codigo")
    @ManyToOne
    private Formaevaluar forCodigo;
    @JoinColumn(name = "dis_codigo", referencedColumnName = "dis_codigo")
    @ManyToOne
    private Distributivomxc disCodigo;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "not_codigo", nullable = false)
    private Integer notCodigo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "not_trabajos", precision = 4, scale = 2)
    private BigDecimal notTrabajos;
    @Column(name = "not_actividadindividual", precision = 4, scale = 2)
    private BigDecimal notActividadindividual;
    @Column(name = "not_actividadgrupal", precision = 4, scale = 2)
    private BigDecimal notActividadgrupal;
    @Column(name = "not_lecciones", precision = 4, scale = 2)
    private BigDecimal notLecciones;
    @Column(name = "not_evaluacionsumativa", precision = 4, scale = 2)
    private BigDecimal notEvaluacionsumativa;
    @Column(name = "not_total", precision = 4, scale = 2)
    private BigDecimal notTotal;
    @Column(name = "not_primerparcial", precision = 4, scale = 2)
    private BigDecimal notPrimerparcial;
    @Size(max = 150)
    @Column(name = "not_eqvdestreza", length = 150)
    private String notEqvdestreza;
    @Size(max = 200)
    @Column(name = "not_observacion", length = 200)
    private String notObservacion;
    @JoinColumn(name = "eva_codigo", referencedColumnName = "eva_codigo")
    @ManyToOne
    private Evaluarparcial evaCodigo;
    @JoinColumn(name = "asi_codigo", referencedColumnName = "asi_codigo")
    @ManyToOne
    private Asignaturas asiCodigo;

    public NotaDestrezaparcial() {
    }

    public NotaDestrezaparcial(Integer notCodigo) {
        this.notCodigo = notCodigo;
    }

    public Integer getNotCodigo() {
        return notCodigo;
    }

    public void setNotCodigo(Integer notCodigo) {
        this.notCodigo = notCodigo;
    }

    public BigDecimal getNotTrabajos() {
        return notTrabajos;
    }

    public void setNotTrabajos(BigDecimal notTrabajos) {
        this.notTrabajos = notTrabajos;
    }

    public BigDecimal getNotActividadindividual() {
        return notActividadindividual;
    }

    public void setNotActividadindividual(BigDecimal notActividadindividual) {
        this.notActividadindividual = notActividadindividual;
    }

    public BigDecimal getNotActividadgrupal() {
        return notActividadgrupal;
    }

    public void setNotActividadgrupal(BigDecimal notActividadgrupal) {
        this.notActividadgrupal = notActividadgrupal;
    }

    public BigDecimal getNotLecciones() {
        return notLecciones;
    }

    public void setNotLecciones(BigDecimal notLecciones) {
        this.notLecciones = notLecciones;
    }

    public BigDecimal getNotEvaluacionsumativa() {
        return notEvaluacionsumativa;
    }

    public void setNotEvaluacionsumativa(BigDecimal notEvaluacionsumativa) {
        this.notEvaluacionsumativa = notEvaluacionsumativa;
    }

    public BigDecimal getNotTotal() {
        return notTotal;
    }

    public void setNotTotal(BigDecimal notTotal) {
        this.notTotal = notTotal;
    }

    public BigDecimal getNotPrimerparcial() {
        return notPrimerparcial;
    }

    public void setNotPrimerparcial(BigDecimal notPrimerparcial) {
        this.notPrimerparcial = notPrimerparcial;
    }

    public String getNotEqvdestreza() {
        return notEqvdestreza;
    }

    public void setNotEqvdestreza(String notEqvdestreza) {
        this.notEqvdestreza = notEqvdestreza;
    }

    public String getNotObservacion() {
        return notObservacion;
    }

    public void setNotObservacion(String notObservacion) {
        this.notObservacion = notObservacion;
    }

    public Evaluarparcial getEvaCodigo() {
        return evaCodigo;
    }

    public void setEvaCodigo(Evaluarparcial evaCodigo) {
        this.evaCodigo = evaCodigo;
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
        hash += (notCodigo != null ? notCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotaDestrezaparcial)) {
            return false;
        }
        NotaDestrezaparcial other = (NotaDestrezaparcial) object;
        if ((this.notCodigo == null && other.notCodigo != null) || (this.notCodigo != null && !this.notCodigo.equals(other.notCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.NotaDestrezaparcial[ notCodigo=" + notCodigo + " ]";
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

    public Distributivomxc getDisCodigo() {
        return disCodigo;
    }

    public void setDisCodigo(Distributivomxc disCodigo) {
        this.disCodigo = disCodigo;
    }
    
}
