package com.tspdevelop.teleprompt;

import com.tspdevelop.teleprompt.config.Config;
import com.tspdevelop.teleprompt.config.ConfigFont;
import com.tspdevelop.teleprompt.config.exceptions.ConfigException;
import com.tspdevelop.teleprompt.video.VideoGenerator;
import com.tspdevelop.teleprompt.video.VideoGeneratorListener;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.yaml.snakeyaml.Yaml;


public class App 
{
    public static void main( String[] args ) {
        try {
            Config config = processCLI(args);
            generateVideo(config);
        } catch (ParseException ex) {
            printErrorAndExit("Unable to parse arguments: " + ex.getMessage());
        } catch (IOException | InterruptedException ex) {
            printErrorAndExit("Failed to generate video: " + ex.getMessage());
        }
    }
    
    private static void generateVideo(Config config) throws IOException, InterruptedException {
        try {
            String path = config.getVideo().getFilename();
            if(path == null || path.isBlank()) {
                path = "tellej.mp4";
            }
            // Not Implemented yet
            File f = new File(path);
            if(f.exists()) {
                throw new IOException("Video file already exists: " + path);
            }
            Font font = config.getFont().getFont();
            Color background = config.getBackground().getColor();
            Color forground = config.getForground().getColor();
            VideoGenerator vg = new VideoGenerator(
                    config.getVideo().getSize(), 
                    config.getVideo().getFramerate(), 
                    config.getVideo().getFilename(), 
                    config.getVideo().getFormat(), 
                    config.getVideo().getCodec()
            );
            vg.setFont(font);
            if (config.getScript().getDelay() > 0) {
                vg.setDelay(config.getScript().getDelay());
            }
            VideoGeneratorListener listener = new VideoGeneratorListener() {
                private final int MAX_LENGTH = 80;
                private boolean firstRun = true;
                @Override
                public void updateProgress(double percent) {
                    String label = String.valueOf(percent) + "%";
                    int len = this.MAX_LENGTH - label.length() - 2;
                    int half = len / 2;
                    int count = (int) ((percent / 100) * len);
                    String pLabel = "[";
                    for (int i = 0; i<len; i++) {
                        if (i < count) {
                            if(i == (count-1)) {
                                pLabel += ">";
                            } else {
                                pLabel += "=";
                            }
                        } else {
                            pLabel += " ";
                        }
                        if (i == half) {
                            pLabel += label;
                        }
                    }
                    pLabel += "]";
                    if (firstRun) {
                        System.out.println("Generating Video");
                        System.out.print(pLabel);
                        firstRun = false;
                    } else {
                        System.out.print("\r" + pLabel);
                    }
                }
                public void done() {
                    System.out.println();
                    System.out.println("Done");
                }
            };
            vg.setListener(listener);
            vg.writeTeleprompter(
                    config.getScript().getScriptValue(), 
                    config.getScript().getWordsPerMinValue(), 
                    background, 
                    forground);
        } catch (ConfigException ex) {
            printErrorAndExit("Config Exception: " + ex.getMessage());
        }
    }
    
    private static void printErrorAndExit(String msg) {
        System.err.println(msg);
        System.exit(1);
    }
    
