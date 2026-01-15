/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.controller;

import com.yhon.dam.proyectodamnavidad.dao.UsuarioDAO;
import com.yhon.dam.proyectodamnavidad.modelo.Rol;
import com.yhon.dam.proyectodamnavidad.modelo.Usuario;
import com.yhon.dam.proyectodamnavidad.util.PasswordUtil;

/**
 * Controlador encargado del registro de nuevos usuarios.
 * <p>
 * Esta clase valida los datos introducidos por el usuario,
 * comprueba la existencia previa del email y, en caso correcto,
 * crea un nuevo usuario en la base de datos.
 * </p>
 *
 * @author yhon
 */
public class RegistroControlador {

    /**
     * Resultados posibles del proceso de registro.
     */
    public enum RegistroResult {
        /** Registro realizado correctamente */
        OK,
        /** Existen campos obligatorios vacíos */
        CAMPOS_VACIOS,
        /** El email ya está registrado en el sistema */
        EMAIL_EXISTE,
        /** Error inesperado durante el registro */
        ERROR
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * <p>
     * El método realiza las siguientes validaciones:
     * </p>
     * <ul>
     *   <li>Comprueba que los campos no estén vacíos</li>
     *   <li>Verifica que el email no exista previamente</li>
     *   <li>Almacena la contraseña de forma segura usando hash</li>
     * </ul>
     *
     * @param username nombre de usuario
     * @param password contraseña en texto plano
     * @param email correo electrónico del usuario
     * @param rol rol asignado al usuario
     * @return resultado del proceso de registro
     */
    public RegistroResult registrar(
            String username,
            String password,
            String email,
            Rol rol
    ) {

        if (username.isBlank() || password.isBlank() || email.isBlank()) {
            return RegistroResult.CAMPOS_VACIOS;
        }

        if (UsuarioDAO.existeEmail(email)) {
            return RegistroResult.EMAIL_EXISTE;
        }

        try {
            Usuario u = new Usuario();
            u.setUsername(username);
            u.setEmail(email);
            u.setPasswordHash(PasswordUtil.hashPassword(password));
            u.setRol(rol);
            u.setPuntos(0);

            UsuarioDAO.guardar(u);
            return RegistroResult.OK;

        } catch (Exception e) {
            e.printStackTrace();
            return RegistroResult.ERROR;
        }
    }
}
