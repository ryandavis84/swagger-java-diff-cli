/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger.cmd;

import com.rdavis.swagger.parsers.RuleParser;
import com.rdavis.swagger.parsers.SwaggerDiffParser;
import com.rdavis.swagger.parsers.impl.RuleParserV1;
import com.rdavis.swagger.parsers.impl.SwaggerDiffParserV1;
import com.rdavis.swagger.rules.BrokenRule;
import com.rdavis.swagger.rules.Rule;
import io.airlift.airline.Command;
import io.airlift.airline.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import v2.io.swagger.models.Swagger;
import v2.io.swagger.parser.SwaggerParser;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Command(name = "diff")
public class Diff implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Diff.class);

    @Option(name = {"-c", "--current"},
            description = "Current Swagger Document",
            required = true
    )
    private String currentSwaggerDoc = null;


    @Option(name = {"-d", "--deployed"},
            description = "Deployed Swagger Document",
            required = true
    )
    private String deployedSwaggerDoc = null;

    @Option(name = {"-r", "--rules"},
            description = "Swagger Diff Rules"
    )
    private String swaggerRules = null;


    @Override
    public void run() {
        RuleParser ruleParser = new RuleParserV1();
        Map<String, Rule> rules = null;
        if (swaggerRules != null) {
            try {
                rules = ruleParser.parseCustomRules(swaggerRules);
                LOGGER.info("rules:" + rules);
            } catch (IOException ioe) {
                LOGGER.error("Can not read or parse swagger rules", ioe);
                System.exit(1);
            }
        }

        Swagger currentSwagger = parseSwagger(currentSwaggerDoc);
        Swagger deployedSwagger = parseSwagger(deployedSwaggerDoc);

        SwaggerDiffParser swaggerDiffParser = new SwaggerDiffParserV1();
        List<BrokenRule> brokenRules = swaggerDiffParser.applyRules(currentSwagger, deployedSwagger, rules);
        if (brokenRules.size() > 0) {
            for (BrokenRule brokenRule : brokenRules) {
                LOGGER.error(brokenRule.getDescription());
            }
            System.exit(1);
        }

    }

    private Swagger parseSwagger(String file) {
        SwaggerParser swaggerParser = new SwaggerParser();
        return swaggerParser.read(file);
    }
}
