/**
 *
 * @author Diego Fernando Jácome
 */
$(document).bind("touchmove", function(event) {
    event.preventDefault();
});

/*
 * Función que permite bloquear la pantalla mientras esta procesando
 */
$(function() {
    $(document).ajaxStart($.blockUI).ajaxStop($.unblockUI);
});


/*
 * Función que bloquea la tecla F5 y Enter de la pagina y  detectar tecla arriba y abajo
 */
$(function() {
    $(document).keydown(function(e) {
        if ($('#formulario\\:menu').length) {
            var code = (e.keyCode ? e.keyCode : e.which);
            //     if(code == 116 || code == 13) {
            //         e.preventDefault();               
            //     }        
            if (code == 40) {
                teclaAbajo();
                e.preventDefault();
            }
            if (code == 38) {
                teclaArriba();
                e.preventDefault();
            }
        }
    });
});

/*
 *Función Desabilitar el clik derecho 
 */
//$(function() {
//    $(document).ready(function(){
//        $(document).bind("contextmenu",function(e){
//            return false;
//        });
//    });
//});


/*
 * Funcion que calcula y asigna el ancho y el alto del navegador 
 */
function dimiensionesNavegador() {
    var na = $(window).height();
    document.getElementById('formulario:alto').value = na;
    var ancho = $(window).width();
    document.getElementById('formulario:ancho').value = ancho;
}

/*
 * Funcion que calcula y asigna el ancho y el alto disponible sin la barra de menu
 */
function dimensionesDisponibles() {
    var na = $(window).height();
    var me = $('#formulario\\:menu').height();
    var alto = na - me - 45;
    alto = parseInt(alto);
    document.getElementById('formulario:ith_alto').value = alto;
    document.getElementById('formulario:alto').value = na;
    var ancho = $(window).width();
    document.getElementById('formulario:ancho').value = ancho;
}




/*
 * Para poner los calendarios, las horas en español
 */
PrimeFaces.locales['es'] = {
    closeText: 'Cerrar',
    prevText: 'Anterior',
    nextText: 'Siguiente',
    monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
    monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
    dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
    dayNamesShort: ['Dom', 'Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab'],
    dayNamesMin: ['D', 'L', 'M', 'M', 'J', 'V', 'S'],
    weekHeader: 'Semana',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: '',
    timeOnlyTitle: 'Sólo hora',
    timeText: 'Tiempo',
    hourText: 'Hora',
    minuteText: 'Minuto',
    secondText: 'Segundo',
    currentText: 'Hora actual',
    ampm: false,
    month: 'Mes',
    week: 'Semana',
    day: 'Día',
    allDayText: 'Todo el día'
};

function abrirPopUp(dir) {
    var w = window.open(dir, "sistemadj", "width=" + screen.availWidth + ", height=" + screen.availHeight + ", screenX=0,screenY=0, top=0, left=0, status=0 , resizable=yes, scrollbars=yes");
    w.focus();
}
function abrirNuevoPopUp(dir) {
    var w = window.open(dir, "", "width=" + screen.availWidth + ", height=" + screen.availHeight + ", screenX=0,screenY=0, top=0, left=0, status=0 , resizable=yes, scrollbars=yes");
    w.focus();
}



function fillthescreen() {
    winH = windowHeight(); //This returns the screen heigth
    heightNeeded = winH - 90; //We need to substract the footer height
    if (typeof(window.innerWidth) != 'number') { //Explorer doesn't recognize minHeight
        document.getElementById('col1').style.height = heightNeeded + 'px'; //So, we use height (and explroer bug)
    }
    document.getElementById('col1').style.minHeight = heightNeeded + 'px'; //For every other browser, we use minHeight
}

function windowHeight() {
    var alto = 0;
    if (typeof(window.innerWidth) == 'number') {
        alto = window.innerHeight;
    } else if (document.documentElement && (document.documentElement.clientWidth || document.documentElement.clientHeight)) {
        alto = document.documentElement.clientHeight;
    } else if (document.body && (document.body.clientWidth || document.body.clientHeight)) {
        alto = document.body.clientHeight;
    }
    return alto;
}


var nav4 = window.Event ? true : false;
function IsNumber(evt) {
// Backspace = 8, Enter = 13, ’0′ = 48, ’9′ = 57, ‘.’ = 46
    var key = nav4 ? evt.which : evt.keyCode;
    return (key <= 13 || (key >= 48 && key <= 57) || key == 46);
}




