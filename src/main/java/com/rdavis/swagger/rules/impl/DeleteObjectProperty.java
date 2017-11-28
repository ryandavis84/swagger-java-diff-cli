/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger.rules.impl;

import com.google.common.base.CaseFormat;
import com.rdavis.swagger.rules.BrokenRule;
import com.rdavis.swagger.rules.RuleV2;
import v2.io.swagger.models.Model;
import v2.io.swagger.models.Swagger;
import v2.io.swagger.models.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteObjectProperty implements RuleV2 {
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
        if (isBreakingChange() && deployedSwagger.getDefinitions() != null) {
            for (Map.Entry<String, Model> modelEntry : deployedSwagger.getDefinitions().entrySet()) {
                if (modelEntry.getValue().getProperties() != null) {
                    for (Map.Entry<String, Property> propertyEntry : modelEntry.getValue().getProperties().entrySet()) {
                        if (currentSwagger.getDefinitions() != null &&
                                currentSwagger.getDefinitions().containsKey(modelEntry.getKey()) &&
                                currentSwagger.getDefinitions().get(modelEntry.getKey()).getProperties() != null &&
                                !currentSwagger.getDefinitions().get(modelEntry.getKey()).getProperties().containsKey(propertyEntry.getKey())) {
                            BrokenRule brokenRule = new BrokenRule();
                            brokenRule.setRule(this);
                            brokenRule.setDescription("Property was Deleted for " + propertyEntry.getKey());
                            brokenRules.add(brokenRule);
                        }
                    }
                }
            }
        }
        return brokenRules;
    }
}
