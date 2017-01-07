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
    public static int[] poll_id = new int[]{
          756,	// 0 // Se encuentra Abierto el punto?  617, // 0 Se encuentra Abierto el punto?"
          757,	// 1 // ¿Cliente permitió tomar información?  618, // 1 ¿Cliente permitió tomar información?"
          758,	// 2 // ¿Ventana esta Trabajada? (Tiene fronterizador arriba y abajo)  619, // 2 ¿Ventana esta Trabajada? (Tiene fronterizador arriba y abajo)"
          759,	// 3 // ¿Existe Ventana?  620, // 3 ¿Existe Ventana?"
          760,	// 4 // ¿ La Ventana es visible ?  621, // 4 ¿ La Ventana es visible ?"
          761,	// 5 // ¿ Como se encuentra la Ventana ?  622, // 5 ¿ Como se encuentra la Ventana ?"
          762,	// 6 // ¿ Se encontro exhibidor ?  623, // 6 ¿ Se encontro exhibidor ?"
          763,	// 7 // ¿ Es cliente perfecto ?  624, // 7 ¿ Es cliente perfecto ?"
          764,	// 8 // ¿Desde cuando es cliente perfecto?  625, // 8 ¿Desde cuando es cliente perfecto?"
          765,	// 9 // ¿Ha sido premiado por ser cliente perfecto?  626, // 9 ¿Ha sido premiado por ser cliente perfecto?"
    } ;


   // public static String albunName = "AlicorpPhoto";
    //public static String directory_images = "/Pictures/" + albunName;
    public static String directory_images = "/Pictures/" ;
    public static String type_aplication = "android";
}

