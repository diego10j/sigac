/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion;

import framework.aplicacion.Framework;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.Constants;

/**
 *
 * @author Diego
 */
public class Utilitario extends Framework {

    public boolean validarRUC(String str_ruc) {
        boolean boo_correcto = false;
        try {
            if (str_ruc.length() == 13) {
                int[] int_digitos = new int[10];
                int int_coeficiente = 10;
                String str_valida = str_ruc.substring(10, 13);

                if (str_valida.equals("001")) {
                    for (int i = 0; i < int_digitos.length; i++) {
                        int_digitos[i] = Integer.parseInt(str_ruc.charAt(i) + "");
                    }
                    int int_digito_verifica = int_digitos[9];
                    int[] int_multiplica = {2, 1, 2, 1, 2, 1, 2, 1, 2};
                    if (int_digitos[2] == 9) {
                        int[] int_multiplica9 = {4, 3, 2, 7, 6, 5, 4, 3, 2};
                        int_multiplica = int_multiplica9;
                        int_coeficiente = 11;
                    }
                    if (int_digitos[2] == 6) {
                        int[] aint_multiplica6 = {3, 2, 7, 6, 5, 4, 3, 2};
                        int_digito_verifica = int_digitos[8];
                        int_multiplica = aint_multiplica6;
                        int_coeficiente = 11;
                    }
                    int int_suma = 0;
                    for (int i = 0; i < (int_digitos.length - 1); i++) {
                        try {
                            if (int_coeficiente == 10) {
                                int mul = int_digitos[i] * int_multiplica[i];
                                if (mul > 9) {
                                    String aux = mul + "";
                                    mul = Integer.parseInt(aux.charAt(0) + "") + Integer.parseInt(aux.charAt(1) + "");
                                }
                                int_suma += mul;
                            } else {
                                int_suma += (int_digitos[i] * int_multiplica[i]);
                            }
                        } catch (Exception ex) {
                        }
                    }
                    int int_valida = 0;
                    if (int_coeficiente == 10) {
                        if (int_suma % 10 == 0) {
                            int_valida = 0;
                        } else {
                            int_valida = 10 - (int_suma % 10);
                        }
                    } else {
                        if (int_suma % 11 == 0) {
                            int_valida = 0;
                        } else {
                            int_valida = 11 - (int_suma % 11);
                        }
                    }

                    if (int_valida == 0) {
                        int_digito_verifica = 0;
                    }
                    if (int_valida == int_digito_verifica) {
                        boo_correcto = true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return boo_correcto;
    }

    public boolean validarCedula(String str_cedula) {
        boolean boo_correcto = false;
        try {
            if (str_cedula.length() == 10) {

                if (!str_cedula.equals("2222222222")) {
                    int lint_suma = 0;

                    for (int i = 0; i < 9; i++) {
                        int lstr_digito = Integer.parseInt(str_cedula.charAt(i) + "");
                        if (i % 2 == 0) {
                            lstr_digito = lstr_digito * 2;
                            if (lstr_digito > 9) {
                                String lstr_aux = lstr_digito + "";
                                lstr_digito = Integer.parseInt(lstr_aux.charAt(0) + "") + Integer.parseInt(lstr_aux.charAt(1) + "");
                            }
                        }
                        lint_suma += lstr_digito;
                    }
                    if (str_cedula.charAt(9) != '0') {
                        String lstr_aux = lint_suma + "";
                        int lint_superior = (Integer.parseInt(lstr_aux.charAt(0) + "") + 1) * 10;
                        int lint_ultimo_real = lint_superior - lint_suma;
                        int lint_ultimo_digito = Integer.parseInt(str_cedula.charAt(9) + "");
                        if (lint_ultimo_digito == lint_ultimo_real) {
                            boo_correcto = true;
                        }
                    } else {
                        //Para cedulas que terminan en 0
                        if (lint_suma % 10 == 0) {
                            boo_correcto = true;
                        }
                    }

                } else {
                    boo_correcto = false;
                }

            }
        } catch (Exception ex) {
        }
        return boo_correcto;
    }

    public int getDiferenciasDeFechas(Date fechaInicial, Date fechaFinal) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_FECHA);
        String fechaInicioString = formatoFecha.format(fechaInicial);
        try {
            fechaInicial = formatoFecha.parse(fechaInicioString);
        } catch (ParseException ex) {
        }

        String fechaFinalString = formatoFecha.format(fechaFinal);
        try {
            fechaFinal = formatoFecha.parse(fechaFinalString);
        } catch (ParseException ex) {
        }
        long fechaInicialMs = fechaInicial.getTime();
        long fechaFinalMs = fechaFinal.getTime();
        long diferencia = fechaFinalMs - fechaInicialMs;
        double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
        return ((int) dias);
    }

    public int getNumeroDiasSemana(Date fecha) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fecha.getTime());
        //Considera que lunes es el dia 1
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public Date sumarDiasFecha(Date fch, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, dias);
        return new Date(cal.getTimeInMillis());
    }

