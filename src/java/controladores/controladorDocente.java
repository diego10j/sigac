/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Alumnos;
import entidades.Docentes;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioAlumno;
import servcios.servicioDocente;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorDocente {

    @EJB
    private servicioDocente servDocente;
    private Docentes docDocente = new Docentes();
    private Utilitario utilitario = new Utilitario();

    public void guardar() {
        if (utilitario.validarCedula(docDocente.getDocCedula())) {
            String str_mensaje = servDocente.guardarDocente(docDocente);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                docDocente = new Docentes();
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        } else {
            utilitario.agregarMensajeInfo("La cédula ingresada no es válida", "");
        }
    }

    public Docentes getDocente() {
        return docDocente;
    }

    public void setDocDocente(Docentes docDocente) {
        this.docDocente = docDocente;
    }
}
