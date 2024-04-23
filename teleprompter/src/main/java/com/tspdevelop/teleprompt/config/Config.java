package com.tspdevelop.teleprompt.config;

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
    
    
    
}
