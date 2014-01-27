/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Cursos;
import entidades.Asignaturas;
import entidades.EquivalenciaAprovechamiento;
import entidades.Pantalla;
import entidades.Permisos;
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
public class servicioAdministracion {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    private Utilitario utilitario = new Utilitario();

    public String guardarPantalla(Pantalla pantalla) {
        try {
            utx.begin();
            manejador.joinTransaction();
            //nombre tabla y atributo
            if (pantalla.getPanCodigo() == null) {
                long lon_codigo = utilitario.getConexion().getMaximo("pantalla", "pan_codigo", 1);
                pantalla.setPanCodigo(new Integer(String.valueOf(lon_codigo)));
                manejador.persist(pantalla);
            } else {
                manejador.merge(pantalla);
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

    public String elimnarPantalla(String panCodigo) {
        try {
            utx.begin();
            Pantalla borra = manejador.find(Pantalla.class, new Integer(panCodigo));
            manejador.remove(borra);
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

    public String guardarRol(Roles rol) {
        try {
            utx.begin();
            manejador.joinTransaction();
            //nombre tabla y atributo
            if (rol.getRolCodigo() != null) {
                long lon_codigo = utilitario.getConexion().getMaximo("roles", "rol_codigo", 1);
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

    public String guardarPermiso(Permisos permiso) {
        try {
            utx.begin();
            manejador.joinTransaction();
            //nombre tabla y atributo
            if (permiso.getPrmCodigo() != null) {
                long lon_codigo = utilitario.getConexion().getMaximo("permisos", "prm_codigo", 1);
                permiso.setPrmCodigo(new Integer(String.valueOf(lon_codigo)));
                manejador.persist(permiso);
            } else {
                manejador.merge(permiso);
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

    public List<Roles> getRoles() {
        try {
            Query q = manejador.createNamedQuery("Roles.findAll");
            return q.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    public List<Permisos> getPermisosRol(String rolCodigo) {
        try {
            Query q = manejador.createQuery("SELECT p FROM Permisos p WHERE p.rolCodigo.rolCodigo=" + rolCodigo);
            return q.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    public List<Pantalla> getPantallasPadre() {
        try {
            Query q = manejador.createQuery("SELECT p FROM Pantalla p WHERE p.panPanCodigo is null");
            return q.getResultList();
        } catch (Exception e) {
            System.out.println("ERROR SQL PANTALLAS:  " + e.getMessage());
        }
        return null;
    }

    public List<Pantalla> getPantallasHijas(String panCodigo) {
        try {
            Query q = manejador.createQuery("SELECT p FROM Pantalla p WHERE p.panPanCodigo.panCodigo =" + panCodigo);
            return q.getResultList();
        } catch (Exception e) {
            System.out.println("ERROR SQL PANTALLAS:  " + e.getMessage());
        }
        return null;
    }

    public Pantalla getPantalla(String panCodigo) {

        try {
            Query q = manejador.createNamedQuery("Pantalla.findByPanCodigo");
            q.setParameter("panCodigo", new Integer(panCodigo));
            return (Pantalla) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
}
