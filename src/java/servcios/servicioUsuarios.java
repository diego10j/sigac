/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import entidades.Cursos;
import entidades.Usuario;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Encriptar;
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
                long lon_codigo = utilitario.getConexion().getMaximo("Usuario", "usu_codigo", 1);
                usuario.setUsuCodigo(new Integer(String.valueOf(lon_codigo)));
                usuario.setUsuActivo(true);
                Encriptar encriptar = new Encriptar();
                usuario.setUsuClave(encriptar.getEncriptar(usuario.getUsuClave()));
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

    public int actualizarUsuarios() {
        //Inserta los alumnos y docentes que no existan en la tabla de usuarios
        TablaGenerica tab_alumno = utilitario.consultar("SELECT * FROM alumnos WHERE alu_codigo not in (select alu_codigo from usuario where alu_codigo is not null)");
        //inserto 
        TablaGenerica tab_usuario = new TablaGenerica();
        tab_usuario.setTabla("usuario", "usu_codigo", -1);
        tab_usuario.setCondicion("usu_codigo=-1");
        tab_usuario.ejecutarSql();
        Encriptar encriptar = new Encriptar();
        for (int i = 0; i < tab_alumno.getTotalFilas(); i++) {
            String str_cedula = tab_alumno.getValor(i, "alu_cedula");
            if (str_cedula == null || str_cedula.isEmpty()) {
                continue;
            } else {
                str_cedula = str_cedula.replace("'", "");
            }
            tab_usuario.insertar();
            tab_usuario.setValor("ins_codigo", "0");
            tab_usuario.setValor("rol_codigo", "3");//Alumno
            tab_usuario.setValor("usu_nombre", tab_alumno.getValor(i, "alu_nombres") + " " + tab_alumno.getValor(i, "alu_apellidos"));
            tab_usuario.setValor("usu_nick", str_cedula);
            tab_usuario.setValor("usu_clave", encriptar.getEncriptar(str_cedula));//ecripta clave
            tab_usuario.setValor("usu_fechacreacion", utilitario.getFechaActual());
            tab_usuario.setValor("usu_activo", "true");
            tab_usuario.setValor("alu_codigo", tab_alumno.getValor(i, "alu_codigo"));
        }

        //Importa docentes
        TablaGenerica tab_docente = utilitario.consultar("SELECT * FROM docentes WHERE doc_codigo not in (select doc_codigo from usuario where doc_codigo is not null)");

        for (int i = 0; i < tab_docente.getTotalFilas(); i++) {
            String str_cedula = tab_docente.getValor(i, "doc_cedula");
            if (str_cedula == null || str_cedula.isEmpty()) {
                continue;
            } else {
                str_cedula = str_cedula.replace("'", "");
            }
            tab_usuario.insertar();
            tab_usuario.setValor("ins_codigo", "0");
            tab_usuario.setValor("rol_codigo", "6");//Docente
            tab_usuario.setValor("usu_nombre", tab_docente.getValor(i, "doc_nombres"));
            tab_usuario.setValor("usu_nick", str_cedula);
            tab_usuario.setValor("usu_clave", encriptar.getEncriptar(str_cedula));//clave encriptada
            tab_usuario.setValor("usu_fechacreacion", utilitario.getFechaActual());
            tab_usuario.setValor("usu_activo", "true");
            tab_usuario.setValor("doc_codigo", tab_docente.getValor(i, "doc_codigo"));
        }
        int int_num = tab_usuario.getTotalFilas();
        tab_usuario.guardar();
        if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
            return int_num;
        } else {
            return -1;
        }
    }

    public String activarDesactivarUsuario(Usuario usuario) {
        String str_msj = "";

        if (usuario.getUsuActivo() == null || usuario.getUsuActivo().equals(false)) {
            //activa
            str_msj = "Se activo el usuario " + usuario.getUsuNombre();
            usuario.setUsuActivo(true);
            guardarUsuarios(usuario);
        } else {
            //desactiva 
            str_msj = "Se desactivo el usuario " + usuario.getUsuNombre();
            usuario.setUsuActivo(false);
            guardarUsuarios(usuario);
        }

        return str_msj;
    }
    
    public void resetearClave(Usuario usuario) {
        Encriptar encriptar= new Encriptar();
        usuario.setUsuClave(encriptar.getEncriptar(usuario.getUsuNick()));
        guardarUsuarios(usuario);
    }
}
