package ru.one.more.model;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by aboba on 23.01.17.
 */
@Entity
@Table(indexes={@Index(name = "searchIndex", columnList = "title")})
@BatchSize(size = 10)
public class NewsArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @Size(min = 1, max = 200)

    String title;

    String shortContent;

    @Lob @Basic(fetch = FetchType.LAZY)
    String fullContent;

    @Temporal(TemporalType.DATE)
    Date postDate;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date loadDate;

    String sourceLink;

    @OneToMany
    NewsSource source;

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
}
