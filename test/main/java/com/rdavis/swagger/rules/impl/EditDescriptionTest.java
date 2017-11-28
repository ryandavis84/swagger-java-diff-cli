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

public class EditDescriptionTest {

    private Swagger swaggerDeployed;
    private Swagger swaggerDefinition;
    private Swagger swaggerParameter;
    private Swagger swaggerProperty;
    private Swagger swaggerOperation;
    private Swagger swaggerExternalDoc;
    private Swagger swaggerExternalDocModified;

    @BeforeClass
    public void setup() throws Exception {
        File file1 = new File(getClass().getClassLoader().getResource("swaggerEditDefinitionDescription.json").toURI());
        File file2 = new File(getClass().getClassLoader().getResource("swaggerEditParameterDescription.json").toURI());
        File file3 = new File(getClass().getClassLoader().getResource("swaggerEditPropertyDescription.json").toURI());
        File file4 = new File(getClass().getClassLoader().getResource("swaggerEditOperationDescription.json").toURI());
        File file5 = new File(getClass().getClassLoader().getResource("swaggerEditExternalDocsDescription.json").toURI());
        File file6 = new File(getClass().getClassLoader().getResource("swaggerExternalDocModified.json").toURI());
        File deployedFile = new File(getClass().getClassLoader().getResource("swagger.json").toURI());
        swaggerDeployed = new SwaggerParser().read(deployedFile.getAbsolutePath());
        swaggerDefinition = new SwaggerParser().read(file1.getAbsolutePath());
        swaggerParameter = new SwaggerParser().read(file2.getAbsolutePath());
        swaggerProperty = new SwaggerParser().read(file3.getAbsolutePath());
        swaggerOperation = new SwaggerParser().read(file4.getAbsolutePath());
        swaggerExternalDoc = new SwaggerParser().read(file5.getAbsolutePath());
        swaggerExternalDocModified = new SwaggerParser().read(file6.getAbsolutePath());
    }

    @Test
    public void testSetBreakingChange() throws Exception {
        EditDescription editDescription = new EditDescription();
        boolean defaultValue = editDescription.isBreakingChange();
        editDescription.setBreakingChange(true);
        boolean newValue = editDescription.isBreakingChange();
        assertNotEquals(defaultValue, newValue);
    }

    @Test
    public void testIsBreakingChange() throws Exception {
        assertEquals(new EditDescription().isBreakingChange(), false);
    }

    @Test
    public void testGetRuleName() throws Exception {
        String ruleName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, EditDescription.class.getSimpleName());
        assertEquals(new EditDescription().getRuleName(), ruleName);
    }

    @Test
    public void testValidateBreakingOnDefinitions() throws Exception {
        EditDescription EditDescription = new EditDescription();
        EditDescription.setBreakingChange(true);
        List<BrokenRule> brokenRulesList = EditDescription.validate(swaggerDefinition, swaggerDeployed);
        assertEquals(brokenRulesList.size() >= 1, true);
    }

    @Test
    public void testValidateBreakingOnParameters() throws Exception {
        EditDescription EditDescription = new EditDescription();
        EditDescription.setBreakingChange(true);
        List<BrokenRule> brokenRulesList = EditDescription.validate(swaggerParameter, swaggerDeployed);
        assertEquals(brokenRulesList.size() >= 1, true);
    }

    @Test
    public void testValidateBreakingOnProperties() throws Exception {
        EditDescription EditDescription = new EditDescription();
        EditDescription.setBreakingChange(true);
        List<BrokenRule> brokenRulesList = EditDescription.validate(swaggerProperty, swaggerDeployed);
        assertEquals(brokenRulesList.size() >= 1, true);
    }

    @Test
    public void testValidateBreakingOnExternalDocs() throws Exception {
        EditDescription EditDescription = new EditDescription();
        EditDescription.setBreakingChange(true);
        List<BrokenRule> brokenRulesList = EditDescription.validate(swaggerExternalDoc, swaggerExternalDocModified);
        assertEquals(brokenRulesList.size() >= 1, true);
    }

    @Test
    public void testValidateBreakingOnOperations() throws Exception {
        EditDescription EditDescription = new EditDescription();
        EditDescription.setBreakingChange(true);
        List<BrokenRule> brokenRulesList = EditDescription.validate(swaggerOperation, swaggerDeployed);
        assertEquals(brokenRulesList.size() >= 1, true);
    }


    @Test
    public void testValidateBreakingOff() throws Exception {
        EditDescription EditDescription = new EditDescription();
        EditDescription.setBreakingChange(false);
        List<BrokenRule> brokenRulesList = EditDescription.validate(swaggerOperation, swaggerDeployed);
        assertEquals(brokenRulesList.size() == 0, true);
    }

}