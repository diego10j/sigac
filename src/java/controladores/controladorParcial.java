/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Docentes;
import entidades.NotaDestrezaparcial;
import entidades.PeriodoLectivo;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;
import servcios.servicioDocente;
import servcios.servicioEvaluarparcial;
import servcios.servicioFormaevaluar;
import servcios.servicioParcial;
import servcios.servicioPeriodo;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class controladorParcial {

    private Utilitario utilitario = new Utilitario();
    @EJB
    private servicioPeriodo servPeriodo;
    private PeriodoLectivo perActual;
    @EJB
    private servicioDocente servDocente;
    private Docentes docDocente;
    @EJB
    private servicioFormaevaluar servFormaEvaluar;
    private List comFormas;
    @EJB
    private servicioEvaluarparcial servEvaluarParcial;
    private List comParciales;
    @EJB
    private servicioParcial servParcial;
    private List lisCursos;
    private Object objCursoSeleccionado;
    private List lisAsignaturas;
    private Object objAsignaturaSeleccionada;
    private List lisNotasParcial;
    private String strForma;
    private String strParcial;

    @PostConstruct
    public void cargarDatos() {
        perActual = servPeriodo.getPeriodoActivo();
        if (perActual == null) {
            //No tiene ningun periodo activo
            return;
        }
        docDocente = servDocente.getDocente("2");
        if (docDocente == null) {
            //No tiene ningun docente
            return;
        }

        comFormas = servFormaEvaluar.getListaFormasEvaluar();
        comParciales = servEvaluarParcial.getListaEvaluarParcial();

        //cursos y materias
        lisCursos = servParcial.getCursosDocente(perActual.getPerCodigo().toString(), docDocente.getDocCodigo().toString());

        if (lisCursos != null) {
            if (!lisCursos.isEmpty()) {
                objCursoSeleccionado = lisCursos.get(0);
            }
        }

        if (objCursoSeleccionado != null) {
            lisAsignaturas = servParcial.getMateriasCursoDocente(((Object[]) objCursoSeleccionado)[0] + "", docDocente.getDocCodigo().toString());
        } else {
            lisAsignaturas = servParcial.getMateriasCursoDocente("-1", "-1");
        }

    }

    /**
     * Cuando selecciona un curso fiultra las materias
     *
     * @param evt
     */
    public void seleccionarCursos(SelectEvent evt) {
        if (objCursoSeleccionado != null) {
            lisAsignaturas = servParcial.getMateriasCursoDocente(((Object[]) objCursoSeleccionado)[0] + "", docDocente.getDocCodigo().toString());
        } else {
            lisAsignaturas = servParcial.getMateriasCursoDocente("-1", "-1");
        }
        cargarAlumnos();
    }

    public void seleccionoCombo() {
        cargarAlumnos();
    }

    public void seleccionoAsignatura(SelectEvent evt) {
        cargarAlumnos();
    }

    private void cargarAlumnos() {
        if (objAsignaturaSeleccionada != null && objCursoSeleccionado != null) {
            if (strForma != null && strParcial != null) {
                lisNotasParcial = servParcial.getNotasParcialDistributivo(((Object[]) objCursoSeleccionado)[0] + "", strForma, strParcial, ((Object[]) objAsignaturaSeleccionada)[0] + "");
            }
        }
    }

    public void actualizarNotasParcial() {
        if (objAsignaturaSeleccionada != null && objCursoSeleccionado != null) {
            if (strForma != null && strParcial != null) {
                int num_matriculados = servParcial.inscribirParcialDistributivo(((Object[]) objCursoSeleccionado)[0] + "", strForma, strParcial, ((Object[]) objAsignaturaSeleccionada)[0] + "");
                if (num_matriculados > 0) {
                    utilitario.agregarMensaje("Se importaron " + num_matriculados + " alumnos", "");
                }
                lisNotasParcial = servParcial.getNotasParcialDistributivo(((Object[]) objCursoSeleccionado)[0] + "", strForma, strParcial, ((Object[]) objAsignaturaSeleccionada)[0] + "");
            } else {
                utilitario.agregarMensajeInfo("Seleccione un Quimestre y un Parcial", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione un Curso y una Asignatura", "");
        }
    }

    public void guardar() {
        if (lisNotasParcial != null) {
            String str_mensaje = servParcial.guardarNotasParcial(lisNotasParcial);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        }
    }

    public PeriodoLectivo getPerActual() {
        return perActual;
    }

    public void setPerActual(PeriodoLectivo perActual) {
        this.perActual = perActual;
    }

    public Docentes getDocDocente() {
        return docDocente;
    }

    public void setDocDocente(Docentes docDocente) {
        this.docDocente = docDocente;
    }

    public List getComFormas() {
        return comFormas;
    }

    public void setComFormas(List comFormas) {
        this.comFormas = comFormas;
    }

    public List getComParciales() {
        return comParciales;
    }

    public void setComParciales(List comParciales) {
        this.comParciales = comParciales;
    }

    public List getLisCursos() {
        return lisCursos;
    }

    public void setLisCursos(List lisCursos) {
        this.lisCursos = lisCursos;
    }

    public List getLisAsignaturas() {
        return lisAsignaturas;
    }

    public void setLisAsignaturas(List lisAsignaturas) {
        this.lisAsignaturas = lisAsignaturas;
    }

    public Object getObjAsignaturaSeleccionada() {
        return objAsignaturaSeleccionada;
    }

    public void setObjAsignaturaSeleccionada(Object objAsignaturaSeleccionada) {
        this.objAsignaturaSeleccionada = objAsignaturaSeleccionada;
    }

    public Object getObjCursoSeleccionado() {
        return objCursoSeleccionado;
    }

    public void setObjCursoSeleccionado(Object objCursoSeleccionado) {
        this.objCursoSeleccionado = objCursoSeleccionado;
    }

    public String getStrForma() {
        return strForma;
    }

    public void setStrForma(String strForma) {
        this.strForma = strForma;
    }

    public String getStrParcial() {
        return strParcial;
    }

    public void setStrParcial(String strParcial) {
        this.strParcial = strParcial;
    }

    public List<NotaDestrezaparcial> getLisNotasParcial() {
        return lisNotasParcial;
    }

    public void setLisNotasParcial(List<NotaDestrezaparcial> lisNotasParcial) {
        this.lisNotasParcial = lisNotasParcial;
    }
}
