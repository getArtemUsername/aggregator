package ru.one.more.parsers;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.one.more.app.entities.Feed;
import ru.one.more.app.entities.FeedSource;
import ru.one.more.app.entities.ItemRule;
import ru.one.more.parsers.rule.ParserResult;
import ru.one.more.parsers.rule.ParserRule;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static ru.one.more.app.entities.FeedSource.SourceType.RSS;


/**
 * Created by aboba on 24.01.17.
 */
public class RssParser {


    public Optional<RuledParser> withRule(ParserRule parserRule) {
        return Optional.of(new RuledParser(parserRule));
    }

    class RuledParser {
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
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            XPathFactory xFactory = XPathFactory.newInstance();
            XPath xpath = xFactory.newXPath();

            FeedSource feedSource = new FeedSource();
            feedSource.setSourceType(RSS);
            String channelTitleQ = "//channel/" + parserRule.getSourceRule().getChannelTitleTag() + "/text()";
            XPathExpression expr = xpath.compile(channelTitleQ);
            String result = (String)expr.evaluate(doc, XPathConstants.STRING);
            feedSource.setTitle(result);

            String channelDescriptionQ = "//channel/" + parserRule.getSourceRule().getChannelDescriptionTag() + "/text()";
            expr = xpath.compile(channelDescriptionQ);
            result = (String)expr.evaluate(doc, XPathConstants.STRING);
            feedSource.setDescription(result);

            String channelLangQ = "//channel/" + parserRule.getSourceRule().getChannelLanguageTag() + "/text()";
            expr = xpath.compile(channelLangQ);
            result = (String)expr.evaluate(doc, XPathConstants.STRING);
            feedSource.setLang(result);

            String channelUrlQ = "//channel/" + parserRule.getSourceRule().getChannelUrlTag() + "/text()";
            expr = xpath.compile(channelUrlQ);
            result = (String)expr.evaluate(doc, XPathConstants.STRING);
            feedSource.setUrl(result);

            ItemRule itemRule = parserRule.getSourceRule().getItemRule();
            String channelItemQ = "//channel/" + parserRule.getSourceRule().getChannelItemTag();
            expr = xpath.compile(channelItemQ);
            NodeList items = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            List<Feed> feeds = new ArrayList<>();
            for (int i = 0; i < items.getLength(); i++) {
                Node node = items.item(i);
                node.getParentNode().removeChild(node);
                NodeList itemInfo = node.getChildNodes();
                Feed feed = new Feed();
                for (int j = 0; j < itemInfo.getLength(); j++) {
                    Node itemProperty = itemInfo.item(j);
                    String propertyName = itemProperty.getNodeName();
                    String propertyValue = itemProperty.getTextContent();
                    if (propertyName.equals(itemRule.getItemTitleTag())) feed.setTitle(propertyValue);
                    else if (propertyName.equals(itemRule.getItemDescriptionTag())) feed.setShortContent(propertyValue);
                    else if (propertyName.equals(itemRule.getItemPubDateTag())) feed.setPostDate(parseDate(propertyValue));
                    else if (propertyName.equals(itemRule.getItemUrlTag())) feed.setSourceLink(propertyValue);
                }
                feed.setSource(feedSource);
                feeds.add(feed);
            }
            return Optional.of(new ParserResult(feedSource, feeds));
        } catch (ParserConfigurationException | XPathExpressionException
                | SAXException | IOException e) {
            return Optional.empty();
        }
    }

    private Date parseDate(String propertyValue) {
        //ToDo
        return new Date();
    }

}
