package com.tspdevelop.teleprompt.video;

import io.humble.video.Codec;
import io.humble.video.Encoder;
import io.humble.video.MediaPacket;
import io.humble.video.MediaPicture;
import io.humble.video.Muxer;
import io.humble.video.MuxerFormat;
import io.humble.video.PixelFormat;
import io.humble.video.Rational;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author tspdev
 */
public class VideoGenerator {

    private final int IMAGE_TYPE = BufferedImage.TYPE_3BYTE_BGR;
    private final PixelFormat.Type PIXEL_FORMAT = PixelFormat.Type.PIX_FMT_YUV420P;
    private final RenderingHints RENDERING_HINTS;
    private final Rational framerate;
    private final String Filename;
    private final String formatName;
    private final Muxer muxer;
    private final MuxerFormat format;
    private final Codec codec;

    private final int height;
    private final int width;
    private final int framesPerSec;
    private Encoder encoder;
    private Font font;
    private long index;
    private MediaPacket packet;
    private int delay;
    private VideoGeneratorListener listener;

    public VideoGenerator(int videoSize, int frameRate, String filename, String formatName, String codecname) throws IOException {
        this.height = videoSize;
        this.width = (int) Math.round(videoSize * 1.77777778);
        this.framesPerSec = frameRate;
        this.Filename = filename;
        File f = new File(this.Filename);
        if (f.exists()) {
            throw new IOException("Video file already exists: " + this.Filename);
        }
        this.formatName = formatName;
        this.muxer = Muxer.make(filename, null, this.formatName);
        this.format = muxer.getFormat();
        if (codecname != null) {
            codec = Codec.findEncodingCodecByName(codecname);
        } else {
            codec = Codec.findEncodingCodec(format.getDefaultVideoCodecId());
        }
        this.framerate = Rational.make(1, framesPerSec);
        RENDERING_HINTS = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        RENDERING_HINTS.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        this.delay = 0;
        this.listener = null;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    private void createEncoder() throws InterruptedException, IOException {
        if (this.encoder == null) {
            this.encoder = Encoder.make(codec);
            encoder.setWidth(this.width);
            encoder.setHeight(this.height);
            encoder.setPixelFormat(this.PIXEL_FORMAT);
            encoder.setTimeBase(framerate);
            if (format.getFlag(MuxerFormat.Flag.GLOBAL_HEADER)) {
                encoder.setFlag(Encoder.Flag.FLAG_GLOBAL_HEADER, true);
            }
            encoder.open(null, null);
            muxer.addNewStream(encoder);
            muxer.open(null, null);
            this.index = 0;
        }
    }

    public void setDelay(int seconds) throws InterruptedException, IOException {
        this.delay = seconds;
    }
    
    private FontMetrics getFontMetrics() {
        BufferedImage image = new BufferedImage(width, height, IMAGE_TYPE);
        Graphics2D g2 = image.createGraphics();
        return g2.getFontMetrics(font);
    }
    
    private ArrayList<String> processScript(String script) {
        ArrayList<String> list = new ArrayList<>();
        FontMetrics metrics = getFontMetrics();
        
        String line = "";
        String tmpLine;
        String[] words = script.split(" ");
        for(String word: words) {
            if(word.contains("\n")) {
                String[] wordSplit = word.split("\n");
                tmpLine = line + (line.isEmpty()?"":" ") + wordSplit[0];
                if(metrics.stringWidth(tmpLine) > (width-10)) {
                    list.add(line);
                    line = wordSplit[0];
                    list.add(line);
                    line = wordSplit[1];
                } else {
                    line = tmpLine;
                    list.add(line);
                    line = wordSplit[1];
                }
            } else {
                tmpLine = line + (line.isEmpty()?"":" ") + word;
                if(metrics.stringWidth(tmpLine) > (width-10)) {
                    list.add(line);
                    line = word;
                } else {
                    line = tmpLine;
//                    list.add(line);
                }
            }
        }
        if(!line.isEmpty()) {
            list.add(line);
        }
        
        return list;
    }
    
    private int wordsPerPage(FontMetrics fm) {
        int linesPerPage = height/fm.getHeight();
        int wordsPerLine = width/fm.stringWidth("tests ");
        return linesPerPage * wordsPerLine;
    }
    
    public void setListener(VideoGeneratorListener listener) {
        this.listener = listener;
    }
    
    public void writeTeleprompter(String script, int wordsPerMin, Color bg, Color fg) throws InterruptedException, IOException {
        createEncoder();
        MediaPictureConverter converter = null;
        final MediaPicture picture = MediaPicture.make(
                encoder.getWidth(),
                encoder.getHeight(),
                this.PIXEL_FORMAT);
        picture.setTimeBase(framerate);
        this.packet = MediaPacket.make();
        
        ArrayList<String> lines = processScript(script);
        FontMetrics fm = this.getFontMetrics();
        int wordsPerPage = this.wordsPerPage(fm);
        
        FrameGenerator generator = new FrameGenerator(
                                    lines, 
                                    wordsPerPage, 
                                    wordsPerMin, 
                                    this.framesPerSec, 
                                    this.height, 
                                    this.width, 
                                    fm.getHeight(), 
                                    bg, 
                                    fg);
        generator.setDelay(delay);
        generator.setImageType(this.IMAGE_TYPE);
        generator.setFont(font);
        generator.setRenderHints(RENDERING_HINTS);
        int totalFrames = generator.totalFrames();
        int previousPercentage = -1;
        double percentage =  0;
        
        while(generator.hasNextFrame()) {
            if(this.listener != null) {
                if (this.index > 0) {
                    percentage = ((this.index * 1.0) / totalFrames) * 100;
                }
                if(percentage > previousPercentage) {
                    previousPercentage = (int)Math.round(percentage) + 1;
                    this.listener.updateProgress(percentage);
                }
            }
            final BufferedImage screen = generator.nextFrame();
            if (converter == null) {
                converter = MediaPictureConverterFactory.createConverter(screen, picture);
            }
            converter.toPicture(picture, screen, index);
            do {
                encoder.encode(packet, picture);
                if (packet.isComplete()) {
                    muxer.write(packet, false);
                }
            } while (packet.isComplete());
            index++;
        }
        if (packet != null) {
            do {
                encoder.encode(packet, null);
                if (packet.isComplete()) {
                    muxer.write(packet, false);
                }
            } while (packet.isComplete());
        }

        /**
         * Finally, let's clean up after ourselves.
         */
        muxer.close();
    }

}
