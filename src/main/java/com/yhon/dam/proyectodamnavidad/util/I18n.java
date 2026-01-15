/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.util;

import com.yhon.dam.proyectodamnavidad.modelo.Rol;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Utilidad para la internacionalización (i18n) de la aplicación.
 * <p>
 * Esta clase centraliza la gestión de textos traducidos mediante
 * archivos {@code .properties}, permitiendo cambiar el idioma
 * de la aplicación en tiempo de ejecución.
 * </p>
 *
 * Utiliza {@link ResourceBundle} para cargar los textos según
 * el {@link Locale} seleccionado.
 *
 * @author yhon
 */
public class I18n {

    /**
     * ResourceBundle que contiene las traducciones del idioma actual.
     */
    private static ResourceBundle bundle;

    /**
     * Inicializa el idioma por defecto de la aplicación.
     * <p>
     * Se establece el idioma español al cargar la clase.
     * </p>
     */
    static {
        setLanguage("es");
    }

    /**
     * Cambia el idioma activo de la aplicación.
     * <p>
     * Carga el archivo de propiedades correspondiente al idioma
     * indicado.
     * </p>
     *
     * @param lang código de idioma ("es" o "en")
     */
    public static void setLanguage(String lang) {
        Locale locale = lang.equals("en")
                ? Locale.ENGLISH
                : new Locale("es");

        bundle = ResourceBundle.getBundle("messages", locale);
    }

    /**
     * Obtiene la traducción asociada a una clave.
     * <p>
     * Si la clave no existe, se devuelve la propia clave
     * entre signos de exclamación para facilitar la detección
     * de errores.
     * </p>
     *
     * @param key clave del texto a traducir
     * @return texto traducido o la clave entre exclamaciones
     */
    public static String t(String key) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return "!" + key + "!";
        }
    }

    /**
     * Obtiene la traducción de un rol de usuario.
     * <p>
     * El nombre del rol debe coincidir con el valor del enum
     * {@link Rol} (por ejemplo: PADRE, MADRE, HIJO).
     * </p>
     *
     * @param rol nombre del rol (Rol.name())
     * @return traducción del rol según el idioma activo
     */
    public static String getRolTranslation(String rol) {
        return bundle.getString("rol." + rol.toLowerCase());
    }

    /**
     * Obtiene la traducción de una recompensa.
     * <p>
     * Este método permite traducir recompensas almacenadas
     * originalmente en español dentro de la base de datos,
     * devolviendo la clave correspondiente según el idioma activo.
     * </p>
     *
     * @param rewardName nombre original de la recompensa
     * @return traducción de la recompensa o el nombre original
     */
    public static String getRewardTranslation(String rewardName) {
        switch (rewardName) {
            case "Elegir la película":
                return t("reward.choose_movie");
            case "1 hora de consola":
                return t("reward.console_1h");
            case "2 horas de consola":
                return t("reward.console_2h");
            case "Elegir juego de mesa":
                return t("reward.board_game");
            case "Tarde de juegos en familia":
                return t("reward.family_games");
            case "Ir al cine":
                return t("reward.cinema");
            case "Maratón de series":
                return t("reward.series_marathon");
            case "Elegir la música del día":
                return t("reward.choose_music");
            case "Elegir qué ver en la TV":
                return t("reward.choose_tv");
            case "Postre especial":
                return t("reward.special_dessert");
            case "Elegir la cena":
                return t("reward.choose_dinner");
            case "Cenar fuera":
                return t("reward.dinner_out");
            case "Pedir comida a domicilio":
                return t("reward.delivery_food");
            case "Helado o dulce especial":
                return t("reward.special_ice_cream");
            case "Desayuno especial":
                return t("reward.special_breakfast");
            case "Noche de pizza":
                return t("reward.pizza_night");
            case "Día libre sin tareas":
                return t("reward.free_day");
            case "Mañana libre":
                return t("reward.free_morning");
            case "Dormir una hora más":
                return t("reward.extra_hour");
            case "Tarde libre":
                return t("reward.free_afternoon");
            case "Fin de semana sin tareas":
                return t("reward.weekend_no_tasks");
            case "Obtener un regalo pequeño":
                return t("reward.small_gift");
            case "Obtener un regalo":
                return t("reward.gift");
            case "Regalo especial":
                return t("reward.special_gift");
            case "Vale sorpresa":
                return t("reward.surprise_voucher");
            default:
                return rewardName;
        }
    }
}
