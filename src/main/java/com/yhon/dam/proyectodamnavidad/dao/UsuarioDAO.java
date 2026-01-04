/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.dao;

import com.yhon.dam.proyectodamnavidad.modelo.Usuario;
import com.yhon.dam.proyectodamnavidad.util.HibernateUtil;
import org.hibernate.Session;

public class UsuarioDAO {

    public static boolean guardar(Usuario u) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(u);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // MUY IMPORTANTE para ver errores reales
            return false;
        }
    }
    
    
    public static Usuario buscarPorUsername(String username) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        return session
                .createQuery("FROM Usuario WHERE username = :u", Usuario.class)
                .setParameter("u", username)
                .uniqueResult();
    }
}
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

}