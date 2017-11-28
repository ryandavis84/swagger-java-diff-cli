/*
 * Copyright (c) 2017. Ryan Davis <Ryandavis84@gmail.com> Swagger Diff java CLI
 */

package com.rdavis.swagger.rules;

public class BrokenRule {
    private Rule _rule;
    private String _description;

    public Rule getRule() {
        return _rule;
    }

    public void setRule(Rule rule) {
        _rule = rule;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }
}
