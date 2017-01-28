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

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    Date postDate;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date loadDate;

    @Column(nullable = false)
    String sourceLink;

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
        this.shortContent = shortContent;
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

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public FeedSource getSource() {
        return source;
    }

    public void setSource(FeedSource source) {
        this.source = source;
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
                ", sourceLink='" + sourceLink + '\'' +
                ", source=" + source +
                '}';
    }
}
