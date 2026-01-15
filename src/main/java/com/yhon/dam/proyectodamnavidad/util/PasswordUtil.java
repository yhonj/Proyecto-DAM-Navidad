/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.util;

import com.password4j.Password;
import com.password4j.Hash;

/**
 * Utilidad para la gestión segura de contraseñas.
 * <p>
 * Esta clase encapsula el uso de la librería {@code password4j}
 * para generar y verificar hashes de contraseñas utilizando
 * el algoritmo BCrypt.
 * </p>
 *
 * <p>
 * En ningún caso se almacenan contraseñas en texto plano,
 * garantizando así una mayor seguridad del sistema.
 * </p>
 *
 * @author yhon
 */
public class PasswordUtil {

    /**
     * Genera un hash seguro a partir de una contraseña en texto plano.
     * <p>
     * Utiliza el algoritmo BCrypt, recomendado para el almacenamiento
     * de contraseñas debido a su resistencia frente a ataques
     * de fuerza bruta.
     * </p>
     *
     * @param plainPassword contraseña en texto plano
     * @return hash de la contraseña
     */
    public static String hashPassword(String plainPassword) {
        Hash hash = Password.hash(plainPassword).withBcrypt();
        return hash.getResult();
    }

    /**
     * Comprueba si una contraseña coincide con su hash almacenado.
     * <p>
     * Aplica el algoritmo BCrypt para verificar la validez
     * de la contraseña introducida.
     * </p>
     *
     * @param plainPassword contraseña introducida por el usuario
     * @param hashFromDB hash de la contraseña almacenada
     * @return {@code true} si la contraseña es correcta, {@code false} en caso contrario
     */
    public static boolean checkPassword(
            String plainPassword,
            String hashFromDB
    ) {
        return Password.check(plainPassword, hashFromDB).withBcrypt();
    }
}
