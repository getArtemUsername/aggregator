package ru.one.more.app.services.impl;

import ru.one.more.app.entities.SourceRule;
import ru.one.more.app.services.RulesService;
import ru.one.more.workers.DataAccessHelper;

import java.util.List;

/**
 * Created by aboba on 30.01.17.
 */
public class RulesServiceImpl implements RulesService {
    @Override
    public List<SourceRule> fetchRules() {
        return DataAccessHelper.getInst().fetchRules();
    }
}
