package ru.one.more.parsers.rule;

import ru.one.more.model.ItemRule;
import ru.one.more.model.SourceRule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by aboba on 24.01.17.
 */

public class ParserRule {
    public static final String CHANNEL_TITLE = "channel.title";
    public static final String CHANNEL_DESC = "channel.desc";
    public static final String CHANNEL_LANG = "channel.lang";
    public static final String CHANNEL_URL = "channel.url";
    public static final String CHANNEL_ITEM = "channel.item";

    public static final String ITEM_TITLE = "item.title";
    public static final String ITEM_DESC = "item.desc";
    public static final String ITEM_PUB_DATE = "item.pubDate";
    public static final String ITEM_URL = "item.url";

    private static final Properties defaultProps = new Properties();
    static {
        try {
            defaultProps.load(ParserRule.class.getClassLoader().getResourceAsStream("rules/default.rule"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Optional<ParserRule> from(String filePath) {
        try(InputStream ruleIS = ParserRule.class.getClassLoader().getResourceAsStream(filePath)) {
            Properties p = defaultProps.stringPropertyNames().isEmpty()
                    ? new Properties()
                    : new Properties(defaultProps);

            if (ruleIS != null) p.load(ruleIS);

            return Optional.of(new ParserRule(readRule(p)));
        } catch (IOException e) {
            //log
            e.printStackTrace();
        }
        return defaultProps.stringPropertyNames().isEmpty()
                ? Optional.empty()
                : Optional.of(new ParserRule(readRule(defaultProps)));
    }

    private static SourceRule readRule(Properties p) {
        SourceRule sourceRule = new SourceRule();
        sourceRule.setChannelTitleTag(p.getProperty(CHANNEL_TITLE));
        sourceRule.setChannelDescriptionTag(p.getProperty(CHANNEL_DESC));
        sourceRule.setChannelLanguageTag(p.getProperty(CHANNEL_LANG));
        sourceRule.setChannelUrlTag(p.getProperty(CHANNEL_URL));
        sourceRule.setChannelItemTag(p.getProperty(CHANNEL_ITEM));

        ItemRule itemRule = new ItemRule();
        itemRule.setItemTitleTag(p.getProperty(ITEM_TITLE));
        itemRule.setItemDescriptionTag(p.getProperty(ITEM_DESC));
        itemRule.setItemPubDateTag(p.getProperty(ITEM_PUB_DATE));
        itemRule.setItemUrlTag(p.getProperty(ITEM_URL));

        sourceRule.setItemRule(itemRule);
        return sourceRule;
    }

    public ParserRule(SourceRule sourceRule) {
        this.sourceRule = sourceRule;
    }

    final SourceRule sourceRule;
    public SourceRule getSourceRule() { return sourceRule; }


}
