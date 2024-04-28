package com.tspdevelop.teleprompt.config;

import com.tspdevelop.teleprompt.config.exceptions.ConfigException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

/**
 *
 * @author tspdev
 */
public class ConfigScript {
    
    private String script;
    private String path;
    private int wordsPerMin;
    private int delay;

    public ConfigScript() {
        this.script = null;
        this.path = null;
        this.wordsPerMin = 200;
        this.delay = 0;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getWordsPerMin() {
        return wordsPerMin;
    }

    public void setWordsPerMin(int wordsPerMin) {
        this.wordsPerMin = wordsPerMin;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
    
    public String getScriptValue() throws ConfigException {
        if (script == null && path == null) {
            throw new ConfigException("Script and path can't be both null.");
        }
        if (script == null && path.isBlank()) {
            throw new ConfigException("Path can not be empty.");
        }
        if (script == null && !path.isBlank()) {
            File f = new File(path);
            if(!f.exists() || !f.canRead()) {
                throw new ConfigException("Could not find or read script file: " + path);
            }
            try {
                StringBuilder stringBuilder;
                try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                    stringBuilder = new StringBuilder();
                    String line;
                    String ls = System.getProperty("line.separator");
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                        stringBuilder.append(ls);
                    }   // delete the last new line separator
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }

                return stringBuilder.toString();
            } catch (FileNotFoundException ex) {
                throw new ConfigException("Could not find or read script file: " + path);
            } catch (IOException ex) {
                throw new ConfigException("Had failure while reading script file: " + path);
            }
        }
        if (script != null) {
            return script;
        }
        return null;
    }

    public int getWordsPerMinValue() {
        if (this.wordsPerMin == 0) {
            return 200;
        } else {
            return this.wordsPerMin;
        }
    }
    
    @SuppressWarnings("static-access")
    public static Option[] getCmdCLIOptions() {
        Option[] options = new Option[4];
        options[0] = OptionBuilder.withArgName("script")
            .withLongOpt("script-text")
            .hasArg().
            withDescription("The text for the script to use in the video")
            .create("st");
        options[1] = OptionBuilder.withArgName("scriptFilePath")
            .withLongOpt("script-file")
            .hasArg().
            withDescription("The path to the script in a .txt file format")
            .create("sp");
        options[2] = OptionBuilder.withArgName("wordsPerMin")
            .withLongOpt("words-per-min")
            .hasArg().
            withDescription("The speed the text should be read at")
            .create("wpm");
        options[3] = OptionBuilder.withArgName("delay")
            .withLongOpt("delay")
            .hasArg().
            withDescription("Count down delay shown before the text starts scrolling")
            .create("d");
        
        return options;
    }
    
    public void processCLI(CommandLine cmd) throws ConfigException {
        if (cmd.hasOption("script-text")) {
            this.script = cmd.getOptionValue("script-text");
        }
        
        if (cmd.hasOption("script-file")) {
            this.path = cmd.getOptionValue("script-file");
            File f = new File(this.path);
            if (!f.exists()) {
                throw new ConfigException("Could not find file at path: " + this.path);
            }
            if(!f.canRead()) {
                throw new ConfigException("Could not read file at path: " + this.path);
            }
        }
        
        if(cmd.hasOption("words-per-min")) {
            try {
                this.wordsPerMin = Integer.parseInt(cmd.getOptionValue("words-per-min"));
                if (this.wordsPerMin < 1) {
                    throw new ConfigException("Words per minute must be greater then 0");
                }
            } catch(NumberFormatException nfe) {
                throw new ConfigException("Not a valid number: " + cmd.getOptionValue("words-per-min"));
            }
        }
        
        if(cmd.hasOption("delay")) {
            try {
                this.delay = Integer.parseInt(cmd.getOptionValue("delay"));
                if (this.delay < 1) {
                    throw new ConfigException("Delay must be greater then 0");
                }
            } catch(NumberFormatException nfe) {
                throw new ConfigException("Not a valid number: " + cmd.getOptionValue("delay"));
            }
        }
    }
    
}
