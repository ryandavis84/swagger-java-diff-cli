/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger.rules.impl;

import com.google.common.base.CaseFormat;
import com.rdavis.swagger.rules.BrokenRule;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import v2.io.swagger.models.*;
import v2.io.swagger.models.parameters.Parameter;
import v2.io.swagger.parser.SwaggerParser;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class DeleteParamTest {

    private Swagger swagger;
    private Swagger swaggerDeployed;

    @BeforeClass
    public void setup() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("swaggerDeleteParam.json").toURI());
        File deployedFile = new File(getClass().getClassLoader().getResource("swagger.json").toURI());
        swagger = new SwaggerParser().read(file.getAbsolutePath());
        swaggerDeployed = new SwaggerParser().read(deployedFile.getAbsolutePath());
    }

    @Test
    public void testSetBreakingChange() throws Exception {
        DeleteParam deleteParam = new DeleteParam();
        boolean defaultValue = deleteParam.isBreakingChange();
        deleteParam.setBreakingChange(true);
        boolean newValue = deleteParam.isBreakingChange();
        assertNotEquals(defaultValue, newValue);
    }

    @Test
    public void testIsBreakingChange() throws Exception {
        assertEquals(new DeleteParam().isBreakingChange(), false);
    }

    @Test
    public void testGetRuleName() throws Exception {
        String ruleName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, DeleteParam.class.getSimpleName());
        assertEquals(new DeleteParam().getRuleName(), ruleName);
    }

    @Test
    public void testValidateBreakingOn() throws Exception {
        Model model = new ModelImpl();
        model.setDescription("This is a test");
        model.setTitle("This is a test");
        Swagger swagger = new SwaggerParser().read("http://petstore.swagger.io/v2/swagger.json");
        Swagger swaggerDeployed = new SwaggerParser().read("http://petstore.swagger.io/v2/swagger.json");

        for (Map.Entry<String, Path> pathEntry : swaggerDeployed.getPaths().entrySet()) {
            if (pathEntry.getValue().getOperationMap() != null) {
                for (Map.Entry<HttpMethod, Operation> operationEntry : pathEntry.getValue().getOperationMap().entrySet()) {
                    if (operationEntry.getValue().getParameters() != null) {
                        for (Parameter parameter : operationEntry.getValue().getParameters()) {
                            swagger.getPath(pathEntry.getKey()).getOperationMap().get(operationEntry.getKey()).getParameters().remove(parameter);
                            break;
                        }
                    }
                    break;
                }
            }
            break;
        }

        DeleteParam addOptionalObjectProperty = new DeleteParam();
        addOptionalObjectProperty.setBreakingChange(true);
        List<BrokenRule> brokenRulesList = addOptionalObjectProperty.validate(swagger, swaggerDeployed);

        assertEquals(brokenRulesList.size() >= 1, true);
    }

    @Test
    public void testValidateBreakingOff() throws Exception {
        Model model = new ModelImpl();
        model.setDescription("This is a test");
        model.setTitle("This is a test");
        Swagger swagger = new SwaggerParser().read("http://petstore.swagger.io/v2/swagger.json");
        Swagger swaggerDeployed = new SwaggerParser().read("http://petstore.swagger.io/v2/swagger.json");
        for (Map.Entry<String, Path> pathEntry : swaggerDeployed.getPaths().entrySet()) {
            if (pathEntry.getValue().getOperationMap() != null) {
                for (Map.Entry<HttpMethod, Operation> operationEntry : pathEntry.getValue().getOperationMap().entrySet()) {
                    if (operationEntry.getValue().getParameters() != null) {
                        for (Parameter parameter : operationEntry.getValue().getParameters()) {
                            swagger.getPath(pathEntry.getKey()).getOperationMap().get(operationEntry.getKey()).getParameters().remove(parameter);
                            break;
                        }
                    }
                    break;
                }
            }
            break;
        }
        DeleteParam addOptionalObjectProperty = new DeleteParam();
        addOptionalObjectProperty.setBreakingChange(false);
        List<BrokenRule> brokenRulesList = addOptionalObjectProperty.validate(swagger, swaggerDeployed);
        assertEquals(brokenRulesList.size() == 0, true);
    }

}