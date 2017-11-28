/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger.rules.impl;

import com.google.common.base.CaseFormat;
import com.rdavis.swagger.rules.BrokenRule;
import com.rdavis.swagger.rules.RuleV2;
import v2.io.swagger.models.*;
import v2.io.swagger.models.parameters.Parameter;
import v2.io.swagger.models.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AddDescription implements RuleV2 {
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

        // Search all models for new descriptions
        if (isBreakingChange() && currentSwagger.getDefinitions() != null) {
            for (Map.Entry<String, Model> modelEntry : currentSwagger.getDefinitions().entrySet()) {
                String modelDescription = modelEntry.getValue().getDescription();
                if (deployedSwagger.getDefinitions() != null &&
                        deployedSwagger.getDefinitions().containsKey(modelEntry.getKey()) &&
                        deployedSwagger.getDefinitions().get(modelEntry.getKey()).getDescription() == null && modelDescription != null) {
                    BrokenRule brokenRule = new BrokenRule();
                    brokenRule.setRule(this);
                    brokenRule.setDescription("Description for " + modelEntry.getKey() + " was added");
                    brokenRules.add(brokenRule);
                }

                for (Map.Entry<String, Property> propertyEntry : modelEntry.getValue().getProperties().entrySet()) {
                    String currentDescription = propertyEntry.getValue().getDescription();
                    if (deployedSwagger.getDefinitions().containsKey(modelEntry.getKey()) &&
                            deployedSwagger.getDefinitions().get(modelEntry.getKey()).getProperties().containsKey(propertyEntry.getKey())) {
                        String deployedDescription = deployedSwagger.getDefinitions().get(modelEntry.getKey()).getProperties().get(propertyEntry.getKey()).getDescription();
                        if (deployedDescription == null && currentDescription != null) {

                            BrokenRule brokenRule = new BrokenRule();
                            brokenRule.setRule(this);
                            brokenRule.setDescription("Description for " + propertyEntry.getKey() + " was added");
                            brokenRules.add(brokenRule);

                        }
                    }

                }
            }
        }

        if (isBreakingChange() && deployedSwagger.getPaths() != null) {
            for (Map.Entry<String, Path> pathEntry : deployedSwagger.getPaths().entrySet()) {
                if (pathEntry.getValue().getOperationMap() != null) {
                    for (Map.Entry<HttpMethod, Operation> operationEntry : pathEntry.getValue().getOperationMap().entrySet()) {
                        if (operationEntry.getValue().getParameters() != null) {
                            for (Parameter parameter : operationEntry.getValue().getParameters()) {
                                if (currentSwagger.getPaths().containsKey(pathEntry.getKey()) &&
                                        currentSwagger.getPaths().get(pathEntry.getKey()).getOperationMap() != null &&
                                        currentSwagger.getPaths().get(pathEntry.getKey()).getOperationMap().containsKey(operationEntry.getKey()) &&
                                        currentSwagger.getPaths().get(pathEntry.getKey()).getOperationMap().get(operationEntry.getKey()).getParameters() != null) {
                                    String deployedDescription = parameter.getDescription();
                                    for (Parameter param : currentSwagger.getPath(pathEntry.getKey()).getOperationMap().get(operationEntry.getKey()).getParameters()) {
                                        if (parameter.getName().equalsIgnoreCase(param.getName())) {
                                            String currentDescription = param.getDescription();
                                            if ((currentDescription != null && deployedDescription == null)) {
                                                BrokenRule brokenRule = new BrokenRule();
                                                brokenRule.setRule(this);
                                                brokenRule.setDescription("Description for " + param.getName() + " was edited");
                                                brokenRules.add(brokenRule);
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }

        String currentDocsDescription = currentSwagger.getExternalDocs().getDescription();
        String deployedDocsDescription = deployedSwagger.getExternalDocs().getDescription();

        if ((currentDocsDescription != null && deployedDocsDescription == null) && isBreakingChange()) {
            BrokenRule brokenRule = new BrokenRule();
            brokenRule.setRule(this);
            brokenRule.setDescription("Description for external docs was added");
            brokenRules.add(brokenRule);
        }

        // Search Paths for new descriptions
        if (isBreakingChange() && currentSwagger.getPaths() != null) {
            for (Map.Entry<String, Path> pathEntry : currentSwagger.getPaths().entrySet()) {
                if (pathEntry.getValue().getOperationMap() != null) {
                    for (Map.Entry<HttpMethod, Operation> operationEntry : pathEntry.getValue().getOperationMap().entrySet()) {
                        String currentDescription = operationEntry.getValue().getDescription();
                        String deployedDescription = deployedSwagger.getPath(pathEntry.getKey()).getOperationMap().get(operationEntry.getKey()).getDescription();

                        if ((currentDescription != null && deployedDescription == null)) {
                            BrokenRule brokenRule = new BrokenRule();
                            brokenRule.setRule(this);
                            brokenRule.setDescription("Description for " + operationEntry.getKey() + " was added");
                            brokenRules.add(brokenRule);
                        }
                    }
                }
            }
        }

        return brokenRules;
    }
}
