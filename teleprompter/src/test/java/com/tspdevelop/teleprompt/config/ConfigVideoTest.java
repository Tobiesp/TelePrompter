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
public class ConfigVideoTest {
    
    public ConfigVideoTest() {
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
        Option[] optionArray = ConfigVideo.getCmdCLIOptions();
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
     * Test of getSize method, of class ConfigVideo.
     */
    @Test
    public void testGetSize() {
        System.out.println("getSize");
        ConfigVideo instance = new ConfigVideo();
        String[] args = {"-vz", "1080", "-vf", "22", "-vn", "test_123.mp4"};
        try {
            instance.processCli(getArguments(args));
        } catch (ParseException | ConfigException ex) {
            fail(ex.getMessage());
        }
        int expResultSize = 1080;
        int resultSize = instance.getSize();
        assertEquals(expResultSize, resultSize);
        
        int expResultFrame = 22;
        int resultFrame = instance.getFramerate();
        assertEquals(expResultFrame, resultFrame);
        
        String expResultName = "test_123.mp4";
        String resultName = instance.getFilename();
        assertEquals(expResultName, resultName);
    }
    
}
