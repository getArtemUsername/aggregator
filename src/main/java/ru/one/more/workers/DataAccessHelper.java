package ru.one.more.workers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.one.more.app.entities.Feed;
import ru.one.more.app.entities.FeedSource;
import ru.one.more.app.entities.SourceRule;
import ru.one.more.parsers.rule.ParserResult;
import ru.one.more.parsers.rule.ParserRule;

import javax.persistence.RollbackException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by aboba on 26.01.17.
 */
public class DataAccessHelper {
    private static final int PAGE_SIZE = 9;
    SessionFactory sessionFactory;

    private final static DataAccessHelper inst = new DataAccessHelper();

    public static DataAccessHelper getInst() {
        return inst;
    }

    private DataAccessHelper() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    Session getSession() {
            return sessionFactory.openSession();
    }

    public void saveResult(ParserResult parserResult,
                           ParserRule rule) throws SaveResultException {
        FeedSource feedSource  = parserResult.getFeedSource();
        List<Feed> feedList = parserResult.getFeeds();
        Session s = getSession();
        Transaction transaction = s.getTransaction();
        try {
            transaction.begin();
            SourceRule sr = s.get(SourceRule.class, rule.getSourceRule().getName());
            s.saveOrUpdate(sr == null ? rule.getSourceRule() : sr);
            s.saveOrUpdate(feedSource);
            List<Feed> feedListToSave = filterFeedList(s, feedSource.getTitle(), feedList);
            feedListToSave.forEach(s::save);
            transaction.commit();
        } catch (RollbackException e) {
            transaction.rollback();
            throw new SaveResultException(e);
        }
        s.close();
    }

    public List<FeedSource> fetchFeedSources() {
        Session s = getSession();
        List<FeedSource> feedSources = s.createQuery("" +
                "select fs " +
                "from FeedSource fs", FeedSource.class)
                .getResultList();
        s.close();
        return feedSources;

    }

    private List<Feed> filterFeedList(Session s, String title, List<Feed> feedList) {
        List<Date> lastPostDate = s.createQuery("" +
                "select f.postDate from Feed f " +
                "where f.source.title = :title " +
                "order by f.postDate desc", Date.class)
                .setParameter("title", title)
                .setMaxResults(1)
                .getResultList();
        if (!lastPostDate.isEmpty()) {
            return feedList.stream()
                    .filter(feed -> feed.getPostDate().getTime() > lastPostDate.get(0).getTime())
                    .collect(toList());
        }
        return feedList;
    }

    public List<Feed> fetchFeeds(String searchString, int page) {
        Session s = getSession();
        String queryString = "select f from Feed f";
        boolean useSearch = false;
        if (searchString != null && !searchString.isEmpty()) {
            queryString += " where lower(title) like :searchStr";
            useSearch = true;
        }
        queryString += " order by f.postDate desc";
        Query<Feed> query = s.createQuery(queryString, Feed.class);
        if (useSearch) query.setParameter("searchStr", "%"+searchString.trim().toLowerCase()+"%");

        int start = page * PAGE_SIZE;
        int count = PAGE_SIZE;
        return query.setFirstResult(start).setMaxResults(count).getResultList();
    }

    public boolean noMoreFeeds(String searchString, Integer page) {
        return fetchFeeds(searchString, ++page).size() == 0;
    }

    public List<SourceRule> fetchRules() {
        Session s = getSession();
        List<SourceRule> rules  = s.createQuery("" +
                        "select r " +
                        "from SourceRule r " +
                        "order by name",
                SourceRule.class)
                .getResultList();
        s.close();
        return rules;
    }

    public static class SaveResultException extends Exception {
        public SaveResultException(Exception e) {
            super(e);
        }
    }
}
