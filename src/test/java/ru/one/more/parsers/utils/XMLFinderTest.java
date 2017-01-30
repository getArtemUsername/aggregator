package ru.one.more.parsers.utils;

import co.unruly.matchers.OptionalMatchers;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.one.more.parsers.XmlFeedsParserTest;
import ru.one.more.parsers.util.XMLFinder;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.any;
import static org.junit.Assert.assertThat;
import static ru.one.more.parsers.util.XMLFinder.findElementText;

/**
 * Created by aboba on 27.01.17.
 */
public class XMLFinderTest {
    Document d;
    @Before
    public void pre() throws DocumentException {
        InputStream rbcIS = XMLFinderTest.class.getClassLoader().getResourceAsStream("in/rbc-rss.xml");
        SAXReader reader = new SAXReader();
        d = reader.read(rbcIS);
    }

    @Test
    public void testFindNode() {
        Optional<String> titleText = XMLFinder.findElementText(d, "//channel/title");
        assertThat(titleText, OptionalMatchers.contains("РБК - Все материалы"));
    }

    @Test
    public void testFindNodes() {
        Optional<List<Node>> nodesOpt = XMLFinder.findElementList(d, "//channel/item");
        assertThat(nodesOpt, OptionalMatchers.contains(any(List.class)));
        List<Node> nodes = nodesOpt.get();
        assertThat(nodes, hasSize(159));
        Node itemTitleNode = nodes.get(0).selectSingleNode("title");
        assertThat(itemTitleNode, notNullValue());
        assertThat(nodes.get(0).selectSingleNode("title").getText(),
                is("Volkswagen обязали выплатить $1,2 млрд дилерам за «дизельгейт»"));
    }

    @Test
    public void testFindAttributeText() throws DocumentException {
        SAXReader reader = new SAXReader();
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><feed><title>Title</title><author name=\"Test\"/></feed>";
        d = reader.read(IOUtils.toInputStream(xml));
        Optional<String> authorName = XMLFinder.findElementText(d, "//author@name");
        assertThat(authorName, OptionalMatchers.contains("Test"));
    }

}
