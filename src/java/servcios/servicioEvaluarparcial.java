/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Evaluarparcial;
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
public class servicioEvaluarparcial {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    private Utilitario utilitario = new Utilitario();

    public String guardarEvaluarparcial(Evaluarparcial ievaluarparcial) {
        Evaluarparcial evaluarparcial = ievaluarparcial;
        try {
            utx.begin();
            manejador.joinTransaction();
            if (evaluarparcial.getEvaCodigo() == null) {
                //nombre tabla y atributo
                long lon_codigo = utilitario.getConexion().getMaximo("evaluarparcial", "eva_codigo", 1);
                System.out.println("ide " + lon_codigo);
                evaluarparcial.setEvaCodigo(new Integer(String.valueOf(lon_codigo)));
                manejador.persist(evaluarparcial);
            } else {
                manejador.merge(evaluarparcial);
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

    public String elimnarEvaluarparcial(String evaCodigo) {
        try {
            utx.begin();
            Evaluarparcial borraEvaluarparcial = manejador.find(Evaluarparcial.class, new Integer(evaCodigo));
            manejador.remove(borraEvaluarparcial);
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

    public Evaluarparcial getEvaluarparcial() {

        try {
            Query q = manejador.createNamedQuery("Evaluarparcial.findByEvaCodigo");
            q.setParameter("evaCodigo", new Integer("0"));
            return (Evaluarparcial) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    public List<Evaluarparcial> getEvaluarparcials() {
        try {
            Query q = manejador.createNamedQuery("Evaluarparcial.findAll");
            return q.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Lista para combos
     *
     * @return
     */
    public List getListaEvaluarParcial() {
        return utilitario.getConexion().consultar("SELECT eva_codigo,eva_nombre from evaluarparcial order by eva_nombre");
    }
}
