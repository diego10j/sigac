/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servcios;

import aplicacion.Utilitario;
import framework.aplicacion.TablaGenerica;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author Diego
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class servicioQuimestre {

    @PersistenceContext
    private EntityManager manejador;
    @Resource
    private UserTransaction utx;
    private Utilitario utilitario = new Utilitario();

    public void actualizarQuimestre(String dis_codigo, String for_codigo) {
        //Calcula la nota 80& de los parciales
        TablaGenerica tab_imforme = new TablaGenerica();
        tab_imforme.setTabla("informe_quimestre", "inf_codigo", -1);
        tab_imforme.setCondicion("for_codigo=" + for_codigo + " AND dis_codigo=" + dis_codigo);
        tab_imforme.ejecutarSql();

        if (tab_imforme.isEmpty()) {
            //No existen registros 
            //Simplemente calculo la nota sobre 80 de todos los parciales e inserto en la tabla de informe
            TablaGenerica tab_suma = utilitario.consultar("select mat_codigo,sum(not_primerparcial) as not80,dis_codigo from nota_destrezaparcial\n"
                    + "where for_codigo= " + for_codigo
                    + " AND dis_codigo=" + dis_codigo
                    + " group by mat_codigo,dis_codigo\n"
                    + "order by mat_codigo,dis_codigo");
            if (tab_suma.isEmpty() == false) {
                for (int i = 0; i < tab_suma.getTotalFilas(); i++) {
                    tab_imforme.insertar();
                    tab_imforme.setValor("for_codigo", for_codigo);
                    tab_imforme.setValor("dis_codigo", dis_codigo);
                    tab_imforme.setValor("mat_codigo", tab_suma.getValor(i, "mat_codigo"));

                    double dou_suma = 0;
                    try {
                        dou_suma = Double.parseDouble(tab_suma.getValor(i, "not80"));
                    } catch (Exception e) {
                    }

                    double dou_eqv80 = (dou_suma * 80) / 300;

                    tab_imforme.setValor("inf_sumatoria", utilitario.getFormatoNumero(dou_suma));
                    tab_imforme.setValor("inf_eqv80", utilitario.getFormatoNumero(dou_eqv80));
                    tab_imforme.setValor("inf_exa20", "0.00");
                    tab_imforme.setValor("inf_nota", utilitario.getFormatoNumero(dou_eqv80));
                }
            }
        } else {
            //Toca modificar la nota 80% y si tienen pasada la nota examen 20 calcular el nuevo total
            for (int i = 0; i < tab_imforme.getTotalFilas(); i++) {
                //Calculo la suma 80 de cada estudiante
                TablaGenerica tab_suma = utilitario.consultar("select mat_codigo,sum(not_primerparcial) as not80,dis_codigo"
                        + " from nota_destrezaparcial\n"
                        + "where for_codigo= " + for_codigo
                        + " AND dis_codigo=" + dis_codigo
                        + " and mat_codigo=" + tab_imforme.getValor(i, "mat_codigo")
                        + " group by mat_codigo,dis_codigo\n"
                        + "order by mat_codigo,dis_codigo");

                if (tab_suma.isEmpty() == false) {
                    //actualiza 
                    tab_imforme.modificar(i);
                    double dou_suma = 0;
                    try {
                        dou_suma = Double.parseDouble(tab_suma.getValor("not80"));
                    } catch (Exception e) {
                    }

                    double dou_eqv80 = (dou_suma * 80) / 300;

                    tab_imforme.setValor("inf_sumatoria", utilitario.getFormatoNumero(dou_suma));
                    tab_imforme.setValor("inf_eqv80", utilitario.getFormatoNumero(dou_eqv80));
                    double dou_examen = 0;
                    try {
                        dou_examen = Double.parseDouble(tab_imforme.getValor(i, "inf_examen"));
                    } catch (Exception e) {
                    }
                    double dou20 = (dou_examen * 2) / 10;

                    tab_imforme.setValor("inf_nota", utilitario.getFormatoNumero((dou_eqv80 + dou20)));
                }
            }
        }
        tab_imforme.guardar();
        utilitario.getConexion().ejecutarListaSql();
    }

    public List getListaInformeQuimestre(String dis_codigo, String for_codigo) {        
        actualizarQuimestre(dis_codigo, for_codigo);
        return utilitario.getConexion().consultar("SELECT a.inf_codigo,alu_apellidos,alu_nombres,inf_eqv80,inf_exa20,inf_examen,inf_nota,inf_sumatoria FROM informe_quimestre a "
                + "inner join matricula b on a.mat_codigo =b.mat_codigo\n"
                + "inner join alumnos c on b.alu_codigo=c.alu_codigo\n"
                + " WHERE a.for_codigo=" + for_codigo + " AND a.dis_codigo=" + dis_codigo + " order by alu_apellidos,alu_nombres");
    }

    public String guardarInformeQuimestre(List notas) {
        for (Object actual : notas) {
            Object[] fila = (Object[]) actual;
            utilitario.getConexion().agregarSql("UPDATE informe_quimestre set inf_exa20=" + fila[4] + ", inf_examen=" + fila[5] + ",inf_nota=" + fila[6] + " where inf_codigo=" + fila[0]);

        }
        return utilitario.getConexion().ejecutarListaSql();
    }
}
