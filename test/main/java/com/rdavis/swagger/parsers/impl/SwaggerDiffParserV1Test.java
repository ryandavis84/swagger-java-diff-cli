/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger.parsers.impl;

import com.rdavis.swagger.parsers.RuleParser;
import com.rdavis.swagger.rules.BrokenRule;
import com.rdavis.swagger.rules.Rule;
import org.testng.annotations.Test;
import v2.io.swagger.models.Swagger;
import v2.io.swagger.parser.SwaggerParser;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class SwaggerDiffParserV1Test {
    @Test
    public void testApplyRules() throws Exception {
        SwaggerDiffParserV1 swaggerDiffParser = new SwaggerDiffParserV1();
        File file = new File(getClass().getClassLoader().getResource("swagger.json").toURI());
        File file2 = new File(getClass().getClassLoader().getResource("swaggerBroken.json").toURI());
        File rulesFile = new File(getClass().getClassLoader().getResource("testRules.json").toURI());
        Swagger deployedSwagger = new SwaggerParser().read(file.getAbsolutePath());
        Swagger currentSwagger = new SwaggerParser().read(file2.getAbsolutePath());
        RuleParser ruleParser = new RuleParserV1();
        Map<String, Rule> ruleMap = ruleParser.parseCustomRules(rulesFile.getAbsolutePath());

        List<BrokenRule> brokenRuleList = swaggerDiffParser.applyRules(currentSwagger, deployedSwagger, ruleMap);
        assertEquals(brokenRuleList.size() >= 1, true);
    }

}