package ru.one.more.app.it;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.*;
import ru.one.more.app.entities.Feed;
import ru.one.more.app.entities.FeedSource;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by aboba on 25.01.17.
 */
public class CRUDModelsIT {
    static Session session;
    static SessionFactory sessionFactory;

    @BeforeClass
    public static void createSession() {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
            session = sessionFactory.openSession();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testCreateFeed() throws Exception {
        session.getTransaction().begin();
        FeedSource feedSource = new FeedSource();
        feedSource.setTitle("Some Title");
        feedSource.setDescription("Some Description");
        feedSource.setLang("RU");
        feedSource.setLink("some url");
        Feed feed1 = new Feed();
        feed1.setSource(feedSource);
        feed1.setTitle("Feed 1");
        feed1.setPostDate(new Date());
        feed1.setShortContent(".....");
        Feed feed2 = new Feed();
        feed1.setSource(feedSource);
        feed1.setTitle("Feed 2");
        feed1.setPostDate(new Date());
        feed1.setShortContent(".....");
        session.persist(feedSource);
        session.persist(feed1);
        session.persist(feed2);
        session.getTransaction().commit();
        List<Feed> feeds = session.createQuery("select f from Feed f", Feed.class).getResultList();
        assertThat(feeds, hasSize(2));
        assertThat(feeds.get(0).getId(), notNullValue());
        assertThat(feeds.get(1).getId(), notNullValue());
        assertThat(feeds.get(0).getSource().getTitle(), notNullValue());
    }

    @AfterClass
    public static void destroySession() {
        if (session != null) session.close();
        if (sessionFactory != null) sessionFactory.close();
    }
}
