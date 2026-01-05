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
public class LoginControlador {

      public enum LoginResult {
        OK, USER_NOT_FOUND, WRONG_PASSWORD
    }

    private Usuario usuarioLogueado;

    public LoginResult login(String username, String password) {
        Usuario u = UsuarioDAO.buscarPorUsername(username);

        if (u == null) return LoginResult.USER_NOT_FOUND;

        if (!PasswordUtil.checkPassword(password, u.getPasswordHash())) {
            return LoginResult.WRONG_PASSWORD;
        }

        usuarioLogueado = u;
        return LoginResult.OK;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }
}

