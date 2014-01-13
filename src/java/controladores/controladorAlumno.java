/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Alumnos;
import entidades.Representante;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioAlumno;
import servcios.servicioRepresentante;

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
    @EJB
    private servicioRepresentante servRepresentante;
    private Representante repRepresentante = new Representante();
    private Utilitario utilitario = new Utilitario();
    private List<Alumnos> listaAlimnos;

    @PostConstruct
    public void cargarDatos() {
        listaAlimnos = servAlumno.getAlumnos();
    }

    public void guardar() {
        if (utilitario.validarCedula(aluAlumno.getAluCedula())) {
            String str_mensaje = servAlumno.guardarAlumno(aluAlumno);
            if (str_mensaje.isEmpty()) {
                repRepresentante.setAluCodigo(aluAlumno);
                str_mensaje = servRepresentante.guardarRepresentante(repRepresentante);
                if (str_mensaje.isEmpty()) {
                    utilitario.agregarMensaje("Se guardo correctamente", "");
                    repRepresentante = new Representante();
                } else {
                    utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
                }
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

    public Representante getRepRepresentante() {
        return repRepresentante;
    }

    public void setRepRepresentante(Representante repRepresentante) {
        this.repRepresentante = repRepresentante;
    }
}
