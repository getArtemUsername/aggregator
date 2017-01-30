package ru.one.more.parsers.rule;

import co.unruly.matchers.OptionalMatchers;
import org.junit.Test;
import ru.one.more.app.entities.SourceRule;

import java.net.URL;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by aboba on 24.01.17.
 */
public class ParserRuleTest {
    @Test
    public void testRssRuleCreation() {
        URL ruleFile = ParserRuleTest.class.getClassLoader().getResource("rules/rss.rule");
        Optional<ParserRule> parserRule  = ParserRule.from(ruleFile.getPath());
        assertThat(parserRule, OptionalMatchers.contains(any(ParserRule.class)));
        ParserRule rule = parserRule.get();
        assertThat(rule.getSourceRule().getName(), is("RSS"));
        assertThat(rule.getSourceRule().getDataType(), is(SourceRule.DataType.XML));
        assertThat(rule.getSourceRule().getTitleTag(), is("title"));
        assertThat(rule.getSourceRule().getDescriptionTag(), is("description"));
        assertThat(rule.getSourceRule().getLanguageTag(), is("language"));
        assertThat(rule.getSourceRule().getItemTag(), is("item"));
        assertThat(rule.getSourceRule().getLinkTag(), is("link"));
        assertThat(rule.getSourceRule().getItemRule().getItemTitleTag(), is("title"));
        assertThat(rule.getSourceRule().getItemRule().getItemDescriptionTag(), is("description"));
        assertThat(rule.getSourceRule().getItemRule().getItemPubDateTag(), is("pubDate"));
        assertThat(rule.getSourceRule().getItemRule().getItemLinkTag(), is("link"));
    }
}
