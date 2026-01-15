/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.yhon.dam.proyectodamnavidad;

import com.yhon.dam.proyectodamnavidad.dao.UsuarioDAO;
import com.yhon.dam.proyectodamnavidad.modelo.Usuario;
import com.yhon.dam.proyectodamnavidad.util.I18n;
import com.yhon.dam.proyectodamnavidad.util.PasswordUtil;

/**
 *
 * @author yhonadminmailcom
 */
public class Main {

    public static void main(String[] args) {
        Usuario u = new Usuario();
        u.setUsername("admin");
        u.setEmail("admin@mail.com");
        u.setPasswordHash(PasswordUtil.hashPassword("1234"));

        UsuarioDAO.guardar(u);

        System.out.println("Usuario guardado correctamente");
    }
}
