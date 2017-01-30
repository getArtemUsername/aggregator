package ru.one.more.parsers.util;

import org.dom4j.Document;
import org.dom4j.Node;

import java.util.List;
import java.util.Optional;

/**
 * Created by aboba on 27.01.17.
 */
public class XMLFinder {

    static public Optional<String> findElementText(Node doc, String xPathQuery) {
        Node nodeElement;
        if (xPathQuery.contains("@")) {
            String tag = xPathQuery.split("@")[0];
            String attr = xPathQuery.split("@")[1];
            nodeElement = doc.selectSingleNode(tag);
            nodeElement = nodeElement.selectSingleNode("@"+attr);

        } else {
            nodeElement = doc.selectSingleNode(xPathQuery);
        }
        return Optional.ofNullable(nodeElement!=null?nodeElement.getText():null);
    }

    @SuppressWarnings("unchecked")
    static public Optional<List<Node>> findElementList(Node doc, String xPathQuery) {
        List<Node> nodes = (List<Node>)doc.selectNodes(xPathQuery);
        return Optional.ofNullable(nodes);
    }
}
