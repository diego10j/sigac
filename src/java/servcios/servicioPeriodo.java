/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Cursos;
import entidades.PeriodoLectivo;
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
public class servicioPeriodo {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    private Utilitario utilitario = new Utilitario();

    public String guardarPeriodoLectivo(PeriodoLectivo periodolectivo) {
        try {
            utx.begin();
            manejador.joinTransaction();
            //nombre tabla y atributo
            if (periodolectivo.getPerCodigo() == null) {
                long lon_codigo = utilitario.getConexion().getMaximo("PeriodoLectivo", "per_codigo", 1);
                periodolectivo.setPerCodigo(new Integer(String.valueOf(lon_codigo)));
                System.out.println("ide " + lon_codigo);
                periodolectivo.setPerCodigo(new Integer(String.valueOf(lon_codigo)));
                manejador.persist(periodolectivo);
            } else {
                manejador.merge(periodolectivo);
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

    public String elimnarPeriodoLectivo(String perCodigo) {
        try {
            utx.begin();
            PeriodoLectivo borraPeriodoLectivo = manejador.find(PeriodoLectivo.class, new Integer(perCodigo));
            manejador.remove(borraPeriodoLectivo);
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

    public PeriodoLectivo getPeriodoLectivo(String perCodigo) {

        try {
            Query q = manejador.createNamedQuery("PeriodoLectivo.findByPerCodigo");
            q.setParameter("perCodigo", new Integer(perCodigo));
            return (PeriodoLectivo) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    public List<PeriodoLectivo> getPeriodoLectivo() {
        try {
            Query q = manejador.createNamedQuery("PeriodoLectivo.findAll");
            return q.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
}
