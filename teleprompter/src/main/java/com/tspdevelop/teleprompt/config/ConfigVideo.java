package com.tspdevelop.teleprompt.config;

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
        return this.size <= 0 ? 720 : this.size;
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
    
}