    public boolean isEmailValido(String email) {
        Pattern pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
        Matcher mat = pat.matcher(email);
        if (mat.find()) {
            return true;
        } else {
            return false;
        }
    }

    public String getFormatoNumero(Object numero, int numero_decimales) {
        String lstr_formato = "#";
        for (int i = 0; i < numero_decimales; i++) {
            if (i == 0) {
                lstr_formato += ".";
            }
            lstr_formato += "#";
        }
        DecimalFormat formatoNumero;
        DecimalFormatSymbols idfs_simbolos = new DecimalFormatSymbols();
        idfs_simbolos.setDecimalSeparator('.');
        formatoNumero = new DecimalFormat(lstr_formato, idfs_simbolos);
        try {
            double ldob_valor = Double.parseDouble(numero.toString());
            return formatoNumero.format(ldob_valor);
        } catch (Exception ex) {
            return null;
        }
    }

    public Date getDate() {
        return new Date();
    }

    public double evaluarExpresion(String expresion) {
        //Resuleve el valor de una expresion Ejemplo: 5+3-3
        double resultado = 0;
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Object operacion;
        try {
            expresion = expresion.replace("[", "(");
            expresion = expresion.replace("]", ")");
            operacion = engine.eval(expresion);
            resultado = Double.parseDouble(operacion.toString());
        } catch (ScriptException e) {
            System.out.println("ERROR al evaluarExpresion( " + expresion + " )  :" + e.toString());
        }
        return resultado;
    }

    public String getLetrasNumero(Object numero) {
        String letras = getFormatoNumero(numero);
        if (letras != null) {
            try {
                letras = recursivoNumeroLetras(Integer.parseInt(letras.substring(0, letras.lastIndexOf(".")))) + " CON" + recursivoNumeroLetras(Integer.parseInt(letras.substring((letras.lastIndexOf(".") + 1), letras.length())));
                letras = letras.toUpperCase();
                letras = letras.trim();
            } catch (Exception e) {
            }
        }
        return letras;
    }

