package com.tspdevelop.teleprompt.config;

import com.tspdevelop.teleprompt.config.exceptions.ConfigException;
import java.awt.Font;

/**
 *
 * @author tspdev
 */
public class ConfigFont {
    
    private String name;
    private int size;
    private String style;

    public ConfigFont() {
        this.name = null;
        this.size = 100;
        this.style = "plain";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    
    public Font getFont() throws ConfigException {
        int s = Font.PLAIN;
        if (name == null || name.isBlank()) {
            throw new ConfigException("Could not find font name.");
        }
        if (style == null || style.isBlank()) {
            style = "plain";
        }
        style = style.toLowerCase();
        switch (style){
            case "plain":
                s = Font.PLAIN;
                break;
            case "bold":
                s = Font.BOLD;
                break;
            case "italic":
                s = Font.ITALIC;
                break;
        }
        if (size < 0) {
            throw new ConfigException("Could not find font size has to be bigger then 0: " + String.valueOf(size));
        }
        if (size == 0) {
            size = 100;
        }
        return new Font(name, s, size);
    }
    
}