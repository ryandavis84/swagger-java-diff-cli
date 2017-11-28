/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger;

import com.rdavis.swagger.cmd.Diff;
import com.rdavis.swagger.cmd.Version;
import io.airlift.airline.Cli;
import io.airlift.airline.Help;

public class SwaggerDiff {

    public static void main(String[] args) {

        String version = Version.readVersionFromResources();
        @SuppressWarnings("unchecked")
        Cli.CliBuilder<Runnable> builder =
                Cli.<Runnable>builder("Swagger-Diff")
                        .withDescription(
                                String.format(
                                        "Swagger Diff CLI (version %s).",
                                        version))
                        .withDefaultCommand(Diff.class)
                        .withCommands(Diff.class, Help.class, Version.class);

        builder.build().parse(args).run();
    }
}
