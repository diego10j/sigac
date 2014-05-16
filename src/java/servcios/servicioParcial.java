/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.NotaDestrezaparcial;
import framework.aplicacion.TablaGenerica;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author Diego
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class servicioParcial {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    private Utilitario utilitario = new Utilitario();

    /**
     * Retorna los cursos que tiene asignado un docente para pasar notas
     *
     * @param per_codigo periodo
     * @param doc_codigo docente
     * @return Lista con los cursos y pararalelos
     */
    public List getCursosDocente(String per_codigo, String doc_codigo) {
        return utilitario.getConexion().consultar("select d.cre_codigo,cu.cur_anio,pa.par_nombre,c.cur_codigo from distributivomxc d\n"
                + "inner join crear_curso c on d.cre_codigo=c.cre_codigo INNER JOIN cursos cu on c.cur_codigo=cu.cur_codigo inner join paralelo pa on c.par_codigo=pa.par_codigo inner join docentes p on d.doc_codigo=p.doc_codigo"
                + " where p.doc_codigo=" + doc_codigo + " and c.per_codigo=" + per_codigo + "\n"
                + "group  by d.cre_codigo,cu.cur_codigo,cu.cur_anio,pa.par_nombre,c.cur_codigo\n"
                + "order by cu.cur_codigo,par_nombre");

    }

    public List getMatriculasCursoAlumno(String alu_codigo) {
        return utilitario.getConexion().consultar("select d.cre_codigo,per_nombre,cu.cur_anio,pa.par_nombre,c.cur_codigo from distributivomxc d\n"
                + " inner join crear_curso c on d.cre_codigo=c.cre_codigo \n"
                + " INNER JOIN cursos cu on c.cur_codigo=cu.cur_codigo inner join paralelo pa on c.par_codigo=pa.par_codigo \n"
                + " inner join matricula p on d.cre_codigo=p.cre_codigo\n"
                + " inner join periodo_lectivo pl on c.per_codigo=pl.per_codigo\n"
                + " where p.alu_codigo=" + alu_codigo + "\n"
                + " group  by d.cre_codigo,per_nombre,cu.cur_codigo,cu.cur_anio,pa.par_nombre,c.cur_codigo\n"
                + " order by cu.cur_codigo,par_nombre");

    }

    public List getCursosAlumno(String alu_codigo) {
        return utilitario.getConexion().consultar("select a.cre_codigo,cur_anio,par_nombre,c.cur_codigo from matricula a\n"
                + "inner join crear_curso b on a.cre_codigo=b.cre_codigo\n"
                + "INNER JOIN cursos c on b.cur_codigo=c.cur_codigo \n"
                + "inner join paralelo d on b.par_codigo=d.par_codigo\n"
                + "where alu_codigo=" + alu_codigo);

    }

    public List getCursosDisciplinaDocente(String per_codigo, String doc_codigo) {
        return utilitario.getConexion().consultar("SELECT c.cre_codigo,cu.cur_anio,pa.par_nombre,c.cur_codigo FROM crear_curso c\n"
                + "INNER JOIN cursos cu on c.cur_codigo=cu.cur_codigo\n"
                + "inner join paralelo pa on c.par_codigo=pa.par_codigo\n"
                + "where c.doc_codigo=" + doc_codigo + " and c.per_codigo=" + per_codigo + "");
    }

    public List getMateriasCursoDocente(String cre_codigo, String doc_codigo) {
        return utilitario.getConexion().consultar("select d.dis_codigo,a.asi_nombre,a.tip_codigo,c.asi_nombre,d.asi_codigo from distributivomxc d\n"
                + " INNER JOIN asignaturas a on d.asi_codigo=a.asi_codigo\n"
                + " LEFT JOIN asignaturas c on a.asi_asi_codigo=c.asi_codigo "
                + "where d.doc_codigo=" + doc_codigo + "\n"
                + "and d.cre_codigo=" + cre_codigo + " order by c.asi_nombre,a.asi_nombre");
    }

    public List getMateriasCursoDocente(String cre_codigo) {
        return utilitario.getConexion().consultar("select d.dis_codigo,a.asi_nombre,a.tip_codigo,c.asi_nombre from distributivomxc d\n"
                + " INNER JOIN asignaturas a on d.asi_codigo=a.asi_codigo\n"
                + " LEFT JOIN asignaturas c on a.asi_asi_codigo=c.asi_codigo "
                + "where d.cre_codigo=" + cre_codigo + " order by c.asi_nombre,a.asi_nombre");
    }

    public List<NotaDestrezaparcial> getNotasDestreza() {
        try {
            Query q = manejador.createNamedQuery("PeriodoLectivo.findAll");
            return q.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Reorna los alumnos que no estan inscritos en
     *
     * @param cre_codigo Curso
     * @param for_codigo Quimestre
     * @param eva_codigo Parcial
     * @param dis_codigo Distributivo Asignatura
     * @return
     */
    public TablaGenerica getParcialDistributivo(String cre_codigo, String for_codigo, String eva_codigo, String dis_codigo) {
        return utilitario.consultar("select * from matricula where cre_codigo=" + cre_codigo + " and mat_codigo not in (SELECT mat_codigo from nota_destrezaparcial where for_codigo=" + for_codigo + " and eva_codigo=" + eva_codigo + " and dis_codigo=" + dis_codigo + ") ");
    }

    /**
     * Inscribe los alumnos en un Curso, en una Asignaura en un parcial de un
     * quimestre
     *
     * @param cre_codigo Curso
     * @param for_codigo Quimestre
     * @param eva_codigo Parcial
     * @param dis_codigo Distributivo Asignatura
     * @return int con el número de alumnos insertados
     */
    public int inscribirParcialDistributivo(String cre_codigo, String for_codigo, String eva_codigo, String dis_codigo) {
        TablaGenerica tab_alumno = getParcialDistributivo(cre_codigo, for_codigo, eva_codigo, dis_codigo);
        if (tab_alumno.isEmpty() == false) {
            TablaGenerica tab_notas = new TablaGenerica();
            tab_notas.setTabla("nota_destrezaparcial", "not_codigo", -1);
            tab_notas.setCondicion("not_codigo=-1");
            tab_notas.ejecutarSql();

            for (int i = 0; i < tab_alumno.getTotalFilas(); i++) {
                tab_notas.insertar();
                tab_notas.setValor("eva_codigo", eva_codigo);
                tab_notas.setValor("for_codigo", for_codigo);
                tab_notas.setValor("mat_codigo", tab_alumno.getValor(i, "mat_codigo"));
                tab_notas.setValor("dis_codigo", dis_codigo);
                tab_notas.setValor("not_actividadindividual", "0.00");
                tab_notas.setValor("not_actividadgrupal", "0.00");
                tab_notas.setValor("not_lecciones", "0.00");
                tab_notas.setValor("not_evaluacionsumativa", "0.00");
                tab_notas.setValor("not_total", "0.00");
                tab_notas.setValor("not_primerparcial", "0.00");
                tab_notas.setValor("not_actividadindividual", "0.00");
                tab_notas.setValor("not_trabajos", "0.00");
                tab_notas.setValor("not_observacion", " ");
            }
            tab_notas.guardar();
            if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
                return tab_alumno.getTotalFilas();
            }
        }
        return 0;
    }

    /**
     * Retorna una lista con los alumnos que pertenecen a un parcial de un
     * distributivo
     *
     * @param cre_codigo Curso
     * @param for_codigo Quimestre
     * @param eva_codigo Parcial
     * @param dis_codigo Distributivo
     * @return
     */
    public List getNotasParcialDistributivo(String cre_codigo, String for_codigo, String eva_codigo, String dis_codigo) {
        return utilitario.getConexion().consultar("select a.not_codigo,alu_apellidos,alu_nombres,not_actividadindividual\n"
                + ",not_actividadgrupal,not_lecciones,not_evaluacionsumativa,not_total,\n"
                + "not_primerparcial,not_eqvdestreza,not_observacion,not_trabajos,a.for_codigo,a.dis_codigo,a.mat_codigo,a.eva_codigo from nota_destrezaparcial a\n"
                + "inner join matricula b on a.mat_codigo =b.mat_codigo\n"
                + "inner join alumnos c on b.alu_codigo=c.alu_codigo\n"
                + "where a.for_codigo=" + for_codigo + "  and a.dis_codigo=" + dis_codigo + " and a.eva_codigo=" + eva_codigo + " order by alu_apellidos");
    }

    /**
     * Guarda todas las notas de un parcial
     *
     * @param notas
     * @return
     */
    public String guardarNotasParcial(List notas) {
        for (Object actual : notas) {
            Object[] fila = (Object[]) actual;
            utilitario.getConexion().agregarSql("UPDATE nota_destrezaparcial set not_actividadindividual=" + fila[3] + ""
                    + ",not_actividadgrupal=" + fila[4] + ",not_lecciones=" + fila[5] + ",not_evaluacionsumativa=" + fila[6] + ",not_total=" + fila[7] + ",\n"
                    + "not_primerparcial=" + fila[8] + ",not_eqvdestreza='" + fila[9] + "',not_observacion='" + fila[10] + "', not_trabajos=" + fila[11] + " where not_codigo=" + fila[0]);
        }
        return utilitario.getConexion().ejecutarListaSql();
    }

    /**
     * DISCIPLINA
     */
    /**
     * Reorna los alumnos que no estan inscritos para pasar la nota de
     * disciplina
     *
     * @param cre_codigo Curso
     * @param for_codigo Quimestre
     * @param eva_codigo Parcial
     * @return
     */
    public TablaGenerica getParcialDisciplina(String cre_codigo, String for_codigo, String eva_codigo) {
        return utilitario.consultar("select * from matricula where cre_codigo=" + cre_codigo + " and mat_codigo not in (SELECT mat_codigo from comportamientoparcial where for_codigo=" + for_codigo + " and eva_codigo=" + eva_codigo + ") ");
    }

    /**
     * Inscribe los alumnos en un Curso, en una Asignaura en un parcial de un
     * quimestre para pasar la disciplina
     *
     * @param cre_codigo Curso
     * @param for_codigo Quimestre
     * @param eva_codigo Parcial
     * @return int con el número de alumnos insertados
     */
    public int inscribirParcialDisciplina(String cre_codigo, String for_codigo, String eva_codigo) {
        TablaGenerica tab_alumno = getParcialDisciplina(cre_codigo, for_codigo, eva_codigo);
        if (tab_alumno.isEmpty() == false) {
            TablaGenerica tab_notas = new TablaGenerica();
            tab_notas.setTabla("comportamientoparcial", "com_codigo", -1);
            tab_notas.setCondicion("com_codigo=-1");
            tab_notas.ejecutarSql();

            for (int i = 0; i < tab_alumno.getTotalFilas(); i++) {
                tab_notas.insertar();
                tab_notas.setValor("eva_codigo", eva_codigo);
                tab_notas.setValor("for_codigo", for_codigo);
                tab_notas.setValor("mat_codigo", tab_alumno.getValor(i, "mat_codigo"));
                tab_notas.setValor("com_semana1", "0.00");
                tab_notas.setValor("com_semana2", "0.00");
                tab_notas.setValor("com_semana3", "0.00");
                tab_notas.setValor("com_semana4", "0.00");
                tab_notas.setValor("com_semana5", "0.00");
                tab_notas.setValor("com_sumatoria", "0.00");
            }
            tab_notas.guardar();
            if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
                return tab_alumno.getTotalFilas();
            }
        }
        return 0;
    }

    /**
     * Retorna una lista con los alumnos que pertenecen a un parcial de un
     * distributivo
     *
     * @param cre_codigo Curso
     * @param for_codigo Quimestre
     * @param dis_codigo Distributivo
     * @return
     */
    public List getNotasParcialDisciplina(String cre_codigo, String for_codigo, String eva_codigo) {
        return utilitario.getConexion().consultar("SELECT a.com_codigo,alu_apellidos,alu_nombres,com_semana1,com_semana2,\n"
                + "com_semana3,com_semana4,com_semana5,com_sumatoria,com_equivalencia,com_equi,a.for_codigo,a.mat_codigo,a.eva_codigo from comportamientoparcial a\n"
                + "inner join matricula b on a.mat_codigo =b.mat_codigo\n"
                + "inner join alumnos c on b.alu_codigo=c.alu_codigo "
                + "where a.for_codigo=" + for_codigo + " and a.eva_codigo=" + eva_codigo + " order by alu_apellidos");
    }

    /**
     * Guarda todas las notas de un parcial
     *
     * @param notas
     * @return
     */
    public String guardarDisciplinaParcial(List notas) {
        for (Object actual : notas) {
            Object[] fila = (Object[]) actual;
            utilitario.getConexion().agregarSql("UPDATE comportamientoparcial set com_semana1=" + fila[3] + " "
                    + ",com_semana2=" + fila[4] + ", com_semana3=" + fila[5] + ", com_semana4=" + fila[6] + ", com_semana5=" + fila[7] + ",\n"
                    + "com_sumatoria=" + fila[8] + ",com_equivalencia='" + fila[9] + "',com_equi='" + fila[10] + "' where com_codigo=" + fila[0]);
        }
        return utilitario.getConexion().ejecutarListaSql();
    }

    /**
     * ASISTENCIA
     *
     */
    /**
     * Reorna los alumnos que no estan inscritos para pasar la nota de
     * asistencia
     *
     * @param cre_codigo Curso
     * @param for_codigo Quimestre
     * @param eva_codigo Parcial
     * @return
     */
    public TablaGenerica getParcialAsistencia(String cre_codigo, String for_codigo, String eva_codigo) {
        return utilitario.consultar("select * from matricula where cre_codigo=" + cre_codigo + " and mat_codigo not in (SELECT mat_codigo from registroasistencia where for_codigo=" + for_codigo + " and eva_codigo=" + eva_codigo + ") ");
    }

    /**
     * Inscribe los alumnos en un Curso, en una Asignaura en un parcial de un
     * quimestre para pasar la disciplina
     *
     * @param cre_codigo Curso
     * @param for_codigo Quimestre
     * @param eva_codigo Parcial
     * @return int con el número de alumnos insertados
     */
    public int inscribirParcialAsistencia(String cre_codigo, String for_codigo, String eva_codigo, int dias) {
        TablaGenerica tab_alumno = getParcialAsistencia(cre_codigo, for_codigo, eva_codigo);
        if (tab_alumno.isEmpty() == false) {
            TablaGenerica tab_notas = new TablaGenerica();
            tab_notas.setTabla("registroasistencia", "reg_codigo", -1);
            tab_notas.setCondicion("reg_codigo=-1");
            tab_notas.ejecutarSql();

            for (int i = 0; i < tab_alumno.getTotalFilas(); i++) {
                tab_notas.insertar();
                tab_notas.setValor("eva_codigo", eva_codigo);
                tab_notas.setValor("for_codigo", for_codigo);
                tab_notas.setValor("mat_codigo", tab_alumno.getValor(i, "mat_codigo"));
                tab_notas.setValor("reg_atrasos", "0");
                tab_notas.setValor("reg_faltasjustificadas", "0");
                tab_notas.setValor("reg_faltasinjustificadas", "0");
                tab_notas.setValor("reg_totalfaltas", "0");
                tab_notas.setValor("reg_diaslaborados", dias + "");
                tab_notas.setValor("reg_dias", dias + "");
            }
            tab_notas.guardar();
            if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
                return tab_alumno.getTotalFilas();
            }
        }
        return 0;
    }

    /**
     * Retorna una lista con los alumnos que pertenecen a un parcial de un
     * distributivo
     *
     * @param cre_codigo Curso
     * @param for_codigo Quimestre
     * @param dis_codigo Distributivo
     * @return
     */
    public List getListaParcialAsistencia(String cre_codigo, String for_codigo, String eva_codigo) {
        return utilitario.getConexion().consultar("select a.reg_codigo,alu_apellidos,alu_nombres,"
                + "reg_atrasos,reg_faltasjustificadas,reg_faltasinjustificadas,reg_totalfaltas,reg_diaslaborados,reg_dias from registroasistencia a\n"
                + "inner join matricula b on a.mat_codigo =b.mat_codigo\n"
                + "inner join alumnos c on b.alu_codigo=c.alu_codigo "
                + "where a.for_codigo=" + for_codigo + " and a.eva_codigo=" + eva_codigo + " order by alu_apellidos");
    }

    /**
     * Guarda todas las notas de un parcial
     *
     * @param notas
     * @return
     */
    public String guardarAsistenciaParcial(List notas) {
        for (Object actual : notas) {
            Object[] fila = (Object[]) actual;
            utilitario.getConexion().agregarSql("UPDATE registroasistencia set reg_atrasos=" + fila[3] + " "
                    + ",reg_faltasjustificadas=" + fila[4] + ", reg_faltasinjustificadas=" + fila[5] + ", reg_totalfaltas=" + fila[6] + ", reg_diaslaborados=" + fila[7] + "\n"
                    + " where reg_codigo=" + fila[0]);

        }
        return utilitario.getConexion().ejecutarListaSql();
    }
}
