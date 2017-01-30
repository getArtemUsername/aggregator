package ru.one.more.app.pages;

import org.apache.commons.validator.routines.UrlValidator;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.upload.services.UploadedFile;
import ru.one.more.app.entities.SourceRule;
import ru.one.more.app.services.FeedsDownloadService;
import ru.one.more.app.services.RulesService;
import ru.one.more.workers.FeedsDownloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.*;

/**
 * Created by aboba on 29.01.17.
 */
public class LoadForm {

    private static final String NEW_RULE_SEL = "Новое";

    @Property @Validate("required")
    private UploadedFile ruleFile;

    @Property @Validate("required")
    private String url;

    @Property
    private List<SourceRule> rules;

    @Property @Persist
    private boolean isCustom;

    @Property @Value(NEW_RULE_SEL)
    private String ruleName;

    @Inject
    FeedsDownloadService feedsDownloadService;


    @InjectComponent
    Form loadFeedForm;
    @InjectComponent
    Zone rssRule;

    @Inject
    Messages messages;

    @Inject
    RulesService rulesService;

    @SessionState(create = false)
    Map<String, SourceRule> rulesMap;

    void onActivate() {
        rules = rulesService.fetchRules();
        if (rulesMap == null) {
            rulesMap = new TreeMap<>();
        }
        if (rules.isEmpty()) {
            isCustom = true;
        }
        rules.forEach(rule -> rulesMap.put(rule.getName(), rule));
    }

    public Object onValueChangedFromRuleName(String rule)
    {
        isCustom = NEW_RULE_SEL.equals(rule);
        return rssRule.getBody();
    }

    public List<String> getRuleNames() {
        List<String> rules = new ArrayList<>();
        rules.add(NEW_RULE_SEL);
        rules.addAll(rulesMap.keySet());
        return rules;
    }

    void onValidateFromLoadFeedForm() {
        if (loadFeedForm.isValid()) {
            String filePath = null;
            if (isCustom) {
                try {
                    Path tempDir = Files.createTempDirectory("aggregator");
                    Path tempFile = Files.createTempFile(tempDir, "rule", String.valueOf(new Date().getTime()));
                    ruleFile.write(tempFile.toFile());
                    filePath = tempFile.toAbsolutePath().toString();
                } catch (IOException e) {
                    loadFeedForm.recordError(messages.get("error.upload"));
                }
            }
            if (!UrlValidator.getInstance().isValid(url)) {
                loadFeedForm.recordError(messages.get("error.bad.url"));
                return;
            }
            boolean isSuccessDownload = isCustom
                    ? feedsDownloadService.downloadFeeds(url, filePath)
                    : feedsDownloadService.downloadFeeds(url, rulesMap.get(ruleName));

            if (!isSuccessDownload) {
                loadFeedForm.recordError(messages.get("error.failed.download"));
            }
        } else {
            loadFeedForm.recordError(messages.get("error.empty.field"));
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        return Index.class;
    }
}