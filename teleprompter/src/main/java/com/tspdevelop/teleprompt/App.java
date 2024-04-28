package com.tspdevelop.teleprompt;

import com.tspdevelop.teleprompt.config.Config;
import com.tspdevelop.teleprompt.config.exceptions.ConfigException;
import com.tspdevelop.teleprompt.video.VideoGenerator;
import com.tspdevelop.teleprompt.video.VideoGeneratorListener;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.File;


public class App 
{
    public static void main( String[] args ) {
        try {
            Config config = Config.processCLI(args);
            generateVideo(config);
        } catch (ConfigException ex) {
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
                    String label = String.valueOf(Math.round(percent*100)/100.0) + "%";
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
}
