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

    /**
     * Test of setSize method, of class ConfigVideo.
     */
    @Test
    public void testSetSize() {
        System.out.println("setSize");
        int size = 0;
        ConfigVideo instance = new ConfigVideo();
        instance.setSize(size);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFramerate method, of class ConfigVideo.
     */
    @Test
    public void testGetFramerate() {
        System.out.println("getFramerate");
        ConfigVideo instance = new ConfigVideo();
        int expResult = 0;
        int result = instance.getFramerate();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setFramerate method, of class ConfigVideo.
     */
    @Test
    public void testSetFramerate() {
        System.out.println("setFramerate");
        int framerate = 0;
        ConfigVideo instance = new ConfigVideo();
        instance.setFramerate(framerate);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFilename method, of class ConfigVideo.
     */
    @Test
    public void testGetFilename() {
        System.out.println("getFilename");
        ConfigVideo instance = new ConfigVideo();
        String expResult = "";
        String result = instance.getFilename();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setFilename method, of class ConfigVideo.
     */
    @Test
    public void testSetFilename() {
        System.out.println("setFilename");
        String filename = "";
        ConfigVideo instance = new ConfigVideo();
        instance.setFilename(filename);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFormat method, of class ConfigVideo.
     */
    @Test
    public void testGetFormat() {
        System.out.println("getFormat");
        ConfigVideo instance = new ConfigVideo();
        String expResult = "";
        String result = instance.getFormat();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setFormat method, of class ConfigVideo.
     */
    @Test
    public void testSetFormat() {
        System.out.println("setFormat");
        String format = "";
        ConfigVideo instance = new ConfigVideo();
        instance.setFormat(format);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCodec method, of class ConfigVideo.
     */
    @Test
    public void testGetCodec() {
        System.out.println("getCodec");
        ConfigVideo instance = new ConfigVideo();
        String expResult = "";
        String result = instance.getCodec();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCodec method, of class ConfigVideo.
     */
    @Test
    public void testSetCodec() {
        System.out.println("setCodec");
        String codec = "";
        ConfigVideo instance = new ConfigVideo();
        instance.setCodec(codec);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCmdCLIOptions method, of class ConfigVideo.
     */
    @Test
    public void testGetCmdCLIOptions() {
        System.out.println("getCmdCLIOptions");
        Option[] expResult = null;
        Option[] result = ConfigVideo.getCmdCLIOptions();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of processCli method, of class ConfigVideo.
     */
    @Test
    public void testProcessCli() throws Exception {
        System.out.println("processCli");
        CommandLine cmd = null;
        ConfigVideo instance = new ConfigVideo();
        instance.processCli(cmd);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
