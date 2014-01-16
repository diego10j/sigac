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

    public void eliminar() {
        if (aluAlumno.getAluCodigo() != null) {
            String str_mensaje = servAlumno.elimnarAlumno(aluAlumno.getAluCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }

    public void guardar() {
        System.out.println("....  "+aluAlumno.getAluCedula());
        if (aluAlumno.getAluCedula() != null && utilitario.validarCedula(aluAlumno.getAluCedula())==false) {
            utilitario.agregarMensajeInfo("La cédula ingresada no es válida", "");
            return;
        }
        String str_mensaje = servAlumno.guardarAlumno(aluAlumno);
        if (str_mensaje.isEmpty()) {
            repRepresentante.setAluCodigo(aluAlumno);
            str_mensaje = servRepresentante.guardarRepresentante(repRepresentante);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                repRepresentante = new Representante();
                aluAlumno = new Alumnos();
                cargarDatos();
                utilitario.ejecutarJavaScript("wdlgDetalhe.hide()");
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
