package ru.one.more.app.entities;

import javax.persistence.*;

/**
 * Created by aboba on 23.01.17.
 */
@Entity
@Table(name = "rule")
public class SourceRule {

    public enum DataType {
        XML, JSON
    }

    @Id
    String name;

    @Enumerated(EnumType.STRING)
    DataType dataType;

    String rootSearchTag;

    String itemsSearchTag;

    String titleTag;

    String descriptionTag;

    String languageTag;

    String linkTag;

    String itemTag;

    @Embedded
    ItemRule itemRule;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = DataType.valueOf(dataType.toUpperCase());
    }

    public String getRootSearchTag() {
        return rootSearchTag;
    }

    public void setRootSearchTag(String rootSearchTag) {
        this.rootSearchTag = rootSearchTag;
    }

    public String getItemsSearchTag() {
        return itemsSearchTag;
    }

    public void setItemsSearchTag(String itemsSearchTag) {
        this.itemsSearchTag = itemsSearchTag;
    }

    public String getTitleTag() {
        return titleTag;
    }

    public void setTitleTag(String channelTitleTag) {
        this.titleTag = channelTitleTag;
    }

    public String getDescriptionTag() {
        return descriptionTag;
    }

    public void setDescriptionTag(String channelDescriptionTag) {
        this.descriptionTag = channelDescriptionTag;
    }

    public String getLanguageTag() {
        return languageTag;
    }

    public void setLanguageTag(String channelLanguageTag) {
        this.languageTag = channelLanguageTag;
    }

    public String getLinkTag() {
        return linkTag;
    }

    public void setLinkTag(String channelUrlTag) {
        this.linkTag = channelUrlTag;
    }

    public String getItemTag() {
        return itemTag;
    }

    public void setItemTag(String channelItemTag) {
        this.itemTag = channelItemTag;
    }

    public ItemRule getItemRule() {
        return itemRule;
    }

    public void setItemRule(ItemRule itemRule) {
        this.itemRule = itemRule;
    }
}
