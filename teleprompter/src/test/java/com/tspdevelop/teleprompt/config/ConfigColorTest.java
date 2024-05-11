/*
 * Copyright 2024 andywebb.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tspdevelop.teleprompt.config;

import com.tspdevelop.teleprompt.config.exceptions.ConfigException;
import java.awt.Color;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andywebb
 */
public class ConfigColorTest {
    
    public ConfigColorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    private CommandLine getArguments(String[] args) throws ParseException {
        Option[] optionArray = ConfigColor.getCmdCLIOptions();
        Options options = new Options();
        for(Option o: optionArray) {
            options.addOption(o);
        }
        CommandLineParser parser = new org.apache.commons.cli.BasicParser();
        try {
          return parser.parse(options, args);
        } catch (ParseException e) {
          throw e;
        }
    }

    /**
     * Test of getName method, of class ConfigColor.
     */
    @Test
    public void testGetForground() {
        System.out.println("getForground");
        ConfigColor instance = new ConfigColor();
        String[] args = {"-cf", "red"};
        try {
            instance.processCLI(getArguments(args), "color-fg");
        } catch (ParseException | ConfigException ex) {
            fail(ex.getMessage());
        }
        String expResult = "red";
        String result = instance.getName();
        assertEquals(expResult, result);
        Color expResultColor = Color.RED;
        Color resultColor;
        try {
            resultColor = instance.getColor();
            assertEquals(expResultColor, resultColor);
        } catch (ConfigException ex) {
            fail("To get color red.");
        }
    }

    /**
     * Test of getName method, of class ConfigColor.
     */
    @Test
    public void testGetBackground() {
        System.out.println("getBackground");
        ConfigColor instance = new ConfigColor();
        String[] args = {"-cb", "blue"};
        try {
            instance.processCLI(getArguments(args), "color-bg");
        } catch (ParseException | ConfigException ex) {
            fail(ex.getMessage());
        }
        String expResult = "blue";
        String result = instance.getName();
        assertEquals(expResult, result);
        Color expResultColor = Color.BLUE;
        Color resultColor;
        try {
            resultColor = instance.getColor();
            assertEquals(expResultColor, resultColor);
        } catch (ConfigException ex) {
            fail("To get color blue.");
        }
    }
    
}
