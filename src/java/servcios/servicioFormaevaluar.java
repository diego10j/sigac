/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Formaevaluar;
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
public class servicioFormaevaluar {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    private Utilitario utilitario = new Utilitario();

    public String guardarFormaevaluar(Formaevaluar formaevaluar) {
        try {
            utx.begin();
            manejador.joinTransaction();
            if (formaevaluar.getForCodigo() == null) {
                //nombre tabla y atributo
                long lon_codigo = utilitario.getConexion().getMaximo("formaevaluar", "for_codigo", 1);
                System.out.println("ide " + lon_codigo);
                formaevaluar.setForCodigo(new Integer(String.valueOf(lon_codigo)));
                manejador.persist(formaevaluar);
            } else {
                manejador.merge(formaevaluar);
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

    public String elimnarFormaevaluar(String forCodigo) {
        try {
            utx.begin();
            Formaevaluar borraFormaevaluar = manejador.find(Formaevaluar.class, new Integer(forCodigo));
            manejador.remove(borraFormaevaluar);
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

    public Formaevaluar getFormaevaluar() {

        try {
            Query q = manejador.createNamedQuery("Formaevaluar.findByForCodigo");
            q.setParameter("forCodigo", new Integer("0"));
            return (Formaevaluar) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    public List<Formaevaluar> getFormaevaluars() {
        try {
            Query q = manejador.createNamedQuery("Formaevaluar.findAll");
            return q.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
}
