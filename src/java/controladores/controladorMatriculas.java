/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.CrearCurso;
import entidades.Matricula;
import entidades.PeriodoLectivo;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;
import servcios.servicioCrearCurso;
import servcios.servicioMatriculas;
import servcios.servicioPeriodo;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorMatriculas {
    
    @EJB
    private servicioMatriculas servMatriculas;
    private Matricula matMatricula = new Matricula();
    private Utilitario utilitario = new Utilitario();
    private List<Matricula> listaMatriculas;
    private List<Matricula> filtroMatriculas;
    @EJB
    private servicioCrearCurso servCrearCurso;
    @EJB
    private servicioPeriodo servPeriodo;
    private PeriodoLectivo perActivo; //Periodo activo para matricular alumnos
    private List<CrearCurso> lisCrearCursos;
    private CrearCurso creCursoSeleccionado;
    
    @PostConstruct
    public void cargarDatos() {
        
        perActivo = servPeriodo.getPeriodoActivo();
        if (perActivo == null) {
            utilitario.agregarMensajeError("No tiene ningun Período Lectivo Activo", "");
            return;
        }
        
        lisCrearCursos = servCrearCurso.getCursosCreados(perActivo.getPerCodigo().toString());
        if (creCursoSeleccionado == null) {
            if (lisCrearCursos != null && lisCrearCursos.isEmpty() == false) {
                creCursoSeleccionado = lisCrearCursos.get(0);
            }
        }
        if (creCursoSeleccionado == null) {
            listaMatriculas = servMatriculas.getMatriculas("-1");
        } else {
            listaMatriculas = servMatriculas.getMatriculas(creCursoSeleccionado.getCreCodigo().toString());
        }
    }
    
    public void seleccionarCurso(SelectEvent evt) {
        lisCrearCursos = servCrearCurso.getCursosCreados(perActivo.getPerCodigo().toString());
    }
    
    public void insertar() {
        matMatricula = new Matricula();
    }
    
    public void eliminar() {
        if (matMatricula.getMatCodigo() != null) {
            String str_mensaje = servMatriculas.elimnarMatriculas(matMatricula.getMatCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }
    
    public void guardar() {
        
        if (creCursoSeleccionado != null) {
            matMatricula.setCreCodigo(servCrearCurso.getCrearCurso(creCursoSeleccionado.getCreCodigo().toString()));
        }
        String str_mensaje = servMatriculas.guardarMatriculas(matMatricula);
        if (str_mensaje.isEmpty()) {
            utilitario.agregarMensaje("Se guardo correctamente", "");
            matMatricula = new Matricula();
            cargarDatos();
            utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
        } else {
            utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
        }
        
    }
    
    public Matricula getMatMatricula() {
        return matMatricula;
    }
    
    public void setMatMatricula(Matricula matMatricula) {
        this.matMatricula = matMatricula;
    }
    
    public List<Matricula> getListaMatriculas() {
        return listaMatriculas;
    }
    
    public void setListaMatriculas(List<Matricula> listaMatriculas) {
        this.listaMatriculas = listaMatriculas;
    }
    
    public List<Matricula> getFiltroMatriculas() {
        return filtroMatriculas;
    }
    
    public void setFiltroMatriculas(List<Matricula> filtroMatriculas) {
        this.filtroMatriculas = filtroMatriculas;
    }
    
    public PeriodoLectivo getPerActivo() {
        return perActivo;
    }
    
    public void setPerActivo(PeriodoLectivo perActivo) {
        this.perActivo = perActivo;
    }
    
    public List<CrearCurso> getLisCrearCursos() {
        return lisCrearCursos;
    }
    
    public void setLisCrearCursos(List<CrearCurso> lisCrearCursos) {
        this.lisCrearCursos = lisCrearCursos;
    }
    
    public CrearCurso getCreCursoSeleccionado() {
        return creCursoSeleccionado;
    }
    
    public void setCreCursoSeleccionado(CrearCurso creCursoSeleccionado) {
        this.creCursoSeleccionado = creCursoSeleccionado;
    }
}
