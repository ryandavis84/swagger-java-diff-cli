/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger.parsers.impl;

import com.rdavis.swagger.parsers.RuleParser;
import com.rdavis.swagger.rules.Rule;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class RuleParserV1Test {
    @Test
    public void testParseCustomRules() throws Exception {
        RuleParser ruleParser = new RuleParserV1();
        File file = new File(this.getClass().getClassLoader().getResource("testRules.json").toURI());

        Map<String, Rule> customRules = ruleParser.parseCustomRules(file.getAbsolutePath());
        assertEquals(customRules.size() >= 1, true);
    }

}