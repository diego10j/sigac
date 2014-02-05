/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Cursos;
import entidades.Asignaturas;
import entidades.EquivalenciaAprovechamiento;
import entidades.Permisos;
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
    private Utilitario utilitario = new Utilitario();

    public String guardarAsignaturas(Asignaturas asignatura) {
        try {
            utx.begin();
            manejador.joinTransaction();
            //nombre tabla y atributo
            if (asignatura.getAsiCodigo() == null || asignatura.getAsiCodigo().toString().isEmpty()) {
                long lon_codigo = utilitario.getConexion().getMaximo("Asignaturas", "asi_codigo", 1);
                asignatura.setAsiCodigo(new Integer(String.valueOf(lon_codigo)));
                System.out.println("ide " + lon_codigo);
                asignatura.setAsiCodigo(new Integer(String.valueOf(lon_codigo)));
                manejador.persist(asignatura);
            } else {
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

    /**
     * Lista para combos
     *
     * @return
     */
    public List getListaAsignaturas() {
        return utilitario.getConexion().consultar("select asi_codigo,asi_nombre ||' - '|| tip_nombre as nombre from asignaturas mat\n"
                + "left JOIN tipoasignaturas tip on mat.tip_codigo=tip.tip_codigo WHERE asi_asi_codigo is not null");
    }

    public List<Asignaturas> getAsignaturasPadre() {
        try {
            Query q = manejador.createQuery("SELECT a FROM Asignaturas a WHERE a.asiAsiCodigo is null");
            return q.getResultList();
        } catch (Exception e) {
            System.out.println("ERROR SQL ASIGNATURAS:  " + e.getMessage());
        }
        return null;
    }

    public List<Asignaturas> getAsignaturasHijas(String asiCodigo) {
        try {
            Query q = manejador.createQuery("SELECT a FROM Asignaturas a WHERE a.asiAsiCodigo=" + asiCodigo);
            return q.getResultList();
        } catch (Exception e) {
            System.out.println("ERROR SQL ASIGNATURAS Hijas:  " + e.getMessage());
        }
        return null;
    }
}
