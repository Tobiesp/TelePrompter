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

import java.awt.Font;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andywebb
 */
public class ConfigFontTest {
    
    public ConfigFontTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    private CommandLine getArguments(String[] args) throws ParseException {
        Option[] optionArray = ConfigFont.getCmdCLIOptions();
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
     * Test of getFont method, of class ConfigFont.
     */
    @Test
    public void testGetFont() throws Exception {
        System.out.println("getFont");
        ConfigFont instance = new ConfigFont();
        String[] args = {"-fn", "Arial", "-fz", "12", "-fs", "bold"};
        CommandLine cmd = getArguments(args);
        instance.processCLI(cmd);
        Font expResult = new Font("Arial", Font.BOLD, 12);
        Font result = instance.getFont();
        assertEquals(expResult, result);
    }
    
}
