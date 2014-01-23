/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.CrearCurso;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import servcios.servicioCrearCurso;
import servcios.servicioPeriodo;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorCrearCurso {

    private Utilitario utilitario = new Utilitario();
    @EJB
    private servicioCrearCurso servCrearCurso;
    @EJB
    private servicioPeriodo servPeriodo;
    private List lisPeriodos;
    private List<CrearCurso> lisCrearCursos;
    private String strPeriodoSeleccionado;

    @PostConstruct
    public void cargarDatos() {
        lisPeriodos = servPeriodo.getListaPeriodos();
        //Carga el periodo activo por defecto      
        if (servPeriodo.getPeriodoActivo() != null) {
            strPeriodoSeleccionado = servPeriodo.getPeriodoActivo().getPerCodigo().toString();
        }      
        lisCrearCursos = servCrearCurso.getCursosCreados(strPeriodoSeleccionado);
    }

    /**
     * Filtra los cursos creados del periodo seleccionado
     *
     * @param evt
     */
    public void filtrarCursos(AjaxBehaviorEvent evt) {
        lisCrearCursos = servCrearCurso.getCursosCreados(strPeriodoSeleccionado);
    }

    public List getLisPeriodos() {
        return lisPeriodos;
    }

    public void setLisPeriodos(List lisPeriodos) {
        this.lisPeriodos = lisPeriodos;
    }

    public List<CrearCurso> getLisCrearCursos() {
        return lisCrearCursos;
    }

    public void setLisCrearCursos(List<CrearCurso> lisCrearCursos) {
        this.lisCrearCursos = lisCrearCursos;
    }

    public String getStrPeriodoSeleccionado() {
        return strPeriodoSeleccionado;
    }

    public void setStrPeriodoSeleccionado(String strPeriodoSeleccionado) {
        this.strPeriodoSeleccionado = strPeriodoSeleccionado;
    }
}
