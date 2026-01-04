/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.util;

/**
 *
 * @author yhon
 */
import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {

    private static ResourceBundle bundle;

    public static void setLanguage(String lang) {
        Locale locale;
        if ("en".equals(lang)) {
            locale = new Locale("en");
        } else {
            locale = new Locale("es");
        }
        bundle = ResourceBundle.getBundle("messages", locale);
    }

    public static String t(String key) {
        return bundle.getString(key);
    }
}