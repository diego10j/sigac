/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Alumnos;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioAlumno;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorAlumno {

    @EJB
    private servicioAlumno servAlumno;
    private Alumnos aluAlumno = new Alumnos();
    private Utilitario utilitario = new Utilitario();

    public void guardar() {
        if (utilitario.validarCedula(aluAlumno.getAluCedula())) {
            String str_mensaje = servAlumno.guardarAluno(aluAlumno);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                aluAlumno = new Alumnos();
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        } else {
            utilitario.agregarMensajeInfo("La cédula ingresada no es válida", "");
        }
    }

    public Alumnos getAluAlumno() {
        return aluAlumno;
    }

    public void setAluAlumno(Alumnos aluAlumno) {
        this.aluAlumno = aluAlumno;
    }
}
