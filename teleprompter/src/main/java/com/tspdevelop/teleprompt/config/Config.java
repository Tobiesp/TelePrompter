package com.tspdevelop.teleprompt.config;

import com.tspdevelop.teleprompt.config.exceptions.ConfigException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author tspdev
 */
public class Config {
    
    private ConfigScript script;
    private ConfigFont font;
    private ConfigColor forground;
    private ConfigColor background;
    private ConfigVideo video;
    
    public Config() {
        this.background = null;
        this.forground = null;
        this.font = new ConfigFont();
        this.script = new ConfigScript();
        this.video = new ConfigVideo();
    }

    public ConfigScript getScript() {
        return script;
    }

    public void setScript(ConfigScript script) {
        this.script = script;
    }

    public ConfigFont getFont() {
        return font;
    }

    public void setFont(ConfigFont font) {
        this.font = font;
    }

    public ConfigColor getForground() {
        if(this.forground == null) {
            ConfigColor cc = new ConfigColor();
            cc.setName("white");
            return cc;
        }
        return forground;
    }

    public void setForground(ConfigColor forground) {
        this.forground = forground;
    }

    public ConfigColor getBackground() {
        if(this.background == null) {
            ConfigColor cc = new ConfigColor();
            cc.setName("black");
            return cc;
        }
        return background;
    }

    public void setBackground(ConfigColor background) {
        this.background = background;
    }

    public ConfigVideo getVideo() {
        return video;
    }

    public void setVideo(ConfigVideo video) {
        this.video = video;
    }
    
    @SuppressWarnings("static-access")
    public static Options getCmdCLIOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "displays help");
        options.addOption("v", "version", false, "version of this library");
        options.addOption(OptionBuilder.withArgName("yaml")
            .withLongOpt("yaml")
            .hasArg().
            withDescription("A yaml configuration file. Any additional cmd line args will overwrite the yaml file values.")
            .create("y"));
        options.addOption(OptionBuilder.withArgName("example-yaml")
            .withLongOpt("example-yaml")
            .hasArg().
            withDescription("Generate an example YAML config file as example.yaml")
            .create("ey"));
        for(Option o : ConfigScript.getCmdCLIOptions()) {
            options.addOption(o);
        }
        for(Option o : ConfigFont.getCmdCLIOptions()) {
            options.addOption(o);
        }
        for(Option o : ConfigColor.getCmdCLIOptions()) {
            options.addOption(o);
        }
        for(Option o : ConfigVideo.getCmdCLIOptions()) {
            options.addOption(o);
        }
        return options;
    }
    
    public static Config processCLI(String[] args) throws ConfigException {
        Options options = Config.getCmdCLIOptions();
        Config config = new Config();
        CommandLine cmd;
        CommandLineParser parser = new org.apache.commons.cli.BasicParser();
        try {
          cmd = parser.parse(options, args);
        } catch (ParseException e) {
          throw new ConfigException(e);
        }
        String[] parsedArgs = cmd.getArgs();
        Option[] optionArgs = cmd.getOptions();
        if (cmd.hasOption("version")) {
          // let's find what version of the library we're running
          String version = io.humble.video_native.Version.getVersionInfo();
          System.out.println("TelleJ: 1.0.0");
          System.out.println("Humble Version: " + version);
          System.out.println("commons-cli Version: 1.2");
          System.out.println("snakeYAML Version: 2.2");
          System.exit(0);
        } else if (cmd.hasOption("help") || (parsedArgs.length == 0 && optionArgs.length == 0)) {
          HelpFormatter formatter = new HelpFormatter();
          if (parsedArgs.length == 0) {
            System.err.println("Error: No options or YAML file supplied!\n");
            formatter.printHelp("Tellej <yamlfilepath> [Options]", options);
            System.exit(1);
          }
          formatter.printHelp("Tellej <yamlfilepath> [Options]", options);
          System.exit(0);
        } else if (cmd.hasOption("example-yaml")) {
          System.out.println("Generating Example YAML");
          GenerateExampleYAML();
          System.exit(0);
        } else {
          if (cmd.hasOption("yaml")) {
              try {
                  config = processYamlConfig(cmd.getOptionValue("yaml"));
              } catch (IOException ex) {
                  throw new ConfigException(ex.getMessage() + ": " + cmd.getOptionValue("yaml"));
              }
          }
          if (parsedArgs.length == 1) {
              String filename = cmd.getArgs()[0];
              try {
                  config = processYamlConfig(filename);
                  return config;
              } catch (IOException ex) {
                  throw new ConfigException(ex.getMessage() + ": " + filename);
              }
          }
          
          config.getFont().processCLI(cmd);
          
          config.getScript().processCLI(cmd);
          
          if (config.background == null) {
              config.background = new ConfigColor();
              config.background.setName("black");
          }
          config.background.processCLI(cmd, "color-bg");
          
          if (config.forground == null) {
              config.forground = new ConfigColor();
              config.forground.setName("white");
          }
          config.forground.processCLI(cmd, "color-fg");
          
          config.video.processCli(cmd);
        }
        return config;
    }
    
    public static void GenerateExampleYAML() {
        Config config = new Config();
        Yaml yaml = new Yaml();
        StringWriter writer = new StringWriter();
        yaml.dump(config, writer);
        File f = new File("example.yaml");
        if(f.exists()) {
            System.err.println("Warning: Example.yaml already exits not overwriting");
            return;
        }
        try {
            OutputStream os = new FileOutputStream(f);
            os.write(writer.toString().getBytes());
            os.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Error: Failed to write Example.yaml: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Error: Failed to write Example.yaml: " + ex.getMessage());
        }
    }
    
    private static Config processYamlConfig(String path) throws IOException {
        Yaml yaml = new Yaml();
        File f = new File(path);
        if (!f.exists() || !f.canRead()) {
            throw new IOException("Unable to find or read YAML file: " + path);
        }
        InputStream stream = new FileInputStream(f);
        Config config = yaml.loadAs(stream, Config.class);
        return config;
    }
    
    
    
}
