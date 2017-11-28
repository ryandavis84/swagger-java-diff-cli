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

public class EditParamInTest {

    private Swagger swagger;
    private Swagger swaggerDeployed;

    @BeforeClass
    public void setup() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("swaggerEditParamInTest.json").toURI());
        File deployedFile = new File(getClass().getClassLoader().getResource("swagger.json").toURI());
        swagger = new SwaggerParser().read(file.getAbsolutePath());
        swaggerDeployed = new SwaggerParser().read(deployedFile.getAbsolutePath());
    }

    @Test
    public void testSetBreakingChange() throws Exception {
        EditParamIn editParamIn = new EditParamIn();
        boolean defaultValue = editParamIn.isBreakingChange();
        editParamIn.setBreakingChange(false);
        boolean newValue = editParamIn.isBreakingChange();
        assertNotEquals(defaultValue, newValue);
    }

    @Test
    public void testIsBreakingChange() throws Exception {
        assertEquals(new EditParamIn().isBreakingChange(), true);
    }

    @Test
    public void testGetRuleName() throws Exception {
        String ruleName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, EditParamIn.class.getSimpleName());
        assertEquals(new EditParamIn().getRuleName(), ruleName);
    }

    @Test
    public void testValidateBreakingOn() throws Exception {
        EditParamIn editParamIn = new EditParamIn();
        editParamIn.setBreakingChange(true);
        List<BrokenRule> brokenRulesList = editParamIn.validate(swagger, swaggerDeployed);
        assertEquals(brokenRulesList.size() >= 1, true);
    }

    @Test
    public void testValidateBreakingOff() throws Exception {
        EditParamIn editParamIn = new EditParamIn();
        editParamIn.setBreakingChange(false);
        List<BrokenRule> brokenRulesList = editParamIn.validate(swagger, swaggerDeployed);
        assertEquals(brokenRulesList.size() == 0, true);
    }

}