package com.tspdevelop.teleprompt.config;

import com.tspdevelop.teleprompt.config.exceptions.ConfigException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
    
}
