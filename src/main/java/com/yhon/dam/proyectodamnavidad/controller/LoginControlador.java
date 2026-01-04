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
        OK,
        USER_NOT_FOUND,
        WRONG_PASSWORD
    }

    public LoginResult login(String username, String password) {

        Usuario usuario = UsuarioDAO.buscarPorUsername(username);

        if (usuario == null) {
            return LoginResult.USER_NOT_FOUND;
        }

        boolean passwordOk = PasswordUtil.checkPassword(
                password,
                usuario.getPasswordHash()
        );

        if (!passwordOk) {
            return LoginResult.WRONG_PASSWORD;
        }

        return LoginResult.OK;
    }
}
