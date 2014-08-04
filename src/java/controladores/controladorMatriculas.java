/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Alumnos;
import entidades.CrearCurso;
import entidades.Matricula;
import entidades.PeriodoLectivo;
import framework.reportes.GenerarReporte;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import servcios.servicioAlumno;
import servcios.servicioCrearCurso;
import servcios.servicioMatriculas;
import servcios.servicioPeriodo;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorMatriculas {

    @EJB
    private servicioMatriculas servMatriculas;
    private Matricula matMatricula = new Matricula();
    private Utilitario utilitario = new Utilitario();
    private List<Matricula> listaMatriculas;
    private List<Matricula> filtroMatriculas;
    @EJB
    private servicioCrearCurso servCrearCurso;
    @EJB
    private servicioPeriodo servPeriodo;
    private PeriodoLectivo perActivo; //Periodo activo para matricular alumnos
    private List<CrearCurso> lisCrearCursos;
    private CrearCurso creCursoSeleccionado;
    @EJB
    private servicioAlumno servAlumno;
    private List comAlumnos;
    private Object objAlumno;
    private String strPeriodoSeleccionado;
    private CartesianChartModel model;
    private List lisPeriodos;
    private String str_path_reporte;

    @PostConstruct
    public void cargarDatos() {
        lisPeriodos = servPeriodo.getListaPeriodos();
        perActivo = servPeriodo.getPeriodoActivo();
        if (perActivo == null) {
            utilitario.agregarMensajeError("No tiene ningun Período Lectivo Activo", "");
            return;
        }
        str_path_reporte = utilitario.getURL() + "/reportes/reporte" + utilitario.getVariable("ide_usua") + ".pdf";

        lisCrearCursos = servCrearCurso.getCursosCreados(perActivo.getPerCodigo().toString());
        strPeriodoSeleccionado = perActivo.getPerCodigo().toString();

        if (creCursoSeleccionado == null) {
            if (lisCrearCursos != null && lisCrearCursos.isEmpty() == false) {
                creCursoSeleccionado = lisCrearCursos.get(0);
            }
        }
        if (creCursoSeleccionado == null) {
            listaMatriculas = servMatriculas.getMatriculas("-1");
        } else {
            listaMatriculas = servMatriculas.getMatriculas(creCursoSeleccionado.getCreCodigo().toString());
        }
        comAlumnos = servAlumno.getListaAlumnos();
        matMatricula = new Matricula();
        matMatricula.setAluCodigo(new Alumnos());
        objAlumno = null;
    }

    public void seleccionarCurso(SelectEvent evt) {
        creCursoSeleccionado = (CrearCurso) evt.getObject();
        listaMatriculas = servMatriculas.getMatriculas(creCursoSeleccionado.getCreCodigo().toString());
    }

    public void insertar() {
        matMatricula = new Matricula();
        matMatricula.setAluCodigo(new Alumnos());
        objAlumno = null;
    }

    public void eliminar() {
        if (matMatricula.getMatCodigo() != null) {
            String str_mensaje = servMatriculas.elimnarMatriculas(matMatricula.getMatCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }

    public void modificar() {
        objAlumno = new Object[]{matMatricula.getAluCodigo().getAluCodigo(), matMatricula.getAluCodigo().getAluApellidos(), matMatricula.getAluCodigo().getAluNombres(), matMatricula.getAluCodigo().getAluCedula()};
    }

    public void seleccionarAlumno(SelectEvent evt) {
        objAlumno = evt.getObject();
    }

    public void guardar() {

        if (creCursoSeleccionado != null) {
            matMatricula.setCreCodigo(servCrearCurso.getCrearCurso(creCursoSeleccionado.getCreCodigo().toString()));
        }

        if (objAlumno != null) {
            matMatricula.setAluCodigo(servAlumno.getAlumno(((Object[]) objAlumno)[0] + ""));
        }

        boolean nuevo = true;
        if (matMatricula.getMatCodigo() != null) {
            nuevo = false;
        }

        String str_mensaje = servMatriculas.guardarMatriculas(matMatricula);
        if (str_mensaje.isEmpty()) {
            utilitario.agregarMensaje("Se guardo correctamente", "");
            cargarDatos();
            if (!nuevo) {
                utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
            }
        } else {
            utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
        }

    }

    public void verReporteAlumnosCursos(String formato) {
        if (creCursoSeleccionado != null) {
            Map p = new HashMap();
            p.put("cur_codigo", creCursoSeleccionado.getCurCodigo().getCurCodigo().toString());
            p.put("par_codigo", creCursoSeleccionado.getParCodigo().getParCodigo().toString());
            GenerarReporte generar = new GenerarReporte();
            if (formato == null || formato.equalsIgnoreCase("xls")) {
                generar.generarXLS(p, "/reportes/rep_alumnos/rep_distribucion_alumnos.jasper");
            } else {
                generar.generar(p, "/reportes/rep_alumnos/rep_distribucion_alumnos.jasper");
                utilitario.ejecutarJavaScript("window.open('" + str_path_reporte + "');");
            }
        } else {
            utilitario.agregarMensajeError("Debe seleccionar un Curso", str_path_reporte);
        }
    }

    public List autocompletar(String query) {
        List suggestions = new ArrayList();
        for (int i = 0; i < comAlumnos.size(); i++) {
            Object[] f = (Object[]) comAlumnos.get(i);
            for (int j = 1; j < f.length; j++) {
                if (f[j] != null) {
                    String fl = f[j] + "";
                    if (fl.toUpperCase().startsWith(query.trim().toUpperCase())) {
                        suggestions.add(f);
                        break;
                    }
                }
            }
            if (suggestions.size() >= 10) {
                break;
            }
        }
        return suggestions;
    }

    public void cambioPeriodoReporte(AjaxBehaviorEvent evt) {
        generaGrafico();
    }

    private void generaGrafico() {
        List repNumMatriculados;
        if (strPeriodoSeleccionado != null) {
            repNumMatriculados = servMatriculas.getMatriculadosxCurso(strPeriodoSeleccionado);
        } else {
            repNumMatriculados = servMatriculas.getMatriculadosxCurso("-1");
        }

        model = new CartesianChartModel();

        if (repNumMatriculados != null && repNumMatriculados.isEmpty() == false) {
            for (int i = 0; i < repNumMatriculados.size(); i++) {
                Object[] fila = (Object[]) repNumMatriculados.get(i);
                ChartSeries curso = new ChartSeries();
                curso.setLabel(fila[1] + "  " + fila[2]);
                int num = 0;
                try {
                    num = Integer.parseInt(fila[3] + "");
                } catch (Exception e) {
                }
                curso.set("   ", num);
                model.addSeries(curso);
            }
        } else {
            model = null;
        }
    }

    public Matricula getMatMatricula() {
        return matMatricula;
    }

    public void setMatMatricula(Matricula matMatricula) {
        this.matMatricula = matMatricula;
    }

    public List<Matricula> getListaMatriculas() {
        return listaMatriculas;
    }

    public void setListaMatriculas(List<Matricula> listaMatriculas) {
        this.listaMatriculas = listaMatriculas;
    }

    public List<Matricula> getFiltroMatriculas() {
        return filtroMatriculas;
    }

    public void setFiltroMatriculas(List<Matricula> filtroMatriculas) {
        this.filtroMatriculas = filtroMatriculas;
    }

    public PeriodoLectivo getPerActivo() {
        return perActivo;
    }

    public void setPerActivo(PeriodoLectivo perActivo) {
        this.perActivo = perActivo;
    }

    public List<CrearCurso> getLisCrearCursos() {
        return lisCrearCursos;
    }

    public void setLisCrearCursos(List<CrearCurso> lisCrearCursos) {
        this.lisCrearCursos = lisCrearCursos;
    }

    public CrearCurso getCreCursoSeleccionado() {
        return creCursoSeleccionado;
    }

    public void setCreCursoSeleccionado(CrearCurso creCursoSeleccionado) {
        this.creCursoSeleccionado = creCursoSeleccionado;
    }

    public List getComAlumnos() {
        return comAlumnos;
    }

    public Object getObjAlumno() {
        return objAlumno;
    }

    public void setObjAlumno(Object objAlumno) {
        this.objAlumno = objAlumno;
    }

    public String getStrPeriodoSeleccionado() {
        return strPeriodoSeleccionado;
    }

    public void setStrPeriodoSeleccionado(String strPeriodoSeleccionado) {
        this.strPeriodoSeleccionado = strPeriodoSeleccionado;
    }

    public CartesianChartModel getModel() {
        generaGrafico();
        return model;
    }

    public void setModel(CartesianChartModel model) {
        this.model = model;
    }

    public List getLisPeriodos() {
        return lisPeriodos;
    }

    public void setLisPeriodos(List lisPeriodos) {
        this.lisPeriodos = lisPeriodos;
    }

    public String getStr_path_reporte() {
        return str_path_reporte;
    }

    public void setStr_path_reporte(String str_path_reporte) {
        this.str_path_reporte = str_path_reporte;
    }
}
