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

public class DeleteObjectPropertyTest {

    private Swagger swagger;
    private Swagger swaggerDeployed;

    @BeforeClass
    public void setup() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("swaggerDeleteObjectProperty.json").toURI());
        File deployedFile = new File(getClass().getClassLoader().getResource("swagger.json").toURI());
        swagger = new SwaggerParser().read(file.getAbsolutePath());
        swaggerDeployed = new SwaggerParser().read(deployedFile.getAbsolutePath());
    }

    @Test
    public void testSetBreakingChange() throws Exception {
        DeleteObjectProperty deleteObjectProperty = new DeleteObjectProperty();
        boolean defaultValue = deleteObjectProperty.isBreakingChange();
        deleteObjectProperty.setBreakingChange(false);
        boolean newValue = deleteObjectProperty.isBreakingChange();
        assertNotEquals(defaultValue, newValue);
    }

    @Test
    public void testIsBreakingChange() throws Exception {
        assertEquals(new DeleteObjectProperty().isBreakingChange(), true);
    }

    @Test
    public void testGetRuleName() throws Exception {
        String ruleName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, DeleteObjectProperty.class.getSimpleName());
        assertEquals(new DeleteObjectProperty().getRuleName(), ruleName);
    }

    @Test
    public void testValidateBreakingOn() throws Exception {
        DeleteObjectProperty deleteObjectProperty = new DeleteObjectProperty();
        deleteObjectProperty.setBreakingChange(true);
        List<BrokenRule> brokenRulesList = deleteObjectProperty.validate(swagger, swaggerDeployed);
        assertEquals(brokenRulesList.size() >= 1, true);
    }

    @Test
    public void testValidateBreakingOff() throws Exception {
        DeleteObjectProperty deleteObjectProperty = new DeleteObjectProperty();
        deleteObjectProperty.setBreakingChange(false);
        List<BrokenRule> brokenRulesList = deleteObjectProperty.validate(swagger, swaggerDeployed);
        assertEquals(brokenRulesList.size() == 0, true);
    }

}