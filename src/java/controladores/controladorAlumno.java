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
    private Alumnos aluAlumno;
    @EJB
    private servicioRepresentante servRepresentante;
    private Representante repRepresentante = new Representante();
    private Utilitario utilitario = new Utilitario();
    private List<Alumnos> listaAlumnos;

    @PostConstruct
    public void cargarDatos() {
        listaAlumnos = servAlumno.getAlumnos();
        aluAlumno = new Alumnos();
        repRepresentante = new Representante();
    }

    public void insertar() {
        aluAlumno = new Alumnos();
        repRepresentante = new Representante();
    }

    public void modificar(Alumnos alumno) {
        aluAlumno=alumno;
    }

    public void eliminar(Alumnos alumno) {
        if (alumno.getAluCodigo() != null) {
            String str_mensaje = servAlumno.elimnarAlumno(alumno.getAluCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }

    public void guardar(Alumnos alumno) {
        if (alumno.getAluCedula() != null && utilitario.validarCedula(alumno.getAluCedula())) {
            utilitario.agregarMensajeInfo("La cédula ingresada no es válida", "");
            return;
        }
        String str_mensaje = servAlumno.guardarAlumno(alumno);
        if (str_mensaje.isEmpty()) {
            repRepresentante.setAluCodigo(alumno);
            str_mensaje = servRepresentante.guardarRepresentante(repRepresentante);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                repRepresentante = new Representante();
                aluAlumno = new Alumnos();
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        } else {
            utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
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

    public List<Alumnos> getListaAlumnos() {
        return listaAlumnos;
    }

    public void setListaAlumnos(List<Alumnos> listaAlumnos) {
        this.listaAlumnos = listaAlumnos;
    }
}
