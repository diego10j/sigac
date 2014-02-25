/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import aplicacion.Utilitario;
import entidades.Docentes;
import entidades.NotaDestrezaparcial;
import entidades.PeriodoLectivo;
import framework.aplicacion.TablaGenerica;
import framework.reportes.GenerarReporte;
import framework.reportes.ReporteDataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
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



        if (utilitario.getURLCompleto().endsWith("PasarParcial.jsf") || utilitario.getURLCompleto().endsWith("InformeQuimestre.jsf")) {
            //cursos y materias
            lisCursos = servParcial.getCursosDocente(perActual.getPerCodigo().toString(), docDocente.getDocCodigo().toString());

        } else {
            lisCursos = servParcial.getCursosDisciplinaDocente(perActual.getPerCodigo().toString(), docDocente.getDocCodigo().toString());
        }

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
        str_path_reporte = utilitario.getURL() + "/reportes/reporte" + utilitario.getVariable("ide_usua") + ".pdf";
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
                lisNotasParcial = servParcial.getNotasParcialDistributivo(((Object[]) objCursoSeleccionado)[0] + "", strForma, strParcial, ((Object[]) objAsignaturaSeleccionada)[0] + "");
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

        requestContext.update("tabNotas:" + event.getRowIndex() + ":eqv20");
        requestContext.update("tabNotas:" + event.getRowIndex() + ":nota");

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
                fila[11] = 0;
                utilitario.agregarMensajeError("La nota de Trabajos debe estar en el rango de 0 a 10", "");
            } else if (event.getColumn().getClientId().endsWith("cActInd")) {
                fila[3] = 0;
                utilitario.agregarMensajeError("La nota de Actividades Individuales debe estar en el rango de 0 a 10", "");
            } else if (event.getColumn().getClientId().endsWith("cActGrup")) {
                fila[4] = 0;
                utilitario.agregarMensajeError("La nota de Actividades en Grupo debe estar en el rango de 0 a 10", "");
            } else if (event.getColumn().getClientId().endsWith("cLecc")) {
                fila[5] = 0;
                utilitario.agregarMensajeError("La nota de Lecciones debe estar en el rango de 0 a 10", "");
            } else if (event.getColumn().getClientId().endsWith("cEval")) {
                fila[6] = 0;
                utilitario.agregarMensajeError("La nota de Evaluaciiones debe estar en el rango de 0 a 10", "");
            } else if (event.getColumn().getClientId().endsWith("cNota")) {
                fila[8] = 0;
                utilitario.agregarMensajeError("La nota debe estar en el rango de 0 a 10", "");
            }
            requestContext.update("tabNotas");
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

        TablaGenerica tab_alumnos = getAlumnosCurso(((Object[]) objCursoSeleccionado)[0] + "");

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
            parametros.put("CURSO", ((Object[]) objCursoSeleccionado)[1] + "");
            parametros.put("PARALELO", ((Object[]) objCursoSeleccionado)[2] + "");
        }
        parametros.put("PROFESOR", docDocente.getDocNombres());
        parametros.put("PERIODO", perActual.getPerNombre());

        genera.setDataSource(new ReporteDataSource(tab_reporte));
        genera.generar(parametros, "/reportes/rep_parcial/rep_registro.jasper");

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

    public TablaGenerica getAlumnosCurso(String cur_codigo) {
        return utilitario.consultar("select a.mat_codigo,b.alu_apellidos||' '|| b.alu_nombres as Alumnos from matricula a\n"
                + "inner JOIN alumnos b on a.alu_codigo=b.alu_codigo\n"
                + "where a.cre_codigo=" + cur_codigo + "\n"
                + "ORDER BY b.alu_apellidos||' '|| b.alu_nombres DESC");
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

    public void verReporteComportamiento() {
        if (objCursoSeleccionado != null) {
            generarReporteComportamiento();
            utilitario.ejecutarJavaScript("window.open('" + str_path_reporte + "');");
        } else {
            utilitario.agregarMensajeInfo("Debe selecccionar un curso", "");
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
}
