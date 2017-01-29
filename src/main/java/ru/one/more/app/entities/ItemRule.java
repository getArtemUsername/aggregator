package ru.one.more.app.entities;

import javax.persistence.Embeddable;

/**
 * Created by aboba on 24.01.17.
 */
@Embeddable
public class ItemRule {

    String itemTitleTag;

    String itemDescriptionTag;

    String itemLinkTag;

    String itemPubDateTag;

    String itemThumbLinkTag;

    public String getItemTitleTag() {
        return itemTitleTag;
    }

    public void setItemTitleTag(String itemTitleTag) {
        this.itemTitleTag = itemTitleTag;
    }

    public String getItemDescriptionTag() {
        return itemDescriptionTag;
    }

    public void setItemDescriptionTag(String itemDescriptionTag) {
        this.itemDescriptionTag = itemDescriptionTag;
    }

    public String getItemLinkTag() {
        return itemLinkTag;
    }

    public void setItemLinkTag(String itemUrlTag) {
        this.itemLinkTag = itemUrlTag;
    }

    public String getItemThumbLinkTag() {
        return itemThumbLinkTag;
    }

    public void setItemThumbLinkTag(String itemThumbLinkTag) {
        this.itemThumbLinkTag = itemThumbLinkTag;
    }

    public String getItemPubDateTag() {
        return itemPubDateTag;
    }

    public void setItemPubDateTag(String itemPubDateTag) {
        this.itemPubDateTag = itemPubDateTag;
    }
}
