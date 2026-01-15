package com.yhon.dam.proyectodamnavidad.controller;


import com.yhon.dam.proyectodamnavidad.dao.UsuarioDAO;
import com.yhon.dam.proyectodamnavidad.modelo.Usuario;
import com.yhon.dam.proyectodamnavidad.util.PasswordUtil;


/**
 * Controlador encargado de gestionar el proceso de inicio de sesión.
 * <p>
 * Esta clase valida las credenciales del usuario comprobando:
 * </p>
 * <ul>
 *   <li>La existencia del usuario</li>
 *   <li>La corrección de la contraseña introducida</li>
 * </ul>
 *
 * Si el inicio de sesión es correcto, almacena el usuario autenticado
 * para su posterior acceso desde la vista.
 *
 * @author yhon
 */
public class LoginControlador {

    /**
     * Resultados posibles del intento de inicio de sesión.
     */
    public enum LoginResult {
        /** Inicio de sesión correcto */
        OK,
        /** El usuario no existe */
        USER_NOT_FOUND,
        /** La contraseña es incorrecta */
        WRONG_PASSWORD
    }

    /**
     * Usuario que ha iniciado sesión correctamente.
     */
    private Usuario usuarioLogueado;

    /**
     * Valida las credenciales de un usuario.
     * <p>
     * Busca el usuario por su nombre y comprueba que la contraseña
     * introducida coincida con la almacenada en la base de datos.
     * </p>
     *
     * @param username nombre de usuario introducido
     * @param password contraseña introducida en texto plano
     * @return resultado del proceso de autenticación
     */
    public LoginResult login(String username, String password) {

        Usuario u = UsuarioDAO.buscarPorUsername(username);

        if (u == null) {
            return LoginResult.USER_NOT_FOUND;
        }

        if (!PasswordUtil.checkPassword(password, u.getPasswordHash())) {
            return LoginResult.WRONG_PASSWORD;
        }

        usuarioLogueado = u;
        return LoginResult.OK;
    }

    /**
     * Devuelve el usuario que ha iniciado sesión.
     * <p>
     * Solo contiene valor si el login ha sido correcto.
     * </p>
     *
     * @return usuario autenticado o {@code null} si no hay sesión activa
     */
    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }
}
