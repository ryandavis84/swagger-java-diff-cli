/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger.parsers;

import com.rdavis.swagger.rules.BrokenRule;
import com.rdavis.swagger.rules.Rule;
import v2.io.swagger.models.Swagger;

import java.util.List;
import java.util.Map;


public interface SwaggerDiffParser {

    List<BrokenRule> applyRules(Swagger currentSwagger, Swagger deployedSwagger, Map<String, Rule> rules);

}
