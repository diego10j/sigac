/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Tipoasignaturas;
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
public class servicioTipoAsignaturas {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    private Utilitario utilitario = new Utilitario();

    public String guardarTipoAsignaturas(Tipoasignaturas tipoasignaturas) {
        try {
            utx.begin();
            manejador.joinTransaction();
            //nombre tabla y atributo
            if (tipoasignaturas.getTipCodigo() == null) {
                long lon_codigo = utilitario.getConexion().getMaximo("tipoasignaturas", "tip_codigo", 1);
                tipoasignaturas.setTipCodigo(new Integer(String.valueOf(lon_codigo)));
                System.out.println("ide " + lon_codigo);
                tipoasignaturas.setTipCodigo(new Integer(String.valueOf(lon_codigo)));
                manejador.persist(tipoasignaturas);
            } else {
                manejador.merge(tipoasignaturas);
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

    public String elimnarTipoAsignaturas(String tipCodigo) {
        try {
            utx.begin();
            Tipoasignaturas borraTipoasignaturas = manejador.find(Tipoasignaturas.class, new Integer(tipCodigo));
            manejador.remove(borraTipoasignaturas);
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

    public Tipoasignaturas getTipoasignaturas(String tipCodigo) {

        try {
            Query q = manejador.createNamedQuery("Tipoasignaturas.findByTipCodigo");
            q.setParameter("tipCodigo", new Integer(tipCodigo));
            return (Tipoasignaturas) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    public List<Tipoasignaturas> getTipoasignaturas() {
        try {
            Query q = manejador.createNamedQuery("Tipoasignaturas.findAll");
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
    public List getListaTipoAsignatura() {
        return utilitario.getConexion().consultar("select tip_codigo,tip_nombre from tipoasignaturas order by tip_nombre");
    }
}
