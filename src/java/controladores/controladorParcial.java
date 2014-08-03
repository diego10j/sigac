/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import entidades.CrearCurso;
import entidades.Docentes;
import entidades.NotaDestrezaparcial;
import entidades.PeriodoLectivo;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.reportes.GenerarReporte;
import framework.reportes.ReporteDataSource;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import servcios.servicioCrearCurso;
import servcios.servicioDocente;
import servcios.servicioEvaluarparcial;
import servcios.servicioFormaevaluar;
import servcios.servicioParcial;
import servcios.servicioPeriodo;
import servcios.servicioQuimestre;

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
    private boolean booMuestra = true;
    private List lisDisciplinaParcial;
    //Asistencia
    private List lisAsistenciaParcial;
    private int numDias = 0;
    ////Quimestre
    @EJB
    private servicioQuimestre servQuimestre;
    private List lisInformeQuimestre;
    private String str_path_reporte;
    //Consulta
    private List lisConsulta;
    @EJB
    private servicioCrearCurso servCreaCurso;

    @PostConstruct
    public void cargarDatos() {

        perActual = servPeriodo.getPeriodoActivo();
        if (perActual == null) {
            //No tiene ningun periodo activo
            return;
        }
        try {
            docDocente = servDocente.getDocente(utilitario.getVariable("doc_codigo"));
            //    if (docDocente == null) {
            //No tiene ningun docente
            //        return;
            //    }

            comFormas = servFormaEvaluar.getListaFormasEvaluar();
            comParciales = servEvaluarParcial.getListaEvaluarParcial();

            if (utilitario.getURLCompleto().endsWith("PasarParcial.jsf") || utilitario.getURLCompleto().endsWith("InformeQuimestre.jsf") || utilitario.getURLCompleto().endsWith("ReporteQuimestre.jsf")) {
                //cursos y materias
                lisCursos = servParcial.getCursosDocente(perActual.getPerCodigo().toString(), docDocente.getDocCodigo().toString());

            } else if (utilitario.getURLCompleto().endsWith("Consultar.jsf") || utilitario.getURLCompleto().endsWith("RepoParcial.jsf")) {
                String alumno = utilitario.getVariable("alu_codigo");
                lisCursos = servParcial.getMatriculasCursoAlumno(alumno == null ? "-1" : alumno);
            } else {
                lisCursos = servParcial.getCursosDisciplinaDocente(perActual.getPerCodigo().toString(), docDocente.getDocCodigo().toString());
            }

            if (objCursoSeleccionado != null) {
                lisAsignaturas = servParcial.getMateriasCursoDocente(((Object[]) objCursoSeleccionado)[0] + "", docDocente.getDocCodigo().toString());
            } else {
                lisAsignaturas = servParcial.getMateriasCursoDocente("-1", "-1");
            }
            str_path_reporte = utilitario.getURL() + "/reportes/reporte" + utilitario.getVariable("ide_usua") + ".pdf";

            if (utilitario.getURLCompleto().endsWith("Consultar.jsf") || utilitario.getURLCompleto().endsWith("RepoParcial.jsf")) {
                String alumno = utilitario.getVariable("alu_codigo");
                if (alumno == null) {
                    alumno = "-1";
                }
                if (objCursoSeleccionado != null) {
                    lisCursos = servParcial.getCursosAlumno(alumno);
                    lisConsulta = consultarNotas(alumno, ((Object[]) objCursoSeleccionado)[0] + "");
                } else {
                    lisConsulta = consultarNotas("-1", "-1");
                }
            }

        } catch (Exception e) {
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
        }
        objAsignaturaSeleccionada = null;
        if (utilitario.getURLCompleto().endsWith("PasarParcial.jsf")) {
            cargarAlumnos();
        } else {
            cargarInforme();
        }

        if (utilitario.getURLCompleto().endsWith("Consultar.jsf") || utilitario.getURLCompleto().endsWith("RepoParcial.jsf")) {
            String alumno = utilitario.getVariable("alu_codigo");
            if (alumno == null) {
                alumno = "-1";
            }

            if (objCursoSeleccionado != null) {
                lisCursos = servParcial.getCursosAlumno(alumno);
                lisConsulta = consultarNotas(alumno, ((Object[]) objCursoSeleccionado)[0] + "");
            } else {
                lisConsulta = consultarNotas("-1", "-1");
            }
        }




    }

    public void seleccionarCursos(AjaxBehaviorEvent evt) {
        if (objCursoSeleccionado != null) {
            if (utilitario.getURLCompleto().endsWith("Consultar.jsf") || utilitario.getURLCompleto().endsWith("RepoParcial.jsf")) {
                lisAsignaturas = servParcial.getMateriasCursoDocente(((Object[]) objCursoSeleccionado)[0] + "", docDocente.getDocCodigo().toString());
            } else {
                lisAsignaturas = servParcial.getMateriasCursoDocente(objCursoSeleccionado + "", docDocente.getDocCodigo().toString());
            }
        }
        objAsignaturaSeleccionada = null;
        if (utilitario.getURLCompleto().endsWith("PasarParcial.jsf")) {
            cargarAlumnos();
        } else {
            cargarInforme();
        }
        if (utilitario.getURLCompleto().endsWith("Consultar.jsf") || utilitario.getURLCompleto().endsWith("RepoParcial.jsf")) {
            String alumno = utilitario.getVariable("alu_codigo");
            if (alumno == null) {
                alumno = "-1";
            }

            if (objCursoSeleccionado != null) {
                lisCursos = servParcial.getCursosAlumno(alumno);
                lisConsulta = consultarNotas(alumno, ((Object[]) objCursoSeleccionado)[0] + "");
            } else {
                lisConsulta = consultarNotas("-1", "-1");
            }
        }

    }

    public void seleccionarCursosAsistencia(SelectEvent evt) {
        cargarAlumnosAsistencia();
    }

    public void seleccionoComboAsistencia() {
        cargarAlumnosAsistencia();
    }

    public void seleccionarCursosDisciplina(SelectEvent evt) {
        cargarAlumnosDisciplina();
    }

    public void seleccionoComboDisciplina() {
        cargarAlumnosDisciplina();
    }

    public void seleccionoCombo() {
        if (utilitario.getURLCompleto().endsWith("PasarParcial.jsf")) {
            cargarAlumnos();
        } else {
            cargarInforme();
        }
    }

    public void seleccionoAsignatura(SelectEvent evt) {
        if (utilitario.getURLCompleto().endsWith("PasarParcial.jsf")) {
            cargarAlumnos();
            Object[] fila = (Object[]) evt.getObject();
            if (fila != null) {
                if (fila[2] != null && fila[2].toString().equals("3")) {
                    //SOLO MATERIAS DE PREBASICA
                    booMuestra = false;
                } else {
                    booMuestra = true;
                }
            } else {
                booMuestra = true;
            }
        } else {
            cargarInforme();
        }

    }

    private void cargarInforme() {
        if (objAsignaturaSeleccionada != null && objCursoSeleccionado != null) {
            if (strForma != null) {
                lisInformeQuimestre = servQuimestre.getListaInformeQuimestre(((Object[]) objAsignaturaSeleccionada)[0] + "" + "", strForma);
            } else {
                lisInformeQuimestre = null;
            }
        } else {
            lisInformeQuimestre = null;
        }
    }

    private void cargarAlumnos() {
        if (objAsignaturaSeleccionada != null && objCursoSeleccionado != null) {
            if (strForma != null && strParcial != null) {
                if (objCursoSeleccionado != null) {
                    lisNotasParcial = servParcial.getNotasParcialDistributivo(objCursoSeleccionado + "", strForma, strParcial, ((Object[]) objAsignaturaSeleccionada)[0] + "");
                } else {
                    lisNotasParcial = servParcial.getNotasParcialDistributivo("-1", strForma, strParcial, ((Object[]) objAsignaturaSeleccionada)[0] + "");

                }

            } else {
                lisNotasParcial = null;
            }
        } else {
            lisNotasParcial = null;
        }
    }

    private void cargarAlumnosDisciplina() {
        if (objCursoSeleccionado != null) {
            if (strForma != null && strParcial != null) {
                lisDisciplinaParcial = servParcial.getNotasParcialDisciplina(((Object[]) objCursoSeleccionado)[0] + "", strForma, strParcial);
            } else {
                lisDisciplinaParcial = null;
            }
        } else {
            lisDisciplinaParcial = null;
        }
    }

    private void cargarAlumnosAsistencia() {
        if (objCursoSeleccionado != null) {
            if (strForma != null && strParcial != null) {
                lisAsistenciaParcial = servParcial.getListaParcialAsistencia(((Object[]) objCursoSeleccionado)[0] + "", strForma, strParcial);
            } else {
                lisAsistenciaParcial = null;
            }
        } else {
            lisAsistenciaParcial = null;
        }

        if (lisAsistenciaParcial != null) {
            try {
                Object[] fila = (Object[]) lisAsistenciaParcial.get(0);
                numDias = Integer.parseInt(fila[8] + "");
            } catch (Exception e) {
                numDias = 0;
            }
        }
    }

    public void actualizarInforme() {
        if (objAsignaturaSeleccionada != null && objCursoSeleccionado != null) {
            if (strForma != null) {
                cargarInforme();
            } else {
                utilitario.agregarMensajeInfo("Seleccione un Quimestre", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione un Curso y una Asignatura", "");
        }
    }

    public void actualizarNotasParcial() {
        if (objAsignaturaSeleccionada != null && objCursoSeleccionado != null) {
            if (strForma != null && strParcial != null) {
                int num_matriculados = servParcial.inscribirParcialDistributivo(objCursoSeleccionado + "", strForma, strParcial, ((Object[]) objAsignaturaSeleccionada)[0] + "");
                if (num_matriculados > 0) {
                    utilitario.agregarMensaje("Se importaron " + num_matriculados + " alumnos", "");
                }
                lisNotasParcial = servParcial.getNotasParcialDistributivo(objCursoSeleccionado + "", strForma, strParcial, ((Object[]) objAsignaturaSeleccionada)[0] + "");
            } else {
                utilitario.agregarMensajeInfo("Seleccione un Quimestre y un Parcial", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione un Curso y una Asignatura", "");
        }
    }

    public void actualizarDisciplinaParcial() {
        if (objCursoSeleccionado != null) {
            if (strForma != null && strParcial != null) {
                int num_matriculados = servParcial.inscribirParcialDisciplina(((Object[]) objCursoSeleccionado)[0] + "", strForma, strParcial);
                if (num_matriculados > 0) {
                    utilitario.agregarMensaje("Se importaron " + num_matriculados + " alumnos", "");
                }
                lisDisciplinaParcial = servParcial.getNotasParcialDisciplina(((Object[]) objCursoSeleccionado)[0] + "", strForma, strParcial);
            } else {
                utilitario.agregarMensajeInfo("Seleccione un Quimestre y un Parcial", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione un Curso", "");
        }
    }

    public void actualizarAsistenciaParcial() {
        if (numDias > 0) {
            if (objCursoSeleccionado != null) {
                if (strForma != null && strParcial != null) {
                    int num_matriculados = servParcial.inscribirParcialAsistencia(((Object[]) objCursoSeleccionado)[0] + "", strForma, strParcial, numDias);
                    if (num_matriculados > 0) {
                        utilitario.agregarMensaje("Se importaron " + num_matriculados + " alumnos", "");
                    }
                    lisAsistenciaParcial = servParcial.getListaParcialAsistencia(((Object[]) objCursoSeleccionado)[0] + "", strForma, strParcial);
                } else {
                    utilitario.agregarMensajeInfo("Seleccione un Quimestre y un Parcial", "");
                }
            } else {
                utilitario.agregarMensajeInfo("Seleccione un Curso", "");
            }
        } else {
            utilitario.agregarMensajeInfo("El número de días del parcial tiene que ser mayor a 0", "");
        }


    }

    /*
     * cuando cambia una nota, valida que este en el rango de 1 a 10 y calcula la equivalencia 
     */
    public void cabioAsistencia(CellEditEvent event) {
        RequestContext requestContext = RequestContext.getCurrentInstance();

        Object[] fila = (Object[]) lisAsistenciaParcial.get(event.getRowIndex());
        int int_new = 0;
        try {
            int_new = Integer.parseInt(event.getNewValue() + "");
        } catch (Exception e) {
            int_new = -1;
        }
        if (int_new > 15 || int_new < 0) {
            if (event.getColumn().getClientId().endsWith("cAtra")) {
                fila[3] = 0;
                utilitario.agregarMensajeError("La Número de Atrasos debe ser un valor numerico entero entre 0 y 15", "");
            } else if (event.getColumn().getClientId().endsWith("cFJusti")) {
                fila[4] = 0;
                utilitario.agregarMensajeError("La Número de Faltas Justificadas debe ser un valor numerico entero entre 0 y 15", "");
            } else if (event.getColumn().getClientId().endsWith("cFInjus")) {
                fila[5] = 0;
                utilitario.agregarMensajeError("La Número de Faltas Injustificadas debe ser un valor numerico entero entre 0 y 15", "");
            }
            requestContext.update("tabNotas");
        }

        int total = 0;
        int int_justi = 0;
        int int_injusti = 0;
        try {
            int_justi = Integer.parseInt(fila[4] + "");
        } catch (Exception e) {
        }
        try {
            int_injusti = Integer.parseInt(fila[5] + "");
        } catch (Exception e) {
        }

        int num_dias = 0;
        try {
            num_dias = Integer.parseInt(fila[8] + "");
        } catch (Exception e) {
        }

        total = int_justi + int_injusti;
        fila[6] = total;


        fila[7] = num_dias - total;

        lisAsistenciaParcial.set(event.getRowIndex(), fila);

        requestContext.update("tabNotas:" + event.getRowIndex() + ":total");
        requestContext.update("tabNotas:" + event.getRowIndex() + ":dias");
    }

    public void cambioExamen(CellEditEvent event) {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        Object[] fila = (Object[]) lisInformeQuimestre.get(event.getRowIndex());
        double dou_new = 0;
        try {
            dou_new = Double.parseDouble(event.getNewValue() + "");
        } catch (Exception e) {
            dou_new = -1;
        }
        if (dou_new > 10 || dou_new < 0) {
            if (event.getColumn().getClientId().endsWith("cExamen")) {
                fila[5] = 0;
                utilitario.agregarMensajeError("La nota del Exámen debe estar en el rango de 0 a 10", "");
            }
            requestContext.update("tabNotas");
        }

        double dou_examen = 0;
        try {
            dou_examen = Double.parseDouble(fila[5] + "");
        } catch (Exception e) {
        }
        double dou_eqv80 = 0;
        try {
            dou_eqv80 = Double.parseDouble(fila[3] + "");
        } catch (Exception e) {
        }

        double dou20 = (dou_examen * 2) / 10;

        fila[4] = utilitario.getFormatoNumero(dou20);
        fila[6] = utilitario.getFormatoNumero((dou20 + dou_eqv80));


        ///Calcular la equivalencia
        //////////

        TablaGenerica tab_equi = utilitario.consultar("SELECT * FROM equivalencia_aprovechamiento");
        Object obj_resultado = null;
        for (int i = 0; i < tab_equi.getTotalFilas(); i++) {
            String str_expresion = tab_equi.getValor(i, "eqa_escalacuantiva");
            str_expresion = str_expresion.replace("nota", fila[6] + "");
            obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
            if (obj_resultado != null) {
                break;
            }
        }
        if (obj_resultado == null) {
            obj_resultado = "NO EQV";
        }
        fila[8] = obj_resultado;


        requestContext.update("tabNotas:" + event.getRowIndex() + ":eqv20");
        requestContext.update("tabNotas:" + event.getRowIndex() + ":nota");
        requestContext.update("tabNotas:" + event.getRowIndex() + ":eqv");

    }

    /*
     * cuando cambia una nota, valida que este en el rango de 1 a 10 y calcula la equivalencia 
     */
    public void cabioNota(CellEditEvent event) {

        RequestContext requestContext = RequestContext.getCurrentInstance();
        Object[] fila = (Object[]) lisNotasParcial.get(event.getRowIndex());
        double dou_new = 0;
        try {
            dou_new = Double.parseDouble(event.getNewValue() + "");
        } catch (Exception e) {
            dou_new = -1;
        }

        if (dou_new > 10 || dou_new < 0) {
            if (event.getColumn().getClientId().endsWith("cTrabajos")) {
                fila[11] = utilitario.getFormatoNumero(0);
                utilitario.agregarMensajeError("La nota de Trabajos debe estar en el rango de 1 a 10", "");
            } else if (event.getColumn().getClientId().endsWith("cActInd")) {
                fila[3] = utilitario.getFormatoNumero(0);
                utilitario.agregarMensajeError("La nota de Actividades Individuales debe estar en el rango de 1 a 10", "");
            } else if (event.getColumn().getClientId().endsWith("cActGrup")) {
                fila[4] = utilitario.getFormatoNumero(0);
                utilitario.agregarMensajeError("La nota de Actividades en Grupo debe estar en el rango de 1 a 10", "");
            } else if (event.getColumn().getClientId().endsWith("cLecc")) {
                fila[5] = utilitario.getFormatoNumero(0);
                utilitario.agregarMensajeError("La nota de Lecciones debe estar en el rango de 1 a 10", "");
            } else if (event.getColumn().getClientId().endsWith("cEval")) {
                fila[6] = utilitario.getFormatoNumero(0);
                utilitario.agregarMensajeError("La nota de Evaluaciiones debe estar en el rango de 1 a 10", "");
            } else if (event.getColumn().getClientId().endsWith("cNota")) {
                fila[8] = utilitario.getFormatoNumero(0);
                utilitario.agregarMensajeError("La nota debe estar en el rango de 1 a 10", "");
            }
            requestContext.update("tabNotas");
        } else {
            //Si esta en el rango, pone formato 2 decimales
            try {
                if (event.getColumn().getClientId().endsWith("cTrabajos")) {
                    fila[11] = utilitario.getFormatoNumero(dou_new);

                } else if (event.getColumn().getClientId().endsWith("cActInd")) {
                    fila[3] = utilitario.getFormatoNumero(dou_new);

                } else if (event.getColumn().getClientId().endsWith("cActGrup")) {
                    fila[4] = utilitario.getFormatoNumero(dou_new);

                } else if (event.getColumn().getClientId().endsWith("cLecc")) {
                    fila[5] = utilitario.getFormatoNumero(dou_new);

                } else if (event.getColumn().getClientId().endsWith("cEval")) {
                    fila[6] = utilitario.getFormatoNumero(dou_new);

                } else if (event.getColumn().getClientId().endsWith("cNota")) {
                    fila[8] = utilitario.getFormatoNumero(dou_new);

                }
            } catch (Exception e) {
            }
        }


        if (booMuestra) {


            double not_trabajo = 0;
            double not_act_indiv = 0;
            double not_act_group = 0;
            double not_leccion = 0;
            double not_evaluacion = 0;


            try {
                not_trabajo = Double.parseDouble(fila[11] + "");
            } catch (Exception e) {
            }
            try {
                not_act_indiv = Double.parseDouble(fila[3] + "");
            } catch (Exception e) {
            }
            try {
                not_act_group = Double.parseDouble(fila[4] + "");
            } catch (Exception e) {
            }
            try {
                not_leccion = Double.parseDouble(fila[5] + "");
            } catch (Exception e) {
            }
            try {
                not_evaluacion = Double.parseDouble(fila[6] + "");
            } catch (Exception e) {
            }


            double total = not_act_group + not_act_indiv + not_evaluacion + not_leccion + not_trabajo;
            double parcial = total / 5; //Nota del parcial
            fila[7] = utilitario.getFormatoNumero(total);
            fila[8] = utilitario.getFormatoNumero(parcial);

            requestContext.update("tabNotas:" + event.getRowIndex() + ":parcial");
            requestContext.update("tabNotas:" + event.getRowIndex() + ":total");
        } else {
        }


        ///Calcular la equivalencia
        //////////

        TablaGenerica tab_equi = utilitario.consultar("SELECT * FROM equivalencia_aprovechamiento");
        Object obj_resultado = null;
        for (int i = 0; i < tab_equi.getTotalFilas(); i++) {
            String str_expresion = tab_equi.getValor(i, "eqa_escalacuantiva");
            str_expresion = str_expresion.replace("nota", fila[8] + "");
            obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
            if (obj_resultado != null) {
                break;
            }
        }
        if (obj_resultado == null) {
            obj_resultado = "NO EQV";
        }
        fila[9] = obj_resultado;


        lisNotasParcial.set(event.getRowIndex(), fila);



        requestContext.update("tabNotas:" + event.getRowIndex() + ":eqv");



    }

    /*
     * cuando cambia una nota, valida que este en el rango de 1 a 10 y calcula la equivalencia 
     */
    public void cabioDisciplina(CellEditEvent event) {

        RequestContext requestContext = RequestContext.getCurrentInstance();
        Object[] fila = (Object[]) lisDisciplinaParcial.get(event.getRowIndex());
        double dou_new = 0;
        try {
            dou_new = Double.parseDouble(event.getNewValue() + "");
        } catch (Exception e) {
            dou_new = -1;
        }
        if (dou_new > 2 || dou_new < 0) {
            if (event.getColumn().getClientId().endsWith("cSem1")) {
                fila[3] = 0;
                utilitario.agregarMensajeError("La nota de Disciplina de la semana 1 debe estar en el rango de 0 a 2", "");
            } else if (event.getColumn().getClientId().endsWith("cSem2")) {
                fila[4] = 0;
                utilitario.agregarMensajeError("La nota de Disciplina de la semana 2 debe estar en el rango de 0 a 2", "");
            } else if (event.getColumn().getClientId().endsWith("cSem3")) {
                fila[5] = 0;
                utilitario.agregarMensajeError("La nota de Disciplina de la semana 3 debe estar en el rango de 0 a 2", "");
            } else if (event.getColumn().getClientId().endsWith("cSem4")) {
                fila[6] = 0;
                utilitario.agregarMensajeError("La nota de Disciplina de la semana 4 debe estar en el rango de 0 a 2", "");
            } else if (event.getColumn().getClientId().endsWith("cSem5")) {
                fila[7] = 0;
                utilitario.agregarMensajeError("La nota de Disciplina de la semana 5 debe estar en el rango de 0 a 2", "");
            }
            requestContext.update("tabNotas");
        }


        if (booMuestra) {


            double sem1 = 0;
            double sem2 = 0;
            double sem3 = 0;
            double sem4 = 0;
            double sem5 = 0;

            try {
                sem1 = Double.parseDouble(fila[3] + "");
            } catch (Exception e) {
            }
            try {
                sem2 = Double.parseDouble(fila[4] + "");
            } catch (Exception e) {
            }
            try {
                sem3 = Double.parseDouble(fila[5] + "");
            } catch (Exception e) {
            }
            try {
                sem4 = Double.parseDouble(fila[6] + "");
            } catch (Exception e) {
            }
            try {
                sem5 = Double.parseDouble(fila[7] + "");
            } catch (Exception e) {
            }

            double total = sem1 + sem2 + sem3 + sem4 + sem5;

            fila[8] = utilitario.getFormatoNumero(total);

            requestContext.update("tabNotas:" + event.getRowIndex() + ":suma");

        } else {
        }


        ///Calcular la equivalencia
        //////////

        TablaGenerica tab_equi = utilitario.consultar("SELECT * FROM equivalencia_conducta");
        Object obj_resultado = null;
        for (int i = 0; i < tab_equi.getTotalFilas(); i++) {
            String str_expresion = tab_equi.getValor(i, "eqc_escala");
            str_expresion = str_expresion.replace("nota", fila[8] + "");
            obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
            if (obj_resultado != null) {
                fila[10] = tab_equi.getValor(i, "eqc_alterno");
                break;
            }
        }
        if (obj_resultado == null) {
            obj_resultado = "NO EQV";
        }

        if (fila[10] == null) {
            fila[10] = "-";
        }
        fila[9] = obj_resultado;


        lisDisciplinaParcial.set(event.getRowIndex(), fila);

        requestContext.update("tabNotas:" + event.getRowIndex() + ":eqv");
        requestContext.update("tabNotas:" + event.getRowIndex() + ":alter");

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

        if (lisDisciplinaParcial != null) {
            String str_mensaje = servParcial.guardarDisciplinaParcial(lisDisciplinaParcial);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        }

        if (lisAsistenciaParcial != null) {
            String str_mensaje = servParcial.guardarAsistenciaParcial(lisAsistenciaParcial);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        }

        if (lisInformeQuimestre != null) {
            String str_mensaje = servQuimestre.guardarInformeQuimestre(lisInformeQuimestre);
            if (str_mensaje.isEmpty()) {
                utilitario.agregarMensaje("Se guardo correctamente", "");
            } else {
                utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
            }
        }
    }

    private void generarReportePromedio() {
        String str_ide_dis = "-1";
        if (objAsignaturaSeleccionada != null) {
            str_ide_dis = ((Object[]) objAsignaturaSeleccionada)[0] + "";
        }

        TablaGenerica tab_reporte = utilitario.consultar("select '' as CUENTA,'' as NOMINA, '' as Q1P1,'' as Q1P2,'' as Q1P3,'' as Q1SUMATORIA,'' as Q1EQV80,'' as Q1EXAMEN,'' as Q1EQV20,'' as Q1NOTA,\n"
                + "'' as Q2P1,'' as Q2P2,'' as Q2P3,'' as Q2SUMATORIA,'' as Q2EQV80,'' as Q2EXAMEN,'' as Q2EQV20,'' as Q2NOTA,'' as PROMEDIOFINAL");
        tab_reporte.limpiar();
        TablaGenerica tab_alumnos = getAlumnosCurso(objCursoSeleccionado + "");

        for (int i = 0; i < tab_alumnos.getTotalFilas(); i++) {
            tab_reporte.insertar();
            TablaGenerica tab_nota = getNotasParcialQuimestre(str_ide_dis, tab_alumnos.getValor(i, "mat_codigo"), "1");
            tab_reporte.setValor("CUENTA", String.valueOf((tab_alumnos.getTotalFilas() - i)));
            tab_reporte.setValor("NOMINA", tab_alumnos.getValor(i, "Alumnos"));

            if (tab_nota.getTotalFilas() == 3) {
                tab_reporte.setValor("Q1P1", tab_nota.getValor(0, "not_primerparcial"));
                tab_reporte.setValor("Q1P2", tab_nota.getValor(1, "not_primerparcial"));
                tab_reporte.setValor("Q1P3", tab_nota.getValor(2, "not_primerparcial"));
            } else if (tab_nota.getTotalFilas() == 2) {
                tab_reporte.setValor("Q1P1", tab_nota.getValor(0, "not_primerparcial"));
                tab_reporte.setValor("Q1P2", tab_nota.getValor(1, "not_primerparcial"));
                tab_reporte.setValor("Q1P3", "0.00");
            } else if (tab_nota.getTotalFilas() == 1) {
                tab_reporte.setValor("Q1P1", tab_nota.getValor(0, "not_primerparcial"));
                tab_reporte.setValor("Q1P2", "0.00");
                tab_reporte.setValor("Q1P3", "0.00");
            } else if (tab_nota.getTotalFilas() == 0) {
                tab_reporte.setValor("Q1P1", "0.00");
                tab_reporte.setValor("Q1P2", "0.00");
                tab_reporte.setValor("Q1P3", "0.00");
            }

            TablaGenerica tab_suma = gatSumaQuimestre(str_ide_dis, tab_alumnos.getValor(i, "mat_codigo"), "1");

            if (tab_suma.isEmpty() == false) {
                tab_reporte.setValor("Q1SUMATORIA", tab_suma.getValor(0, "inf_sumatoria"));
                tab_reporte.setValor("Q1EQV80", tab_suma.getValor(0, "inf_eqv80"));
                tab_reporte.setValor("Q1EXAMEN", tab_suma.getValor(0, "inf_examen"));
                tab_reporte.setValor("Q1EQV20", tab_suma.getValor(0, "inf_exa20"));
                tab_reporte.setValor("Q1NOTA", tab_suma.getValor(0, "inf_nota"));
            } else {
                tab_reporte.setValor("Q1SUMATORIA", "0.00");
                tab_reporte.setValor("Q1EQV80", "0.00");
                tab_reporte.setValor("Q1EXAMEN", "0.00");
                tab_reporte.setValor("Q1EQV20", "0.00");
                tab_reporte.setValor("Q1NOTA", "0.00");
            }

            ///2 quimestre
            tab_nota = getNotasParcialQuimestre(str_ide_dis, tab_alumnos.getValor(i, "mat_codigo"), "4");
            tab_reporte.setValor("CUENTA", String.valueOf((tab_alumnos.getTotalFilas() - i)));
            tab_reporte.setValor("NOMINA", tab_alumnos.getValor(i, "Alumnos"));

            if (tab_nota.getTotalFilas() == 3) {
                tab_reporte.setValor("Q2P1", tab_nota.getValor(0, "not_primerparcial"));
                tab_reporte.setValor("Q2P2", tab_nota.getValor(1, "not_primerparcial"));
                tab_reporte.setValor("Q2P3", tab_nota.getValor(2, "not_primerparcial"));
            } else if (tab_nota.getTotalFilas() == 2) {
                tab_reporte.setValor("Q2P1", tab_nota.getValor(0, "not_primerparcial"));
                tab_reporte.setValor("Q2P2", tab_nota.getValor(1, "not_primerparcial"));
                tab_reporte.setValor("Q2P3", "0.00");
            } else if (tab_nota.getTotalFilas() == 1) {
                tab_reporte.setValor("Q2P1", tab_nota.getValor(0, "not_primerparcial"));
                tab_reporte.setValor("Q2P2", "0.00");
                tab_reporte.setValor("Q2P3", "0.00");
            } else if (tab_nota.getTotalFilas() == 0) {
                tab_reporte.setValor("Q2P1", "0.00");
                tab_reporte.setValor("Q2P2", "0.00");
                tab_reporte.setValor("Q2P3", "0.00");
            }

            tab_suma = gatSumaQuimestre(str_ide_dis, tab_alumnos.getValor(i, "mat_codigo"), "4");

            if (tab_suma.isEmpty() == false) {
                tab_reporte.setValor("Q2SUMATORIA", tab_suma.getValor(0, "inf_sumatoria"));
                tab_reporte.setValor("Q2EQV80", tab_suma.getValor(0, "inf_eqv80"));
                tab_reporte.setValor("Q2EXAMEN", tab_suma.getValor(0, "inf_examen"));
                tab_reporte.setValor("Q2EQV20", tab_suma.getValor(0, "inf_exa20"));
                tab_reporte.setValor("Q2NOTA", tab_suma.getValor(0, "inf_nota"));
            } else {
                tab_reporte.setValor("Q2SUMATORIA", "0.00");
                tab_reporte.setValor("Q2EQV80", "0.00");
                tab_reporte.setValor("Q2EXAMEN", "0.00");
                tab_reporte.setValor("Q2EQV20", "0.00");
                tab_reporte.setValor("Q2NOTA", "0.00");
            }
            double dou_promedio = 0;
            try {
                dou_promedio = Double.parseDouble(tab_reporte.getValor("Q1NOTA")) + Double.parseDouble(tab_reporte.getValor("Q2NOTA"));
            } catch (Exception e) {
            }
            tab_reporte.setValor("PROMEDIOFINAL", utilitario.getFormatoNumero(dou_promedio / 2));
        }

        GenerarReporte genera = new GenerarReporte();
        Map parametros = new HashMap();
        double dou_suma = tab_reporte.getSumaColumna("Q1P1");
        parametros.put("SQ1P1", utilitario.getFormatoNumero(dou_suma));
        if (tab_reporte.getTotalFilas() > 0) {
            parametros.put("PQ1P1", utilitario.getFormatoNumero(dou_suma / (tab_reporte.getTotalFilas() - 1)));
        } else {
            parametros.put("PQ1P1", utilitario.getFormatoNumero("0"));
        }
        dou_suma = tab_reporte.getSumaColumna("Q1P2");
        parametros.put("SQ1P2", utilitario.getFormatoNumero(dou_suma));
        if (tab_reporte.getTotalFilas() > 0) {
            parametros.put("PQ1P2", utilitario.getFormatoNumero(dou_suma / (tab_reporte.getTotalFilas() - 1)));
        } else {
            parametros.put("PQ1P2", utilitario.getFormatoNumero("0"));
        }
        dou_suma = tab_reporte.getSumaColumna("Q1P3");
        parametros.put("SQ1P3", utilitario.getFormatoNumero(dou_suma));
        if (tab_reporte.getTotalFilas() > 0) {
            parametros.put("PQ1P3", utilitario.getFormatoNumero(dou_suma / (tab_reporte.getTotalFilas() - 1)));
        } else {
            parametros.put("PQ1P3", utilitario.getFormatoNumero("0"));
        }
        if (tab_reporte.getTotalFilas() > 0) {
            dou_suma = tab_reporte.getSumaColumna("Q1NOTA");
            parametros.put("PQ1NOTA", utilitario.getFormatoNumero(dou_suma / (tab_reporte.getTotalFilas() - 1)));
        } else {
            parametros.put("PQ1NOTA", utilitario.getFormatoNumero("0"));
        }

        ////quimestre 2

        dou_suma = tab_reporte.getSumaColumna("Q2P1");
        parametros.put("SQ2P1", utilitario.getFormatoNumero(dou_suma));
        if (tab_reporte.getTotalFilas() > 0) {
            parametros.put("PQ2P1", utilitario.getFormatoNumero(dou_suma / (tab_reporte.getTotalFilas() - 1)));
        } else {
            parametros.put("PQ2P1", utilitario.getFormatoNumero("0"));
        }
        dou_suma = tab_reporte.getSumaColumna("Q2P2");
        parametros.put("SQ2P2", utilitario.getFormatoNumero(dou_suma));
        if (tab_reporte.getTotalFilas() > 0) {
            parametros.put("PQ2P2", utilitario.getFormatoNumero(dou_suma / (tab_reporte.getTotalFilas() - 1)));
        } else {
            parametros.put("PQ2P2", utilitario.getFormatoNumero("0"));
        }
        dou_suma = tab_reporte.getSumaColumna("Q2P3");
        parametros.put("SQ2P3", utilitario.getFormatoNumero(dou_suma));
        if (tab_reporte.getTotalFilas() > 0) {
            parametros.put("PQ2P3", utilitario.getFormatoNumero(dou_suma / (tab_reporte.getTotalFilas() - 1)));
        } else {
            parametros.put("PQ2P3", utilitario.getFormatoNumero("0"));
        }


        if (tab_reporte.getTotalFilas() > 0) {
            dou_suma = tab_reporte.getSumaColumna("Q2NOTA");
            parametros.put("PQ2NOTA", utilitario.getFormatoNumero(dou_suma / (tab_reporte.getTotalFilas() - 1)));
        } else {
            parametros.put("PQ2NOTA", utilitario.getFormatoNumero("0"));
        }


        if (tab_reporte.getTotalFilas() > 0) {
            dou_suma = tab_reporte.getSumaColumna("PROMEDIOFINAL");
            parametros.put("PROMEDIO", Math.round(dou_suma / (tab_reporte.getTotalFilas() - 1)) + "");
        } else {
            parametros.put("PROMEDIO", utilitario.getFormatoNumero("0"));
        }

        if (objAsignaturaSeleccionada != null) {
            parametros.put("ASIGNATURA", ((Object[]) objAsignaturaSeleccionada)[1] + "");
        }
        if (objCursoSeleccionado != null) {
            CrearCurso cre_curso = servCreaCurso.getCrearCurso(objCursoSeleccionado + "");
            parametros.put("CURSO", cre_curso.getCurCodigo().getCurAnio());
            parametros.put("PARALELO", cre_curso.getParCodigo().getParNombre());
        }
        parametros.put("PROFESOR", docDocente.getDocNombres());
        parametros.put("PERIODO", perActual.getPerNombre());

        genera.setDataSource(new ReporteDataSource(tab_reporte));
        genera.generar(parametros, "/reportes/rep_parcial/rep_registro.jasper");

    }

    private TablaGenerica getAlumnoCurso(String cur_codigo, String alu_codigo) {
        return utilitario.consultar("select a.mat_codigo,b.alu_apellidos||' '|| b.alu_nombres as Alumnos from matricula a\n"
                + "inner JOIN alumnos b on a.alu_codigo=b.alu_codigo\n"
                + " where a.cre_codigo=" + cur_codigo + "\n and a.alu_codigo=" + alu_codigo
                + " ORDER BY b.alu_apellidos||' '|| b.alu_nombres DESC");
    }

    private TablaGenerica getAlumnosCurso(String cre_codigo) {
        return utilitario.consultar("select a.mat_codigo,b.alu_apellidos||' '|| b.alu_nombres as Alumnos from matricula a\n"
                + "inner JOIN alumnos b on a.alu_codigo=b.alu_codigo\n"
                + " where a.cre_codigo=" + cre_codigo + "\n "
                + " ORDER BY b.alu_apellidos||' '|| b.alu_nombres DESC");
    }

    private List consultarNotas(String alumno, String curso) {
        String str_ide_dis = "-1";
        String str_nom_dis = "";

        //Consulto todas las asignaturas del curso seleccionado
        List listaAsignaturas = new ArrayList();
        if (objCursoSeleccionado != null) {
            listaAsignaturas = servParcial.getMateriasCursoDocente(((Object[]) objCursoSeleccionado)[0] + "");;

        } else {
            listaAsignaturas = servParcial.getMateriasCursoDocente("-1");;
        }

        TablaGenerica tab_reporte = utilitario.consultar("select '' as CUENTA,'' as NOMINA, '' as Q1P1,'' as Q1P2,'' as Q1P3,'' as Q1SUMATORIA,'' as Q1EQV80,'' as Q1EXAMEN,'' as Q1EQV20,'' as Q1NOTA,\n"
                + "'' as Q2P1,'' as Q2P2,'' as Q2P3,'' as Q2SUMATORIA,'' as Q2EQV80,'' as Q2EXAMEN,'' as Q2EQV20,'' as Q2NOTA,'' as PROMEDIOFINAL");

        TablaGenerica tab_alumnos = getAlumnoCurso(curso, alumno);

        for (int m = (listaAsignaturas.size() - 1); m >= 0; m--) {
            Object[] fila = (Object[]) listaAsignaturas.get(m);
            str_ide_dis = fila[0] + "";
            str_nom_dis = fila[1] + "";

            for (int i = 0; i < tab_alumnos.getTotalFilas(); i++) {
                tab_reporte.insertar();
                TablaGenerica tab_nota = getNotasParcialQuimestre(str_ide_dis, tab_alumnos.getValor(i, "mat_codigo"), "1");
                tab_reporte.setValor("CUENTA", (m + 1) + "");
                tab_reporte.setValor("NOMINA", str_nom_dis); //NOMBRE DE LA MATERIA

                if (tab_nota.getTotalFilas() == 3) {
                    tab_reporte.setValor("Q1P1", tab_nota.getValor(0, "not_primerparcial"));
                    tab_reporte.setValor("Q1P2", tab_nota.getValor(1, "not_primerparcial"));
                    tab_reporte.setValor("Q1P3", tab_nota.getValor(2, "not_primerparcial"));
                } else if (tab_nota.getTotalFilas() == 2) {
                    tab_reporte.setValor("Q1P1", tab_nota.getValor(0, "not_primerparcial"));
                    tab_reporte.setValor("Q1P2", tab_nota.getValor(1, "not_primerparcial"));
                    tab_reporte.setValor("Q1P3", "0.00");
                } else if (tab_nota.getTotalFilas() == 1) {
                    tab_reporte.setValor("Q1P1", tab_nota.getValor(0, "not_primerparcial"));
                    tab_reporte.setValor("Q1P2", "0.00");
                    tab_reporte.setValor("Q1P3", "0.00");
                } else if (tab_nota.getTotalFilas() == 0) {
                    tab_reporte.setValor("Q1P1", "0.00");
                    tab_reporte.setValor("Q1P2", "0.00");
                    tab_reporte.setValor("Q1P3", "0.00");
                }

                TablaGenerica tab_suma = gatSumaQuimestre(str_ide_dis, tab_alumnos.getValor(i, "mat_codigo"), "1");

                if (tab_suma.isEmpty() == false) {
                    tab_reporte.setValor("Q1SUMATORIA", tab_suma.getValor(0, "inf_sumatoria"));
                    tab_reporte.setValor("Q1EQV80", tab_suma.getValor(0, "inf_eqv80"));
                    tab_reporte.setValor("Q1EXAMEN", tab_suma.getValor(0, "inf_examen"));
                    tab_reporte.setValor("Q1EQV20", tab_suma.getValor(0, "inf_exa20"));
                    tab_reporte.setValor("Q1NOTA", tab_suma.getValor(0, "inf_nota"));
                } else {
                    tab_reporte.setValor("Q1SUMATORIA", "0.00");
                    tab_reporte.setValor("Q1EQV80", "0.00");
                    tab_reporte.setValor("Q1EXAMEN", "0.00");
                    tab_reporte.setValor("Q1EQV20", "0.00");
                    tab_reporte.setValor("Q1NOTA", "0.00");
                }

                ///2 quimestre
                tab_nota = getNotasParcialQuimestre(str_ide_dis, tab_alumnos.getValor(i, "mat_codigo"), "4");

                tab_reporte.setValor("CUENTA", (m + 1) + "");
                tab_reporte.setValor("NOMINA", str_nom_dis); //NOMBRE DE LA MATERIA

                if (tab_nota.getTotalFilas() == 3) {
                    tab_reporte.setValor("Q2P1", tab_nota.getValor(0, "not_primerparcial"));
                    tab_reporte.setValor("Q2P2", tab_nota.getValor(1, "not_primerparcial"));
                    tab_reporte.setValor("Q2P3", tab_nota.getValor(2, "not_primerparcial"));
                } else if (tab_nota.getTotalFilas() == 2) {
                    tab_reporte.setValor("Q2P1", tab_nota.getValor(0, "not_primerparcial"));
                    tab_reporte.setValor("Q2P2", tab_nota.getValor(1, "not_primerparcial"));
                    tab_reporte.setValor("Q2P3", "0.00");
                } else if (tab_nota.getTotalFilas() == 1) {
                    tab_reporte.setValor("Q2P1", tab_nota.getValor(0, "not_primerparcial"));
                    tab_reporte.setValor("Q2P2", "0.00");
                    tab_reporte.setValor("Q2P3", "0.00");
                } else if (tab_nota.getTotalFilas() == 0) {
                    tab_reporte.setValor("Q2P1", "0.00");
                    tab_reporte.setValor("Q2P2", "0.00");
                    tab_reporte.setValor("Q2P3", "0.00");
                }

                tab_suma = gatSumaQuimestre(str_ide_dis, tab_alumnos.getValor(i, "mat_codigo"), "4");

                if (tab_suma.isEmpty() == false) {
                    tab_reporte.setValor("Q2SUMATORIA", tab_suma.getValor(0, "inf_sumatoria"));
                    tab_reporte.setValor("Q2EQV80", tab_suma.getValor(0, "inf_eqv80"));
                    tab_reporte.setValor("Q2EXAMEN", tab_suma.getValor(0, "inf_examen"));
                    tab_reporte.setValor("Q2EQV20", tab_suma.getValor(0, "inf_exa20"));
                    tab_reporte.setValor("Q2NOTA", tab_suma.getValor(0, "inf_nota"));
                } else {
                    tab_reporte.setValor("Q2SUMATORIA", "0.00");
                    tab_reporte.setValor("Q2EQV80", "0.00");
                    tab_reporte.setValor("Q2EXAMEN", "0.00");
                    tab_reporte.setValor("Q2EQV20", "0.00");
                    tab_reporte.setValor("Q2NOTA", "0.00");
                }
                double dou_promedio = 0;
                try {
                    dou_promedio = Double.parseDouble(tab_reporte.getValor("Q1NOTA")) + Double.parseDouble(tab_reporte.getValor("Q2NOTA"));
                } catch (Exception e) {
                }
                String val = (dou_promedio / 2) + "";
                BigDecimal big = new BigDecimal(val);
                big = big.setScale(2, RoundingMode.HALF_UP);
                tab_reporte.setValor("PROMEDIOFINAL", big + "");
            }
        }
        List lisResultado = new ArrayList();

        for (int i = 0; i < tab_reporte.getTotalFilas(); i++) {
            Object[] fila = new Object[tab_reporte.getTotalColumnas()];
            for (int j = 0; j < tab_reporte.getTotalColumnas(); j++) {
                fila[j] = tab_reporte.getValor(i, tab_reporte.getColumnas()[j].getNombre());

            }
            lisResultado.add(fila);
        }

        return lisResultado;
    }

    private void cuadroGeneral() {
        //inicializa tablas vacias
        TablaGenerica tab_materias = utilitario.consultar("select d.dis_codigo,a.asi_nombre,a.tip_codigo,c.asi_nombre from distributivomxc d\n"
                + "INNER JOIN asignaturas a on d.asi_codigo=a.asi_codigo\n"
                + "LEFT JOIN asignaturas c on a.asi_asi_codigo=c.asi_codigo \n"
                + "where d.cre_codigo=-1 and a.tip_codigo=1 order by a.tip_codigo,a.asi_nombre");
        TablaGenerica tab_optativas = utilitario.consultar("select d.dis_codigo,a.asi_nombre,a.tip_codigo,c.asi_nombre from distributivomxc d\n"
                + "INNER JOIN asignaturas a on d.asi_codigo=a.asi_codigo\n"
                + "LEFT JOIN asignaturas c on a.asi_asi_codigo=c.asi_codigo \n"
                + "where d.cre_codigo=-1 and a.tip_codigo=2 order by a.tip_codigo,a.asi_nombre");

        TablaGenerica tab_reporte = utilitario.consultar("select '' as num, '' as mat_codigo,'' as alumnos,'' as dis_codigo,'' as asi_nombre,"
                + " '' as q1, '' as q2, '' as p,'' as orden");
        tab_reporte.limpiar();
        //Materias del curso   
        if (objCursoSeleccionado != null) {
            //solo materias academicas
            tab_materias = utilitario.consultar("select d.dis_codigo,a.asi_nombre,a.tip_codigo,c.asi_nombre from distributivomxc d\n"
                    + "INNER JOIN asignaturas a on d.asi_codigo=a.asi_codigo\n"
                    + "LEFT JOIN asignaturas c on a.asi_asi_codigo=c.asi_codigo \n"
                    + "where d.cre_codigo=" + objCursoSeleccionado + " and a.tip_codigo=1 order by a.tip_codigo,a.asi_nombre");
            tab_optativas = utilitario.consultar("select d.dis_codigo,a.asi_nombre,a.tip_codigo,c.asi_nombre from distributivomxc d\n"
                    + "INNER JOIN asignaturas a on d.asi_codigo=a.asi_codigo\n"
                    + "LEFT JOIN asignaturas c on a.asi_asi_codigo=c.asi_codigo \n"
                    + "where d.cre_codigo=" + objCursoSeleccionado + " and a.tip_codigo=2 order by a.tip_codigo,a.asi_nombre");
        }
        //Alumnos del curso
        TablaGenerica tab_alumnos = getAlumnosCurso(objCursoSeleccionado + "");

        for (int i = 0; i < tab_alumnos.getTotalFilas(); i++) {
            double dou_rendimientoq1 = 0;
            double dou_rendimientoq2 = 0;
            //Materias Academicas
            if (tab_materias != null && tab_materias.isEmpty() == false) {
                for (int j = 0; j < tab_materias.getTotalFilas(); j++) {
                    //Busca la nota final de cada quimestre por alumno y por materia
                    TablaGenerica tab_quimestre1 = utilitario.consultar("select * from informe_quimestre\n"
                            + "where mat_codigo=" + tab_alumnos.getValor(i, "mat_codigo") + " and for_codigo=1 \n"
                            + "and dis_codigo=" + tab_materias.getValor(j, "dis_codigo"));

                    TablaGenerica tab_quimestre2 = utilitario.consultar("select * from informe_quimestre\n"
                            + "where mat_codigo=" + tab_alumnos.getValor(i, "mat_codigo") + " and for_codigo=4 \n"
                            + "and dis_codigo=" + tab_materias.getValor(j, "dis_codigo"));

                    if (tab_materias.getValor(j, "asi_nombre") != null) {
                        tab_reporte.insertar();
                        tab_reporte.setValor("num", (tab_alumnos.getTotalFilas() - i) + "");
                        tab_reporte.setValor("mat_codigo", tab_alumnos.getValor(i, "mat_codigo"));
                        tab_reporte.setValor("alumnos", tab_alumnos.getValor(i, "Alumnos"));
                        tab_reporte.setValor("dis_codigo", tab_materias.getValor(j, "dis_codigo"));
                        tab_reporte.setValor("asi_nombre", tab_materias.getValor(j, "asi_nombre"));
                        tab_reporte.setValor("orden", j + "");
                        if (tab_quimestre1.isEmpty() == false) {
                            tab_reporte.setValor("q1", tab_quimestre1.getValor("inf_nota"));
                        } else {
                            tab_reporte.setValor("q1", "0.00");
                        }

                        if (tab_quimestre2.isEmpty() == false) {
                            tab_reporte.setValor("q2", tab_quimestre2.getValor("inf_nota"));
                        } else {
                            tab_reporte.setValor("q2", "0.00");
                        }
                        try {
                            double dou_prom = Double.parseDouble(tab_reporte.getValor("q1")) + Double.parseDouble(tab_reporte.getValor("q2"));
                            dou_prom = dou_prom / 2;
                            String val = dou_prom + "";
                            BigDecimal big = new BigDecimal(val);
                            big = big.setScale(2, RoundingMode.HALF_UP);
                            tab_reporte.setValor("p", big + "");
                            //acumula rendimientoq1
                            dou_rendimientoq1 += Double.parseDouble(tab_reporte.getValor("q1"));
                            dou_rendimientoq2 += Double.parseDouble(tab_reporte.getValor("q2"));
                        } catch (Exception e) {
                            tab_reporte.setValor("p", "0.00");
                        }

                    } else {
                        break;
                    }
                }
            }

            //Promedio materias Optativas 
            double op_q1 = 0;
            double op_q2 = 0;
            if (tab_optativas != null && tab_optativas.isEmpty() == false) {
                for (int j = 0; j < tab_optativas.getTotalFilas(); j++) {
                    //Busca la nota final de cada quimestre por alumno y por materia
                    TablaGenerica tab_quimestre1 = utilitario.consultar("select * from informe_quimestre\n"
                            + "where mat_codigo=" + tab_alumnos.getValor(i, "mat_codigo") + " and for_codigo=1 \n"
                            + "and dis_codigo=" + tab_optativas.getValor(j, "dis_codigo"));

                    TablaGenerica tab_quimestre2 = utilitario.consultar("select * from informe_quimestre\n"
                            + "where mat_codigo=" + tab_alumnos.getValor(i, "mat_codigo") + " and for_codigo=4 \n"
                            + "and dis_codigo=" + tab_optativas.getValor(j, "dis_codigo"));
                    double vq1 = 0;
                    double vq2 = 0;
                    if (tab_optativas.getValor(j, "asi_nombre") != null) {
                        if (tab_quimestre1.isEmpty() == false) {
                            try {
                                vq1 = Double.parseDouble(tab_quimestre1.getValor("inf_nota"));
                            } catch (Exception e) {
                            }
                        }
                        if (tab_quimestre2.isEmpty() == false) {
                            try {
                                vq2 = Double.parseDouble(tab_quimestre2.getValor("inf_nota"));
                            } catch (Exception e) {
                            }
                        }
                        //acumula rendimientoq1
                        op_q1 += vq1;
                        op_q2 += vq2;
                    } else {
                        break;
                    }
                }
            }

            if (tab_optativas != null && tab_optativas.isEmpty() == false) {
                //////////////////////////
                tab_reporte.insertar();
                tab_reporte.setValor("num", (tab_alumnos.getTotalFilas() - i) + "");
                tab_reporte.setValor("mat_codigo", tab_alumnos.getValor(i, "mat_codigo"));
                tab_reporte.setValor("alumnos", tab_alumnos.getValor(i, "Alumnos"));
                tab_reporte.setValor("dis_codigo", "1235");
                tab_reporte.setValor("asi_nombre", "OPTATIVA");
                //saca promedio q1 y q2
                op_q1 = op_q1 / tab_optativas.getTotalFilas();
                op_q2 = op_q2 / tab_optativas.getTotalFilas();

                tab_reporte.setValor("q1", utilitario.getFormatoNumero(op_q1));
                tab_reporte.setValor("q2", utilitario.getFormatoNumero(op_q2));
                try {

                    double dou_prom = (op_q1 + op_q2) / 2;
                    String val = dou_prom + "";
                    BigDecimal big = new BigDecimal(val);
                    big = big.setScale(2, RoundingMode.HALF_UP);
                    tab_reporte.setValor("p", big + "");
                } catch (Exception e) {
                    tab_reporte.setValor("p", "0.00");
                }
                tab_reporte.setValor("orden", tab_materias.getTotalFilas() + "");
            }

            //Rendimiento
            if (tab_materias != null && tab_materias.isEmpty() == false) {
                if (tab_optativas != null && tab_optativas.isEmpty() == false) {
                    dou_rendimientoq1 = (dou_rendimientoq1 + op_q1) / (tab_materias.getTotalFilas() + 1);
                    dou_rendimientoq2 = (dou_rendimientoq2 + op_q2) / (tab_materias.getTotalFilas() + 1);
                } else {
                    dou_rendimientoq1 = dou_rendimientoq1 / tab_materias.getTotalFilas();
                    dou_rendimientoq2 = dou_rendimientoq2 / tab_materias.getTotalFilas();
                }
                tab_reporte.insertar();
                tab_reporte.setValor("num", (tab_alumnos.getTotalFilas() - i) + "");
                tab_reporte.setValor("mat_codigo", tab_alumnos.getValor(i, "mat_codigo"));
                tab_reporte.setValor("alumnos", tab_alumnos.getValor(i, "Alumnos"));
                tab_reporte.setValor("dis_codigo", "1234");
                tab_reporte.setValor("asi_nombre", "RENDIMIENTO ACADEMICO");
                tab_reporte.setValor("q1", utilitario.getFormatoNumero(dou_rendimientoq1));
                tab_reporte.setValor("q2", utilitario.getFormatoNumero(dou_rendimientoq2));
                tab_reporte.setValor("orden", (tab_materias.getTotalFilas() + 1) + "");
                try {
                    double dou_prom = Double.parseDouble(tab_reporte.getValor("q1")) + Double.parseDouble(tab_reporte.getValor("q2"));
                    dou_prom = dou_prom / 2;
                    String val = dou_prom + "";
                    BigDecimal big = new BigDecimal(val);
                    big = big.setScale(2, RoundingMode.HALF_UP);
                    tab_reporte.setValor("p", big + "");
                } catch (Exception e) {
                    tab_reporte.setValor("p", "0.00");
                }
            }

            //COMPORTAMIENTO
            //CALCULA DISCIPLINA DEL QUIMESTRE

            TablaGenerica tab_dis_q1 = utilitario.consultar("SELECT mat_codigo,round(avg(com_sumatoria),2) as prom FROM comportamientoparcial\n"
                    + "where  for_codigo=1\n"
                    + "and  mat_codigo=" + tab_alumnos.getValor(i, "mat_codigo") + "\n"
                    + "group by mat_codigo");
            TablaGenerica tab_dis_q2 = utilitario.consultar("SELECT mat_codigo,round(avg(com_sumatoria),2) as prom FROM comportamientoparcial\n"
                    + "where  for_codigo=4\n"
                    + "and  mat_codigo=" + tab_alumnos.getValor(i, "mat_codigo") + "\n"
                    + "group by mat_codigo");
            double dou_prom_q1 = 0;
            double dou_prom_q2 = 0;
            String str_eq1 = "";
            String str_eq2 = "";
            if (tab_dis_q1.isEmpty() == false) {
                //DISCIPLINA Q1
                try {
                    dou_prom_q1 = Double.parseDouble(tab_dis_q1.getValor("prom"));
                } catch (Exception e) {
                }
            }
            if (tab_dis_q2.isEmpty() == false) {
                //DISCIPLINA Q1
                try {
                    dou_prom_q2 = Double.parseDouble(tab_dis_q2.getValor("prom"));
                } catch (Exception e) {
                }
            }


            TablaGenerica tab_equi = utilitario.consultar("SELECT * FROM equivalencia_conducta");


            for (int j = 0; j < tab_equi.getTotalFilas(); j++) {
                String str_expresion = tab_equi.getValor(j, "eqc_escala");
                str_expresion = str_expresion.replace("nota", dou_prom_q1 + "");
                Object obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
                if (obj_resultado != null) {
                    str_eq1 = tab_equi.getValor(j, "eqc_alterno");
                    break;
                }
            }
            for (int j = 0; j < tab_equi.getTotalFilas(); j++) {
                String str_expresion = tab_equi.getValor(j, "eqc_escala");
                str_expresion = str_expresion.replace("nota", dou_prom_q2 + "");
                Object obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
                if (obj_resultado != null) {
                    str_eq2 = tab_equi.getValor(j, "eqc_alterno");
                    break;
                }
            }


            Object obj_resultado = null;
            String str_eqv = "";
            for (int j = 0; j < tab_equi.getTotalFilas(); j++) {
                String str_expresion = tab_equi.getValor(j, "eqc_escala");
                str_expresion = str_expresion.replace("nota", ((dou_prom_q1 + dou_prom_q2) / 2) + "");
                obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
                if (obj_resultado != null) {
                    str_eqv = tab_equi.getValor(j, "eqc_alterno");
                    break;
                }
            }
            if (obj_resultado == null) {
                obj_resultado = "NO EQV";
            }


            tab_reporte.insertar();
            tab_reporte.setValor("num", (tab_alumnos.getTotalFilas() - i) + "");
            tab_reporte.setValor("mat_codigo", tab_alumnos.getValor(i, "mat_codigo"));
            tab_reporte.setValor("alumnos", tab_alumnos.getValor(i, "Alumnos"));
            tab_reporte.setValor("dis_codigo", "1236");
            tab_reporte.setValor("asi_nombre", "COMPORTAMIENTO");
            tab_reporte.setValor("q1", str_eq1);
            tab_reporte.setValor("q2", str_eq2);
            tab_reporte.setValor("p", str_eqv);
            tab_reporte.setValor("orden", (tab_materias.getTotalFilas() + 2) + "");


            ///ASISTENCIA
            TablaGenerica tab_asiq1 = utilitario.consultar("SELECT sum(reg_diaslaborados)as reg_diaslaborados,sum(reg_faltasinjustificadas) as reg_faltasinjustificadas,sum(reg_faltasjustificadas) as reg_faltasjustificadas,sum(reg_atrasos) as reg_atrasos FROM registroasistencia\n"
                    + "where  for_codigo=1\n"
                    + "and  mat_codigo=" + tab_alumnos.getValor(i, "mat_codigo") + "");
            TablaGenerica tab_asiq2 = utilitario.consultar("SELECT sum(reg_diaslaborados)as reg_diaslaborados,sum(reg_faltasinjustificadas) as reg_faltasinjustificadas,sum(reg_faltasjustificadas) as reg_faltasjustificadas,sum(reg_atrasos) as reg_atrasos FROM registroasistencia\n"
                    + "where  for_codigo=4\n"
                    + "and  mat_codigo=" + tab_alumnos.getValor(i, "mat_codigo") + "");
            int int_diasq1 = 0;
            int int_diasq2 = 0;
            if (tab_asiq1.isEmpty() == false) {
                try {
                    int_diasq1 = Integer.parseInt(tab_asiq1.getValor("reg_diaslaborados"));
                } catch (Exception e) {
                }
            }
            if (tab_asiq2.isEmpty() == false) {
                try {
                    int_diasq2 = Integer.parseInt(tab_asiq2.getValor("reg_diaslaborados"));
                } catch (Exception e) {
                }
            }
            tab_reporte.insertar();
            tab_reporte.setValor("num", (tab_alumnos.getTotalFilas() - i) + "");
            tab_reporte.setValor("mat_codigo", tab_alumnos.getValor(i, "mat_codigo"));
            tab_reporte.setValor("alumnos", tab_alumnos.getValor(i, "Alumnos"));
            tab_reporte.setValor("dis_codigo", "1237");
            tab_reporte.setValor("asi_nombre", "DIAS ASISTIDOS");
            tab_reporte.setValor("q1", int_diasq1 + "");
            tab_reporte.setValor("q2", int_diasq2 + "");
            tab_reporte.setValor("p", (int_diasq1 + int_diasq2) + "");
            tab_reporte.setValor("orden", (tab_materias.getTotalFilas() + 3) + "");
        }

        GenerarReporte genera = new GenerarReporte();
        Map parametros = new HashMap();

        if (objCursoSeleccionado != null) {
            CrearCurso cre_curso = servCreaCurso.getCrearCurso(objCursoSeleccionado + "");
            parametros.put("CURSO", cre_curso.getCurCodigo().getCurAnio());
            parametros.put("PARALELO", cre_curso.getParCodigo().getParNombre());
        }
        parametros.put("PROFESOR", docDocente.getDocNombres());
        parametros.put("PERIODO", perActual.getPerNombre());

        genera.setDataSource(new ReporteDataSource(tab_reporte));
        genera.generar(parametros, "/reportes/rep_parcial/rep_cuadro_general.jasper");

    }

    public void verReporteCuadroGeneral() {
        if (objCursoSeleccionado != null) {
            cuadroGeneral();
            utilitario.ejecutarJavaScript("window.open('" + str_path_reporte + "');");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un curso", "");
        }

    }

    public void verReporteTodos() {
        if (objAsignaturaSeleccionada != null) {
            generarReportePromedio();
            utilitario.ejecutarJavaScript("window.open('" + str_path_reporte + "');");
        } else {
            utilitario.agregarMensajeInfo("Debe selecccionar una asignatura", "");
        }
    }

    private TablaGenerica getNotasParcialQuimestre(String dis_codigo, String mat_codigo, String for_codigo) {
        return utilitario.consultar("select mat_codigo,not_primerparcial from nota_destrezaparcial\n"
                + "where eva_codigo in(1,2,3) and for_codigo=" + for_codigo + "\n"
                + "and mat_codigo=" + mat_codigo + "\n"
                + "and dis_codigo=" + dis_codigo + "\n"
                + "order by eva_codigo");
    }

    private TablaGenerica gatSumaQuimestre(String dis_codigo, String mat_codigo, String for_codigo) {
        return utilitario.consultar("select mat_codigo,inf_sumatoria,inf_eqv80,inf_examen,inf_exa20,inf_nota from informe_quimestre\n"
                + "where for_codigo=" + for_codigo + "\n"
                + "and mat_codigo=" + mat_codigo + "\n"
                + "and dis_codigo=" + dis_codigo + "");
    }

    //Reporte esistencia
    public void generarReporteAsistencia() {

        TablaGenerica tab_reporte = utilitario.consultar("select '' AS CUENTA,'' as NOMINA\n"
                + "\n"
                + ",'' as Q1P1A,'' as Q1P1FJ,'' as Q1P1FI,'' as Q1P1TOTAL,'' as Q1P1DIAS\n"
                + ",'' as Q1P2A,'' as Q1P2FJ,'' as Q1P2FI,'' as Q1P2TOTAL,'' as Q1P2DIAS\n"
                + ",'' as Q1P3A,'' as Q1P3FJ,'' as Q1P3FI,'' as Q1P3TOTAL,'' as Q1P3DIAS\n"
                + ",'' as TQ1A,'' as TQ1FJ,'' as TQ1FI,'' as TQ1TOTAL,'' as TQ1DIAS\n"
                + "\n"
                + ",'' as Q2P1A,'' as Q2P1FJ,'' as Q2P1FI,'' as Q2P1TOTAL,'' as Q2P1DIAS\n"
                + ",'' as Q2P2A,'' as Q2P2FJ,'' as Q2P2FI,'' as Q2P2TOTAL,'' as Q2P2DIAS\n"
                + ",'' as Q2P3A,'' as Q2P3FJ,'' as Q2P3FI,'' as Q2P3TOTAL,'' as Q2P3DIAS\n"
                + ",'' as TQ2A,'' as TQ2FJ,'' as TQ2FI,'' as TQ2TOTAL,'' as TQ2DIAS\n"
                + "\n"
                + ",'' as TA,'' as TFJ,'' as TFI,'' as TTOTAL,'' as TDIAS");
        tab_reporte.limpiar();
        TablaGenerica tab_alumnos = getAlumnosCurso(((Object[]) objCursoSeleccionado)[0] + "");


        for (int i = 0; i < tab_alumnos.getTotalFilas(); i++) {
            tab_reporte.insertar();
            tab_reporte.setValor("CUENTA", String.valueOf((tab_alumnos.getTotalFilas() - i)));
            tab_reporte.setValor("NOMINA", tab_alumnos.getValor(i, "Alumnos"));

            TablaGenerica tab_asistencia = getAsistenciaQuimestre(tab_alumnos.getValor(i, "mat_codigo"), "1");

            if (tab_asistencia.getTotalFilas() == 3) {
                tab_reporte.setValor("Q1P1A", tab_asistencia.getValor(0, "reg_atrasos"));
                tab_reporte.setValor("Q1P1FJ", tab_asistencia.getValor(0, "reg_faltasjustificadas"));
                tab_reporte.setValor("Q1P1FI", tab_asistencia.getValor(0, "reg_faltasinjustificadas"));
                tab_reporte.setValor("Q1P1TOTAL", tab_asistencia.getValor(0, "reg_totalfaltas"));
                tab_reporte.setValor("Q1P1DIAS", tab_asistencia.getValor(0, "reg_diaslaborados"));

                tab_reporte.setValor("Q1P2A", tab_asistencia.getValor(1, "reg_atrasos"));
                tab_reporte.setValor("Q1P2FJ", tab_asistencia.getValor(1, "reg_faltasjustificadas"));
                tab_reporte.setValor("Q1P2FI", tab_asistencia.getValor(1, "reg_faltasinjustificadas"));
                tab_reporte.setValor("Q1P2TOTAL", tab_asistencia.getValor(1, "reg_totalfaltas"));
                tab_reporte.setValor("Q1P2DIAS", tab_asistencia.getValor(1, "reg_diaslaborados"));

                tab_reporte.setValor("Q1P3A", tab_asistencia.getValor(2, "reg_atrasos"));
                tab_reporte.setValor("Q1P3FJ", tab_asistencia.getValor(2, "reg_faltasjustificadas"));
                tab_reporte.setValor("Q1P3FI", tab_asistencia.getValor(2, "reg_faltasinjustificadas"));
                tab_reporte.setValor("Q1P3TOTAL", tab_asistencia.getValor(2, "reg_totalfaltas"));
                tab_reporte.setValor("Q1P3DIAS", tab_asistencia.getValor(2, "reg_diaslaborados"));
            } else if (tab_asistencia.getTotalFilas() == 2) {
                tab_reporte.setValor("Q1P1A", tab_asistencia.getValor(0, "reg_atrasos"));
                tab_reporte.setValor("Q1P1FJ", tab_asistencia.getValor(0, "reg_faltasjustificadas"));
                tab_reporte.setValor("Q1P1FI", tab_asistencia.getValor(0, "reg_faltasinjustificadas"));
                tab_reporte.setValor("Q1P1TOTAL", tab_asistencia.getValor(0, "reg_totalfaltas"));
                tab_reporte.setValor("Q1P1DIAS", tab_asistencia.getValor(0, "reg_diaslaborados"));

                tab_reporte.setValor("Q1P2A", tab_asistencia.getValor(1, "reg_atrasos"));
                tab_reporte.setValor("Q1P2FJ", tab_asistencia.getValor(1, "reg_faltasjustificadas"));
                tab_reporte.setValor("Q1P2FI", tab_asistencia.getValor(1, "reg_faltasinjustificadas"));
                tab_reporte.setValor("Q1P2TOTAL", tab_asistencia.getValor(1, "reg_totalfaltas"));
                tab_reporte.setValor("Q1P2DIAS", tab_asistencia.getValor(1, "reg_diaslaborados"));
            } else if (tab_asistencia.getTotalFilas() == 1) {
                tab_reporte.setValor("Q1P1A", tab_asistencia.getValor(0, "reg_atrasos"));
                tab_reporte.setValor("Q1P1FJ", tab_asistencia.getValor(0, "reg_faltasjustificadas"));
                tab_reporte.setValor("Q1P1FI", tab_asistencia.getValor(0, "reg_faltasinjustificadas"));
                tab_reporte.setValor("Q1P1TOTAL", tab_asistencia.getValor(0, "reg_totalfaltas"));
                tab_reporte.setValor("Q1P1DIAS", tab_asistencia.getValor(0, "reg_diaslaborados"));
            }
            int int_a = 0;
            int int_fj = 0;
            int int_fi = 0;
            int int_total = 0;
            int int_dias = 0;

            try {
                int_a = Integer.parseInt(tab_reporte.getValor("Q1P1A") == null ? "0" : tab_reporte.getValor("Q1P1A")) + Integer.parseInt(tab_reporte.getValor("Q1P2A") == null ? "0" : tab_reporte.getValor("Q1P2A")) + Integer.parseInt(tab_reporte.getValor("Q1P3A") == null ? "0" : tab_reporte.getValor("Q1P3A"));
            } catch (Exception e) {
            }

            try {
                int_fj = Integer.parseInt(tab_reporte.getValor("Q1P1FJ") == null ? "0" : tab_reporte.getValor("Q1P1FJ")) + Integer.parseInt(tab_reporte.getValor("Q1P2FJ") == null ? "0" : tab_reporte.getValor("Q1P2FJ")) + Integer.parseInt(tab_reporte.getValor("Q1P3FJ") == null ? "0" : tab_reporte.getValor("Q1P3FJ"));
            } catch (Exception e) {
            }

            try {
                int_fi = Integer.parseInt(tab_reporte.getValor("Q1P1FI") == null ? "0" : tab_reporte.getValor("Q1P1FI")) + Integer.parseInt(tab_reporte.getValor("Q1P2FI") == null ? "0" : tab_reporte.getValor("Q1P2FI")) + Integer.parseInt(tab_reporte.getValor("Q1P3FI") == null ? "0" : tab_reporte.getValor("Q1P3FI"));
            } catch (Exception e) {
            }

            try {
                int_total = Integer.parseInt(tab_reporte.getValor("Q1P1TOTAL") == null ? "0" : tab_reporte.getValor("Q1P1TOTAL")) + Integer.parseInt(tab_reporte.getValor("Q1P2TOTAL") == null ? "0" : tab_reporte.getValor("Q1P2TOTAL")) + Integer.parseInt(tab_reporte.getValor("Q1P3TOTAL") == null ? "0" : tab_reporte.getValor("Q1P3TOTAL"));
            } catch (Exception e) {
            }

            int_dias = 0 - int_total;

            tab_reporte.setValor("TQ1A", int_a + "");
            tab_reporte.setValor("TQ1FJ", int_fj + "");
            tab_reporte.setValor("TQ1FI", int_fi + "");
            tab_reporte.setValor("TQ1TOTAL", int_total + "");
            tab_reporte.setValor("TQ1DIAS", int_dias + "");


            ///QUIMESTRE 2



            tab_asistencia = getAsistenciaQuimestre(tab_alumnos.getValor(i, "mat_codigo"), "4");

            if (tab_asistencia.getTotalFilas() == 3) {
                tab_reporte.setValor("Q2P1A", tab_asistencia.getValor(0, "reg_atrasos"));
                tab_reporte.setValor("Q2P1FJ", tab_asistencia.getValor(0, "reg_faltasjustificadas"));
                tab_reporte.setValor("Q2P1FI", tab_asistencia.getValor(0, "reg_faltasinjustificadas"));
                tab_reporte.setValor("Q2P1TOTAL", tab_asistencia.getValor(0, "reg_totalfaltas"));
                tab_reporte.setValor("Q2P1DIAS", tab_asistencia.getValor(0, "reg_diaslaborados"));

                tab_reporte.setValor("Q2P2A", tab_asistencia.getValor(1, "reg_atrasos"));
                tab_reporte.setValor("Q2P2FJ", tab_asistencia.getValor(1, "reg_faltasjustificadas"));
                tab_reporte.setValor("Q2P2FI", tab_asistencia.getValor(1, "reg_faltasinjustificadas"));
                tab_reporte.setValor("Q2P2TOTAL", tab_asistencia.getValor(1, "reg_totalfaltas"));
                tab_reporte.setValor("Q2P2DIAS", tab_asistencia.getValor(1, "reg_diaslaborados"));

                tab_reporte.setValor("Q2P3A", tab_asistencia.getValor(2, "reg_atrasos"));
                tab_reporte.setValor("Q2P3FJ", tab_asistencia.getValor(2, "reg_faltasjustificadas"));
                tab_reporte.setValor("Q2P3FI", tab_asistencia.getValor(2, "reg_faltasinjustificadas"));
                tab_reporte.setValor("Q2P3TOTAL", tab_asistencia.getValor(2, "reg_totalfaltas"));
                tab_reporte.setValor("Q2P3DIAS", tab_asistencia.getValor(2, "reg_diaslaborados"));
            } else if (tab_asistencia.getTotalFilas() == 2) {
                tab_reporte.setValor("Q2P1A", tab_asistencia.getValor(0, "reg_atrasos"));
                tab_reporte.setValor("Q2P1FJ", tab_asistencia.getValor(0, "reg_faltasjustificadas"));
                tab_reporte.setValor("Q2P1FI", tab_asistencia.getValor(0, "reg_faltasinjustificadas"));
                tab_reporte.setValor("Q2P1TOTAL", tab_asistencia.getValor(0, "reg_totalfaltas"));
                tab_reporte.setValor("Q2P1DIAS", tab_asistencia.getValor(0, "reg_diaslaborados"));

                tab_reporte.setValor("Q2P2A", tab_asistencia.getValor(1, "reg_atrasos"));
                tab_reporte.setValor("Q2P2FJ", tab_asistencia.getValor(1, "reg_faltasjustificadas"));
                tab_reporte.setValor("Q2P2FI", tab_asistencia.getValor(1, "reg_faltasinjustificadas"));
                tab_reporte.setValor("Q2P2TOTAL", tab_asistencia.getValor(1, "reg_totalfaltas"));
                tab_reporte.setValor("Q2P2DIAS", tab_asistencia.getValor(1, "reg_diaslaborados"));
            } else if (tab_asistencia.getTotalFilas() == 1) {
                tab_reporte.setValor("Q2P1A", tab_asistencia.getValor(0, "reg_atrasos"));
                tab_reporte.setValor("Q2P1FJ", tab_asistencia.getValor(0, "reg_faltasjustificadas"));
                tab_reporte.setValor("Q2P1FI", tab_asistencia.getValor(0, "reg_faltasinjustificadas"));
                tab_reporte.setValor("Q2P1TOTAL", tab_asistencia.getValor(0, "reg_totalfaltas"));
                tab_reporte.setValor("Q2P1DIAS", tab_asistencia.getValor(0, "reg_diaslaborados"));
            }
            int_a = 0;
            int_fj = 0;
            int_fi = 0;
            int_total = 0;
            int_dias = 0;

            try {
                int_a = Integer.parseInt(tab_reporte.getValor("Q2P1A") == null ? "0" : tab_reporte.getValor("Q2P1A")) + Integer.parseInt(tab_reporte.getValor("Q2P2A") == null ? "0" : tab_reporte.getValor("Q2P2A")) + Integer.parseInt(tab_reporte.getValor("Q2P3A") == null ? "0" : tab_reporte.getValor("Q2P3A"));
            } catch (Exception e) {
            }

            try {
                int_fj = Integer.parseInt(tab_reporte.getValor("Q2P1FJ") == null ? "0" : tab_reporte.getValor("Q2P1FJ")) + Integer.parseInt(tab_reporte.getValor("Q2P2FJ") == null ? "0" : tab_reporte.getValor("Q2P2FJ")) + Integer.parseInt(tab_reporte.getValor("Q2P3FJ") == null ? "0" : tab_reporte.getValor("Q2P3FJ"));
            } catch (Exception e) {
            }

            try {
                int_fi = Integer.parseInt(tab_reporte.getValor("Q2P1FI") == null ? "0" : tab_reporte.getValor("Q2P1FI")) + Integer.parseInt(tab_reporte.getValor("Q2P2FI") == null ? "0" : tab_reporte.getValor("Q2P2FI")) + Integer.parseInt(tab_reporte.getValor("Q2P3FI") == null ? "0" : tab_reporte.getValor("Q2P3FI"));
            } catch (Exception e) {
            }

            try {
                int_total = Integer.parseInt(tab_reporte.getValor("Q2P1TOTAL") == null ? "0" : tab_reporte.getValor("Q2P1TOTAL")) + Integer.parseInt(tab_reporte.getValor("Q2P2TOTAL") == null ? "0" : tab_reporte.getValor("Q2P2TOTAL")) + Integer.parseInt(tab_reporte.getValor("Q2P3TOTAL") == null ? "0" : tab_reporte.getValor("Q2P3TOTAL"));
            } catch (Exception e) {
            }

            int_dias = 0 - int_total;

            tab_reporte.setValor("TQ2A", int_a + "");
            tab_reporte.setValor("TQ2FJ", int_fj + "");
            tab_reporte.setValor("TQ2FI", int_fi + "");
            tab_reporte.setValor("TQ2TOTAL", int_total + "");
            tab_reporte.setValor("TQ2DIAS", int_dias + "");


            //TOTALES

            int_a = 0;
            int_fj = 0;
            int_fi = 0;
            int_total = 0;
            int_dias = 0;

            try {
                int_a = Integer.parseInt(tab_reporte.getValor("TQ1A")) + Integer.parseInt(tab_reporte.getValor("TQ2A"));
            } catch (Exception e) {
            }

            try {
                int_fj = Integer.parseInt(tab_reporte.getValor("TQ1FJ")) + Integer.parseInt(tab_reporte.getValor("TQ2FJ"));
            } catch (Exception e) {
            }

            try {
                int_fi = Integer.parseInt(tab_reporte.getValor("TQ1FI")) + Integer.parseInt(tab_reporte.getValor("TQ2FI"));
            } catch (Exception e) {
            }

            try {
                int_total = Integer.parseInt(tab_reporte.getValor("TQ1TOTAL")) + Integer.parseInt(tab_reporte.getValor("TQ2TOTAL"));
            } catch (Exception e) {
            }

            int_dias = 0 - int_total;

            tab_reporte.setValor("TA", int_a + "");
            tab_reporte.setValor("TFJ", int_fj + "");
            tab_reporte.setValor("TFI", int_fi + "");
            tab_reporte.setValor("TTOTAL", int_total + "");
            tab_reporte.setValor("TDIAS", int_dias + "");
        }
        GenerarReporte genera = new GenerarReporte();
        genera.setDataSource(new ReporteDataSource(tab_reporte));
        Map parametros = new HashMap();
        if (objCursoSeleccionado != null) {
            parametros.put("CURSO", ((Object[]) objCursoSeleccionado)[1] + "");
            parametros.put("PARALELO", ((Object[]) objCursoSeleccionado)[2] + "");
        }
        parametros.put("PROFESOR", docDocente.getDocNombres());
        parametros.put("PERIODO", perActual.getPerNombre());
        genera.generar(parametros, "/reportes/rep_parcial/rep_atrasos.jasper");

    }

    private TablaGenerica getAsistenciaQuimestre(String mat_codigo, String for_codigo) {
        return utilitario.consultar("select reg_atrasos,reg_faltasjustificadas,reg_faltasinjustificadas,reg_totalfaltas,reg_diaslaborados from registroasistencia\n"
                + "where eva_codigo in(1,2,3)\n"
                + "and for_codigo=" + for_codigo + "\n"
                + "and mat_codigo=" + mat_codigo + "\n"
                + "order by eva_codigo");
    }

    public void verReporteasitencia() {
        if (objCursoSeleccionado != null) {
            generarReporteAsistencia();
            utilitario.ejecutarJavaScript("window.open('" + str_path_reporte + "');");
        } else {
            utilitario.agregarMensajeInfo("Debe selecccionar un curso", "");
        }
    }

    public void verReporteDestrezas() {
        if (objCursoSeleccionado == null) {
            utilitario.agregarMensajeInfo("Debe selecccionar un curso", "");
            return;
        }
        if (objAsignaturaSeleccionada == null) {
            utilitario.agregarMensajeInfo("Debe selecccionar una Asignatura", "");
            return;
        }
        if (strForma == null || strForma.isEmpty()) {
            utilitario.agregarMensajeInfo("Debe selecccionar un quimestre", "");
            return;
        }
        if (strParcial == null || strParcial.isEmpty()) {
            utilitario.agregarMensajeInfo("Debe selecccionar un parcial", "");
            return;
        }
        ///aumenta asignatura, parcial, quimestr
        generarReporteDestrezas();
        utilitario.ejecutarJavaScript("window.open('" + str_path_reporte + "');");

    }

    private void generarReporteDestrezas() {
        GenerarReporte genera = new GenerarReporte();
        Map parametros = new HashMap();
        CrearCurso cre_curso = servCreaCurso.getCrearCurso(objCursoSeleccionado + "");
        parametros.put("cur_codigo", cre_curso.getCurCodigo().getCurCodigo() + "");
        parametros.put("per_codigo", perActual.getPerCodigo());
        parametros.put("asi_codigo", ((Object[]) objAsignaturaSeleccionada)[4] + "");
        parametros.put("for_codigo", strForma);
        parametros.put("eva_codigo", strParcial);
        genera.generar(parametros, "/reportes/rep_parcial/rep_destrezas.jasper");
    }

    //Reporte Comportamieto
    private TablaGenerica getComportamientoQuimestre(String mat_codigo, String for_codigo) {
        return utilitario.consultar("select com_semana1,com_semana2,com_semana3,com_semana4,com_semana5,com_sumatoria,com_equi,com_equivalencia from  comportamientoparcial \n"
                + "where eva_codigo in(1,2,3)\n"
                + "and for_codigo=" + for_codigo + "\n"
                + "and mat_codigo=" + mat_codigo + " order by eva_codigo");
    }

    public void generarReporteComportamiento() {
        TablaGenerica tab_reporte = utilitario.consultar("SELECT  ''AS CUENTA,''AS NOMINA\n"
                + ",''AS P1S1,''AS P1S2,''AS P1S3,''AS P1S4,''AS P1S5,''AS P1SUMA,''AS P1LETRA,''AS P1EQUI\n"
                + ",''AS P2S1,''AS P2S2,''AS P2S3,''AS P2S4,''AS P2S5,''AS P2SUMA,''AS P2LETRA,''AS P2EQUI\n"
                + ",''AS P3S1,''AS P3S2,''AS P3S3,''AS P3S4,''AS P3S5,''AS P3SUMA,''AS P3LETRA,''AS P3EQUI\n"
                + ",''AS TPROM,''AS TLETRA,''AS TEQUI,''AS GRUPO");

        tab_reporte.limpiar();
        TablaGenerica tab_alumnos = getAlumnosCurso(((Object[]) objCursoSeleccionado)[0] + "");

        TablaGenerica tab_equi = utilitario.consultar("SELECT * FROM equivalencia_conducta");


        for (int i = 0; i < tab_alumnos.getTotalFilas(); i++) {
            ////////////////////////////////////
            //QUIMESTRE 2
            tab_reporte.insertar();
            tab_reporte.setValor("CUENTA", String.valueOf((tab_alumnos.getTotalFilas() - i)));
            tab_reporte.setValor("NOMINA", tab_alumnos.getValor(i, "Alumnos"));
            TablaGenerica tab_comportamiento = getComportamientoQuimestre(tab_alumnos.getValor(i, "mat_codigo"), "4");
            tab_reporte.setValor("GRUPO", "SEGUNDO QUIMESTRE");
            double dou_promedio = 0;
            if (tab_comportamiento.getTotalFilas() == 3) {
                tab_reporte.setValor("P1S1", tab_comportamiento.getValor(0, "com_semana1"));
                tab_reporte.setValor("P1S2", tab_comportamiento.getValor(0, "com_semana2"));
                tab_reporte.setValor("P1S3", tab_comportamiento.getValor(0, "com_semana3"));
                tab_reporte.setValor("P1S4", tab_comportamiento.getValor(0, "com_semana4"));
                tab_reporte.setValor("P1S5", tab_comportamiento.getValor(0, "com_semana5"));
                tab_reporte.setValor("P1SUMA", tab_comportamiento.getValor(0, "com_sumatoria"));
                tab_reporte.setValor("P1LETRA", tab_comportamiento.getValor(0, "com_equi"));
                tab_reporte.setValor("P1EQUI", tab_comportamiento.getValor(0, "com_equivalencia"));

                try {
                    dou_promedio += Double.parseDouble(tab_reporte.getValor("P1SUMA"));
                } catch (Exception e) {
                }

                tab_reporte.setValor("P2S1", tab_comportamiento.getValor(1, "com_semana1"));
                tab_reporte.setValor("P2S2", tab_comportamiento.getValor(1, "com_semana2"));
                tab_reporte.setValor("P2S3", tab_comportamiento.getValor(1, "com_semana3"));
                tab_reporte.setValor("P2S4", tab_comportamiento.getValor(1, "com_semana4"));
                tab_reporte.setValor("P2S5", tab_comportamiento.getValor(1, "com_semana5"));
                tab_reporte.setValor("P2SUMA", tab_comportamiento.getValor(1, "com_sumatoria"));
                tab_reporte.setValor("P2LETRA", tab_comportamiento.getValor(1, "com_equi"));
                tab_reporte.setValor("P2EQUI", tab_comportamiento.getValor(1, "com_equivalencia"));

                try {
                    dou_promedio += Double.parseDouble(tab_reporte.getValor("P2SUMA"));
                } catch (Exception e) {
                }

                tab_reporte.setValor("P3S1", tab_comportamiento.getValor(2, "com_semana1"));
                tab_reporte.setValor("P3S2", tab_comportamiento.getValor(2, "com_semana2"));
                tab_reporte.setValor("P3S3", tab_comportamiento.getValor(2, "com_semana3"));
                tab_reporte.setValor("P3S4", tab_comportamiento.getValor(2, "com_semana4"));
                tab_reporte.setValor("P3S5", tab_comportamiento.getValor(2, "com_semana5"));
                tab_reporte.setValor("P3SUMA", tab_comportamiento.getValor(2, "com_sumatoria"));
                tab_reporte.setValor("P3LETRA", tab_comportamiento.getValor(2, "com_equi"));
                tab_reporte.setValor("P3EQUI", tab_comportamiento.getValor(2, "com_equivalencia"));

                try {
                    dou_promedio += Double.parseDouble(tab_reporte.getValor("P3SUMA"));
                } catch (Exception e) {
                }

                //busco la equivalencia del promedio
                Object obj_resultado = null;
                String str_alterno = "";
                long l_prom = Math.round(dou_promedio / 3);
                for (int j = 0; j < tab_equi.getTotalFilas(); j++) {
                    String str_expresion = tab_equi.getValor(j, "eqc_escala");
                    str_expresion = str_expresion.replace("nota", l_prom + "");
                    obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
                    if (obj_resultado != null) {
                        str_alterno = tab_equi.getValor(j, "eqc_alterno");
                        break;
                    }
                }
                if (obj_resultado == null) {
                    obj_resultado = "NO EQV";
                }
                tab_reporte.setValor("TPROM", l_prom + "");
                tab_reporte.setValor("TLETRA", str_alterno);
                tab_reporte.setValor("TEQUI", obj_resultado.toString());
            } else if (tab_comportamiento.getTotalFilas() == 2) {
                tab_reporte.setValor("P1S1", tab_comportamiento.getValor(0, "com_semana1"));
                tab_reporte.setValor("P1S2", tab_comportamiento.getValor(0, "com_semana2"));
                tab_reporte.setValor("P1S3", tab_comportamiento.getValor(0, "com_semana3"));
                tab_reporte.setValor("P1S4", tab_comportamiento.getValor(0, "com_semana4"));
                tab_reporte.setValor("P1S5", tab_comportamiento.getValor(0, "com_semana5"));
                tab_reporte.setValor("P1SUMA", tab_comportamiento.getValor(0, "com_sumatoria"));
                tab_reporte.setValor("P1LETRA", tab_comportamiento.getValor(0, "com_equi"));
                tab_reporte.setValor("P1EQUI", tab_comportamiento.getValor(0, "com_equivalencia"));

                try {
                    dou_promedio += Double.parseDouble(tab_reporte.getValor("P1SUMA"));
                } catch (Exception e) {
                }

                tab_reporte.setValor("P2S1", tab_comportamiento.getValor(1, "com_semana1"));
                tab_reporte.setValor("P2S2", tab_comportamiento.getValor(1, "com_semana2"));
                tab_reporte.setValor("P2S3", tab_comportamiento.getValor(1, "com_semana3"));
                tab_reporte.setValor("P2S4", tab_comportamiento.getValor(1, "com_semana4"));
                tab_reporte.setValor("P2S5", tab_comportamiento.getValor(1, "com_semana5"));
                tab_reporte.setValor("P2SUMA", tab_comportamiento.getValor(1, "com_sumatoria"));
                tab_reporte.setValor("P2LETRA", tab_comportamiento.getValor(1, "com_equi"));
                tab_reporte.setValor("P2EQUI", tab_comportamiento.getValor(1, "com_equivalencia"));

                try {
                    dou_promedio += Double.parseDouble(tab_reporte.getValor("P2SUMA"));
                } catch (Exception e) {
                }



                //busco la equivalencia del promedio
                Object obj_resultado = null;
                String str_alterno = "";
                for (int j = 0; j < tab_equi.getTotalFilas(); j++) {
                    String str_expresion = tab_equi.getValor(j, "eqc_escala");
                    str_expresion = str_expresion.replace("nota", utilitario.getFormatoNumero(dou_promedio / 3));
                    obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
                    if (obj_resultado != null) {
                        str_alterno = tab_equi.getValor(j, "eqc_alterno");
                        break;
                    }
                }
                if (obj_resultado == null) {
                    obj_resultado = "NO EQV";
                }
                tab_reporte.setValor("TPROM", utilitario.getFormatoNumero(dou_promedio / 3));
                tab_reporte.setValor("TLETRA", str_alterno);
                tab_reporte.setValor("TEQUI", obj_resultado.toString());
            } else if (tab_comportamiento.getTotalFilas() == 1) {
                tab_reporte.setValor("P1S1", tab_comportamiento.getValor(0, "com_semana1"));
                tab_reporte.setValor("P1S2", tab_comportamiento.getValor(0, "com_semana2"));
                tab_reporte.setValor("P1S3", tab_comportamiento.getValor(0, "com_semana3"));
                tab_reporte.setValor("P1S4", tab_comportamiento.getValor(0, "com_semana4"));
                tab_reporte.setValor("P1S5", tab_comportamiento.getValor(0, "com_semana5"));
                tab_reporte.setValor("P1SUMA", tab_comportamiento.getValor(0, "com_sumatoria"));
                tab_reporte.setValor("P1LETRA", tab_comportamiento.getValor(0, "com_equi"));
                tab_reporte.setValor("P1EQUI", tab_comportamiento.getValor(0, "com_equivalencia"));

                try {
                    dou_promedio += Double.parseDouble(tab_reporte.getValor("P1SUMA"));
                } catch (Exception e) {
                }

                //busco la equivalencia del promedio
                Object obj_resultado = null;
                String str_alterno = "";
                long l_prom = Math.round(dou_promedio / 3);
                for (int j = 0; j < tab_equi.getTotalFilas(); j++) {
                    String str_expresion = tab_equi.getValor(j, "eqc_escala");
                    str_expresion = str_expresion.replace("nota", l_prom + "");
                    obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
                    if (obj_resultado != null) {
                        str_alterno = tab_equi.getValor(j, "eqc_alterno");
                        break;
                    }
                }
                if (obj_resultado == null) {
                    obj_resultado = "NO EQV";
                }
                tab_reporte.setValor("TPROM", l_prom + "");
                tab_reporte.setValor("TLETRA", str_alterno);
                tab_reporte.setValor("TEQUI", obj_resultado.toString());
            }
        }



        for (int i = 0; i < tab_alumnos.getTotalFilas(); i++) {
            tab_reporte.insertar();
            tab_reporte.setValor("CUENTA", String.valueOf((tab_alumnos.getTotalFilas() - i)));
            tab_reporte.setValor("NOMINA", tab_alumnos.getValor(i, "Alumnos"));
            TablaGenerica tab_comportamiento = getComportamientoQuimestre(tab_alumnos.getValor(i, "mat_codigo"), "1");
            tab_reporte.setValor("GRUPO", "PRIMER QUIMESTRE");
            double dou_promedio = 0;
            if (tab_comportamiento.getTotalFilas() == 3) {
                tab_reporte.setValor("P1S1", tab_comportamiento.getValor(0, "com_semana1"));
                tab_reporte.setValor("P1S2", tab_comportamiento.getValor(0, "com_semana2"));
                tab_reporte.setValor("P1S3", tab_comportamiento.getValor(0, "com_semana3"));
                tab_reporte.setValor("P1S4", tab_comportamiento.getValor(0, "com_semana4"));
                tab_reporte.setValor("P1S5", tab_comportamiento.getValor(0, "com_semana5"));
                tab_reporte.setValor("P1SUMA", tab_comportamiento.getValor(0, "com_sumatoria"));
                tab_reporte.setValor("P1LETRA", tab_comportamiento.getValor(0, "com_equi"));
                tab_reporte.setValor("P1EQUI", tab_comportamiento.getValor(0, "com_equivalencia"));

                try {
                    dou_promedio += Double.parseDouble(tab_reporte.getValor("P1SUMA"));
                } catch (Exception e) {
                }

                tab_reporte.setValor("P2S1", tab_comportamiento.getValor(1, "com_semana1"));
                tab_reporte.setValor("P2S2", tab_comportamiento.getValor(1, "com_semana2"));
                tab_reporte.setValor("P2S3", tab_comportamiento.getValor(1, "com_semana3"));
                tab_reporte.setValor("P2S4", tab_comportamiento.getValor(1, "com_semana4"));
                tab_reporte.setValor("P2S5", tab_comportamiento.getValor(1, "com_semana5"));
                tab_reporte.setValor("P2SUMA", tab_comportamiento.getValor(1, "com_sumatoria"));
                tab_reporte.setValor("P2LETRA", tab_comportamiento.getValor(1, "com_equi"));
                tab_reporte.setValor("P2EQUI", tab_comportamiento.getValor(1, "com_equivalencia"));

                try {
                    dou_promedio += Double.parseDouble(tab_reporte.getValor("P2SUMA"));
                } catch (Exception e) {
                }

                tab_reporte.setValor("P3S1", tab_comportamiento.getValor(2, "com_semana1"));
                tab_reporte.setValor("P3S2", tab_comportamiento.getValor(2, "com_semana2"));
                tab_reporte.setValor("P3S3", tab_comportamiento.getValor(2, "com_semana3"));
                tab_reporte.setValor("P3S4", tab_comportamiento.getValor(2, "com_semana4"));
                tab_reporte.setValor("P3S5", tab_comportamiento.getValor(2, "com_semana5"));
                tab_reporte.setValor("P3SUMA", tab_comportamiento.getValor(2, "com_sumatoria"));
                tab_reporte.setValor("P3LETRA", tab_comportamiento.getValor(2, "com_equi"));
                tab_reporte.setValor("P3EQUI", tab_comportamiento.getValor(2, "com_equivalencia"));

                try {
                    dou_promedio += Double.parseDouble(tab_reporte.getValor("P3SUMA"));
                } catch (Exception e) {
                }

                //busco la equivalencia del promedio
                Object obj_resultado = null;
                String str_alterno = "";
                long l_prom = Math.round(dou_promedio / 3);
                for (int j = 0; j < tab_equi.getTotalFilas(); j++) {
                    String str_expresion = tab_equi.getValor(j, "eqc_escala");
                    str_expresion = str_expresion.replace("nota", l_prom + "");
                    obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
                    if (obj_resultado != null) {
                        str_alterno = tab_equi.getValor(j, "eqc_alterno");
                        break;
                    }
                }
                if (obj_resultado == null) {
                    obj_resultado = "NO EQV";
                }
                tab_reporte.setValor("TPROM", l_prom + "");
                tab_reporte.setValor("TLETRA", str_alterno);
                tab_reporte.setValor("TEQUI", obj_resultado.toString());
            } else if (tab_comportamiento.getTotalFilas() == 2) {
                tab_reporte.setValor("P1S1", tab_comportamiento.getValor(0, "com_semana1"));
                tab_reporte.setValor("P1S2", tab_comportamiento.getValor(0, "com_semana2"));
                tab_reporte.setValor("P1S3", tab_comportamiento.getValor(0, "com_semana3"));
                tab_reporte.setValor("P1S4", tab_comportamiento.getValor(0, "com_semana4"));
                tab_reporte.setValor("P1S5", tab_comportamiento.getValor(0, "com_semana5"));
                tab_reporte.setValor("P1SUMA", tab_comportamiento.getValor(0, "com_sumatoria"));
                tab_reporte.setValor("P1LETRA", tab_comportamiento.getValor(0, "com_equi"));
                tab_reporte.setValor("P1EQUI", tab_comportamiento.getValor(0, "com_equivalencia"));

                try {
                    dou_promedio += Double.parseDouble(tab_reporte.getValor("P1SUMA"));
                } catch (Exception e) {
                }

                tab_reporte.setValor("P2S1", tab_comportamiento.getValor(1, "com_semana1"));
                tab_reporte.setValor("P2S2", tab_comportamiento.getValor(1, "com_semana2"));
                tab_reporte.setValor("P2S3", tab_comportamiento.getValor(1, "com_semana3"));
                tab_reporte.setValor("P2S4", tab_comportamiento.getValor(1, "com_semana4"));
                tab_reporte.setValor("P2S5", tab_comportamiento.getValor(1, "com_semana5"));
                tab_reporte.setValor("P2SUMA", tab_comportamiento.getValor(1, "com_sumatoria"));
                tab_reporte.setValor("P2LETRA", tab_comportamiento.getValor(1, "com_equi"));
                tab_reporte.setValor("P2EQUI", tab_comportamiento.getValor(1, "com_equivalencia"));

                try {
                    dou_promedio += Double.parseDouble(tab_reporte.getValor("P2SUMA"));
                } catch (Exception e) {
                }

                //busco la equivalencia del promedio
                Object obj_resultado = null;
                String str_alterno = "";
                long l_prom = Math.round(dou_promedio / 3);
                for (int j = 0; j < tab_equi.getTotalFilas(); j++) {
                    String str_expresion = tab_equi.getValor(j, "eqc_escala");
                    str_expresion = str_expresion.replace("nota", l_prom + "");
                    obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
                    if (obj_resultado != null) {
                        str_alterno = tab_equi.getValor(j, "eqc_alterno");
                        break;
                    }
                }
                if (obj_resultado == null) {
                    obj_resultado = "NO EQV";
                }
                tab_reporte.setValor("TPROM", l_prom + "");
                tab_reporte.setValor("TLETRA", str_alterno);
                tab_reporte.setValor("TEQUI", obj_resultado.toString());
            } else if (tab_comportamiento.getTotalFilas() == 1) {
                tab_reporte.setValor("P1S1", tab_comportamiento.getValor(0, "com_semana1"));
                tab_reporte.setValor("P1S2", tab_comportamiento.getValor(0, "com_semana2"));
                tab_reporte.setValor("P1S3", tab_comportamiento.getValor(0, "com_semana3"));
                tab_reporte.setValor("P1S4", tab_comportamiento.getValor(0, "com_semana4"));
                tab_reporte.setValor("P1S5", tab_comportamiento.getValor(0, "com_semana5"));
                tab_reporte.setValor("P1SUMA", tab_comportamiento.getValor(0, "com_sumatoria"));
                tab_reporte.setValor("P1LETRA", tab_comportamiento.getValor(0, "com_equi"));
                tab_reporte.setValor("P1EQUI", tab_comportamiento.getValor(0, "com_equivalencia"));

                try {
                    dou_promedio += Double.parseDouble(tab_reporte.getValor("P1SUMA"));
                } catch (Exception e) {
                }

                //busco la equivalencia del promedio
                Object obj_resultado = null;
                String str_alterno = "";
                long l_prom = Math.round(dou_promedio / 3);
                for (int j = 0; j < tab_equi.getTotalFilas(); j++) {
                    String str_expresion = tab_equi.getValor(j, "eqc_escala");
                    str_expresion = str_expresion.replace("nota", l_prom + "");
                    obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
                    if (obj_resultado != null) {
                        str_alterno = tab_equi.getValor(j, "eqc_alterno");
                        break;
                    }
                }
                if (obj_resultado == null) {
                    obj_resultado = "NO EQV";
                }
                tab_reporte.setValor("TPROM", l_prom + "");
                tab_reporte.setValor("TLETRA", str_alterno);
                tab_reporte.setValor("TEQUI", obj_resultado.toString());
            }

        }

        GenerarReporte genera = new GenerarReporte();
        genera.setDataSource(new ReporteDataSource(tab_reporte));
        Map parametros = new HashMap();
        if (objCursoSeleccionado != null) {
            parametros.put("CURSO", ((Object[]) objCursoSeleccionado)[1] + "");
            parametros.put("PARALELO", ((Object[]) objCursoSeleccionado)[2] + "");
        }
        parametros.put("PROFESOR", docDocente.getDocNombres());
        parametros.put("PERIODO", perActual.getPerNombre());
        genera.generar(parametros, "/reportes/rep_parcial/rep_comportamieto.jasper");

    }

    public void verReporteParcialDocente() {
    }

    public void generarreporteParcial(String istrForma, String istrParcial, String imatCodigo) {
        Map parametros = new HashMap();
        //solo materias academicas
        TablaGenerica tab_reporte = utilitario.consultar("select i.per_nombre,e.alu_apellidos || ' ' || alu_nombres as nombres, g.cur_anio,h.par_nombre,c.asi_nombre,j.tip_nombre,\n"
                + "a.not_primerparcial,a.not_eqvdestreza,k.for_nombre,l.eva_nombre,m.doc_nombres, c.tip_codigo\n"
                + "from nota_destrezaparcial a\n"
                + "inner join distributivomxc  b on a.dis_codigo=b.dis_codigo\n"
                + "inner join asignaturas c on b.asi_codigo=c.asi_codigo\n"
                + "INNER JOIN matricula d on a.mat_codigo=d.mat_codigo\n"
                + "inner join alumnos e on d.alu_codigo=e.alu_codigo\n"
                + "INNER JOIN crear_curso f on b.cre_codigo=f.cre_codigo\n"
                + "inner join cursos g on f.cur_codigo=g.cur_codigo\n"
                + "INNER JOIN paralelo h on f.par_codigo=h.par_codigo\n"
                + "inner join periodo_lectivo i on f.per_codigo=i.per_codigo\n"
                + "INNER JOIN tipoasignaturas j on c.tip_codigo=j.tip_codigo\n"
                + "inner join formaevaluar k on k.for_codigo=a.for_codigo\n"
                + "inner join  evaluarparcial l on l.eva_codigo=a.eva_codigo\n"
                + "INNER JOIN docentes m on f.doc_codigo=m.doc_codigo\n"
                + "where a.eva_codigo=" + istrParcial + " and a.for_codigo=" + istrForma + "\n"
                + "and c.tip_codigo=1 and a.mat_codigo =" + imatCodigo + "\n"
                + "order by c.tip_codigo,c.asi_nombre");


        TablaGenerica tab_optativas = utilitario.consultar("select i.per_nombre,e.alu_apellidos || ' ' || alu_nombres as nombres, g.cur_anio,h.par_nombre,c.asi_nombre,j.tip_nombre,\n"
                + "a.not_primerparcial,a.not_eqvdestreza,k.for_nombre,l.eva_nombre,m.doc_nombres, c.tip_codigo\n"
                + "from nota_destrezaparcial a\n"
                + "inner join distributivomxc  b on a.dis_codigo=b.dis_codigo\n"
                + "inner join asignaturas c on b.asi_codigo=c.asi_codigo\n"
                + "INNER JOIN matricula d on a.mat_codigo=d.mat_codigo\n"
                + "inner join alumnos e on d.alu_codigo=e.alu_codigo\n"
                + "INNER JOIN crear_curso f on b.cre_codigo=f.cre_codigo\n"
                + "inner join cursos g on f.cur_codigo=g.cur_codigo\n"
                + "INNER JOIN paralelo h on f.par_codigo=h.par_codigo\n"
                + "inner join periodo_lectivo i on f.per_codigo=i.per_codigo\n"
                + "INNER JOIN tipoasignaturas j on c.tip_codigo=j.tip_codigo\n"
                + "inner join formaevaluar k on k.for_codigo=a.for_codigo\n"
                + "inner join  evaluarparcial l on l.eva_codigo=a.eva_codigo\n"
                + "INNER JOIN docentes m on f.doc_codigo=m.doc_codigo\n"
                + "where a.eva_codigo=" + istrParcial + " and a.for_codigo=" + istrForma + "\n"
                + "and c.tip_codigo=2 and a.mat_codigo =" + imatCodigo + "\n"
                + "order by c.tip_codigo,c.asi_nombre");

        //Creo una linea para materias Optativas
        if (tab_optativas.isEmpty() == false) {
            //inserto 1 fila 
            tab_reporte.insertar();
            if (tab_reporte.isEmpty() == false) {
                for (int i = 0; i < tab_reporte.getTotalColumnas(); i++) {
                    tab_reporte.setValor(tab_reporte.getColumnas()[i].getNombre(), tab_reporte.getValor(1, tab_reporte.getColumnas()[i].getNombre()));
                }
            }
            tab_reporte.setValor("asi_nombre", "OPTATIVA");
            double dou_prom_opt = tab_optativas.getPromedioColumna("not_primerparcial");
            tab_reporte.setValor("not_primerparcial", utilitario.getFormatoNumero(dou_prom_opt));

            //consulto la equivalencia

            TablaGenerica tab_equi = utilitario.consultar("SELECT * FROM equivalencia_aprovechamiento");
            Object obj_resultado = null;
            for (int i = 0; i < tab_equi.getTotalFilas(); i++) {
                String str_expresion = tab_equi.getValor(i, "eqa_escalacuantiva");
                str_expresion = str_expresion.replace("nota", dou_prom_opt + "");
                obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
                if (obj_resultado != null) {
                    break;
                }
            }
            if (obj_resultado == null) {
                obj_resultado = "NO EQV";
            }
            tab_reporte.setValor("not_eqvdestreza", obj_resultado + "");

            //PONGO AL FINAL LA NUEVA FILA
            Fila fil_optativa = tab_reporte.getFila(0);
            tab_reporte.getFilas().remove(0);
            tab_reporte.getFilas().add(fil_optativa);

            //Calcula el promedio y equivalencia del promedio
            double dou_prom = tab_reporte.getPromedioColumna("not_primerparcial");
            parametros.put("RENDIMIENTO", utilitario.getFormatoNumero(dou_prom));
            obj_resultado = null;
            for (int i = 0; i < tab_equi.getTotalFilas(); i++) {
                String str_expresion = tab_equi.getValor(i, "eqa_escalacuantiva");
                str_expresion = str_expresion.replace("nota", dou_prom + "");
                obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
                if (obj_resultado != null) {
                    break;
                }
            }
            if (obj_resultado == null) {
                obj_resultado = "NO EQV";
            }
            parametros.put("EQVRENDIMIENTO", obj_resultado + "");


            //agrego las filas de materias optativas            
            for (int i = 0; i < tab_optativas.getTotalFilas(); i++) {
                tab_reporte.getFilas().add(tab_optativas.getFila(i));
            }

        }

        GenerarReporte genera = new GenerarReporte();

        if (objCursoSeleccionado != null) {
            try {
                parametros.put("matricula", Integer.parseInt(((Object[]) objCursoSeleccionado)[5] + ""));
            } catch (Exception e) {
            }
        }
        try {
            parametros.put("quimestre", Integer.parseInt(strForma));
        } catch (Exception e) {
        }
        try {
            parametros.put("parcial", Integer.parseInt(strParcial));
        } catch (Exception e) {
        }
        genera.setDataSource(new ReporteDataSource(tab_reporte));
        parametros.put("REPORT_CONNECTION", utilitario.getConexion().getConnection());        
        genera.generar(parametros, "/reportes/rep_parcial/rep_parcialD.jasper");
    }

    public void generarReporteQuimestre(String istrForma, String imatCodigo) {
        Map parametros = new HashMap();
        //solo materias academicas
        String istrParcial = "1";
        TablaGenerica tab_reporte = utilitario.consultar("select i.per_nombre,e.alu_apellidos || ' ' || alu_nombres as nombres, g.cur_anio,h.par_nombre,c.asi_nombre,j.tip_nombre,\n"
                + "a.not_primerparcial,a.not_eqvdestreza,k.for_nombre,l.eva_nombre,m.doc_nombres, c.tip_codigo,a.dis_codigo,'' as p2, '' as p3,'' as eqv80,'' as exa20,'' as nota,'' as equiv,a.mat_codigo \n"
                + "from nota_destrezaparcial a\n"
                + "inner join distributivomxc  b on a.dis_codigo=b.dis_codigo\n"
                + "inner join asignaturas c on b.asi_codigo=c.asi_codigo\n"
                + "INNER JOIN matricula d on a.mat_codigo=d.mat_codigo\n"
                + "inner join alumnos e on d.alu_codigo=e.alu_codigo\n"
                + "INNER JOIN crear_curso f on b.cre_codigo=f.cre_codigo\n"
                + "inner join cursos g on f.cur_codigo=g.cur_codigo\n"
                + "INNER JOIN paralelo h on f.par_codigo=h.par_codigo\n"
                + "inner join periodo_lectivo i on f.per_codigo=i.per_codigo\n"
                + "INNER JOIN tipoasignaturas j on c.tip_codigo=j.tip_codigo\n"
                + "inner join formaevaluar k on k.for_codigo=a.for_codigo\n"
                + "inner join  evaluarparcial l on l.eva_codigo=a.eva_codigo\n"
                + "INNER JOIN docentes m on f.doc_codigo=m.doc_codigo\n"
                + "where a.eva_codigo=" + istrParcial + " and a.for_codigo=" + istrForma + "\n"
                + "and c.tip_codigo=1 and a.mat_codigo =" + imatCodigo + "\n"
                + "order by c.tip_codigo,c.asi_nombre");

        //llena los campos de p2 y p3  de materias 
        for (int i = 0; i < tab_reporte.getTotalFilas(); i++) {
            TablaGenerica tab_resto = utilitario.consultar("select eva_codigo ,not_primerparcial from nota_destrezaparcial \n"
                    + "where for_codigo=" + istrForma + " \n"
                    + "and mat_codigo=" + tab_reporte.getValor(i, "mat_codigo") + " and dis_codigo=" + tab_reporte.getValor(i, "dis_codigo") + "\n"
                    + "and eva_codigo>=2\n"
                    + "order by eva_codigo ");
            if (tab_resto.isEmpty() == false) {
                for (int j = 0; j < tab_resto.getTotalFilas(); j++) {
                    if (tab_resto.getValor(j, "eva_codigo") != null) {
                        if (tab_resto.getValor(j, "eva_codigo").equals("2")) {//segundo parcial
                            tab_reporte.setValor(i, "p2", tab_resto.getValor(j, "not_primerparcial"));
                        } else if (tab_resto.getValor(j, "eva_codigo").equals("3")) {//tercer parcial
                            tab_reporte.setValor(i, "p3", tab_resto.getValor(j, "not_primerparcial"));
                        }
                    }
                }
            }

            //llena eqv80,exa20,nota y equiv
            TablaGenerica tab_nq = utilitario.consultar("select inf_eqv80,inf_exa20,inf_nota,inf_eqvquimestre from informe_quimestre \n"
                    + "where for_codigo=" + istrForma + " and mat_codigo=" + tab_reporte.getValor(i, "mat_codigo") + " and dis_codigo=" + tab_reporte.getValor(i, "dis_codigo") + "");
            if (tab_nq.isEmpty() == false) {
                tab_reporte.setValor(i, "eqv80", tab_nq.getValor("inf_eqv80"));
                tab_reporte.setValor(i, "exa20", tab_nq.getValor("inf_exa20"));
                tab_reporte.setValor(i, "nota", tab_nq.getValor("inf_nota"));
                tab_reporte.setValor(i, "equiv", tab_nq.getValor("inf_eqvquimestre"));
            }
        }


        TablaGenerica tab_optativas = utilitario.consultar("select i.per_nombre,e.alu_apellidos || ' ' || alu_nombres as nombres, g.cur_anio,h.par_nombre,c.asi_nombre,j.tip_nombre,\n"
                + "a.not_primerparcial,a.not_eqvdestreza,k.for_nombre,l.eva_nombre,m.doc_nombres, c.tip_codigo,a.dis_codigo,'' as p2, '' as p3,'' as eqv80,'' as exa20,'' as nota,'' as equiv,a.mat_codigo \n"
                + "from nota_destrezaparcial a\n"
                + "inner join distributivomxc  b on a.dis_codigo=b.dis_codigo\n"
                + "inner join asignaturas c on b.asi_codigo=c.asi_codigo\n"
                + "INNER JOIN matricula d on a.mat_codigo=d.mat_codigo\n"
                + "inner join alumnos e on d.alu_codigo=e.alu_codigo\n"
                + "INNER JOIN crear_curso f on b.cre_codigo=f.cre_codigo\n"
                + "inner join cursos g on f.cur_codigo=g.cur_codigo\n"
                + "INNER JOIN paralelo h on f.par_codigo=h.par_codigo\n"
                + "inner join periodo_lectivo i on f.per_codigo=i.per_codigo\n"
                + "INNER JOIN tipoasignaturas j on c.tip_codigo=j.tip_codigo\n"
                + "inner join formaevaluar k on k.for_codigo=a.for_codigo\n"
                + "inner join  evaluarparcial l on l.eva_codigo=a.eva_codigo\n"
                + "INNER JOIN docentes m on f.doc_codigo=m.doc_codigo\n"
                + "where a.eva_codigo=" + istrParcial + " and a.for_codigo=" + istrForma + "\n"
                + "and c.tip_codigo=2 and a.mat_codigo =" + imatCodigo + "\n"
                + "order by c.tip_codigo,c.asi_nombre");


        //llena los campos de p2 y p3  de materias OPTATIVAS
        for (int i = 0; i < tab_optativas.getTotalFilas(); i++) {
            TablaGenerica tab_resto = utilitario.consultar("select eva_codigo ,not_primerparcial from nota_destrezaparcial \n"
                    + "where for_codigo=" + istrForma + " \n"
                    + "and mat_codigo=" + tab_optativas.getValor(i, "mat_codigo") + " and dis_codigo=" + tab_optativas.getValor(i, "dis_codigo") + "\n"
                    + "and eva_codigo>=2\n"
                    + "order by eva_codigo ");
            if (tab_resto.isEmpty() == false) {
                for (int j = 0; j < tab_resto.getTotalFilas(); j++) {
                    if (tab_resto.getValor(j, "eva_codigo") != null) {
                        if (tab_resto.getValor(j, "eva_codigo").equals("2")) {//segundo parcial
                            tab_optativas.setValor(i, "p2", tab_resto.getValor(j, "not_primerparcial"));
                        } else if (tab_resto.getValor(j, "eva_codigo").equals("3")) {//tercer parcial
                            tab_optativas.setValor(i, "p3", tab_resto.getValor(j, "not_primerparcial"));
                        }
                    }
                }
            }

            //llena eqv80,exa20,nota y equiv
            TablaGenerica tab_nq = utilitario.consultar("select inf_eqv80,inf_exa20,inf_nota,inf_eqvquimestre from informe_quimestre \n"
                    + "where for_codigo=" + istrForma + " and mat_codigo=" + tab_optativas.getValor(i, "mat_codigo") + " and dis_codigo=" + tab_optativas.getValor(i, "dis_codigo") + "");
            if (tab_nq.isEmpty() == false) {
                tab_optativas.setValor(i, "eqv80", tab_nq.getValor("inf_eqv80"));
                tab_optativas.setValor(i, "exa20", tab_nq.getValor("inf_exa20"));
                tab_optativas.setValor(i, "nota", tab_nq.getValor("inf_nota"));
                tab_optativas.setValor(i, "equiv", tab_nq.getValor("inf_eqvquimestre"));
            }
        }






        //Creo una linea para materias Optativas
        if (tab_optativas.isEmpty() == false) {
            //inserto 1 fila 
            tab_reporte.insertar();
            if (tab_reporte.isEmpty() == false) {
                for (int i = 0; i < tab_reporte.getTotalColumnas(); i++) {
                    tab_reporte.setValor(tab_reporte.getColumnas()[i].getNombre(), tab_reporte.getValor(1, tab_reporte.getColumnas()[i].getNombre()));
                }
            }
            tab_reporte.setValor("asi_nombre", "OPTATIVA");

            double dou_prom_opt = tab_optativas.getPromedioColumna("not_primerparcial");
            tab_reporte.setValor("not_primerparcial", utilitario.getFormatoNumero(dou_prom_opt));

            dou_prom_opt = tab_optativas.getPromedioColumna("p2");
            tab_reporte.setValor("p2", utilitario.getFormatoNumero(dou_prom_opt));

            dou_prom_opt = tab_optativas.getPromedioColumna("p3");
            tab_reporte.setValor("p3", utilitario.getFormatoNumero(dou_prom_opt));

            dou_prom_opt = tab_optativas.getPromedioColumna("eqv80");
            tab_reporte.setValor("eqv80", utilitario.getFormatoNumero(dou_prom_opt));

            dou_prom_opt = tab_optativas.getPromedioColumna("exa20");
            tab_reporte.setValor("exa20", utilitario.getFormatoNumero(dou_prom_opt));

            dou_prom_opt = tab_optativas.getPromedioColumna("nota");
            tab_reporte.setValor("nota", utilitario.getFormatoNumero(dou_prom_opt));

            //consulto la equivalencia

            TablaGenerica tab_equi = utilitario.consultar("SELECT * FROM equivalencia_aprovechamiento");
            Object obj_resultado = null;
            for (int i = 0; i < tab_equi.getTotalFilas(); i++) {
                String str_expresion = tab_equi.getValor(i, "eqa_escalacuantiva");
                str_expresion = str_expresion.replace("nota", dou_prom_opt + "");
                obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
                if (obj_resultado != null) {
                    break;
                }
            }
            if (obj_resultado == null) {
                obj_resultado = "NO EQV";
            }
            tab_reporte.setValor("equiv", obj_resultado + "");

            //PONGO AL FINAL LA NUEVA FILA
            Fila fil_optativa = tab_reporte.getFila(0);
            tab_reporte.getFilas().remove(0);
            tab_reporte.getFilas().add(fil_optativa);

            //Calcula el promedio y equivalencia del promedio
            double dou_prom = tab_reporte.getPromedioColumna("nota");
            parametros.put("RENDIMIENTO", utilitario.getFormatoNumero(dou_prom));
            obj_resultado = null;
            for (int i = 0; i < tab_equi.getTotalFilas(); i++) {
                String str_expresion = tab_equi.getValor(i, "eqa_escalacuantiva");
                str_expresion = str_expresion.replace("nota", dou_prom + "");
                obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
                if (obj_resultado != null) {
                    break;
                }
            }
            if (obj_resultado == null) {
                obj_resultado = "NO EQV";
            }
            parametros.put("EQVRENDIMIENTO", obj_resultado + "");


            //agrego las filas de materias optativas            
            for (int i = 0; i < tab_optativas.getTotalFilas(); i++) {
                tab_reporte.getFilas().add(tab_optativas.getFila(i));
            }

        }


        //CALCULA DISCIPLINA DEL QUIMESTRE
        TablaGenerica tab_dis = utilitario.consultar("SELECT mat_codigo,round(avg(com_sumatoria),2) as prom FROM comportamientoparcial\n"
                + "where  for_codigo=" + istrForma + "\n"
                + "and  mat_codigo=" + imatCodigo + "\n"
                + "group by mat_codigo");
        if (tab_dis.isEmpty() == false) {
            //DISCIPLINA
            double dou_prom = 0;
            try {
                dou_prom = Double.parseDouble(tab_dis.getValor("prom"));
            } catch (Exception e) {
            }

            TablaGenerica tab_equi = utilitario.consultar("SELECT * FROM equivalencia_conducta");
            Object obj_resultado = null;
            String str_eqv = "";
            for (int i = 0; i < tab_equi.getTotalFilas(); i++) {
                String str_expresion = tab_equi.getValor(i, "eqc_escala");
                str_expresion = str_expresion.replace("nota", dou_prom + "");
                obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
                if (obj_resultado != null) {
                    str_eqv = tab_equi.getValor(i, "eqc_alterno");
                    break;
                }
            }
            if (obj_resultado == null) {
                obj_resultado = "NO EQV";
            }
            parametros.put("EQVDISCIPLINA", obj_resultado);
            parametros.put("DISCIPLINA", str_eqv);

        }
        GenerarReporte genera = new GenerarReporte();

        if (objCursoSeleccionado != null) {
            try {
                parametros.put("matricula", Integer.parseInt(((Object[]) objCursoSeleccionado)[5] + ""));
            } catch (Exception e) {
            }
        }
        try {
            parametros.put("quimestre", Integer.parseInt(strForma));
        } catch (Exception e) {
        }
        try {
            parametros.put("parcial", Integer.parseInt(strParcial));
        } catch (Exception e) {
        }
        genera.setDataSource(new ReporteDataSource(tab_reporte));
        parametros.put("REPORT_CONNECTION", utilitario.getConexion().getConnection());
        genera.generar(parametros, "/reportes/rep_parcial/rep_parcialQ.jasper");
    }

    public void verReporteQuimestre() {

        if (strForma == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Quimestre", "");
            return;
        }
        if (strForma.equals("-1")) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Quimestre", "");
            return;
        }


        if (objCursoSeleccionado != null) {
            generarReporteQuimestre(strForma, ((Object[]) objCursoSeleccionado)[5] + "");
            utilitario.ejecutarJavaScript("window.open('" + str_path_reporte + "');");
        } else {
            utilitario.agregarMensajeInfo("Debe selecccionar un curso", "");
        }
    }

    public void verReporteParcial() {

        if (strForma == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Quimestre", "");
            return;
        }
        if (strForma.equals("-1")) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Quimestre", "");
            return;
        }

        if (strParcial == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Parcial", "");
            return;
        }
        if (strParcial.equals("-1")) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Parcial", "");
            return;
        }

        if (objCursoSeleccionado != null) {
            generarreporteParcial(strForma, strParcial, ((Object[]) objCursoSeleccionado)[5] + "");
            utilitario.ejecutarJavaScript("window.open('" + str_path_reporte + "');");
        } else {
            utilitario.agregarMensajeInfo("Debe selecccionar un curso", "");
        }
    }

    public void verReporteCertificado() {
        if (objCursoSeleccionado != null) {
            //Genera el archivo de todos los estudiantes del periodo matriculado
            TablaGenerica tab_alumnos = utilitario.consultar("SELECT mat_codigo,d.cur_codigo,d.curso,b.estudiantes,b.alu_codigo,par_nombre,c.cre_codigo FROM matricula a\n"
                    + "INNER JOIN (\n"
                    + "SELECT alu_codigo, alu_apellidos || ' ' || alu_nombres as estudiantes  FROM alumnos\n"
                    + ")b on b.alu_codigo=a.alu_codigo\n"
                    + "INNER JOIN (\n"
                    + "SELECT cre_codigo,cur_codigo,par_codigo FROM crear_curso\n"
                    + ")c on c.cre_codigo=a.cre_codigo\n"
                    + "LEFT JOIN (\n"
                    + "SELECT cur_codigo,cur_anio as curso FROM cursos\n"
                    + ")d on c.cur_codigo=d.cur_codigo\n"
                    + "LEFT JOIN paralelo e on c.par_codigo=e.par_codigo\n"
                    + "where c.cre_codigo=" + objCursoSeleccionado + "\n"
                    + "ORDER BY d.cur_codigo,b.estudiantes");

            if (tab_alumnos.isEmpty()) {
                utilitario.agregarMensajeInfo("El curso seleccionado no tiene alumnos inscritos", "");
                return;
            }

            try {
                ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

                String path = extContext.getRealPath("/");
                File fil_archivo = new File(path + "\\reportes\\reporte" + utilitario.getVariable("ide_usua") + ".pdf");

                int BUFFER_SIZE = 1024;
                // objetos en memoria
                FileInputStream fis = null;
                ZipOutputStream zipos = null;

                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                HttpServletResponse resp = (HttpServletResponse) ec.getResponse();
                CrearCurso cre_curso = servCreaCurso.getCrearCurso(objCursoSeleccionado + "");
                String nombrearchivo = cre_curso.getCurCodigo().getCurAnio() + " " + cre_curso.getParCodigo().getParNombre() + ".zip";
                resp.addHeader("Content-Disposition", "attachment; filename=\"" + nombrearchivo + "\"");

                // buffer
                byte[] buffer = new byte[BUFFER_SIZE];
                try {
                    // fichero comprimido
                    zipos = new ZipOutputStream(resp.getOutputStream());

                    // fichero a comprimir
                    for (int i = 0; i < tab_alumnos.getTotalFilas(); i++) {
                        generarCertificado(tab_alumnos.getValor(i, "mat_codigo"));

                        fis = new FileInputStream(fil_archivo);
                        ZipEntry zipEntry = new ZipEntry(tab_alumnos.getValor(i, "estudiantes") + ".pdf");
                        zipos.putNextEntry(zipEntry);
                        int len = 0;
                        // zippear
                        while ((len = fis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                            zipos.write(buffer, 0, len);
                        }
                    }
                    // volcar la memoria al disco
                    zipos.flush();
                } catch (Exception e) {
                    throw e;
                } finally {
                    // cerramos los files
                    zipos.close();
                    fis.close();
                    fc.getApplication().getStateManager().saveView(fc);
                    fc.responseComplete();
                }



            } catch (Exception e) {
            }


        } else {
            utilitario.agregarMensajeInfo("Debe selecccionar un Curso", "");
        }

    }

    private void generarCertificado(String mat_codigo) {
        TablaGenerica tab_certificado = utilitario.consultar("SELECT e.alu_apellidos||' '||e.alu_nombres as nombres,round(avg(inf_nota),2) as promedio,c.asi_nombre,\n"
                + "g.cur_anio,per_nombre,ins_director,ins_secretaria,c.tip_codigo,'' as letras\n"
                + "FROM informe_quimestre a\n"
                + "inner join distributivomxc b on a.dis_codigo=b.dis_codigo\n"
                + "inner join asignaturas c on b.asi_codigo=c.asi_codigo\n"
                + "inner join matricula d on a.mat_codigo=d.mat_codigo\n"
                + "inner join alumnos e on d.alu_codigo=e.alu_codigo\n"
                + "inner join crear_curso f on d.cre_codigo=f.cre_codigo\n"
                + "inner join periodo_lectivo p on f.per_codigo=p.per_codigo\n"
                + "inner join cursos g on f.cur_codigo=g.cur_codigo\n"
                + "inner join institucion i on e.ins_codigo=i.ins_codigo\n"
                + "where a.mat_codigo=" + mat_codigo + "\n"
                + "and c.tip_codigo=1\n"
                + "group by e.alu_apellidos||' '||e.alu_nombres,c.asi_nombre,g.cur_anio,per_nombre,ins_director,ins_secretaria,c.tip_codigo\n"
                + "order by c.tip_codigo,c.asi_nombre desc");

        if (tab_certificado.isEmpty() == false) {
            //Calcula la nota en letras
            for (int i = 0; i < tab_certificado.getTotalFilas(); i++) {
                if (tab_certificado.getValor(i, "promedio") != null) {
                    tab_certificado.setValor(i, "letras", utilitario.getLetrasNumero(tab_certificado.getValor(i, "promedio").toUpperCase()));
                }
            }
            //inserta una fila para el promedio de materias optativas
            tab_certificado.insertar();
            for (int i = 0; i < tab_certificado.getTotalColumnas(); i++) {
                tab_certificado.setValor(tab_certificado.getColumnas()[i].getNombre(), tab_certificado.getValor(1, tab_certificado.getColumnas()[i].getNombre()));
            }
            tab_certificado.setValor("asi_nombre", "OPTATIVA");
            //calcula el promedio de las materias optativas
            TablaGenerica tab_prom_opta = utilitario.consultar("SELECT  a.mat_codigo,round(avg(inf_nota),2) as promedio,count(*) as num\n"
                    + "FROM informe_quimestre a\n"
                    + "inner join distributivomxc b on a.dis_codigo=b.dis_codigo\n"
                    + "inner join asignaturas c on b.asi_codigo=c.asi_codigo\n"
                    + "where a.mat_codigo=" + mat_codigo + "\n"
                    + "and c.tip_codigo=2\n"
                    + "GROUP BY a.mat_codigo");

            if (tab_prom_opta.isEmpty() == false) {
                tab_certificado.setValor("promedio", tab_prom_opta.getValor("promedio"));
            } else {
                tab_certificado.setValor("promedio", "0");
            }
            //letras de promedio de materias optativas
            tab_certificado.setValor("letras", utilitario.getLetrasNumero(tab_certificado.getValor("promedio").toUpperCase()));

            double dou_prom_general = tab_certificado.getPromedioColumna("promedio");
            String str_prom_general = utilitario.getFormatoNumero(dou_prom_general);
            String str_letras_prom_general = utilitario.getLetrasNumero(dou_prom_general).toUpperCase();

            //paso al final la materia optativa
            Fila fil_optativa = tab_certificado.getFila(0);
            tab_certificado.getFilas().remove(0);
            tab_certificado.getFilas().add(fil_optativa);


            GenerarReporte genera = new GenerarReporte();
            genera.setDataSource(new ReporteDataSource(tab_certificado));
            Map parametros = new HashMap();
            parametros.put("promGeneral", str_prom_general);
            parametros.put("letraspromGeneral", str_letras_prom_general);


            ///Calcular la equivalencia
            //////////

            TablaGenerica tab_equi = utilitario.consultar("SELECT * FROM equivalencia_aprovechamiento");
            Object obj_resultado = null;
            for (int i = 0; i < tab_equi.getTotalFilas(); i++) {
                String str_expresion = tab_equi.getValor(i, "eqa_escalacuantiva");
                str_expresion = str_expresion.replace("nota", str_prom_general + "");
                obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
                if (obj_resultado != null) {
                    break;
                }
            }
            if (obj_resultado == null) {
                obj_resultado = "NO EQV";
            }
            parametros.put("equivalencia", obj_resultado + "");

            //Calcula el comportamiento final

            TablaGenerica tab_comporta = utilitario.consultar("SELECT avg(com_sumatoria)as com_final,mat_codigo FROM comportamientoparcial\n"
                    + "where   mat_codigo=" + mat_codigo + "\n"
                    + "group by mat_codigo");
            if (tab_comporta.isEmpty() == false) {
                ///BUSCA LA EQUIVALENCIA
                tab_equi = utilitario.consultar("SELECT * FROM equivalencia_conducta");
                obj_resultado = null;
                for (int i = 0; i < tab_equi.getTotalFilas(); i++) {
                    String str_expresion = tab_equi.getValor(i, "eqc_escala");
                    str_expresion = str_expresion.replace("nota", tab_comporta.getValor("com_final") + "");
                    obj_resultado = utilitario.evaluarExpresionJavaScript(str_expresion);
                    if (obj_resultado != null) {
                        obj_resultado = tab_equi.getValor(i, "eqc_alterno");
                        break;
                    }
                }
            }
            if (obj_resultado == null) {
                obj_resultado = "NO EQV";
            }
            parametros.put("comporta", obj_resultado + ""); ///Calcular la equivalencia
            genera.generar(parametros, "/reportes/rep_parcial/rep_promocion.jasper");

        }
    }

    public void verReporteComportamiento() {
        if (objCursoSeleccionado != null) {
            generarReporteComportamiento();
            utilitario.ejecutarJavaScript("window.open('" + str_path_reporte + "');");
        } else {
            utilitario.agregarMensajeInfo("Debe selecccionar un curso", "");
        }
    }

    public void preProcessPDF(Object document) {
        Document pdf = (Document) document;
        pdf.setPageSize(PageSize.A4.rotate());
        try {
            pdf.open();

            pdf.setPageSize(PageSize.A4.rotate());
            pdf.add(new Paragraph("ALUMNO :" + utilitario.getVariable("NOMBRE")));
            pdf.add(new Paragraph(""));
            Paragraph paragraph = new Paragraph();
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add("NOTAS DEL " + ((Object[]) objCursoSeleccionado)[1] + "");
            pdf.add(new Paragraph(""));
            pdf.add(paragraph);

            // pdf.close();
        } catch (Exception e) {
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

    public boolean isBooMuestra() {
        return booMuestra;
    }

    public void setBooMuestra(boolean booMuestra) {
        this.booMuestra = booMuestra;
    }

    public List getLisDisciplinaParcial() {
        return lisDisciplinaParcial;
    }

    public void setLisDisciplinaParcial(List lisDisciplinaParcial) {
        this.lisDisciplinaParcial = lisDisciplinaParcial;
    }

    public List getLisAsistenciaParcial() {
        return lisAsistenciaParcial;
    }

    public void setLisAsistenciaParcial(List lisAsistenciaParcial) {
        this.lisAsistenciaParcial = lisAsistenciaParcial;
    }

    public int getNumDias() {
        return numDias;
    }

    public void setNumDias(int numDias) {
        this.numDias = numDias;
    }

    public List getLisInformeQuimestre() {
        return lisInformeQuimestre;
    }

    public void setLisInformeQuimestre(List lisInformeQuimestre) {
        this.lisInformeQuimestre = lisInformeQuimestre;
    }

    public String getStr_path_reporte() {
        return str_path_reporte;
    }

    public void setStr_path_reporte(String str_path_reporte) {
        this.str_path_reporte = str_path_reporte;
    }

    public List getLisConsulta() {
        return lisConsulta;
    }

    public void setLisConsulta(List lisConsulta) {
        this.lisConsulta = lisConsulta;
    }
}
