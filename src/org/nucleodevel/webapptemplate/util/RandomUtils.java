package org.nucleodevel.webapptemplate.util;

/**
 * <p>
 *   Implements several random funcionalities.
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public class RandomUtils {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Methods
	 * --------------------------------------------------------------------------------------------
	 */
    
    
	/**
	 * <p>
	 *   Gets a 6-hex random string that represents a color.
	 * </p>
	 */
    public static String getRandomStringColor() {
    	return 
			Integer.toHexString((int) (Math.random() * 16))
			+ Integer.toHexString((int) (Math.random() * 16))
			+ Integer.toHexString((int) (Math.random() * 16))
			+ Integer.toHexString((int) (Math.random() * 16))
			+ Integer.toHexString((int) (Math.random() * 16))
			+ Integer.toHexString((int) (Math.random() * 16))
		;
    }    
    
    /**
	 * <p>
	 *   Gets a 6-hex random string that represents a color, but a color not too dark for a chart 
	 *   series.
	 * </p>
	 */
    public static String getRandomStringColorForChartSeries() {
    	return 
			Integer.toHexString((int) (Math.random() * 6 + 7))
			+ Integer.toHexString((int) (Math.random() * 16))
			+ Integer.toHexString((int) (Math.random() * 6 + 7))
			+ Integer.toHexString((int) (Math.random() * 16))
			+ Integer.toHexString((int) (Math.random() * 6 + 7))
			+ Integer.toHexString((int) (Math.random() * 16))
		;
    }
	
}