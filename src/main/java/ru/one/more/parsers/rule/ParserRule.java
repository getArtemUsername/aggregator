package ru.one.more.parsers.rule;

import ru.one.more.app.entities.ItemRule;
import ru.one.more.app.entities.SourceRule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import static ru.one.more.parsers.util.StrUtils.isWord;

/**
 * Created by aboba on 24.01.17.
 */

public class ParserRule {

    static final String TYPE = "type";
    static final String NAME= "name";

    static final String ROOT_SEARCH = "root.search.tag";
    static final String ITEMS_SEARCH = "items.search.tag";

    static final String SRC_TITLE = "title.tag";
    static final String SRC_DESC = "desc.tag";
    static final String SRC_LANG = "lang.tag";
    static final String SRC_URL = "url.tag";
    static final String SRC_ITEM = "item.tag";

    static final String ITEM_TITLE = "item.title.tag";
    static final String ITEM_DESC = "item.desc.tag";
    static final String ITEM_PUB_DATE = "item.pubDate.tag";
    static final String ITEM_URL = "item.url.tag";


    final SourceRule sourceRule;

    private ParserRule(SourceRule sourceRule) {
        this.sourceRule = sourceRule;
    }

    public SourceRule getSourceRule() { return sourceRule; }

    public static Optional<ParserRule> from(String filePath) {
        try(InputStream ruleIS = ParserRule.class.getClassLoader().getResourceAsStream(filePath)) {
            Properties p = new Properties();
            if (ruleIS != null) p.load(ruleIS);
            return Optional.of(new ParserRule(readRule(p)));
        } catch (RuleException | IOException e) {
            //log
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private static SourceRule readRule(Properties p) throws RuleException {
        SourceRule sourceRule = new SourceRule();

        sourceRule.setName(p.getProperty(NAME));
        sourceRule.setDataType(p.getProperty(TYPE));
        if (sourceRule.getName() == null || sourceRule.getDataType() == null) {
            throw new RuleException("wrong rule format. no name or datatype");
        }
        if (!hasValue(p, ROOT_SEARCH)){
            throw new RuleException("wrong rule format. need search root tag");
        }
        sourceRule.setRootSearchTag(p.getProperty(ROOT_SEARCH));
        sourceRule.setItemsSearchTag(!hasValue(p, ITEMS_SEARCH) || ".".equals(p.getProperty(ITEMS_SEARCH))
                ? p.getProperty(ROOT_SEARCH)
                : p.getProperty(ITEMS_SEARCH));

        sourceRule.setTitleTag(p.getProperty(SRC_TITLE));
        sourceRule.setDescriptionTag(p.getProperty(SRC_DESC));
        sourceRule.setLanguageTag(p.getProperty(SRC_LANG));
        sourceRule.setUrlTag(p.getProperty(SRC_URL));
        sourceRule.setItemTag(p.getProperty(SRC_ITEM));

        ItemRule itemRule = new ItemRule();
        itemRule.setItemTitleTag(p.getProperty(ITEM_TITLE));
        itemRule.setItemDescriptionTag(p.getProperty(ITEM_DESC));
        itemRule.setItemPubDateTag(p.getProperty(ITEM_PUB_DATE));
        itemRule.setItemUrlTag(p.getProperty(ITEM_URL));

        sourceRule.setItemRule(itemRule);
        return sourceRule;
    }

    private static boolean hasValue(Properties p, String propertyName) {
        return isWord(p.getProperty(propertyName));
    }

    public static class RuleException extends Exception {
        RuleException(String message) {
            super(message);
        }
    }
}
