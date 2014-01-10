/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;

import entidades.EquivalenciaConducta;

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
public class servicioConducta {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    
    private Utilitario utilitario=new Utilitario();

    public String guardarConducta(EquivalenciaConducta conducta) {
        try {
            utx.begin();
            //nombre tabla y atributo
            long lon_codigo=utilitario.getConexion().getMaximo("Equivalencia_conducta", "eqc_codigo", 1);
             conducta.setEqcCodigo(new Integer(String.valueOf(lon_codigo)));
            System.out.println("ide "+lon_codigo);
            conducta.setEqcCodigo(new Integer(String.valueOf(lon_codigo)));
            manejador.joinTransaction();
            manejador.persist(conducta);
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

    public String elimnarConducta(String eqvCodigo) {
        try {
            utx.begin();
            EquivalenciaConducta borraConducta = manejador.find(EquivalenciaConducta.class, new Integer(eqvCodigo));
            manejador.remove(borraConducta);
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

    public EquivalenciaConducta getConducta(String eqvCodigo) {

        try {
            Query q = manejador.createNamedQuery("EquivalenciaConducta.findByDocCodigo");
            q.setParameter("eqvCodigo", new Integer(eqvCodigo));
            return (EquivalenciaConducta) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
}
