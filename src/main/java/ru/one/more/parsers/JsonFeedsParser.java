package ru.one.more.parsers;

import com.google.gson.*;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.one.more.app.entities.Feed;
import ru.one.more.app.entities.FeedSource;
import ru.one.more.app.entities.SourceRule;
import ru.one.more.parsers.rule.ParserResult;
import ru.one.more.parsers.rule.ParserRule;
import ru.one.more.util.DateUtils;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.one.more.util.StrUtils.isNotWord;

/**
 * Created by aboba on 28.01.17.
 */
//ToDo
public class JsonFeedsParser {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public Optional<JsonFeedsParser.RuledParser> withRule(ParserRule parserRule) {
        return Optional.of(new JsonFeedsParser.RuledParser(parserRule));
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
        DocumentContext jsonDoc = JsonPath.parse(is);
        //jsonDoc.read();
        return Optional.empty();
    }


}
