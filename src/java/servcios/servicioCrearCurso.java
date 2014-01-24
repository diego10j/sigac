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
    public String guardarCrearCurso(CrearCurso crearcurso) {
        try {
            utx.begin();
            manejador.joinTransaction();
            //nombre tabla y atributo
            if (crearcurso.getCreCodigo() == null) {
                long lon_codigo = utilitario.getConexion().getMaximo("crear_curso", "cre_codigo", 1);
                crearcurso.setCreCodigo(new Integer(String.valueOf(lon_codigo)));
                manejador.persist(crearcurso);
            } else {
                manejador.merge(crearcurso);
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

    public String elimnarCrearCurso(String creCodigo) {
        try {
            utx.begin();
            CrearCurso borraCrearCurso = manejador.find(CrearCurso.class, new Integer(creCodigo));
            manejador.remove(borraCrearCurso);
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

    public CrearCurso getCrearCurso(String creCodigo) {

        try {
            Query q = manejador.createNamedQuery("CrearCurso.findByCreCodigo");
            q.setParameter("creCodigo", new Integer(creCodigo));
            return (CrearCurso) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    public List<CrearCurso> getCursosCreados(String per_codigo) {
        if (per_codigo != null) {
            try {
                Query q = manejador.createQuery("SELECT c FROM CrearCurso c WHERE c.perCodigo.perCodigo=" + per_codigo + " order by c.curCodigo.curCodigo,c.parCodigo.parCodigo");
                return q.getResultList();
            } catch (Exception e) {
            }
        }

        return null;
    }
}
