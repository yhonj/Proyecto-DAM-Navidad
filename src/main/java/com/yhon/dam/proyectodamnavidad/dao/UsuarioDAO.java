/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.dao;

import com.yhon.dam.proyectodamnavidad.modelo.Rol;
import com.yhon.dam.proyectodamnavidad.modelo.Usuario;
import com.yhon.dam.proyectodamnavidad.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;

/**
 * DAO (Data Access Object) encargado de la gestión de usuarios.
 * <p>
 * Proporciona métodos para realizar operaciones CRUD sobre la entidad
 * {@link Usuario}, así como consultas específicas utilizadas por los
 * controladores de la aplicación.
 * </p>
 *
 * Utiliza Hibernate para el acceso a la base de datos.
 *
 * @author yhon
 */
public class UsuarioDAO {

    /**
     * Obtiene la lista de usuarios con rol PADRE.
     *
     * @return lista de usuarios con rol PADRE
     */
    public static List<Usuario> listarPadres() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Usuario u WHERE u.rol = :rol",
                    Usuario.class
            ).setParameter("rol", Rol.PADRE)
             .list();
        }
    }

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario a buscar
     * @return usuario encontrado o {@code null} si no existe
     */
    public static Usuario buscarPorUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Usuario WHERE username = :u",
                    Usuario.class
            ).setParameter("u", username)
             .uniqueResult();
        }
    }

    /**
     * Guarda un nuevo usuario en la base de datos.
     *
     * @param u usuario a persistir
     * @return {@code true} si se guarda correctamente, {@code false} en caso de error
     */
    public static boolean guardar(Usuario u) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(u);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // útil en entorno de desarrollo
            return false;
        }
    }

    /**
     * Comprueba si existe un usuario con el email indicado.
     *
     * @param email correo electrónico a comprobar
     * @return {@code true} si el email ya existe, {@code false} en caso contrario
     */
    public static boolean existeEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                    "SELECT COUNT(u) FROM Usuario u WHERE u.email = :e",
                    Long.class
            ).setParameter("e", email)
             .uniqueResult();

            return count != null && count > 0;
        }
    }

    /**
     * Obtiene la lista completa de usuarios del sistema.
     *
     * @return lista de usuarios
     */
    public static List<Usuario> listarUsuarios() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Usuario",
                    Usuario.class
            ).getResultList();
        }
    }

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email correo electrónico del usuario
     * @return usuario encontrado o {@code null} si no existe
     */
    public static Usuario buscarPorEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Usuario u WHERE u.email = :e",
                    Usuario.class
            ).setParameter("e", email)
             .uniqueResult();
        }
    }

    /**
     * Actualiza la contraseña de un usuario.
     * <p>
     * La contraseña debe recibirse previamente en formato hash.
     * </p>
     *
     * @param u usuario al que se le actualiza la contraseña
     * @param hash nuevo hash de la contraseña
     */
    public static void actualizarPassword(Usuario u, String hash) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            u.setPasswordHash(hash);
            session.update(u);
            session.getTransaction().commit();
        }
    }
}
