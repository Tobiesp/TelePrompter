package com.tspdevelop.teleprompt.config;

import com.tspdevelop.teleprompt.config.exceptions.ConfigException;
import java.awt.Color;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

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
    
    public static void ListSystemNamedColors() {
        System.out.println("Named Colors in the system:");
        System.out.println("- black");
        System.out.println("- blue");
        System.out.println("- cyan");
        System.out.println("- dark gray");
        System.out.println("- gray");
        System.out.println("- green");
        System.out.println("- light gray");
        System.out.println("- megenta");
        System.out.println("- oragne");
        System.out.println("- pink");
        System.out.println("- red");
        System.out.println("- white");
        System.out.println("- yellow");
    }

    @SuppressWarnings("static-access")
    public static Option[] getCmdCLIOptions() {
        Option[] options = new Option[3];
        options[0] = OptionBuilder.withArgName("forground")
            .withLongOpt("color-fg")
            .hasArg().
            withDescription("Display color of the text")
            .create("cf");
        options[1] = OptionBuilder.withArgName("background")
            .withLongOpt("color-bg")
            .hasArg().
            withDescription("Background color for the display")
            .create("cb");
        options[2] = OptionBuilder.withArgName("list-colors")
            .withLongOpt("list-colors")
            .hasArg(false).
            withDescription("List all the named colors for the system")
            .create("lc");
        return options;
    }

    public void processCLI(CommandLine cmd, String cmdKey) throws ConfigException {
        if (cmd.hasOption(cmdKey)) {
            this.name = cmd.getOptionValue(cmdKey);
            if(this.name == null || this.name.isBlank()) {
                throw new ConfigException("Color option " + cmdKey + " doesn't have a value");
            }
        }
    }
    
}
