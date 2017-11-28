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

public class EditObjectPropertyTypeTest {

    private Swagger swagger;
    private Swagger swaggerDeployed;

    @BeforeClass
    public void setup() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("swaggerEditObjectPropertyType.json").toURI());
        File deployedFile = new File(getClass().getClassLoader().getResource("swagger.json").toURI());
        swagger = new SwaggerParser().read(file.getAbsolutePath());
        swaggerDeployed = new SwaggerParser().read(deployedFile.getAbsolutePath());
    }

    @Test
    public void testSetBreakingChange() throws Exception {
        EditObjectPropertyType editObjectPropertyType = new EditObjectPropertyType();
        boolean defaultValue = editObjectPropertyType.isBreakingChange();
        editObjectPropertyType.setBreakingChange(false);
        boolean newValue = editObjectPropertyType.isBreakingChange();
        assertNotEquals(defaultValue, newValue);
    }

    @Test
    public void testIsBreakingChange() throws Exception {
        assertEquals(new EditObjectPropertyType().isBreakingChange(), true);
    }

    @Test
    public void testGetRuleName() throws Exception {
        String ruleName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, EditObjectPropertyType.class.getSimpleName());
        assertEquals(new EditObjectPropertyType().getRuleName(), ruleName);
    }

    @Test
    public void testValidateBreakingOn() throws Exception {
        EditObjectPropertyType editObjectPropertyType = new EditObjectPropertyType();
        editObjectPropertyType.setBreakingChange(true);
        List<BrokenRule> brokenRulesList = editObjectPropertyType.validate(swagger, swaggerDeployed);
        assertEquals(brokenRulesList.size() >= 1, true);
    }

    @Test
    public void testValidateBreakingOff() throws Exception {
        EditObjectPropertyType editObjectPropertyType = new EditObjectPropertyType();
        editObjectPropertyType.setBreakingChange(false);
        List<BrokenRule> brokenRulesList = editObjectPropertyType.validate(swagger, swaggerDeployed);
        assertEquals(brokenRulesList.size() == 0, true);
    }

}