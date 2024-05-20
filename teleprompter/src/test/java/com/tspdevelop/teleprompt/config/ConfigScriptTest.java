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
public class ConfigScriptTest {
    
    public ConfigScriptTest() {
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
        Option[] optionArray = ConfigScript.getCmdCLIOptions();
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
     * Test of getScript method, of class ConfigScript.
     */
    @Test
    public void testGetScript() {
        System.out.println("getScript");
        ConfigScript instance = new ConfigScript();
        String[] args = {"-st", "This is a test line!", "-wpm", "150", "-d", "10"};
        try {
            instance.processCLI(getArguments(args));
        } catch (ParseException | ConfigException ex) {
            fail(ex.getMessage());
        }
        
        String expResultScript = "This is a test line!";
        String resultScript = instance.getScript();
        assertEquals(expResultScript, resultScript);
        
        int expResultWPM = 150;
        int resultWPM = instance.getWordsPerMin();
        assertEquals(expResultWPM, resultWPM);
        
        int expResultDelay = 10;
        int resultDelay = instance.getDelay();
        assertEquals(expResultDelay, resultDelay);
    }
    
}
