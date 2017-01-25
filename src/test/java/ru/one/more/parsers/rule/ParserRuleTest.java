package ru.one.more.parsers.rule;

import co.unruly.matchers.OptionalMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by aboba on 24.01.17.
 */
public class ParserRuleTest {
    @Test
    public void defaultParserRuleAppliedIfRuleNotExist() {
        Optional<ParserRule> parserRule  = ParserRule.from("not_existed.rule");
        assertThat(parserRule, OptionalMatchers.contains(any(ParserRule.class)));
        ParserRule rule = parserRule.get();
        assertThat(rule.getSourceRule().getChannelTitleTag(), is("title"));
        assertThat(rule.getSourceRule().getChannelDescriptionTag(), is("description"));
        assertThat(rule.getSourceRule().getChannelLanguageTag(), is("language"));
        assertThat(rule.getSourceRule().getChannelItemTag(), is("item"));
        assertThat(rule.getSourceRule().getChannelUrlTag(), is("link"));
        assertThat(rule.getSourceRule().getItemRule().getItemTitleTag(), is("title"));
        assertThat(rule.getSourceRule().getItemRule().getItemDescriptionTag(), is("description"));
        assertThat(rule.getSourceRule().getItemRule().getItemPubDateTag(), is("pubDate"));
        assertThat(rule.getSourceRule().getItemRule().getItemUrlTag(), is("link"));

    }
}
