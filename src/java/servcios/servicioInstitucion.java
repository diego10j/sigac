/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Institucion;
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
public class servicioInstitucion {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    private Utilitario utilitario = new Utilitario();

    public String guardarInstitucion(Institucion iinstituto) {
        Institucion instituto = iinstituto;
        try {
            utx.begin();
            manejador.joinTransaction();
            if (instituto.getInsCodigo() == null) {
                //nombre tabla y atributo
                long lon_codigo = utilitario.getConexion().getMaximo("institucion", "doc_codigo", 1);
                instituto.setInsCodigo(new Integer(String.valueOf(lon_codigo)));
                manejador.persist(instituto);
            } else {
                manejador.merge(instituto);
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

    public String elimnarIntitucion(String insCodigo) {
        try {
            utx.begin();
            Institucion borraInstituto = manejador.find(Institucion.class, new Integer(insCodigo));
            manejador.remove(borraInstituto);
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

    public Institucion getIntitucion() {

        try {
            Query q = manejador.createNamedQuery("Institucion.findByInsCodigo");
            q.setParameter("insCodigo", new Integer("0"));
            return (Institucion) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    public List<Institucion> getInstitucion() {
        try {
            Query q = manejador.createNamedQuery("Institucion.findAll");
            return q.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
}
