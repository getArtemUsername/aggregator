package ru.one.more;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.cfg.Configuration;

/**
 * Created by aboba on 26.01.17.
 */
public class HibernateUtils {
    private static HibernateUtils ourInstance = new HibernateUtils();

    public static HibernateUtils getInstance() {
        return ourInstance;
    }

    SessionFactory sessionFactory;
    Session session;
    private HibernateUtils() {
        sessionFactory = new Configuration().configure().buildSessionFactory();

    }

    public Session getSession() {
        if (session == null || !session.isOpen())
            session = sessionFactory.openSession();
        else
            System.out.println("session already opened");
        return session;
    }

    public void closeSession() {
        if (session != null && session.isOpen())
            session.close();
    }
}
