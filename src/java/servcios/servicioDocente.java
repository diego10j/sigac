/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Docentes;
import entidades.Usuario;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
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
public class servicioDocente {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    private Utilitario utilitario = new Utilitario();
    @EJB
    private servicioUsuarios servUsuario;
    @EJB
    private servicioInstitucion servInstitucion;
    @EJB
    private servicioRoles servRoles;

    public String guardarDocente(Docentes docente) {
        try {
            utx.begin();
            manejador.joinTransaction();
            //nombre tabla y atributo
            if (docente.getDocCodigo() == null) {
                long lon_codigo = utilitario.getConexion().getMaximo("docentes", "doc_codigo", 1);
                System.out.println("ide " + lon_codigo);
                docente.setDocCodigo(new Integer(String.valueOf(lon_codigo)));
                manejador.persist(docente);
                //guardo Usuario
                Usuario user = new Usuario();
                user.setUsuNombre(docente.getDocNombres());
                user.setUsuNick(docente.getDocCedula());
                user.setUsuClave(docente.getDocCedula());
                user.setRolCodigo(servRoles.getRoles("6"));
                user.setInsCodigo(servInstitucion.getIntitucion());
                servUsuario.guardarUsuarios(user);

            } else {
                manejador.merge(docente);
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

    public String elimnarDocente(String docCodigo) {
        try {
            utx.begin();
            Docentes borraDocente = manejador.find(Docentes.class, new Integer(docCodigo));
            manejador.remove(borraDocente);
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

    public Docentes getDocente(String docCodigo) {

        try {
            Query q = manejador.createNamedQuery("Docentes.findByDocCodigo");
            q.setParameter("docCodigo", new Integer(docCodigo));
            return (Docentes) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    public List<Docentes> getDocentes() {
        try {
            Query q = manejador.createNamedQuery("Docentes.findAll");
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
    public List getListaDocentes() {
        return utilitario.getConexion().consultar("select doc_codigo,doc_nombres,doc_cedula from docentes order by doc_nombres");
    }
}
