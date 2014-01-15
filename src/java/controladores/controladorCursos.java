/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Asignaturas;
import entidades.Cursos;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioCursos;


/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorCursos {

    @EJB
    private servicioCursos servCursos;
    private Cursos curCursos = new Cursos();
   
    private Utilitario utilitario = new Utilitario();
    private List<Cursos> listaCursos;

    @PostConstruct
    public void cargarDatos() {
        listaCursos = servCursos.getCursos();
    }
    
    public void insertar(){
        curCursos = new Cursos();
    }
    
    public void guardar() {
        
           String str_mensaje = servCursos.guardarCursos(curCursos);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                curCursos = new Cursos();
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        
    }
   
    public Cursos getCurCursos() {
        return curCursos;
    }

    public void setCurCursos(Cursos curCursos) {
        this.curCursos = curCursos;
    }

    public List<Cursos> getListaCursos() {
        return listaCursos;
    }

    public void setListaCursos(List<Cursos> listaCursos) {
        this.listaCursos = listaCursos;
    }
   
}
