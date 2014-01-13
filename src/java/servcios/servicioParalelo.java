/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;

import entidades.Cursos;
import entidades.Institucion;
import entidades.Paralelo;
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
public class servicioParalelo {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    
    private Utilitario utilitario=new Utilitario();

    public String guardarParalelo(Paralelo paralelo) {
        try {
            utx.begin();
            //nombre tabla y atributo
            long lon_codigo=utilitario.getConexion().getMaximo("Paralelo", "par_codigo", 1);
            System.out.println("ide "+lon_codigo);
            paralelo.setParCodigo(new Integer(String.valueOf(lon_codigo)));
            manejador.joinTransaction();
            manejador.persist(paralelo);
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

    public String elimnarParalelo(String parCodigo) {
        try {
            utx.begin();
            Paralelo borraParalelo = manejador.find(Paralelo.class, new Integer(parCodigo));
            manejador.remove(borraParalelo);
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

    public Paralelo getParalelo(String parCodigo) {

        try {
            Query q = manejador.createNamedQuery("Paralelo.findByDocCodigo");
            q.setParameter("parCodigo", new Integer(parCodigo));
            return (Paralelo) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
    
     public List<Paralelo> getParalelo() {        
        try {
            Query q = manejador.createNamedQuery("Paralelo.findAll");
            return q.getResultList();
        } catch (Exception e) {
        }        
        return null;
    }
}
