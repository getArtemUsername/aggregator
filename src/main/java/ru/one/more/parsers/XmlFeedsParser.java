package ru.one.more.parsers;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.one.more.app.entities.Feed;
import ru.one.more.app.entities.FeedSource;
import ru.one.more.app.entities.ItemRule;
import ru.one.more.app.entities.SourceRule;
import ru.one.more.parsers.rule.ParserResult;
import ru.one.more.parsers.rule.ParserRule;
import ru.one.more.parsers.util.XMLFinder;
import ru.one.more.util.DateUtils;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static ru.one.more.util.StrUtils.isNotWord;


/**
 * Created by aboba on 24.01.17.
 */
public class XmlFeedsParser {

    Logger logger = LoggerFactory.getLogger(this.getClass());

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
            if (isNotWord(feedSource.getTitle())) {
                logger.error("FeedSource = " + feedSource + ". feed source title must be mandatory");
                return Optional.empty();
            }

            String channelItemQ = "//" + parserRule.getSourceRule().getRootSearchTag()
                    + "/" +parserRule.getSourceRule().getItemTag();
            Optional<List<Node>> itemNodesOpt = XMLFinder.findElementList(doc, channelItemQ);

            DateUtils.reloadRulesIfNeed();
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
                sourceRule::getLanguageTag, sourceRule::getLinkTag);
        List<Consumer<String>> ruleSetters = Arrays.asList(feedSource::setTitle,  feedSource::setDescription,
                feedSource::setLang, feedSource::setLink);
        int i = 0;
        for (Consumer<String> ruleSetter : ruleSetters) {
            String query = "//" + parserRule.getSourceRule().getRootSearchTag() +
                    "/" + ruleTagNames.get(i++).get();
            XMLFinder.findElementText(doc, query).ifPresent(ruleSetter);
        }
        return feedSource;
    }


    private List<Feed> fetchFeeds(List<Node> itemNodes, ParserRule parserRule,
                                  FeedSource feedSource) {
        List<Feed> feeds = new ArrayList<>();

        ItemRule itemRule = parserRule.getSourceRule().getItemRule();

        List<Supplier<String>> itemRuleTagNames = Arrays.asList(
                itemRule::getItemTitleTag, itemRule::getItemDescriptionTag,
                itemRule::getItemPubDateTag, itemRule::getItemLinkTag);

        List<BiConsumer<Feed, String>> itemSetters = Arrays
                .asList(Feed::setTitle,
                        Feed::setShortContent,
                        (f, s)-> DateUtils.tryToParseDate(feedSource.getTitle(), s).ifPresent(f::setPostDate),
                        Feed::setLink);

        for (Node itemNode : itemNodes) {
            Feed feed = new Feed();
            int i = 0;
            for (BiConsumer<Feed, String> itemSetter : itemSetters) {
                XMLFinder.findElementText(itemNode,  itemRuleTagNames.get(i++).get())
                        .ifPresent(value -> itemSetter.accept(feed, value));
            }

            boolean houstonWeHaveAProblem = false;
            if (isNotWord(feed.getTitle())) {
                logger.warn("feed title is mandatory");
                houstonWeHaveAProblem = true;
            }
            if (feed.getPostDate() == null) {
                logger.warn("Feed "+ feed + ". date is mandatory. unknown format: "
                        + XMLFinder.findElementText(itemNode, itemRule.getItemPubDateTag())
                        + "\ninsert new format to df.rules");
                houstonWeHaveAProblem = true;
            }
            if (isNotWord(feed.getLink())) {
                logger.warn("Feed "+ feed + ". feed url is mandatory");
                houstonWeHaveAProblem = true;
            }
            if (houstonWeHaveAProblem) continue;

            feed.setSource(feedSource);
            feeds.add(feed);
        }

        return feeds;
    }

}
