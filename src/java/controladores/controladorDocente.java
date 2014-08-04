/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Docentes;
import entidades.PeriodoLectivo;
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
import servcios.servicioPeriodo;

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
    private String strPeriodoSeleccionado;
    private List lisPeriodos;
    @EJB
    private servicioPeriodo servPeriodo;
    private PeriodoLectivo perActivo;

    @PostConstruct
    public void cargarDatos() {
        lisPeriodos = servPeriodo.getListaPeriodos();
        perActivo = servPeriodo.getPeriodoActivo();
        if (perActivo == null) {
            utilitario.agregarMensajeError("No tiene ningun Período Lectivo Activo", "");
            return;
        }
        strPeriodoSeleccionado = perActivo.getPerCodigo().toString();
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
            boolean nuevo = true;
            if (docDocente.getDocCodigo() != null) {
                nuevo = false;
            }
            String str_mensaje = servDocente.guardarDocente(docDocente);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
                docDocente = new Docentes();
                if (!nuevo) {
                    cargarDatos();
                    utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
                }

            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        } else {
            utilitario.agregarMensajeInfo("La cédula ingresada no es válida", "");
        }
    }

    public void verReporteListadoDocentes(String formato) {
        Map p = new HashMap();
        p.put("", null);
        GenerarReporte generar = new GenerarReporte();
        if (formato == null || formato.equalsIgnoreCase("xls")) {
            generar.generarXLS(p, "/reportes/rep_docentes/rep_docentes.jasper");
        } else {
            generar.generar(p, "/reportes/rep_docentes/rep_docentes.jasper");
        }
    }

    public void verReporteListadoCargoDocentes(String formato) {
        Map p = new HashMap();
        int int_periodo = -1;
        try {
            int_periodo = Integer.parseInt(strPeriodoSeleccionado);
        } catch (Exception e) {
            int_periodo = -1;
        }
        p.put("periodo", int_periodo);
        GenerarReporte generar = new GenerarReporte();
        if (formato == null || formato.equalsIgnoreCase("xls")) {
            generar.generarXLS(p, "/reportes/rep_docentes/rep_cargo_docente.jasper");
        } else {
            generar.generar(p, "/reportes/rep_docentes/rep_cargo_docente.jasper");
        }
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

    public String getStrPeriodoSeleccionado() {
        return strPeriodoSeleccionado;
    }

    public void setStrPeriodoSeleccionado(String strPeriodoSeleccionado) {
        this.strPeriodoSeleccionado = strPeriodoSeleccionado;
    }

    public List getLisPeriodos() {
        return lisPeriodos;
    }

    public void setLisPeriodos(List lisPeriodos) {
        this.lisPeriodos = lisPeriodos;
    }
}
