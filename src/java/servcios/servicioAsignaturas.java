/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Cursos;
import entidades.Asignaturas;
import entidades.EquivalenciaAprovechamiento;
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
public class servicioAsignaturas {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    
    private Utilitario utilitario=new Utilitario();

    public String guardarAsignaturas(Asignaturas asignatura) {
        try {
            utx.begin();
              manejador.joinTransaction();
            //nombre tabla y atributo
             if(asignatura.getAsiCodigo()==null){
                 long lon_codigo=utilitario.getConexion().getMaximo("Asignaturas", "asi_codigo", 1);
             asignatura.setAsiCodigo(new Integer(String.valueOf(lon_codigo)));
            System.out.println("ide "+lon_codigo);
            asignatura.setAsiCodigo(new Integer(String.valueOf(lon_codigo)));
            manejador.persist(asignatura);
             }else{
                 manejador.merge(asignatura);
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

    public String elimnarAsignaturas(String asiCodigo) {
        try {
            utx.begin();
            Asignaturas borraAsignatura = manejador.find(Asignaturas.class, new Integer(asiCodigo));
            manejador.remove(borraAsignatura);
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

    public Asignaturas getAsignaturas(String asiCodigo) {

        try {
            Query q = manejador.createNamedQuery("Asignaturas.findByAsiCodigo");
            q.setParameter("asiCodigo", new Integer(asiCodigo));
            return (Asignaturas) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
    
      public List<Asignaturas> getEquivalenciaAsignaturas() {        
        try {
            Query q = manejador.createNamedQuery("Asignaturas.findAll");
            return q.getResultList();
        } catch (Exception e) {
        }        
        return null;
    }
}
