/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.dao;

import com.yhon.dam.proyectodamnavidad.modelo.Recompensa;
import com.yhon.dam.proyectodamnavidad.modelo.Tarea;
import com.yhon.dam.proyectodamnavidad.modelo.Usuario;
import com.yhon.dam.proyectodamnavidad.util.HibernateUtil;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * DAO (Data Access Object) encargado de la gestión de tareas.
 * <p>
 * Esta clase contiene toda la lógica de acceso a datos relacionada con las
 * tareas del sistema, incluyendo:
 * </p>
 * <ul>
 * <li>Creación y eliminación de tareas</li>
 * <li>Finalización y deshacer tareas</li>
 * <li>Canje de recompensas</li>
 * <li>Delegación de tareas</li>
 * </ul>
 *
 * Utiliza Hibernate para la persistencia de datos y gestiona manualmente las
 * transacciones cuando es necesario.
 *
 * @author yhon
 */
public class TareaDAO {

    /**
     * Coste en puntos de la acción especial "Que lo haga otro".
     */
    private static final int COSTE_QUE_LO_HAGA_OTRO = 1000;

    /**
     * Guarda una nueva tarea en la base de datos.
     *
     * @param tarea tarea a persistir
     * @return {@code true} si se guarda correctamente, {@code false} en caso de
     * error
     */
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

    /**
     * Obtiene la lista de usuarios con rol PADRE o MADRE.
     *
     * @return lista de usuarios con rol parental
     */
    public static List<Usuario> listarPadres() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Usuario u WHERE u.rol IN ('PADRE', 'MADRE')",
                    Usuario.class
            ).getResultList();
        }
    }

    /**
     * Obtiene las tareas asignadas a un responsable concreto.
     *
     * @param responsable usuario responsable de las tareas
     * @return lista de tareas asignadas
     */
    public static List<Tarea> listarPorResponsable(Usuario responsable) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Tarea t WHERE t.responsable = :resp",
                    Tarea.class
            ).setParameter("resp", responsable)
                    .getResultList();
        }
    }

    /**
     * Marca una tarea como finalizada y asigna los puntos al responsable.
     * <p>
     * Si la tarea ya está finalizada, no se realiza ninguna acción.
     * </p>
     *
     * @param tarea tarea a finalizar
     */
    public static void marcarFinalizada(Tarea tarea) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Tarea t = session.get(Tarea.class, tarea.getId());

            if ("FINALIZADA".equals(t.getEstado())) {
                tx.rollback();
                return;
            }

            t.setEstado("FINALIZADA");

            Usuario responsable = t.getResponsable();
            responsable.setPuntos(
                    responsable.getPuntos() + t.getPuntosRecompensa()
            );

            session.update(responsable);
            session.update(t);

            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /**
     * Obtiene la lista completa de usuarios del sistema.
     * <p>
     * Se utiliza principalmente para poblar componentes gráficos como
     * JComboBox.
     * </p>
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
     * Canjea la acción especial "Que lo haga otro".
     * <p>
     * Permite a un usuario pagar puntos para reasignar una tarea pendiente a
     * otro usuario.
     * </p>
     *
     * @param usuario usuario que realiza el canje
     * @param tarea tarea a reasignar
     * @param nuevoResponsable nuevo responsable de la tarea
     * @return {@code true} si el canje se realiza correctamente
     */
    public static boolean canjearQueLoHagaOtro(
            Usuario usuario,
            Tarea tarea,
            Usuario nuevoResponsable
    ) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Usuario u = session.get(Usuario.class, usuario.getId());
            Tarea t = session.get(Tarea.class, tarea.getId());

            if (!"PENDIENTE".equals(t.getEstado())) {
                tx.rollback();
                return false;
            }

            if (u.getPuntos() < COSTE_QUE_LO_HAGA_OTRO) {
                tx.rollback();
                return false;
            }

            u.setPuntos(u.getPuntos() - COSTE_QUE_LO_HAGA_OTRO);
            t.setResponsable(nuevoResponsable);

            session.update(u);
            session.update(t);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;

        } finally {
            session.close();
        }
    }

    /**
     * Canjea una recompensa descontando puntos al usuario y creando tareas
     * automáticas para los padres o madres.
     *
     * @param usuario usuario que canjea la recompensa
     * @param recompensa recompensa seleccionada
     * @return {@code true} si el canje se realiza correctamente
     */
    public static boolean canjearRecompensa(
            Usuario usuario,
            Recompensa recompensa
    ) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            Usuario u = session.get(Usuario.class, usuario.getId());

            if (u.getPuntos() < recompensa.getCostePuntos()) {
                session.getTransaction().rollback();
                return false;
            }

            u.setPuntos(u.getPuntos() - recompensa.getCostePuntos());
            session.update(u);

            List<Usuario> padres = session.createQuery(
                    "FROM Usuario u WHERE u.rol IN ('PADRE','MADRE')",
                    Usuario.class
            ).getResultList();

            for (Usuario padre : padres) {
                Tarea t = new Tarea();
                t.setNombre("Recompensa: " + recompensa.getNombre());
                t.setDescripcion(
                        "El usuario " + u.getUsername()
                        + " ha canjeado la recompensa: "
                        + recompensa.getNombre()
                );
                t.setAsignadoPor(u);
                t.setResponsable(padre);
                t.setEstado("PENDIENTE");
                t.setFechaInicio(LocalDate.now());
                t.setFechaLimite(LocalDate.now().plusDays(3));
                t.setPuntosRecompensa(0);

                session.persist(t);
            }

            session.getTransaction().commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una tarea de la base de datos.
     *
     * @param tarea tarea a eliminar
     * @return {@code true} si se elimina correctamente
     */
    public static boolean eliminar(Tarea tarea) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            Tarea t = session.get(Tarea.class, tarea.getId());
            if (t != null) {
                session.remove(t);
            }

            session.getTransaction().commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deshace la finalización de una tarea previamente marcada como finalizada.
     * <p>
     * Restaura el estado de la tarea y revierte los puntos otorgados.
     * </p>
     *
     * @param tarea tarea a restaurar
     * @param puntos puntos que se deben revertir
     * @return {@code true} si la operación se realiza correctamente
     */
    public static boolean deshacerFinalizacion(Long tareaId, int puntos) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Tarea t = session.get(Tarea.class, tareaId);

            if (t == null || !"FINALIZADA".equals(t.getEstado())) {
                tx.rollback();
                return false;
            }

            Usuario u = t.getResponsable();

            // 🔁 Revertir estado y puntos
            t.setEstado("PENDIENTE");
            u.setPuntos(u.getPuntos() - puntos);

            session.update(u);
            session.update(t);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

}
