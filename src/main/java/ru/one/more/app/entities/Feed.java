package ru.one.more.app.entities;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by aboba on 23.01.17.
 */
@Entity
@Table(indexes={@Index(name = "searchIndex", columnList = "title")})
@BatchSize(size = 10)
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String title;

    String shortContent;

    @Lob @Basic(fetch = FetchType.LAZY)
    String fullContent;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    Date postDate;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date loadDate;

    @Column(nullable = false)
    String link;

    @ManyToOne
    FeedSource source;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortContent() {
        return shortContent;
    }

    public void setShortContent(String shortContent) {
        this.shortContent = shortContent != null && shortContent.length()>100
                ? shortContent.substring(0, 100)
                : shortContent;
    }

    public String getFullContent() {
        return fullContent;
    }

    public void setFullContent(String fullContent) {
        this.fullContent = fullContent;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public Date getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(Date loadDate) {
        this.loadDate = loadDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String sourceLink) {
        this.link = sourceLink;
    }

    public FeedSource getSource() {
        return source;
    }

    public void setSource(FeedSource source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feed feed = (Feed) o;

        if (id != null ? !id.equals(feed.id) : feed.id != null) return false;
        if (!title.equals(feed.title)) return false;
        if (!postDate.equals(feed.postDate)) return false;
        return source.equals(feed.source);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + title.hashCode();
        result = 31 * result + postDate.hashCode();
        result = 31 * result + source.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", shortContent='" + shortContent + '\'' +
                ", fullContent='" + fullContent + '\'' +
                ", postDate=" + postDate +
                ", loadDate=" + loadDate +
                ", sourceLink='" + link + '\'' +
                ", source=" + source +
                '}';
    }
}
