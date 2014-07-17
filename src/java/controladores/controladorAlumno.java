/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Alumnos;
import entidades.Representante;
import framework.reportes.GenerarReporte;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioAlumno;
import servcios.servicioInstitucion;
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
    private List<Alumnos> filtroAlumnos;
    @EJB
    private servicioInstitucion servInstitucion;
    private String str_path_reporte;

    @PostConstruct
    public void cargarDatos() {
        listaAlumnos = servAlumno.getAlumnos();
        aluAlumno = new Alumnos();
        repRepresentante = new Representante();
        str_path_reporte = utilitario.getURL() + "/reportes/reporte" + utilitario.getVariable("ide_usua") + ".pdf";

    }

    public void insertar() {
        aluAlumno = new Alumnos();
        repRepresentante = new Representante();
    }

    public void eliminar() {
        if (aluAlumno.getAluCodigo() != null) {
            cargarRepresentante();
            if (repRepresentante.getRepCodigo() != null) {
                servRepresentante.elimnarRepresentante(repRepresentante.getRepCodigo() + "");
            }
            String str_mensaje = servAlumno.elimnarAlumno(aluAlumno.getAluCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }

    public void cargarRepresentante() {
        repRepresentante = servAlumno.getRepresentanteAlumno(aluAlumno.getAluCodigo() + "");
        if (repRepresentante == null) {
            repRepresentante = new Representante();

        }
    }

    public void guardar() {
        aluAlumno.setInsCodigo(servInstitucion.getIntitucion());
        if (aluAlumno.getAluCedula() != null && aluAlumno.getAluCedula().isEmpty() == false && utilitario.validarCedula(aluAlumno.getAluCedula()) == false) {
            utilitario.agregarMensajeInfo("La cédula ingresada no es válida", "");
            return;
        }
        boolean nuevo = true;
        if (aluAlumno.getAluCodigo() != null) {
            nuevo = false;
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
                if (!nuevo) {
                    utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
                }
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        } else {
            utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
        }
    }

    public void verReporteListadoAlumnos() {
        Map p = new HashMap();
        p.put("", null);
        GenerarReporte generar = new GenerarReporte();
        generar.generar(p, "/reportes/rep_alumnos/rep_listado_alumnos.jasper");
    }

    public void verReporteXLSListadoAlumnos() {
        Map p = new HashMap();
        p.put("", null);
        GenerarReporte generar = new GenerarReporte();
        generar.generar(p, "/reportes/rep_alumnos/rep_listado_alumnos.jasper");
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

    public List<Alumnos> getFiltroAlumnos() {
        return filtroAlumnos;
    }

    public void setFiltroAlumnos(List<Alumnos> filtroAlumnos) {
        this.filtroAlumnos = filtroAlumnos;
    }

    public String getStr_path_reporte() {
        return str_path_reporte;
    }

    public void setStr_path_reporte(String str_path_reporte) {
        this.str_path_reporte = str_path_reporte;
    }
}
