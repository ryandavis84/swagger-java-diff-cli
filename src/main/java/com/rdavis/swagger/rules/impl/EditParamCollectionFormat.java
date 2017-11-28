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

public class EditParamCollectionFormat implements RuleV2 {
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
                        if (operationEntry.getValue().getParameters() != null) {
                            for (Parameter parameter : operationEntry.getValue().getParameters()) {
                                String collectionFormat = null;
                                if (parameter.getIn().equalsIgnoreCase("path")) {
                                    PathParameter param = (PathParameter) parameter;
                                    collectionFormat = param.getCollectionFormat();
                                } else if (parameter.getIn().equalsIgnoreCase("query")) {
                                    QueryParameter param = (QueryParameter) parameter;
                                    collectionFormat = param.getCollectionFormat();
                                } else if (parameter.getIn().equalsIgnoreCase("formData")) {
                                    FormParameter param = (FormParameter) parameter;
                                    collectionFormat = param.getCollectionFormat();
                                } else if (parameter.getIn().equalsIgnoreCase("cookie")) {
                                    CookieParameter param = (CookieParameter) parameter;
                                    collectionFormat = param.getCollectionFormat();
                                } else if (parameter.getIn().equalsIgnoreCase("header")) {
                                    HeaderParameter param = (HeaderParameter) parameter;
                                    collectionFormat = param.getCollectionFormat();
                                }

                                if (collectionFormat != null &&
                                        currentSwagger.getPaths().containsKey(pathEntry.getKey()) &&
                                        currentSwagger.getPath(pathEntry.getKey()).getOperationMap().containsKey(operationEntry.getKey())) {
                                    List<Parameter> params = currentSwagger.getPath(pathEntry.getKey()).getOperationMap().get(operationEntry.getKey()).getParameters();
                                    String localCollectionFormation = null;
                                    for (Parameter param : params) {
                                        if (param.getName().equalsIgnoreCase(parameter.getName())) {
                                            if (param.getIn().equalsIgnoreCase("path")) {
                                                PathParameter localParam = (PathParameter) param;
                                                localCollectionFormation = localParam.getCollectionFormat();
                                            } else if (param.getIn().equalsIgnoreCase("query")) {
                                                QueryParameter localParam = (QueryParameter) param;
                                                localCollectionFormation = localParam.getCollectionFormat();
                                            } else if (param.getIn().equalsIgnoreCase("formdata")) {
                                                FormParameter localParam = (FormParameter) param;
                                                localCollectionFormation = localParam.getCollectionFormat();
                                            } else if (param.getIn().equalsIgnoreCase("cookie")) {
                                                CookieParameter localParam = (CookieParameter) param;
                                                localCollectionFormation = localParam.getCollectionFormat();
                                            } else if (param.getIn().equalsIgnoreCase("header")) {
                                                HeaderParameter localParam = (HeaderParameter) param;
                                                localCollectionFormation = localParam.getCollectionFormat();
                                            }
                                            break;
                                        }
                                    }
                                    if (localCollectionFormation != null &&
                                            !localCollectionFormation.equalsIgnoreCase(collectionFormat)) {
                                        BrokenRule brokenRule = new BrokenRule();
                                        brokenRule.setRule(this);
                                        brokenRule.setDescription("Collection Format for " + parameter + " was edited");
                                        brokenRules.add(brokenRule);
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
