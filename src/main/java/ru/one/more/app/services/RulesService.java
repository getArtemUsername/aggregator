package ru.one.more.app.services;

import ru.one.more.app.entities.SourceRule;

import java.util.List;

/**
 * Created by aboba on 30.01.17.
 */
public interface RulesService {
    List<SourceRule> fetchRules();
}
