package com.tspdevelop.teleprompt.video;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author tspdev
 */
public class FrameGenerator {
    private final ArrayList<String>  lines;
    private final int pixelsPerFrame;
    private final int lineHeight;
    private final int screenHeight;
    private final int screenWidth;
    private final int[] pageHeights;
    private final int framesPerSec;
    private final Color bg;
    private final Color fg;
    private int imageType;
    private Font font;
    private RenderingHints renderHints;
    private int delay;
    private int delayIndex;
    private int delayTotalFrames;
    
    public FrameGenerator(ArrayList<String> lines, int wordsPerPage, int wordsPerMin, int framesPerSec, int screenHeight, int screenWidth, int lineHeight, Color bg, Color fg) {
        this.lines = lines;
        this.framesPerSec = framesPerSec;
        int wordsPerSec = (wordsPerMin/60);
        if(wordsPerSec == 0) {
            wordsPerSec = 1;
        }
        int readTime = wordsPerPage/wordsPerSec;
        int framesPerPage = readTime*framesPerSec;
        this.pixelsPerFrame = screenHeight/framesPerPage;
        this.lineHeight = lineHeight;
        this.pageHeights = new int[this.lines.size()];
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.bg = bg;
        this.fg = fg;
        this.delay = 0;
        this.delayIndex = 0;
        this.populateLineHeightPositions();
    }
    
    private void populateLineHeightPositions() {
        int h = (screenHeight/4)+(lineHeight/4)+1;
        for(int i = 0; i < this.pageHeights.length; i++) {
            this.pageHeights[i] = h;
            h += this.lineHeight;
        }
    }
    
    private void moveUpLines() {
        for(int i = 0; i < this.pageHeights.length; i++) {
            this.pageHeights[i] = this.pageHeights[i] - this.pixelsPerFrame;
        }
    }
    
    private Frame getLinesOnScreen() {
        //top = -2
        //bottom = pageHeights + lineHeight
        Frame frame = new Frame();
        int h;
        for(int i = 0; i < this.pageHeights.length; i++) {
            h = this.pageHeights[i];
            if((h > -2) && (h < this.screenHeight + this.lineHeight)) {
                frame.addLine(this.pageHeights[i], this.lines.get(i));
            }
        }
        return frame;
    }
    
    public void setImageType(int type) {
        this.imageType = type;
    }
    
    public void setFont(Font font) {
        this.font = font;
    }
    
    public void setRenderHints(RenderingHints rh) {
        this.renderHints = rh;
    }
    
    public void setDelay(int delay) {
        if(delay < 0) {
            this.delay = 0;
        } else {
            this.delay = delay;
        }
        this.delayIndex = 0;
        this.delayTotalFrames = delay * this.framesPerSec;
    }
    
    private BufferedImage getDelayScreen(int second) {
        BufferedImage image = new BufferedImage(this.screenWidth, this.screenHeight, this.imageType);
        Graphics2D g2 = image.createGraphics();
        g2.setFont(this.font);
        g2.setRenderingHints(this.renderHints);
        g2.setColor(this.bg);
        g2.fillRect(0, 0, this.screenWidth, this.screenHeight);
        FontMetrics fm = g2.getFontMetrics(); 
        
        g2.setColor(this.fg);
        String text = String.valueOf(second);
        double scale = image.getHeight()/(fm.getHeight());
        double xt = -(((scale*image.getWidth())-image.getWidth())/2);
        double yt = -(((scale*image.getHeight())-image.getHeight())/2);
        g2.translate(xt, yt); 
        g2.scale(scale, scale);
        g2.drawString(text, (image.getWidth()/2)-(fm.stringWidth(text)/2), (image.getHeight()/2)+(fm.getHeight()/4)+1);
        
        g2.dispose();
        
        return image;
    }
    
    public BufferedImage nextFrame() {
        if(this.delay > 0 && this.delayIndex < this.delayTotalFrames) {
            if (this.delayIndex == 0) {
                this.delayIndex += 1;
                return this.getDelayScreen(delay);
            } else {
                int second = Math.floorDiv(this.delayIndex, this.framesPerSec);
                if(Math.floorMod(this.delayIndex, this.framesPerSec) > 0) {
                    second += 1;
                }
                this.delayIndex += 1;
                return this.getDelayScreen(this.delay - second);
            }
        }
        Frame frame = this.getLinesOnScreen();
        BufferedImage image = new BufferedImage(this.screenWidth, this.screenHeight, this.imageType);
        Graphics2D g2 = image.createGraphics();
        
        g2.setFont(font);

        g2.setRenderingHints(this.renderHints);

        // Set background
        g2.setColor(this.bg);
        g2.fillRect(0, 0, this.screenWidth, this.screenHeight);
        g2.setColor(this.fg);
        while(frame.hasNext()) {
            g2.drawString(frame.getLine(), 0, frame.getHeight());
            frame.nextLine();
        }
        g2.dispose();
        this.moveUpLines();
        return image;
    }
    
    public boolean hasNextFrame() {
        if(this.delay > 0) {
            return this.delayIndex < this.delayTotalFrames || this.pageHeights[this.pageHeights.length-1] > -2;
        }
        return this.pageHeights[this.pageHeights.length-1] > -2;
    }
    
    public int totalFrames() {
        if (this.delay > 0) {
            return (this.pageHeights[this.pageHeights.length - 1] / this.pixelsPerFrame) + this.delayTotalFrames;
        }
        return this.pageHeights[this.pageHeights.length - 1] / this.pixelsPerFrame;
    }
    
    private class Frame {
        private final ArrayList<Integer> heights;
        private final ArrayList<String> lines;
        private int idx;
        
        public Frame() {
            this.heights = new ArrayList<>();
            this.lines = new ArrayList<>();
            this.idx = 0;  
        }
        
        public void addLine(int height, String line) {
            this.heights.add(height);
            this.lines.add(line);
        }
        
        public String getLine() {
            return this.lines.get(idx);
        }
        
        public int getHeight() {
            return this.heights.get(idx);
        }
        
        public void nextLine() {
            this.idx += 1;
        }
        
        public boolean hasNext() {
            return this.idx < (this.lines.size());
        }
    }
    
}
