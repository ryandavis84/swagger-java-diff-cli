/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger.parsers.impl;

import com.rdavis.swagger.parsers.SwaggerDiffParser;
import com.rdavis.swagger.rules.BrokenRule;
import com.rdavis.swagger.rules.Rule;
import v2.io.swagger.models.Swagger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SwaggerDiffParserV1 implements SwaggerDiffParser {

    @Override
    public List<BrokenRule> applyRules(Swagger currentSwagger, Swagger deployedSwagger, Map<String, Rule> rules) {
        List<BrokenRule> brokenRules = new ArrayList<>();
        for (Map.Entry<String, Rule> ruleEntry : rules.entrySet()) {
            brokenRules.addAll(ruleEntry.getValue().validate(currentSwagger, deployedSwagger));
        }
        return brokenRules;
    }

}
