/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger.rules.impl;

import com.google.common.base.CaseFormat;
import com.rdavis.swagger.rules.BrokenRule;
import com.rdavis.swagger.rules.RuleV2;
import v2.io.swagger.models.Swagger;

import java.util.ArrayList;
import java.util.List;

public class EditBasePath implements RuleV2 {
    private boolean _isBreakingChange = true;

    public boolean isBreakingChange() {
        return _isBreakingChange;
    }

    public void setBreakingChange(boolean isBreakingChange) {
        _isBreakingChange = isBreakingChange;
    }

    public String getRuleName() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.getClass().getSimpleName());
    }

    public List<BrokenRule> validate(Swagger currentSwagger, Swagger deployedSwagger) {
        List<BrokenRule> brokenRules = new ArrayList<>();

        if (isBreakingChange() &&
                !deployedSwagger.getBasePath().equalsIgnoreCase(currentSwagger.getBasePath())) {
            BrokenRule brokenRule = new BrokenRule();
            brokenRule.setRule(this);
            brokenRule.setDescription("Base path was modified");
            brokenRules.add(brokenRule);
        }

        return brokenRules;
    }
}
