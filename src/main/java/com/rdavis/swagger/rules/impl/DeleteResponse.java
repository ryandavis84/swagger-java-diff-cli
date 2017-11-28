/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger.rules.impl;

import com.google.common.base.CaseFormat;
import com.rdavis.swagger.rules.BrokenRule;
import com.rdavis.swagger.rules.RuleV2;
import v2.io.swagger.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteResponse implements RuleV2 {
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

        if (isBreakingChange() && deployedSwagger.getPaths() != null) {
            for (Map.Entry<String, Path> pathEntry : deployedSwagger.getPaths().entrySet()) {
                if (pathEntry.getValue().getOperationMap() != null) {
                    for (Map.Entry<HttpMethod, Operation> operationEntry : pathEntry.getValue().getOperationMap().entrySet()) {
                        if (operationEntry.getValue().getResponses() != null) {
                            for (Map.Entry<String, Response> responseEntry : operationEntry.getValue().getResponses().entrySet()) {
                                if (currentSwagger.getPaths().containsKey(pathEntry.getKey()) &&
                                        currentSwagger.getPaths().get(pathEntry.getKey()).getOperationMap().containsKey(operationEntry.getKey()) &&
                                        !currentSwagger.getPaths().get(pathEntry.getKey()).getOperationMap().get(operationEntry.getKey()).getResponses().containsKey(responseEntry.getKey())) {
                                    BrokenRule brokenRule = new BrokenRule();
                                    brokenRule.setRule(this);
                                    brokenRule.setDescription("Response for " + operationEntry.getKey() + " was deleted");
                                    brokenRules.add(brokenRule);
                                }
                            }
                        }
                    }
                }
            }
        }
        return brokenRules;
    }
}
