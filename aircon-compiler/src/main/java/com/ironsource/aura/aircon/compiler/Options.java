package com.ironsource.aura.aircon.compiler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface Options {
    Set<String> ALL = new HashSet<>(Arrays.asList(Options.BASE_PACKAGE));

    String BASE_PACKAGE = "baseAirConPackage";
}
