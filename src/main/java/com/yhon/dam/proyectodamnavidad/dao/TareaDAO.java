/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.dao;

/**
 *
 * @author yhon
 */

import com.yhon.dam.proyectodamnavidad.modelo.Tarea;
import com.yhon.dam.proyectodamnavidad.modelo.Usuario;
import com.yhon.dam.proyectodamnavidad.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;

public class TareaDAO {

    // 1️⃣ Guardar nueva tarea
    public static boolean guardar(Tarea tarea) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(tarea);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2️⃣ Listar tareas donde el usuario es responsable
    public static List<Tarea> listarPorResponsable(Usuario responsable) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Tarea t WHERE t.responsable = :resp",
                    Tarea.class
            ).setParameter("resp", responsable)
             .getResultList();
        }
    }

    // 3️⃣ Marcar tarea como FINALIZADA
   public static void marcarFinalizada(Tarea tarea) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        session.beginTransaction();
        tarea.setEstado("FINALIZADA");
        session.merge(tarea);
        session.getTransaction().commit();
    }
}

    // 4️⃣ Listar todos los usuarios (para el JComboBox)
    public static List<Usuario> listarUsuarios() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Usuario",
                    Usuario.class
            ).getResultList();
        }
    }
}