    private String recursivoNumeroLetras(int numero) {
        String cadena = new String();
        // Aqui identifico si lleva millones
        if ((numero / 1000000) > 0) {
            if ((numero / 1000000) == 1) {
                cadena = " Un Millon" + recursivoNumeroLetras(numero % 1000000);
            } else {
                cadena = recursivoNumeroLetras(numero / 1000000) + " Millones" + recursivoNumeroLetras(numero % 1000000);
            }
        } else {
            // Aqui identifico si lleva Miles
            if ((numero / 1000) > 0) {

                if ((numero / 1000) == 1) {
                    cadena = " Mil" + recursivoNumeroLetras(numero % 1000);
                } else {
                    cadena = recursivoNumeroLetras(numero / 1000) + " Mil" + recursivoNumeroLetras(numero % 1000);
                }
            } else {
                // Aqui identifico si lleva cientos
                if ((numero / 100) > 0) {
                    if ((numero / 100) == 1) {
                        if ((numero % 100) == 0) {
                            cadena = " Cien";
                        } else {
                            cadena = " Ciento" + recursivoNumeroLetras(numero % 100);
                        }
                    } else {
                        if ((numero / 100) == 5) {
                            cadena = " Quinientos" + recursivoNumeroLetras(numero % 100);
                        } else {
                            if ((numero / 100) == 9) {
                                cadena = " Novecientos" + recursivoNumeroLetras(numero % 100);
                            } else {
                                cadena = recursivoNumeroLetras(numero / 100) + "cientos" + recursivoNumeroLetras(numero % 100);
                            }
                        }
                    }
                } // Aqui se identifican las Decenas
                else {
                    if ((numero / 10) > 0) {
                        switch (numero / 10) {
                            case 1:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Diez";
                                        break;
                                    case 1:
                                        cadena = " Once";
                                        break;
                                    case 2:
                                        cadena = " Doce";
                                        break;
                                    case 3:
                                        cadena = " Trece";
                                        break;
                                    case 4:
                                        cadena = " Catorce";
                                        break;
                                    case 5:
                                        cadena = " Quince";
                                        break;
                                    default:
                                        cadena = " Diez y " + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 2:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Veinte";
                                        break;
                                    default:
                                        cadena = " Veinti" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 3:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Treinta";
                                        break;
                                    default:
                                        cadena = " Treinta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 4:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Cuarenta";
                                        break;
                                    default:
                                        cadena = " Cuarenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 5:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Cincuenta";
                                        break;
                                    default:
                                        cadena = " Cincuenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 6:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Sesenta";
                                        break;
                                    default:
                                        cadena = " Sesenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 7:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Setenta";
                                        break;
                                    default:
                                        cadena = " Setenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 8:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Ochenta";
                                        break;
                                    default:
                                        cadena = " Ochenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 9:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Noventa";
                                        break;
                                    default:
                                        cadena = " Noventa y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                        }
                    } else {
                        switch (numero) {
                            case 1:
                                cadena = " Uno";
                                break;
                            case 2:
                                cadena = " Dos";
                                break;
                            case 3:
                                cadena = " Tres";
                                break;
                            case 4:
                                cadena = " Cuatro";
                                break;
                            case 5:
                                cadena = " Cinco";
                                break;
                            case 6:
                                cadena = " Seis";
                                break;
                            case 7:
                                cadena = " Siete";
                                break;
                            case 8:
                                cadena = " Ocho";
                                break;
                            case 9:
                                cadena = " Nueve";
                                break;
                            case 0:
                                //      cadena = " Cero";
                                break;
                        }
                    }
                }
            }
        }
        return cadena;
    }

    public void crearArchivo(String path) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        StreamedContent content;
        InputStream stream = null;
        try {
            if (path.startsWith("/")) {
                stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(path);
            } else {
                stream = new FileInputStream(path);
            }
        } catch (Exception e) {
            crearError("No se puede generar el archivo path: " + path, "crearArchivo()", e);
        }
        if (stream == null) {
            return;
        }
        content = new DefaultStreamedContent(stream);
        if (content == null) {
            return;
        }
        ExternalContext externalContext = facesContext.getExternalContext();
        String contentDispositionValue = "attachment";
        try {
            externalContext.setResponseContentType(content.getContentType());
            externalContext.setResponseHeader("Content-Disposition", contentDispositionValue + ";filename=\"" + path.substring(path.lastIndexOf("/") + 1) + "\"");
            externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE, "true", new HashMap<String, Object>());
            byte[] buffer = new byte[2048];
            int length;
            InputStream inputStream = content.getStream();
            OutputStream outputStream = externalContext.getResponseOutputStream();
            while ((length = (inputStream.read(buffer))) != -1) {
                outputStream.write(buffer, 0, length);
            }
            externalContext.setResponseStatus(200);
            externalContext.responseFlushBuffer();
            content.getStream().close();
            facesContext.getApplication().getStateManager().saveView(facesContext);
            facesContext.responseComplete();
        } catch (Exception e) {
            crearError("No se puede descargar :  path: " + path, "crearArchivo()", e);
        }
    }

    public String generarComillaSimple(String cadena) {
        String str_cadena = "";
        String[] vec = cadena.split(",");
        for (int i = 0; i < vec.length; i++) {
            if (!str_cadena.isEmpty()) {
                str_cadena += ",";
            }
            str_cadena += "'" + vec[i] + "'";
        }
        return str_cadena;
    }

    public String getNombreProyecto() {
        ExternalContext iecx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) iecx.getRequest();
        String contexto = request.getContextPath() + "";
        contexto = contexto.replace("/", "");
        contexto = contexto.trim();
        return contexto;
    }

    public String getIdSession() {
        String str_id = null;
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
            str_id = session.getId();
        } catch (Exception e) {
        }
        return str_id;
    }

    public int getEdad(String fecha) {
        Calendar fechaNacimiento = Calendar.getInstance();
        Calendar fechaActual = Calendar.getInstance();
        fechaNacimiento.setTime(getFecha(fecha));
        int anios = fechaActual.get(Calendar.YEAR) - fechaNacimiento.get(Calendar.YEAR);
        int mes = fechaActual.get(Calendar.MONTH) - fechaNacimiento.get(Calendar.MONTH);
        int dia = fechaActual.get(Calendar.DATE) - fechaNacimiento.get(Calendar.DATE);
        if (mes < 0 || (mes == 0 && dia < 0)) {
            anios--;
        }
        return anios;
    }

    public String getNombreMes(int numero) {
        String meses[] = {"", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
        return meses[numero];
    }

    public double getDiferenciaHoras(Date fechaInicio, Date fechaFin) {
        double tiempoInicial = fechaInicio.getTime();
        double tiempoFinal = fechaFin.getTime();
        double dou_resta = tiempoFinal - tiempoInicial;
        //el metodo getTime te devuelve en mili segundos para saberlo en mins debes hacer
        dou_resta = dou_resta / (1000 * 3600);
        return dou_resta;
    }

    public double getDiferenciaMinutos(Date fechaInicio, Date fechaFin) {
        double tiempoInicial = fechaInicio.getTime();
        double tiempoFinal = fechaFin.getTime();
        double dou_resta = tiempoFinal - tiempoInicial;
        dou_resta = dou_resta / (1000 * 60);
        return dou_resta;
    }

    public boolean isFechasValidas(String fechaInicial, String fechaFinal) {

        if ((fechaInicial != null && isFechaValida(fechaInicial)) && (fechaFinal != null && isFechaValida(fechaFinal))) {
            //comparo que fecha2 es mayor a fecha1
            if (isFechaMayor(getFecha(fechaFinal), getFecha(fechaInicial)) || getFecha(fechaInicial).equals(getFecha(fechaFinal))) {
                return true;
            }
        }

        return false;
    }

    public String getFechaLarga(String fecha) {
        SimpleDateFormat formateador = new SimpleDateFormat(
                "EEEE d 'de' MMMM 'del' yyyy");
        String str_fecha = formateador.format(getFecha(fecha));
        return str_fecha;
    }

    public Date getHoraCalendario(String hora) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date date;
        try {
            date = formatter.parse(hora);
            cal.setTime(date);
        } catch (Exception e) {
            try {
                cal.setTime(getHora(hora));
            } catch (Exception e1) {
            }
        }
        cal.set(Calendar.AM_PM, Calendar.PM);
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.get(Calendar.SECOND));
        return cal.getTime();
    }

    public Object evaluarExpresionJavaScript(String expresion) {
        //Resuleve el valor de una expresion Ejemplo: 5+3-3

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Object operacion;
        try {
            expresion = expresion.replace("[", "(");
            expresion = expresion.replace("]", ")");
            operacion = engine.eval(expresion);
            
            return operacion;
        } catch (ScriptException e) {
            System.out.println("ERROR al evaluarExpresion( " + expresion + " )  :" + e.toString());
        }
        return null;
    }

      public String getURLCompleto() {
        ExternalContext iecx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) iecx.getRequest();
        String path = request.getRequestURL() + "";       
        return path;
    }
    
    public static void main(String args[]) {
        Utilitario u = new Utilitario();
        System.out.println(u.evaluarExpresionJavaScript("if(7>=7 && 7<=8){'Alcanza'}"));
        System.out.println(u.evaluarExpresionJavaScript("if(7>=5 && 7<=6){'Proximo'}"));
        System.out.println(u.evaluarExpresionJavaScript("if(7<=4){'No Alcanza'}"));
        System.out.println(u.evaluarExpresionJavaScript("if(7==10){'Supera'}"));
        System.out.println(u.evaluarExpresionJavaScript("if(7>=9 && 7<10){'Domina'}"));       
        
    }
}
