/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Alumnos;
import entidades.Usuario;
import java.sql.ResultSet;
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
public class servicioUsuario {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    private Utilitario utilitario = new Utilitario();

    public String guardarUsuario(Usuario usuario) {
        try {
            utx.begin();
            long lon_codigo = utilitario.getConexion().getMaximo("usuario", "usu_codigo", 1);
            usuario.setUsuCodigo(new Integer(String.valueOf(lon_codigo)));
            manejador.joinTransaction();

            manejador.persist(usuario);

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

    public String elimnarUsuario(String aluUsuario) {
        try {
            utx.begin();
            Usuario borraUsuario = manejador.find(Usuario.class, new Integer(aluUsuario));
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

    public Usuario getUsuario(String codUsuario) {
        
        try {
            Query q = manejador.createNamedQuery("Usuario.findByUsuCodigo");
            q.setParameter("usuCodigo", new Integer(codUsuario));
            return (Usuario) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
}
