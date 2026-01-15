/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.controller;

import com.yhon.dam.proyectodamnavidad.dao.UsuarioDAO;
import com.yhon.dam.proyectodamnavidad.modelo.Usuario;
import com.yhon.dam.proyectodamnavidad.util.EmailUtil;
import com.yhon.dam.proyectodamnavidad.util.PasswordGenerator;
import com.yhon.dam.proyectodamnavidad.util.PasswordUtil;

/**
 * Controlador encargado del proceso de recuperación de contraseña.
 * <p>
 * Permite generar una nueva contraseña aleatoria para un usuario
 * identificado por su email, actualizarla en la base de datos
 * y enviarla por correo electrónico.
 * </p>
 *
 * @author yhon
 */
public class RecuperarControlador {

    /**
     * Resultados posibles del proceso de recuperación de contraseña.
     */
    public enum Resultado {
        /** Proceso completado correctamente */
        OK,
        /** No existe ningún usuario con el email indicado */
        EMAIL_NO_EXISTE,
        /** Error inesperado durante el proceso */
        ERROR
    }

    /**
     * Inicia el proceso de recuperación de contraseña.
     * <p>
     * El método realiza los siguientes pasos:
     * </p>
     * <ol>
     *   <li>Busca el usuario por su email</li>
     *   <li>Genera una nueva contraseña aleatoria</li>
     *   <li>Almacena el nuevo hash en la base de datos</li>
     *   <li>Envía la nueva contraseña por correo electrónico</li>
     * </ol>
     *
     * @param email correo electrónico del usuario
     * @return resultado del proceso de recuperación
     */
    public Resultado recuperar(String email) {
        try {
            Usuario u = UsuarioDAO.buscarPorEmail(email);

            if (u == null) {
                return Resultado.EMAIL_NO_EXISTE;
            }

            String nuevaPassword = PasswordGenerator.generar(10);
            String hash = PasswordUtil.hashPassword(nuevaPassword);

            UsuarioDAO.actualizarPassword(u, hash);

            EmailUtil.enviar(
                    email,
                    "Recuperación de contraseña",
                    "Tu nueva contraseña es:\n\n" + nuevaPassword
            );

            return Resultado.OK;

        } catch (Exception e) {
            e.printStackTrace();
            return Resultado.ERROR;
        }
    }
}
