package ru.one.more.app.entities;

import javax.persistence.*;

/**
 * Created by aboba on 23.01.17.
 */
@Entity
public class SourceRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String channelTitleTag;

    String channelDescriptionTag;

    String channelLanguageTag;

    String channelUrlTag;

    String channelItemTag;

    @Embedded
    ItemRule itemRule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelTitleTag() {
        return channelTitleTag;
    }

    public void setChannelTitleTag(String channelTitleTag) {
        this.channelTitleTag = channelTitleTag;
    }

    public String getChannelDescriptionTag() {
        return channelDescriptionTag;
    }

    public void setChannelDescriptionTag(String channelDescriptionTag) {
        this.channelDescriptionTag = channelDescriptionTag;
    }

    public String getChannelLanguageTag() {
        return channelLanguageTag;
    }

    public void setChannelLanguageTag(String channelLanguageTag) {
        this.channelLanguageTag = channelLanguageTag;
    }

    public String getChannelUrlTag() {
        return channelUrlTag;
    }

    public void setChannelUrlTag(String channelUrlTag) {
        this.channelUrlTag = channelUrlTag;
    }

    public String getChannelItemTag() {
        return channelItemTag;
    }

    public void setChannelItemTag(String channelItemTag) {
        this.channelItemTag = channelItemTag;
    }

    public ItemRule getItemRule() {
        return itemRule;
    }

    public void setItemRule(ItemRule itemRule) {
        this.itemRule = itemRule;
    }
}
