/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Asignaturas;
import entidades.CrearCurso;
import entidades.Cursos;
import entidades.Distributivomxc;
import entidades.Docentes;
import entidades.Paralelo;
import framework.reportes.GenerarReporte;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import servcios.servicioAsignaturas;
import servcios.servicioCrearCurso;
import servcios.servicioCursos;
import servcios.servicioDocente;
import servcios.servicioParalelo;
import servcios.servicioPeriodo;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorCrearCurso {

    private Utilitario utilitario = new Utilitario();
    private CrearCurso creCrearc = new CrearCurso();
    @EJB
    private servicioCrearCurso servCrearCurso;
    @EJB
    private servicioPeriodo servPeriodo;
    @EJB
    private servicioCursos servCursos;
    @EJB
    private servicioParalelo servParalelos;
    @EJB
    private servicioDocente servDocente;
    @EJB
    private servicioAsignaturas servAsignatura;
    private List lisPeriodos;
    private List<CrearCurso> lisCrearCursos;
    private String strPeriodoSeleccionado;
    private List listaCursos;
    private List listaParalelos;
    private List listaDocentes;
    //DISTRIBUTIVO
    private List<Distributivomxc> lisDistributivo;
    private Distributivomxc disDistributivo;
    private List listaMaterias;
    private String str_path_reporte;

    @PostConstruct
    public void cargarDatos() {
        lisPeriodos = servPeriodo.getListaPeriodos();
        //Carga el periodo activo por defecto   
        if (strPeriodoSeleccionado == null) {
            if (servPeriodo.getPeriodoActivo() != null) {
                strPeriodoSeleccionado = servPeriodo.getPeriodoActivo().getPerCodigo().toString();
            }
        }
        lisCrearCursos = servCrearCurso.getCursosCreados(strPeriodoSeleccionado);
        listaCursos = servCursos.getListaCursos();
        listaParalelos = servParalelos.getListaParalelos();
        listaDocentes = servDocente.getListaDocentes();
        creCrearc = new CrearCurso();
        creCrearc.setCurCodigo(new Cursos());
        creCrearc.setParCodigo(new Paralelo());
        creCrearc.setDocCodigo(new Docentes());

        disDistributivo = new Distributivomxc();
        disDistributivo.setDocCodigo(new Docentes());
        disDistributivo.setAsiCodigo(new Asignaturas());
        listaMaterias = servAsignatura.getListaAsignaturas();
        
        str_path_reporte=utilitario.getURL()+"/reportes/reporte"+utilitario.getVariable("ide_usua")+".pdf";
    }

    public void insertarDistributivo() {
        disDistributivo = new Distributivomxc();
        disDistributivo.setDocCodigo(new Docentes());
        disDistributivo.setAsiCodigo(new Asignaturas());
    }

    public void guardarDistributivo() {

        if (disDistributivo.getDocCodigo().getDocCodigo() != null) {
            disDistributivo.setDocCodigo(servDocente.getDocente(disDistributivo.getDocCodigo().getDocCodigo().toString()));
        }
        if (disDistributivo.getAsiCodigo().getAsiCodigo() != null) {
            disDistributivo.setAsiCodigo(servAsignatura.getAsignaturas(disDistributivo.getAsiCodigo().getAsiCodigo().toString()));
        }
        disDistributivo.setCreCodigo(creCrearc);

        String str_mensaje = servCrearCurso.guardarDistributivo(disDistributivo);
        if (str_mensaje.isEmpty()) {
            utilitario.agregarMensaje("Se guardo correctamente", "");
            disDistributivo = new Distributivomxc();
            disDistributivo.setDocCodigo(new Docentes());
            disDistributivo.setAsiCodigo(new Asignaturas());
            cargarDistributivo();
        } else {
            utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
        }

    }

    public void eliminarDistributivo() {
        if (disDistributivo.getDisCodigo() != null) {
            String str_mensaje = servCrearCurso.elimnarDistributivo(disDistributivo.getDisCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDistributivo();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }

    public void cargarDistributivo() {

        lisDistributivo = servCrearCurso.getDistributivoCurso(creCrearc.getCreCodigo().toString());

    }

    public void insertar() {
        creCrearc = new CrearCurso();
        creCrearc.setCurCodigo(new Cursos());
        creCrearc.setParCodigo(new Paralelo());
        creCrearc.setDocCodigo(new Docentes());
    }

    public void eliminar() {
        if (creCrearc.getCreCodigo() != null) {
            String str_mensaje = servCrearCurso.elimnarCrearCurso(creCrearc.getCreCodigo().toString());
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se elimino correctamente", "");
                cargarDatos();
            } else {
                utilitario.agregarMensajeError("No se puede eliminar " + str_mensaje, "");
            }
        }
    }

    public void guardar() {
        if (creCrearc.getCurCodigo().getCurCodigo() != null) {
            creCrearc.setCurCodigo(servCursos.getCursos(creCrearc.getCurCodigo().getCurCodigo().toString()));
        }
        if (creCrearc.getParCodigo().getParCodigo() != null) {
            creCrearc.setParCodigo(servParalelos.getParalelo(creCrearc.getParCodigo().getParCodigo().toString()));
        }
        if (creCrearc.getDocCodigo().getDocCodigo() != null) {
            creCrearc.setDocCodigo(servDocente.getDocente(creCrearc.getDocCodigo().getDocCodigo().toString()));
        }
        if (strPeriodoSeleccionado != null) {
            creCrearc.setPerCodigo(servPeriodo.getPeriodoLectivo(strPeriodoSeleccionado));
        }


        String str_mensaje = servCrearCurso.guardarCrearCurso(creCrearc);
        if (str_mensaje.isEmpty()) {
            utilitario.agregarMensaje("Se guardo correctamente", "");
            creCrearc = new CrearCurso();
            cargarDatos();
            utilitario.ejecutarJavaScript("wdlgDetalle.hide()");
        } else {
            utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
        }

    }

    public void verReporteCursos() {
        if (strPeriodoSeleccionado != null) {
            GenerarReporte genera = new GenerarReporte();
            Map p = new HashMap();
            p.put("per_codigo", strPeriodoSeleccionado);
            GenerarReporte generar = new GenerarReporte();
            generar.generar(p, "/reportes/rep_docente/rep_distribucion_cursos.jasper");
        }
    }

    /**
     * Filtra los cursos creados del periodo seleccionado
     *
     * @param evt
     */
    public void filtrarCursos(AjaxBehaviorEvent evt) {
        lisCrearCursos = servCrearCurso.getCursosCreados(strPeriodoSeleccionado);
    }

    public List getLisPeriodos() {
        return lisPeriodos;
    }

    public void setLisPeriodos(List lisPeriodos) {
        this.lisPeriodos = lisPeriodos;
    }

    public List<CrearCurso> getLisCrearCursos() {
        return lisCrearCursos;
    }

    public void setLisCrearCursos(List<CrearCurso> lisCrearCursos) {
        this.lisCrearCursos = lisCrearCursos;
    }

    public String getStrPeriodoSeleccionado() {
        return strPeriodoSeleccionado;
    }

    public void setStrPeriodoSeleccionado(String strPeriodoSeleccionado) {
        this.strPeriodoSeleccionado = strPeriodoSeleccionado;
    }

    public servicioCrearCurso getServCrearCurso() {
        return servCrearCurso;
    }

    public void setServCrearCurso(servicioCrearCurso servCrearCurso) {
        this.servCrearCurso = servCrearCurso;
    }

    public servicioPeriodo getServPeriodo() {
        return servPeriodo;
    }

    public void setServPeriodo(servicioPeriodo servPeriodo) {
        this.servPeriodo = servPeriodo;
    }

    public CrearCurso getCreCrearc() {
        return creCrearc;
    }

    public void setCreCrearc(CrearCurso creCrearc) {
        this.creCrearc = creCrearc;
    }

    public List getListaCursos() {
        return listaCursos;
    }

    public void setListaCursos(List listaCursos) {
        this.listaCursos = listaCursos;
    }

    public List getListaParalelos() {
        return listaParalelos;
    }

    public void setListaParalelos(List listaParalelos) {
        this.listaParalelos = listaParalelos;
    }

    public List getListaDocentes() {
        return listaDocentes;
    }

    public void setListaDocentes(List listaDocentes) {
        this.listaDocentes = listaDocentes;
    }

    public List<Distributivomxc> getLisDistributivo() {
        return lisDistributivo;
    }

    public void setLisDistributivo(List<Distributivomxc> lisDistributivo) {
        this.lisDistributivo = lisDistributivo;
    }

    public Distributivomxc getDisDistributivo() {
        return disDistributivo;
    }

    public void setDisDistributivo(Distributivomxc disDistributivo) {
        this.disDistributivo = disDistributivo;
    }

    public List getListaMaterias() {
        return listaMaterias;
    }

    public void setListaMaterias(List listaMaterias) {
        this.listaMaterias = listaMaterias;
    }

    public String getStr_path_reporte() {
        return str_path_reporte;
    }

    public void setStr_path_reporte(String str_path_reporte) {
        this.str_path_reporte = str_path_reporte;
    }    
}
