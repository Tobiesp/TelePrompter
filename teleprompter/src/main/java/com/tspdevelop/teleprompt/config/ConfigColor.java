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
    
    private Color getColorByName(String name) {
        if(name == null || name.isBlank()) {
            return null;
        }
        switch(name) {
            case "black":
                return Color.BLACK;
            case "blue":
                return Color.BLUE;
            case "cyan":
                return Color.CYAN;
            case "dark gray":
                return Color.DARK_GRAY;
            case "gray":
                return Color.GRAY;
            case "green":
                return Color.GREEN;
            case "light gray":
                return Color.LIGHT_GRAY;
            case "megenta":
                return Color.MAGENTA;
            case "oragne":
                return Color.ORANGE;
            case "pink":
                return Color.PINK;
            case "red":
                return Color.RED;
            case "white":
                return Color.WHITE;
            case "yellow":
                return Color.YELLOW;
        }
        return null;
    }
    
    public Color getColor() throws ConfigException {
        Color color = getColorByName(name);
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
