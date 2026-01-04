/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.util;

/**
 *
 * @author yhon
 */
import com.password4j.Password;
import com.password4j.Hash;

public class PasswordUtil {

    public static String hashPassword(String plainPassword) {
        Hash hash = Password.hash(plainPassword).withBcrypt();
        return hash.getResult();
    }

    public static boolean checkPassword(String plainPassword, String hashFromDB) {
        return Password.check(plainPassword, hashFromDB).withBcrypt();
    }
}
