/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger.rules.impl;

import com.google.common.base.CaseFormat;
import com.rdavis.swagger.rules.BrokenRule;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import v2.io.swagger.models.Model;
import v2.io.swagger.models.ModelImpl;
import v2.io.swagger.models.Swagger;
import v2.io.swagger.parser.SwaggerParser;

import java.io.File;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class EditBasePathTest {

    private Swagger swagger;
    private Swagger swaggerDeployed;

    @BeforeClass
    public void setup() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("swaggerAddDefinition.json").toURI());
        File deployedFile = new File(getClass().getClassLoader().getResource("swagger.json").toURI());
        swagger = new SwaggerParser().read(file.getAbsolutePath());
        swaggerDeployed = new SwaggerParser().read(deployedFile.getAbsolutePath());
    }

    @Test
    public void testSetBreakingChange() throws Exception {
        EditBasePath editBasePath = new EditBasePath();
        boolean defaultValue = editBasePath.isBreakingChange();
        editBasePath.setBreakingChange(false);
        boolean newValue = editBasePath.isBreakingChange();
        assertNotEquals(defaultValue, newValue);
    }

    @Test
    public void testIsBreakingChange() throws Exception {
        assertEquals(new EditBasePath().isBreakingChange(), true);
    }

    @Test
    public void testGetRuleName() throws Exception {
        String ruleName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, EditBasePath.class.getSimpleName());
        assertEquals(new EditBasePath().getRuleName(), ruleName);
    }

    @Test
    public void testValidateBreakingOn() throws Exception {
        Model model = new ModelImpl();
        model.setDescription("This is a test");
        model.setTitle("This is a test");
        Swagger swagger = new SwaggerParser().read("http://petstore.swagger.io/v2/swagger.json");
        Swagger swaggerDeployed = new SwaggerParser().read("http://petstore.swagger.io/v2/swagger.json");

        swaggerDeployed.setBasePath("http://test.com");

        EditBasePath editBasePath = new EditBasePath();
        editBasePath.setBreakingChange(true);
        List<BrokenRule> brokenRulesList = editBasePath.validate(swagger, swaggerDeployed);

        assertEquals(brokenRulesList.size() >= 1, true);
    }

    @Test
    public void testValidateBreakingOff() throws Exception {
        Model model = new ModelImpl();
        model.setDescription("This is a test");
        model.setTitle("This is a test");
        Swagger swagger = new SwaggerParser().read("http://petstore.swagger.io/v2/swagger.json");
        Swagger swaggerDeployed = new SwaggerParser().read("http://petstore.swagger.io/v2/swagger.json");
        swaggerDeployed.setBasePath("http://test.com");
        EditBasePath editBasePath = new EditBasePath();
        editBasePath.setBreakingChange(false);
        List<BrokenRule> brokenRulesList = editBasePath.validate(swagger, swaggerDeployed);
        assertEquals(brokenRulesList.size() == 0, true);
    }

}