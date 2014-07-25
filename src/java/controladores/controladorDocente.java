/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Docentes;
import framework.reportes.GenerarReporte;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import servcios.servicioDocente;
import servcios.servicioInstitucion;

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
    private List<Docentes> listaDocentes;
    private List<Docentes> filtroDocentes;
    @EJB
    private servicioInstitucion servInstitucion;
    private String str_path_reporte;

    @PostConstruct
    public void cargarDatos() {
        listaDocentes = servDocente.getDocentes();
        str_path_reporte = utilitario.getURL() + "/reportes/reporte" + utilitario.getVariable("ide_usua") + ".pdf";
    }

    public void insertar() {
        docDocente = new Docentes();
    }

    public void eliminar() {
        if (docDocente.getDocCodigo() != null) {
            String str_mensaje = servDocente.elimnarDocente(docDocente.getDocCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }

    public void guardar() {
        docDocente.setInsCodigo(servInstitucion.getIntitucion());
        if (utilitario.validarCedula(docDocente.getDocCedula())) {
            String str_mensaje = servDocente.guardarDocente(docDocente);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                docDocente = new Docentes();
                cargarDatos();
                utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        } else {
            utilitario.agregarMensajeInfo("La cédula ingresada no es válida", "");
        }
    }

    public void verReporteListadoDocentes() {
        Map p = new HashMap();
        p.put("", null);
        GenerarReporte generar = new GenerarReporte();
        generar.generar(p, "/reportes/rep_docentes/rep_docentes.jasper");
    }

    public void verReporteListadoCargoDocentes() {
        Map p = new HashMap();
        p.put("", null);
        GenerarReporte generar = new GenerarReporte();
        generar.generar(p, "/reportes/rep_docentes/rep_cargo_docente.jasper");
    }

    public Docentes getDocDocente() {
        return docDocente;
    }

    public void setDocDocente(Docentes docDocente) {
        this.docDocente = docDocente;
    }

    public List<Docentes> getListaDocentes() {
        return listaDocentes;
    }

    public void setListaDocentes(List<Docentes> listaDocentes) {
        this.listaDocentes = listaDocentes;
    }

    public List<Docentes> getFiltroDocentes() {
        return filtroDocentes;
    }

    public void setFiltroDocentes(List<Docentes> filtroDocentes) {
        this.filtroDocentes = filtroDocentes;
    }

    public String getStr_path_reporte() {
        return str_path_reporte;
    }

    public void setStr_path_reporte(String str_path_reporte) {
        this.str_path_reporte = str_path_reporte;
    }
}
