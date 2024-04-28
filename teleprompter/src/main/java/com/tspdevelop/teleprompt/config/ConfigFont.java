package com.tspdevelop.teleprompt.config;

import com.tspdevelop.teleprompt.config.exceptions.ConfigException;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

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
    
    public static String[] getAllFonts() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    }
    
    public Font getFont() throws ConfigException {
        int s = Font.PLAIN;
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
            throw new ConfigException("font size has to be bigger then 0: " + String.valueOf(size));
        }
        if (size == 0) {
            size = 100;
        }
        String font = name;
        if(font == null || font.isBlank()) {
            String[] fontList = getAllFonts();
            for(String f: fontList) {
                if("Arial".equals(f)) {
                    font = f;
                    break;
                }
            }
            if(font == null || font.isBlank()) {
                font = fontList[0];
            }
        }
        return new Font(font, s, size);
    }

    @SuppressWarnings("static-access")
    public static Option[] getCmdCLIOptions() {
        Option[] options = new Option[4];
        options[0] = OptionBuilder.withArgName("list-font")
            .withLongOpt("list-font")
            .hasArg(false).
            withDescription("List all the fonts in the system")
            .create("lf");
        options[1] = OptionBuilder.withArgName("fontName")
            .withLongOpt("font-name")
            .hasArg().
            withDescription("Name of a font available on the current machine")
            .create("fn");
        options[2] = OptionBuilder.withArgName("fontStyle")
            .withLongOpt("font-style")
            .hasArg().
            withDescription("Style of the font")
            .create("fs");
        options[3] = OptionBuilder.withArgName("fontSize")
            .withLongOpt("font-size")
            .hasArg().
            withDescription("Size of the font")
            .create("fz");
        return options;
    }
    
    public void processCLI(CommandLine cmd) throws ConfigException {
        if (cmd.hasOption("list-fonts")) {
          String[] fonts = ConfigFont.getAllFonts();
          System.out.println("Available Fonts on this system:");
          for(String f : fonts){
              System.out.println(f);
          }
          System.exit(0);
        }
        if (cmd.hasOption("font-name")) {
            this.name = cmd.getOptionValue("font-name");
        }
        
        if (cmd.hasOption("font-style")) {
            this.style = cmd.getOptionValue("font-style");
            if (this.style == null) {
                throw new ConfigException("Font style option was supplied without a value");
            }
        }
        
        if (cmd.hasOption("font-size")) {
            try {
                this.size = Integer.parseInt(cmd.getOptionValue("font-size"));
                if (this.size < 1) {
                    throw new ConfigException("Font size must be greater then 0");
                }
            } catch(NumberFormatException nfe) {
                throw new ConfigException("Not a valid number for font size: " + cmd.getOptionValue("font-size"));
            }
        }
    }
    
}