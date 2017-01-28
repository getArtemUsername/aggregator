package ru.one.more.parsers;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.hibernate.annotations.SourceType;
import org.xml.sax.SAXException;
import ru.one.more.app.entities.Feed;
import ru.one.more.app.entities.FeedSource;
import ru.one.more.app.entities.ItemRule;
import ru.one.more.app.entities.SourceRule;
import ru.one.more.parsers.rule.ParserResult;
import ru.one.more.parsers.rule.ParserRule;
import ru.one.more.parsers.util.XMLFinder;
import ru.one.more.util.StrUtils;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;



/**
 * Created by aboba on 24.01.17.
 */
public class XmlFeedsParser {

    public Optional<RuledParser> withRule(ParserRule parserRule) {
        return Optional.of(new RuledParser(parserRule));
    }

    public class RuledParser {
        final ParserRule parserRule;
        RuledParser(ParserRule parserRule) {
            this.parserRule = parserRule;
        }

        public Optional<ParserResult> parse(String xmlString) {
            return parse(IOUtils.toInputStream(xmlString));
        }

        public Optional<ParserResult> parse(InputStream is) {
            return parseToResult(parserRule, is);
        }
    }

    private Optional<ParserResult> parseToResult(ParserRule parserRule, InputStream is) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(is);

            FeedSource feedSource = fetchFeedSource(doc, parserRule);

            String channelItemQ = "//" + parserRule.getSourceRule().getRootSearchTag()
                    + "/" +parserRule.getSourceRule().getItemTag();
            Optional<List<Node>> itemNodesOpt = XMLFinder.findElementList(doc, channelItemQ);

            List<Feed> feeds = itemNodesOpt.map(itemNodes -> fetchFeeds(itemNodes, parserRule, feedSource))
                    .orElse(new ArrayList<>());

            feedSource.setParseRule(parserRule.getSourceRule());

            return Optional.of(new ParserResult(feedSource, feeds));
        } catch (DocumentException e) {
            return Optional.empty();
        }
    }


    private FeedSource fetchFeedSource(Document doc, ParserRule parserRule) {
        FeedSource feedSource = new FeedSource();
        SourceRule sourceRule = parserRule.getSourceRule();
        List<Supplier<String>> ruleTagNames = Arrays.asList(sourceRule::getTitleTag, sourceRule::getDescriptionTag,
                sourceRule::getLanguageTag, sourceRule::getUrlTag);
        List<Consumer<String>> ruleSetters = Arrays.asList(feedSource::setTitle,  feedSource::setDescription,
                feedSource::setLang, feedSource::setUrl);
        int i = 0;
        for (Consumer<String> ruleSetter : ruleSetters) {
            String query = "//" + parserRule.getSourceRule().getRootSearchTag() +
                    "/" + ruleTagNames.get(i++).get();
            XMLFinder.findElementText(doc, query).ifPresent(ruleSetter);
        }
        return feedSource;
    }


    private List<Feed> fetchFeeds(List<Node> itemNodes, ParserRule parserRule, FeedSource feedSource) {
        List<Feed> feeds = new ArrayList<>();

        ItemRule itemRule = parserRule.getSourceRule().getItemRule();

        List<Supplier<String>> itemRuleTagNames = Arrays.asList(
                itemRule::getItemTitleTag, itemRule::getItemDescriptionTag,
                itemRule::getItemPubDateTag, itemRule::getItemUrlTag);
                //itemRule::getItemThumbLinkTag);
        List<BiConsumer<Feed, String>> itemSetters = Arrays
                .asList(Feed::setTitle,
                        Feed::setShortContent,
                        (f, s)-> StrUtils.tryToParseDate(s).ifPresent(f::setPostDate),
                        Feed::setSourceLink);

        itemNodes.forEach(itemNode -> {
            Feed feed = new Feed();
            int i = 0;
            for (BiConsumer<Feed, String> itemSetter : itemSetters) {
                XMLFinder.findElementText(itemNode,  itemRuleTagNames.get(i++).get())
                        .ifPresent(value -> itemSetter.accept(feed, value));
            }
            feed.setSource(feedSource);
            feeds.add(feed);
        });

        return feeds;
    }

}
