/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.controller;

import com.yhon.dam.proyectodamnavidad.dao.UsuarioDAO;
import com.yhon.dam.proyectodamnavidad.modelo.Usuario;
import com.yhon.dam.proyectodamnavidad.util.PasswordUtil;

/**
 *
 * @author yhon
 */
public class RegistroControlador {
 public enum RegistroResult {
        OK,
        CAMPOS_VACIOS,
        EMAIL_EXISTE,
        ERROR
    }

    public RegistroResult registrar(String username, String password, String email) {

        // 1️⃣ Campos vacíos
        if (username.isBlank() || password.isBlank() || email.isBlank()) {
            return RegistroResult.CAMPOS_VACIOS;
        }

        // 2️⃣ Email ya existe
        if (UsuarioDAO.existeEmail(email)) {
            return RegistroResult.EMAIL_EXISTE;
        }

        // 3️⃣ Crear usuario
        Usuario u = new Usuario();
        u.setUsername(username);
        u.setEmail(email);
        u.setPasswordHash(PasswordUtil.hashPassword(password));

        boolean guardado = UsuarioDAO.guardar(u);

        if (!guardado) {
            return RegistroResult.ERROR;
        }

        return RegistroResult.OK;
    }   
}
