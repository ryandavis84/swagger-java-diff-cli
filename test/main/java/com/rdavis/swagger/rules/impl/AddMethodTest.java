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

public class AddMethodTest {

    private Swagger swagger;
    private Swagger swaggerDeployed;

    @BeforeClass
    public void setup() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("swaggerAddMethod.json").toURI());
        File deployedFile = new File(getClass().getClassLoader().getResource("swagger.json").toURI());
        swagger = new SwaggerParser().read(file.getAbsolutePath());
        swaggerDeployed = new SwaggerParser().read(deployedFile.getAbsolutePath());
    }

    @Test
    public void testSetBreakingChange() throws Exception {
        AddMethod addMethod = new AddMethod();
        boolean defaultValue = addMethod.isBreakingChange();
        addMethod.setBreakingChange(true);
        boolean newValue = addMethod.isBreakingChange();
        assertNotEquals(defaultValue, newValue);
    }

    @Test
    public void testIsBreakingChange() throws Exception {
        assertEquals(new AddMethod().isBreakingChange(), false);
    }

    @Test
    public void testGetRuleName() throws Exception {
        String ruleName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, AddMethod.class.getSimpleName());
        assertEquals(new AddMethod().getRuleName(), ruleName);
    }

    @Test
    public void testValidateBreakingOn() throws Exception {
        AddMethod addMethod = new AddMethod();
        addMethod.setBreakingChange(true);
        List<BrokenRule> brokenRulesList = addMethod.validate(swagger, swaggerDeployed);
        assertEquals(brokenRulesList.size() >= 1, true);
    }

    @Test
    public void testValidateBreakingOff() throws Exception {
        AddMethod addMethod = new AddMethod();
        addMethod.setBreakingChange(false);
        List<BrokenRule> brokenRulesList = addMethod.validate(swagger, swaggerDeployed);
        assertEquals(brokenRulesList.size() == 0, true);
    }

}