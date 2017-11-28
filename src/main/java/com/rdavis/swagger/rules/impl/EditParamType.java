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
import v2.io.swagger.models.parameters.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditParamType implements RuleV2 {
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
                deployedSwagger.getPaths() != null) {
            for (Map.Entry<String, Path> pathEntry : deployedSwagger.getPaths().entrySet()) {
                if (pathEntry.getValue().getOperationMap() != null) {
                    for (Map.Entry<HttpMethod, Operation> operationEntry : pathEntry.getValue().getOperationMap().entrySet()) {
                        if (operationEntry.getValue().getParameters() != null) {
                            for (Parameter parameter : operationEntry.getValue().getParameters()) {
                                String currentType = null;
                                if (parameter.getIn().equalsIgnoreCase("path")) {
                                    PathParameter param = (PathParameter) parameter;
                                    currentType = param.getType();
                                } else if (parameter.getIn().equalsIgnoreCase("query")) {
                                    QueryParameter param = (QueryParameter) parameter;
                                    currentType = param.getType();
                                } else if (parameter.getIn().equalsIgnoreCase("formData")) {
                                    FormParameter param = (FormParameter) parameter;
                                    currentType = param.getType();
                                } else if (parameter.getIn().equalsIgnoreCase("cookie")) {
                                    CookieParameter param = (CookieParameter) parameter;
                                    currentType = param.getType();
                                } else if (parameter.getIn().equalsIgnoreCase("header")) {
                                    HeaderParameter param = (HeaderParameter) parameter;
                                    currentType = param.getType();
                                }

                                if (currentSwagger.getPaths() != null &&
                                        currentSwagger.getPaths().containsKey(pathEntry.getKey()) &&
                                        currentSwagger.getPaths().get(pathEntry.getKey()).getOperationMap() != null &&
                                        currentSwagger.getPaths().get(pathEntry.getKey()).getOperationMap().containsKey(operationEntry.getKey())) {
                                    List<Parameter> parameters = currentSwagger.getPaths().get(pathEntry.getKey()).getOperationMap().get(operationEntry.getKey()).getParameters();
                                    for (Parameter parameter1 : parameters) {
                                        String deployedType = null;
                                        if (parameter.getName().equalsIgnoreCase(parameter1.getName())) {
                                            if (parameter.getIn().equalsIgnoreCase("path")) {
                                                PathParameter param = (PathParameter) parameter1;
                                                deployedType = param.getType();
                                            } else if (parameter.getIn().equalsIgnoreCase("query")) {
                                                QueryParameter param = (QueryParameter) parameter1;
                                                deployedType = param.getType();
                                            } else if (parameter.getIn().equalsIgnoreCase("formData")) {
                                                FormParameter param = (FormParameter) parameter1;
                                                deployedType = param.getType();
                                            } else if (parameter.getIn().equalsIgnoreCase("cookie")) {
                                                CookieParameter param = (CookieParameter) parameter1;
                                                deployedType = param.getType();
                                            } else if (parameter.getIn().equalsIgnoreCase("header")) {
                                                HeaderParameter param = (HeaderParameter) parameter1;
                                                deployedType = param.getType();
                                            }
                                        }
                                        if (currentType != null && deployedType != null && !currentType.equalsIgnoreCase(deployedType)) {
                                            BrokenRule brokenRule = new BrokenRule();
                                            brokenRule.setRule(this);
                                            brokenRule.setDescription("Parameter Type Modified for" + pathEntry.getKey() + " " + operationEntry.getValue().getOperationId() + " " + parameter.getName());
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
        return brokenRules;
    }
}
