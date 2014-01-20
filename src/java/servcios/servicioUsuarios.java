/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Cursos;
import entidades.Usuario;
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
public class servicioUsuarios {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    private Utilitario utilitario = new Utilitario();

    public String guardarUsuarios(Usuario usuario) {
        try {
            utx.begin();
            manejador.joinTransaction();
            //nombre tabla y atributo
            if (usuario.getUsuCodigo() == null) {
                long lon_codigo = utilitario.getConexion().getMaximo("Usuario", "cur_codigo", 1);
                usuario.setUsuCodigo(new Integer(String.valueOf(lon_codigo)));
                System.out.println("ide " + lon_codigo);
                usuario.setUsuCodigo(new Integer(String.valueOf(lon_codigo)));
                manejador.persist(usuario);
            } else {
                manejador.merge(usuario);
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

    public String elimnarUsuarios(String usuCodigo) {
        try {
            utx.begin();
            Usuario borraUsuario = manejador.find(Usuario.class, new Integer(usuCodigo));
            manejador.remove(borraUsuario);
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

    public Cursos getUsuarios(String usuCodigo) {

        try {
            Query q = manejador.createNamedQuery("Usuario.findByUsuCodigo");
            q.setParameter("usuCodigo", new Integer(usuCodigo));
            return (Cursos) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    public List<Usuario> getUsuario() {
        try {
            Query q = manejador.createNamedQuery("Usuario.findAll");
            return q.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
}
