package com.dataservicios.ttauditknpos.util;
/**
 * Created by usuario on 11/11/2014.
 */
public final class GlobalConstant {
    public static String dominio = "http://ttaudit.com";
    //public static String dominio = "http://appfiliaibk.com";
    public static final String LOGIN_URL = dominio + "/loginUser";
    public static final String KEY_USERNAME = "username";
    public static String inicio,fin;
    public static  double latitude_open, longitude_open;
    public static  int global_close_audit =0;
    public static int company_id = 54;
    public static int company_id_palmera = 47;
    public static int[] poll_id = new int[]{
            771,    //0	    "Esta Abierto el local?"
            772,    //1	    "Permitio actualizar POS"
            773,    //2	    "Foto Voucher de inicio"
            774,    //3	    "Fotos de POS"
            775,    //4	    "Fotos sticker de POS"
            776,    //5	    "Ingresar modelos de POS"
            777,    //6	    "Ingresar numeros de terminales"
            778,    //7	    "Foto Interior comercio"
            779,    //8	    "Foto de voucher final"
            780,    //9	    "Foto de constancia de instalación"
            781,    //10	"Teléfono y contacto válido?"
    } ;
    public static int[] poll_id_palmera = new int[]{
            630, // 0 Cliente vende Bloqueador en Sachet?"
            631, // 1 Precio de venta del bloqueador"
    } ;

    public static int[] audit_id = new int[]{
            38, // 0 Cliente vende Bloqueador en Sachet?"
    } ;

   // public static String albunName = "AlicorpPhoto";
    //public static String directory_images = "/Pictures/" + albunName;
    public static String directory_images = "/Pictures/" ;
    public static String type_aplication = "android";

    public static final String JPEG_FILE_PREFIX = "_KNPos_";
    public static final String JPEG_FILE_SUFFIX = ".jpg";
}

