/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger.rules.impl;

import com.google.common.base.CaseFormat;
import com.rdavis.swagger.rules.BrokenRule;
import com.rdavis.swagger.rules.RuleV2;
import v2.io.swagger.models.HttpMethod;
import v2.io.swagger.models.Operation;
import v2.io.swagger.models.Path;
import v2.io.swagger.models.Swagger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditSummary implements RuleV2 {
    private boolean _isBreakingChange = false;

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
                currentSwagger.getPaths() != null) {
            for (Map.Entry<String, Path> pathEntry : currentSwagger.getPaths().entrySet()) {
                if (pathEntry.getValue().getOperationMap() != null) {
                    for (Map.Entry<HttpMethod, Operation> operationEntry : pathEntry.getValue().getOperationMap().entrySet()) {
                        if (deployedSwagger.getPaths() != null &&
                                deployedSwagger.getPaths().containsKey(pathEntry.getKey()) &&
                                deployedSwagger.getPaths().get(pathEntry.getKey()).getOperationMap() != null &&
                                deployedSwagger.getPaths().get(pathEntry.getKey()).getOperationMap().containsKey(operationEntry.getKey())) {
                            String deployedSummary = deployedSwagger.getPaths().get(pathEntry.getKey()).getOperationMap().get(operationEntry.getKey()).getSummary();
                            String currentSummary = operationEntry.getValue().getSummary();
                            if ((currentSummary != null && deployedSummary != null && !currentSummary.equalsIgnoreCase(deployedSummary)) ||
                                    (deployedSummary != null && currentSummary == null) ||
                                    (currentSummary != null && deployedSummary == null)) {
                                BrokenRule brokenRule = new BrokenRule();
                                brokenRule.setRule(this);
                                brokenRule.setDescription("Summary Modified for" + pathEntry.getKey() + " " + operationEntry.getValue().getOperationId());
                                brokenRules.add(brokenRule);
                            }
                        }
                    }
                }
            }
        }
        return brokenRules;
    }
}
