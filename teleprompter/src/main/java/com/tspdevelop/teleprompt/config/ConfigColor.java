package com.tspdevelop.teleprompt.config;

import com.tspdevelop.teleprompt.config.exceptions.ConfigException;
import java.awt.Color;

/**
 *
 * @author tspdev
 */
public class ConfigColor {
    
    private String name;
    private int red;
    private int green;
    private int blue;

    public ConfigColor() {
        this.name = null;
        this.red = 0;
        this.blue = 0;
        this.green = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }
    
    public Color getColor() throws ConfigException {
        Color color = Color.getColor(name);
        if (color == null) {
            if(green < 0 | green > 255) {
                throw new ConfigException("Green must be between 0 and 255: " + String.valueOf(green));
            }
            if(blue < 0 | blue > 255) {
                throw new ConfigException("Green must be between 0 and 255: " + String.valueOf(green));
            }
            if(red < 0 | red > 255) {
                throw new ConfigException("Green must be between 0 and 255: " + String.valueOf(green));
            }
            color = new Color(red, green, blue);
        }
        return color;
    }
    
}
