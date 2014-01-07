/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Alumnos;
import entidades.Representante;
import entidades.Cursos;
import entidades.Docentes;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioAlumno;
import servcios.servicioRepresentante;
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
   
}
