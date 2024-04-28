package com.tspdevelop.teleprompt.config;

import com.tspdevelop.teleprompt.config.exceptions.ConfigException;
import java.io.File;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

/**
 *
 * @author tspdev
 */
public class ConfigVideo {
    
    private int size;
    private int framerate;
    private String filename;
    private String format;
    private String codec;

    public ConfigVideo() {
        this.size = 720;
        this.codec = null;
        this.filename = null;
        this.format = null;
        this.framerate = 24;
    }

    public int getSize() {
        return this.size <= 120 ? 720 : this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFramerate() {
        return framerate <= 0 ? 24 : this.framerate;
    }

    public void setFramerate(int framerate) {
        this.framerate = framerate;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }
    
    @SuppressWarnings("static-access")
    public static Option[] getCmdCLIOptions() {
        Option[] options = new Option[5];
        options[0] = OptionBuilder.withArgName("videoSize")
            .withLongOpt("video-size")
            .hasArg().
            withDescription("Video resultion in increaments of 120 with a min of 120 and a max of 2160. Default: 720")
            .create("vz");
        options[1] = OptionBuilder.withArgName("videoFramerate")
            .withLongOpt("video-framerate")
            .hasArg().
            withDescription("Framerate of the video. Default: 24")
            .create("vf");
        options[2] = OptionBuilder.withArgName("videoName")
            .withLongOpt("video-name")
            .hasArg().
            withDescription("Filename of the video. Default: telleprompt.mp4")
            .create("vn");
        options[3] = OptionBuilder.withArgName("videoFormat")
            .withLongOpt("video-format")
            .hasArg().
            withDescription("The format the video need to be encoded in. Default: determined by file ext")
            .create("vr");
        options[4] = OptionBuilder.withArgName("videoCodec")
            .withLongOpt("video-codec")
            .hasArg().
            withDescription("The codec that will be used for the video. Default: determined by file ext")
            .create("vc");
        return options;
    }

    public void processCli(CommandLine cmd) throws ConfigException {
        if (cmd.hasOption("video-size")) {
            try {
                this.size = Integer.parseInt(cmd.getOptionValue("video-size"));
                if (this.size < 120 || this.size > 2160 || (this.size % 120) != 0 ) {
                    throw new ConfigException("video size must be between 120 and 2160 in multiples of 120: " + String.valueOf(size));
                }
            } catch(NumberFormatException nfe) {
                throw new ConfigException("Not a valid number for Video size: " + cmd.getOptionValue("video-size"));
            }
        }
          
        if (cmd.hasOption("video-framerate")) {
            try {
                this.framerate = Integer.parseInt(cmd.getOptionValue("video-framerate"));
                if (this.framerate < 1) {
                    throw new ConfigException("video framerate must be greater then 0: " + String.valueOf(framerate));
                }
            } catch(NumberFormatException nfe) {
                throw new ConfigException("Not a valid number for Video framerate: " + cmd.getOptionValue("video-framerate"));
            }
        }
        
        if(cmd.hasOption("video-name")) {
            this.filename = cmd.getOptionValue("video-name");
            if(this.filename == null || this.filename.isBlank()) {
                throw new ConfigException("Video file path can't be blank or null");
            }
            File f = new File(this.filename);
            if(f.exists()) {
                throw new ConfigException("Video file path already exists: " + this.filename);
            }
        }

        if(cmd.hasOption("video-format")) {
            this.format = cmd.getOptionValue("video-format");
        }

        if(cmd.hasOption("video-codec")) {
            this.format = cmd.getOptionValue("video-codec");
        }
    }
    
}