    private static Config processCLI(String[] args) throws ParseException {
        Options options = BuildCLIProcess(args);
        Config config = new Config();
        CommandLine cmd;
        CommandLineParser parser = new org.apache.commons.cli.BasicParser();
        try {
          cmd = parser.parse(options, args);
        } catch (ParseException e) {
          throw e;
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
        } else if (cmd.hasOption("list-fonts")) {
          String[] fonts = ConfigFont.getAllFonts();
          System.out.println("Available Fonts on this system:");
          for(String f : fonts){
              System.out.println(f);
          }
          System.exit(0);
        } else if (cmd.hasOption("help") || (parsedArgs.length == 0 && optionArgs.length == 0)) {
          HelpFormatter formatter = new HelpFormatter();
          if (parsedArgs.length == 0) {
            System.err.println("Error: No options or YAML file supplied!\n");
            formatter.printHelp("Tellej <yamlfilepath> [Options]", options);
            System.exit(1);
          }
          formatter.printHelp(App.class.getCanonicalName() + " <yamlfilepath> [Options]", options);
          System.exit(0);
        } else {
          if (cmd.hasOption("yaml")) {
              try {
                  config = processYamlConfig(cmd.getOptionValue("yaml"));
              } catch (IOException ex) {
                  printErrorAndExit(ex.getMessage() + ": " + cmd.getOptionValue("yaml"));
              }
          }
          if (parsedArgs.length == 1) {
              String filename = cmd.getArgs()[0];
              try {
                  config = processYamlConfig(filename);
                  return config;
              } catch (IOException ex) {
                  printErrorAndExit(ex.getMessage() + ": " + filename);
              }
          }
          //script
          config.getScript().setScript(cmd.getOptionValue("script-text", config.getScript().getScript()));
          //scriptFilePath
          config.getScript().setPath(cmd.getOptionValue("script-file", config.getScript().getPath()));
          //forground
          config.getForground().setName(cmd.getOptionValue("color-fg", "white"));
          //background
          config.getBackground().setName(cmd.getOptionValue("color-bg", "black"));
          //fontName
          config.getFont().setName(cmd.getOptionValue("font-name", config.getFont().getName()));
          //fontStyle
          config.getFont().setStyle(cmd.getOptionValue("font-style", config.getFont().getStyle()));
          //fontSize
          config.getFont().setSize(Integer.parseInt(cmd.getOptionValue("font-size", String.valueOf(config.getFont().getSize()))));
          //videoSize
          config.getVideo().setSize(Integer.parseInt(cmd.getOptionValue("video-size", String.valueOf(config.getVideo().getSize()))));
          //videoFramerate
          config.getVideo().setFramerate(Integer.parseInt(cmd.getOptionValue("video-framerate", String.valueOf(config.getVideo().getFramerate()))));
          //videoName
          config.getVideo().setFilename(cmd.getOptionValue("video-name", config.getVideo().getFilename()));
          //videoFormat
          config.getVideo().setFormat(cmd.getOptionValue("video-format", config.getVideo().getFormat()));
          //videoCodec
          config.getVideo().setCodec(cmd.getOptionValue("video-codec", config.getVideo().getCodec()));
        }
        return config;
    }
    
    @SuppressWarnings("static-access")
    private static Options BuildCLIProcess(String[] args) {
        Options options = new Options();
        options.addOption("h", "help", false, "displays help");
        options.addOption("v", "version", false, "version of this library");
        options.addOption("lf", "list-fonts", false, "List all the fonts in the system");
        options.addOption(OptionBuilder.withArgName("yaml")
            .withLongOpt("yaml")
            .hasArg().
            withDescription("A yaml configuration file. Any additional cmd line args will overwrite the yaml file values.")
            .create("y"));
        options.addOption(OptionBuilder.withArgName("script")
            .withLongOpt("script-text")
            .hasArg().
            withDescription("The text for the script to use in the video")
            .create("st"));
        options.addOption(OptionBuilder.withArgName("scriptFilePath")
            .withLongOpt("script-file")
            .hasArg().
            withDescription("The path to the script in a .txt file format")
            .create("sp"));
        options.addOption(OptionBuilder.withArgName("forground")
            .withLongOpt("color-fg")
            .hasArg().
            withDescription("Display color of the text")
            .create("cf"));
        options.addOption(OptionBuilder.withArgName("background")
            .withLongOpt("color-bg")
            .hasArg().
            withDescription("Background color for the display")
            .create("cb"));
        options.addOption(OptionBuilder.withArgName("fontName")
            .withLongOpt("font-name")
            .hasArg().
            withDescription("Name of a font available on the current machine")
            .create("fn"));
        options.addOption(OptionBuilder.withArgName("fontStyle")
            .withLongOpt("font-style")
            .hasArg().
            withDescription("Style of the font")
            .create("fs"));
        options.addOption(OptionBuilder.withArgName("fontSize")
            .withLongOpt("font-size")
            .hasArg().
            withDescription("Size of the font")
            .create("fz"));
        options.addOption(OptionBuilder.withArgName("videoSize")
            .withLongOpt("video-size")
            .hasArg().
            withDescription("Video resultion. Default: 720")
            .create("vz"));
        options.addOption(OptionBuilder.withArgName("videoFramerate")
            .withLongOpt("video-framerate")
            .hasArg().
            withDescription("Framerate of the video. Default: 24")
            .create("vf"));
        options.addOption(OptionBuilder.withArgName("videoName")
            .withLongOpt("video-name")
            .hasArg().
            withDescription("Filename of the video. Default: telleprompt.mp4")
            .create("vn"));
        options.addOption(OptionBuilder.withArgName("videoFormat")
            .withLongOpt("video-format")
            .hasArg().
            withDescription("The format the video need to be encoded in. Default: determined by file ext")
            .create("vr"));
        options.addOption(OptionBuilder.withArgName("videoCodec")
            .withLongOpt("video-codec")
            .hasArg().
            withDescription("The codec that will be used for the video. Default: determined by file ext")
            .create("vc"));
        

        return options;
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
