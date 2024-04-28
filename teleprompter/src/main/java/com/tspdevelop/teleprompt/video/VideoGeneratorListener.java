package com.tspdevelop.teleprompt.video;

/**
 *
 * @author tspdev
 */
public interface VideoGeneratorListener {
    
    public void updateProgress(double percent);
    
    public void done();
    
}
