package ru.one.more.app.entities;

import javax.persistence.*;

/**
 * Created by aboba on 23.01.17.
 */
@Entity
public class FeedSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String link;

    String lang;

    String description;

    @ManyToOne
    SourceRule parseRule;

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

    public String getLink() {
        return link;
    }

    public void setLink(String url) {
        this.link = url;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SourceRule getParseRule() {
        return parseRule;
    }

    public void setParseRule(SourceRule parseRule) {
        this.parseRule = parseRule;
    }

    @Override
    public String toString() {
        return "FeedSource{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + link + '\'' +
                ", lang='" + lang + '\'' +
                ", description='" + description + '\'' +
                ", parseRule=" + parseRule +
                '}';
    }
}
