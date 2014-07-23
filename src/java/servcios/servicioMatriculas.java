/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Matricula;
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
public class servicioMatriculas {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    private Utilitario utilitario = new Utilitario();

    public String guardarMatriculas(Matricula imatricula) {
        Matricula matricula = imatricula;
        try {
            utx.begin();
            manejador.joinTransaction();
            if (matricula.getMatCodigo() == null || matricula.getMatCodigo().toString().isEmpty()) {
                //nombre tabla y atributo
                long lon_codigo = utilitario.getConexion().getMaximo("matricula", "mat_codigo", 1);
                matricula.setMatCodigo(new Integer(String.valueOf(lon_codigo)));
                manejador.persist(matricula);
            } else {
                manejador.merge(matricula);
            }


            utx.commit();
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception e1) {
            }
            e.printStackTrace();
            return e.getMessage();
        }
        return "";
    }

    public String elimnarMatriculas(String matCodigo) {
        try {
            utx.begin();
            Matricula borraMatricula = manejador.find(Matricula.class, new Integer(matCodigo));
            manejador.remove(borraMatricula);
            utx.commit();
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception e1) {
            }
            return e.getMessage();
        }
        return "";
    }

    public Matricula getMatricula() {

        try {
            Query q = manejador.createNamedQuery("Matricula.findByMatCodigo");
            q.setParameter("matCodigo", new Integer("0"));
            return (Matricula) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    public List<Matricula> getMatriculas(String creCodigo) {
        try {
            Query q = manejador.createQuery("SELECT m FROM Matricula m WHERE m.creCodigo.creCodigo=" + creCodigo + " order by m.aluCodigo.aluApellidos");
            return q.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    public List getMatriculadosxCurso(String perCodigo) {
        return utilitario.getConexion().consultar("SELECT matricula.cre_codigo,cur_anio,par_nombre,Count(matricula.cre_codigo)\n"
                + "FROM matricula\n"
                + "INNER JOIN crear_curso ON matricula.cre_codigo = crear_curso.cre_codigo\n"
                + "INNER JOIN cursos ON crear_curso.cur_codigo = cursos.cur_codigo\n"
                + "INNER JOIN paralelo ON crear_curso.par_codigo = paralelo.par_codigo\n"
                + "WHERE per_codigo=" + perCodigo + "\n"
                + "GROUP BY matricula.cre_codigo,cur_anio,par_nombre,cursos.cur_codigo\n"
                + "ORDER BY cursos.cur_codigo,par_nombre");
    }
}
