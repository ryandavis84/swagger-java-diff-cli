/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger;


import org.testng.annotations.Test;

import java.io.File;

public class SwaggerDiffTest {

    @Test
    public void testDiff() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("swagger.json").toURI());
        File file2 = new File(getClass().getClassLoader().getResource("testRules.json").toURI());
        String[] args = {"diff", "-c", file.getAbsolutePath(), "-d", file.getAbsolutePath(), "-r", file2.getAbsolutePath()};
        SwaggerDiff.main(args);
    }

    @Test
    public void testVersion() throws Exception {
        String[] args = {"version"};
        SwaggerDiff.main(args);
    }

}