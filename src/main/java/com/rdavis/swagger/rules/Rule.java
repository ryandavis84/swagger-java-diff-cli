/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger.rules;

import v2.io.swagger.models.Swagger;

import java.util.List;

public interface Rule {
    boolean isBreakingChange();

    void setBreakingChange(boolean isBreakingChange);

    String getRuleName();

    List<BrokenRule> validate(Swagger currentSwagger, Swagger deployedSwagger);
}
