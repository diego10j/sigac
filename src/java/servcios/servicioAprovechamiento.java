/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Alumnos;
import entidades.EquivalenciaAprovechamiento;
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
public class servicioAprovechamiento {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    
    private Utilitario utilitario=new Utilitario();

    public String guardarAprovechamiento(EquivalenciaAprovechamiento iaprovechamiento) {
        EquivalenciaAprovechamiento aprovechamiento=iaprovechamiento;
        try {
            utx.begin();
            manejador.joinTransaction();
            //nombre tabla y atributo
           
            if(aprovechamiento.getEqaCodigo()==null){
                long lon_codigo=utilitario.getConexion().getMaximo("Equivalencia_Aprovechamiento", "eqa_codigo", 1);
             aprovechamiento.setEqaCodigo(new Integer(String.valueOf(lon_codigo)));
              System.out.println("ide "+lon_codigo);
             manejador.persist(aprovechamiento);
            }else{
                manejador.merge(aprovechamiento);
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

    public String elimnarAprovechamiento(String eqvCodigo) {
        try {
            utx.begin();
            EquivalenciaAprovechamiento borraAprovechamiento = manejador.find(EquivalenciaAprovechamiento.class, new Integer(eqvCodigo));
            manejador.remove(borraAprovechamiento);
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

    public EquivalenciaAprovechamiento getAprovechamiento(String eqvCodigo) {

        try {
            Query q = manejador.createNamedQuery("EquivalenciaAprovechamiento.findByEqaCodigo");
            q.setParameter("eqvCodigo", new Integer(eqvCodigo));
            return (EquivalenciaAprovechamiento) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
    
    public List<EquivalenciaAprovechamiento> getEquivalenciaAprovechamiento() {        
        try {
            Query q = manejador.createNamedQuery("EquivalenciaAprovechamiento.findAll");
            return q.getResultList();
        } catch (Exception e) {
        }        
        return null;
    }
}
