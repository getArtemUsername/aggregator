package ru.one.more.workers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.one.more.app.entities.Feed;
import ru.one.more.app.entities.FeedSource;
import ru.one.more.parsers.rule.ParserResult;
import ru.one.more.parsers.rule.ParserRule;

import javax.persistence.RollbackException;
import java.util.List;

/**
 * Created by aboba on 26.01.17.
 */
public class DataAccessHelper {
    private static final int PAGE_SIZE = 10;
    SessionFactory sessionFactory;
    Session session;

    private final static DataAccessHelper inst = new DataAccessHelper();

    public static DataAccessHelper getInst() {
        return inst;
    }

    private DataAccessHelper() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    Session getSession() {
        if (session == null || !session.isOpen())
            session = sessionFactory.openSession();
        else
            System.out.println("session already opened");
        return session;
    }

    void closeSession() {
        if (session != null && session.isOpen())
            session.close();
    }

    public void saveResult(ParserResult parserResult,
                           ParserRule rule) throws SaveResultException {
        FeedSource feedSource  = parserResult.getFeedSource();
        List<Feed> feedList = parserResult.getFeeds();
        Session s = getSession();
        Transaction transaction = s.getTransaction();
        try {
            transaction.begin();
            s.save(rule.getSourceRule());
            s.save(feedSource);
            feedList.forEach(s::save);
            transaction.commit();
        } catch (RollbackException e) {
            transaction.rollback();
            throw new SaveResultException(e);
        }
        closeSession();
    }

    public List<Feed> fetchFeeds(String searchString, int page) {
        Session s = getSession();
        String queryString = "select f from Feed f";
        boolean useSearch = false;
        if (searchString != null && !searchString.isEmpty()) {
            queryString += " where lower(title) like :searchStr";
            useSearch = true;
        }
        queryString += " order by f.postDate";
        Query<Feed> query = s.createQuery(queryString, Feed.class);
        if (useSearch) query.setParameter("searchStr", "%"+searchString.trim().toLowerCase()+"%");

        int start = page * PAGE_SIZE;
        int count = start + PAGE_SIZE;
        return query.setFirstResult(start).setMaxResults(count).getResultList();
    }

    public boolean noMoreFeeds(String searchString, Integer page) {
        return fetchFeeds(searchString, ++page).size() == 0;
    }

    public static class SaveResultException extends Exception {
        public SaveResultException(Exception e) {
            super(e);
        }
    }
}
