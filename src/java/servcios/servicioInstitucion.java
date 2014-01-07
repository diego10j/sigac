/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Alumnos;
import entidades.Docentes;
import entidades.Institucion;
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
    
    private Utilitario utilitario=new Utilitario();

    public String guardarInstitucion(Institucion instituto) {
        try {
            utx.begin();
            //nombre tabla y atributo
            long lon_codigo=utilitario.getConexion().getMaximo("institucion", "doc_codigo", 1);
            System.out.println("ide "+lon_codigo);
            instituto.setInsCodigo(new Integer(String.valueOf(lon_codigo)));
            manejador.joinTransaction();
            manejador.persist(instituto);
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

    public Institucion getIntitucion(String insCodigo) {

        try {
            Query q = manejador.createNamedQuery("Institucion.findByDocCodigo");
            q.setParameter("insCodigo", new Integer(insCodigo));
            return (Institucion) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
}
