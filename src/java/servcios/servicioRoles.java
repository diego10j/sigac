/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Roles;
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
public class servicioRoles {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    private Utilitario utilitario = new Utilitario();

    public String guardarRoles(Roles rol) {
        try {
            utx.begin();
            manejador.joinTransaction();
            //nombre tabla y atributo
            if (rol.getRolCodigo() == null) {
                long lon_codigo = utilitario.getConexion().getMaximo("Roles", "rol_codigo", 1);
                rol.setRolCodigo(new Integer(String.valueOf(lon_codigo)));              
                manejador.persist(rol);
            } else {
                manejador.merge(rol);
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

    public String elimnarRoles(String rolCodigo) {
        try {
            utx.begin();
            Roles borraRol = manejador.find(Roles.class, new Integer(rolCodigo));
            manejador.remove(borraRol);
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

    public Roles getRoles(String rolCodigo) {

        try {
            Query q = manejador.createNamedQuery("Roles.findByRolCodigo");
            q.setParameter("rolCodigo", new Integer(rolCodigo));
            return (Roles) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    public List<Roles> getRoles() {
        try {
            Query q = manejador.createNamedQuery("Roles.findAll");
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
    public List getListaRoles() {
        return utilitario.getConexion().consultar("select rol_codigo,rol_nombre from roles order by rol_nombre");
    }
}
