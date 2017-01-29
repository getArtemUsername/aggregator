package ru.one.more.app.pages;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.upload.services.UploadedFile;

/**
 * Created by aboba on 29.01.17.
 */
public class LoadForm {

    @Property
    private UploadedFile ruleFile;

    @Property
    private String url;

    public void onSuccess() {

    }
}