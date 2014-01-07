/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Alumnos;
import entidades.Docentes;
import entidades.Institucion;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioAlumno;
import servcios.servicioDocente;
import servcios.servicioInstitucion;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorInstitucion {

    @EJB
    private servicioInstitucion servInstitucion;
    private Institucion insInstitucion = new Institucion();
    private Utilitario utilitario = new Utilitario();

    public void guardar() {
        
            String str_mensaje = servInstitucion.guardarInstitucion(insInstitucion);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                insInstitucion = new Institucion();
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
       
    }

    public Institucion getInsInstitucion() {
        return insInstitucion;
    }

    public void setInsInstitucion(Institucion insInstitucion) {
        this.insInstitucion = insInstitucion;
    }
    
}
