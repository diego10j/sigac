/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.CrearCurso;
import entidades.Cursos;
import entidades.Docentes;
import entidades.EquivalenciaConducta;
import entidades.Institucion;
import entidades.Paralelo;
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
    private CrearCurso creCrearc= new CrearCurso() ;
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

     public void insertar() {
        creCrearc = new CrearCurso();
        creCrearc.setCurCodigo(new Cursos());
        creCrearc.setParCodigo(new Paralelo());
        creCrearc.setDocCodigo(new Docentes());
    }

    public void eliminar() {
        if (creCrearc.getCreCodigo() != null) {
            String str_mensaje = servCrearCurso.elimnarCrearCurso(creCrearc.getCreCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }

      public void guardar() {
               
           String str_mensaje = servCrearCurso.guardarCrearCurso(creCrearc);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                creCrearc = new CrearCurso();
                cargarDatos();
                utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        
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

    public servicioCrearCurso getServCrearCurso() {
        return servCrearCurso;
    }

    public void setServCrearCurso(servicioCrearCurso servCrearCurso) {
        this.servCrearCurso = servCrearCurso;
    }

    public servicioPeriodo getServPeriodo() {
        return servPeriodo;
    }

    public void setServPeriodo(servicioPeriodo servPeriodo) {
        this.servPeriodo = servPeriodo;
    }

    public CrearCurso getCreCrearc() {
        return creCrearc;
    }

    public void setCreCrearc(CrearCurso creCrearc) {
        this.creCrearc = creCrearc;
    }

    
    
  
    
}
