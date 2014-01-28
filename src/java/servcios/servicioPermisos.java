/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Permisos;
import entidades.Cursos;
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
public class servicioPermisos {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    private Utilitario utilitario = new Utilitario();

    public String guardarPermisos(Permisos permisos) {
        try {
            utx.begin();
            manejador.joinTransaction();
            //nombre tabla y atributo
            if (permisos.getPrmCodigo() == null) {
                long lon_codigo = utilitario.getConexion().getMaximo("Permisos", "prm_codigo", 1);
                System.out.println("ide " + lon_codigo);
                permisos.setPrmCodigo(new Integer(String.valueOf(lon_codigo)));
                manejador.persist(permisos);
            } else {
                manejador.merge(permisos);
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

    public String elimnarPermisos(String prmCodigo) {
        try {
            utx.begin();
            Permisos borraPermisos = manejador.find(Permisos.class, new Integer(prmCodigo));
            manejador.remove(borraPermisos);
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

    public Cursos getPermisos(String prmCodigo) {

        try {
            Query q = manejador.createNamedQuery("Permisos.findByPrmCodigo");
            q.setParameter("prmCodigo", new Integer(prmCodigo));
            return (Cursos) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    public List<Permisos> getPermisosRol(String rolCodigo) {
        if (rolCodigo != null) {
            try {
                Query q = manejador.createQuery("SELECT p FROM Permisos p WHERE p.rolCodigo.rolCodigo=" + rolCodigo);
                return q.getResultList();
            } catch (Exception e) {
            }
        }
        return null;
    }
    /**
     * Lista para combos
     *
     * @return
     */
    /*public List getListaCursos() {
     return utilitario.getConexion().consultar("select cur_codigo,cur_anio from cursos");
     }*/
}
