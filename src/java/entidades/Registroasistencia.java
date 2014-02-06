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
@Table(name = "registroasistencia", catalog = "injp", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"reg_codigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Registroasistencia.findAll", query = "SELECT r FROM Registroasistencia r"),
    @NamedQuery(name = "Registroasistencia.findByRegCodigo", query = "SELECT r FROM Registroasistencia r WHERE r.regCodigo = :regCodigo"),
    @NamedQuery(name = "Registroasistencia.findByRegAtrasos", query = "SELECT r FROM Registroasistencia r WHERE r.regAtrasos = :regAtrasos"),
    @NamedQuery(name = "Registroasistencia.findByRegFaltasjustificadas", query = "SELECT r FROM Registroasistencia r WHERE r.regFaltasjustificadas = :regFaltasjustificadas"),
    @NamedQuery(name = "Registroasistencia.findByRegFaltasinjustificadas", query = "SELECT r FROM Registroasistencia r WHERE r.regFaltasinjustificadas = :regFaltasinjustificadas"),
    @NamedQuery(name = "Registroasistencia.findByRegTotalfaltas", query = "SELECT r FROM Registroasistencia r WHERE r.regTotalfaltas = :regTotalfaltas"),
    @NamedQuery(name = "Registroasistencia.findByRegDiaslaborados", query = "SELECT r FROM Registroasistencia r WHERE r.regDiaslaborados = :regDiaslaborados")})
public class Registroasistencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "reg_codigo", nullable = false)
    private Integer regCodigo;
    @Column(name = "reg_atrasos")
    private Integer regAtrasos;
    @Column(name = "reg_faltasjustificadas")
    private Integer regFaltasjustificadas;
    @Column(name = "reg_faltasinjustificadas")
    private Integer regFaltasinjustificadas;
    @Column(name = "reg_totalfaltas")
    private Integer regTotalfaltas;
    @Column(name = "reg_diaslaborados")
    private Integer regDiaslaborados;
    @JoinColumn(name = "mat_codigo", referencedColumnName = "mat_codigo")
    @ManyToOne
    private Matricula matCodigo;
    @JoinColumn(name = "for_codigo", referencedColumnName = "for_codigo")
    @ManyToOne
    private Formaevaluar forCodigo;
    @JoinColumn(name = "eva_codigo", referencedColumnName = "eva_codigo")
    @ManyToOne
    private Evaluarparcial evaCodigo;

    public Registroasistencia() {
    }

    public Registroasistencia(Integer regCodigo) {
        this.regCodigo = regCodigo;
    }

    public Integer getRegCodigo() {
        return regCodigo;
    }

    public void setRegCodigo(Integer regCodigo) {
        this.regCodigo = regCodigo;
    }

    public Integer getRegAtrasos() {
        return regAtrasos;
    }

    public void setRegAtrasos(Integer regAtrasos) {
        this.regAtrasos = regAtrasos;
    }

    public Integer getRegFaltasjustificadas() {
        return regFaltasjustificadas;
    }

    public void setRegFaltasjustificadas(Integer regFaltasjustificadas) {
        this.regFaltasjustificadas = regFaltasjustificadas;
    }

    public Integer getRegFaltasinjustificadas() {
        return regFaltasinjustificadas;
    }

    public void setRegFaltasinjustificadas(Integer regFaltasinjustificadas) {
        this.regFaltasinjustificadas = regFaltasinjustificadas;
    }

    public Integer getRegTotalfaltas() {
        return regTotalfaltas;
    }

    public void setRegTotalfaltas(Integer regTotalfaltas) {
        this.regTotalfaltas = regTotalfaltas;
    }

    public Integer getRegDiaslaborados() {
        return regDiaslaborados;
    }

    public void setRegDiaslaborados(Integer regDiaslaborados) {
        this.regDiaslaborados = regDiaslaborados;
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

    public Evaluarparcial getEvaCodigo() {
        return evaCodigo;
    }

    public void setEvaCodigo(Evaluarparcial evaCodigo) {
        this.evaCodigo = evaCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (regCodigo != null ? regCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Registroasistencia)) {
            return false;
        }
        Registroasistencia other = (Registroasistencia) object;
        if ((this.regCodigo == null && other.regCodigo != null) || (this.regCodigo != null && !this.regCodigo.equals(other.regCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Registroasistencia[ regCodigo=" + regCodigo + " ]";
    }
    
}
