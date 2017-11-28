/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger.parsers;

import com.rdavis.swagger.rules.Rule;

import java.io.IOException;
import java.util.Map;

public interface RuleParser {
    Map<String, Rule> parseCustomRules(String ruleFile) throws IOException;
}
