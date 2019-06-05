package com.mm3.prototype.graph.lib;

import org.junit.Rule;
import org.junit.rules.ErrorCollector;

public abstract class AbstractTest {

    @Rule
    public ErrorCollector collector= new ErrorCollector();

}
