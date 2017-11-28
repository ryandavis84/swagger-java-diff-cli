/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger.rules.impl;

import com.google.common.base.CaseFormat;
import com.rdavis.swagger.rules.BrokenRule;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import v2.io.swagger.models.Swagger;
import v2.io.swagger.parser.SwaggerParser;

import java.io.File;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class AddDescriptionTest {

    private Swagger swaggerDeployed;
    private Swagger swaggerDefinition;
    private Swagger swaggerParameter;
    private Swagger swaggerProperty;
    private Swagger swaggerOperation;
    private Swagger swaggerExternalDoc;

    @BeforeClass
    public void setup() throws Exception {
        File file1 = new File(getClass().getClassLoader().getResource("swaggerAddDefinitionDescription.json").toURI());
        File file2 = new File(getClass().getClassLoader().getResource("swaggerAddParameterDescription.json").toURI());
        File file3 = new File(getClass().getClassLoader().getResource("swaggerAddPropertyDescription.json").toURI());
        File file4 = new File(getClass().getClassLoader().getResource("swaggerAddOperationDescription.json").toURI());
        File file5 = new File(getClass().getClassLoader().getResource("swaggerAddExternalDocsDescription.json").toURI());
        File deployedFile = new File(getClass().getClassLoader().getResource("swagger.json").toURI());
        swaggerDeployed = new SwaggerParser().read(deployedFile.getAbsolutePath());
        swaggerDefinition = new SwaggerParser().read(file1.getAbsolutePath());
        swaggerParameter = new SwaggerParser().read(file2.getAbsolutePath());
        swaggerProperty = new SwaggerParser().read(file3.getAbsolutePath());
        swaggerOperation = new SwaggerParser().read(file4.getAbsolutePath());
        swaggerExternalDoc = new SwaggerParser().read(file5.getAbsolutePath());
    }

    @Test
    public void testSetBreakingChange() throws Exception {
        AddDescription addDescription = new AddDescription();
        boolean defaultValue = addDescription.isBreakingChange();
        addDescription.setBreakingChange(true);
        boolean newValue = addDescription.isBreakingChange();
        assertNotEquals(defaultValue, newValue);
    }

    @Test
    public void testIsBreakingChange() throws Exception {
        assertEquals(new AddDescription().isBreakingChange(), false);
    }

    @Test
    public void testGetRuleName() throws Exception {
        String ruleName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, AddDescription.class.getSimpleName());
        assertEquals(new AddDescription().getRuleName(), ruleName);
    }

    @Test
    public void testValidateBreakingOnDefinitions() throws Exception {
        AddDescription AddDescription = new AddDescription();
        AddDescription.setBreakingChange(true);
        List<BrokenRule> brokenRulesList = AddDescription.validate(swaggerDefinition, swaggerDeployed);
        assertEquals(brokenRulesList.size() >= 1, true);
    }

    @Test
    public void testValidateBreakingOnParameters() throws Exception {
        AddDescription AddDescription = new AddDescription();
        AddDescription.setBreakingChange(true);
        List<BrokenRule> brokenRulesList = AddDescription.validate(swaggerParameter, swaggerDeployed);
        assertEquals(brokenRulesList.size() >= 1, true);
    }

    @Test
    public void testValidateBreakingOnProperties() throws Exception {
        AddDescription AddDescription = new AddDescription();
        AddDescription.setBreakingChange(true);
        List<BrokenRule> brokenRulesList = AddDescription.validate(swaggerProperty, swaggerDeployed);
        assertEquals(brokenRulesList.size() >= 1, true);
    }

    @Test
    public void testValidateBreakingOnExternalDocs() throws Exception {
        AddDescription AddDescription = new AddDescription();
        AddDescription.setBreakingChange(true);
        List<BrokenRule> brokenRulesList = AddDescription.validate(swaggerExternalDoc, swaggerDeployed);
        assertEquals(brokenRulesList.size() >= 1, true);
    }

    @Test
    public void testValidateBreakingOnOperations() throws Exception {
        AddDescription AddDescription = new AddDescription();
        AddDescription.setBreakingChange(true);
        List<BrokenRule> brokenRulesList = AddDescription.validate(swaggerOperation, swaggerDeployed);
        assertEquals(brokenRulesList.size() >= 1, true);
    }


    @Test
    public void testValidateBreakingOff() throws Exception {
        AddDescription AddDescription = new AddDescription();
        AddDescription.setBreakingChange(false);
        List<BrokenRule> brokenRulesList = AddDescription.validate(swaggerOperation, swaggerDeployed);
        assertEquals(brokenRulesList.size() == 0, true);
    }
}