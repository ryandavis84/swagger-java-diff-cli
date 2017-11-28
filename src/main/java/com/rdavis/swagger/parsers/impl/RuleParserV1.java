/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger.parsers.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import com.rdavis.swagger.parsers.RuleParser;
import com.rdavis.swagger.rules.Rule;
import com.rdavis.swagger.rules.RuleV2;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RuleParserV1 implements RuleParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleParserV1.class);

    public Map<String, Rule> parseCustomRules(String fileName) throws IOException {
        Map<String, Rule> rules = gatherRules();
        if (null != fileName) {
            File file = new File(fileName);
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<HashMap<String, Boolean>> typeReference
                    = new TypeReference<HashMap<String, Boolean>>() {
            };
            Map<String, Boolean> customRules = objectMapper.readValue(file, typeReference);

            for (Map.Entry<String, Boolean> entry : customRules.entrySet()) {
                if (rules.containsKey(entry.getKey().toLowerCase())) {
                    Rule rule = rules.get(entry.getKey());
                    rule.setBreakingChange(entry.getValue());
                    rules.put(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, rule.getRuleName()), rule);
                    LOGGER.info("Submited Rule " + rule.getRuleName() + " to " + entry.getValue());
                }
            }
        }
        return rules;
    }

    private Map<String, Rule> gatherRules() {
        Map<String, Rule> defaultRules = new HashMap<>();
        Reflections reflections = new Reflections("com.rdavis.swagger.rules.impl");
        Set<Class<? extends RuleV2>> classes = reflections.getSubTypesOf(RuleV2.class);
        for (Class classRule : classes) {
            try {
                RuleV2 rule = (RuleV2) classRule.newInstance();
                defaultRules.put(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, rule.getRuleName()), rule);
            } catch (Exception e) {
                LOGGER.error("Exception occurred creating rule " + classRule, e);
            }
        }
        return defaultRules;
    }
}
