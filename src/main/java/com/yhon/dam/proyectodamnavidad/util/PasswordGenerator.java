/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.util;

import java.security.SecureRandom;

/**
 * Utilidad para la generación de contraseñas aleatorias.
 * <p>
 * Genera cadenas seguras utilizando {@link SecureRandom},
 * combinando letras mayúsculas, minúsculas y números.
 * </p>
 *
 * Se utiliza principalmente en el proceso de recuperación
 * de contraseña.
 *
 * @author yhon
 */
public class PasswordGenerator {

    /**
     * Conjunto de caracteres permitidos para la generación
     * de contraseñas.
     */
    private static final String CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * Genera una contraseña aleatoria de la longitud indicada.
     *
     * @param longitud número de caracteres de la contraseña
     * @return contraseña generada aleatoriamente
     */
    public static String generar(int longitud) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < longitud; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
