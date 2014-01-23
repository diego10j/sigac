/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.CrearCurso;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author Diego
 */
@Stateless
public class servicioCrearCurso {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    private Utilitario utilitario = new Utilitario();

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    /**
     * Retorna Los cursos de un periodo
     *
     * @param per_codigo
     * @return
     */
    public List<CrearCurso> getCursosCreados(String per_codigo) {
        try {
            Query q = manejador.createQuery("SELECT c FROM CrearCurso c WHERE c.perCodigo.perCodigo=" + per_codigo);
            return q.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    public String guardarcrearCurso(CrearCurso curso) {
        try {
            utx.begin();
            manejador.joinTransaction();
            //nombre tabla y atributo
            if (curso.getCurCodigo() == null) {
                long lon_codigo = utilitario.getConexion().getMaximo("crear_curso", "cre_codigo", 1);
                curso.setCreCodigo(new Integer(String.valueOf(lon_codigo)));
                manejador.persist(curso);
            } else {
                manejador.merge(curso);
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
}
