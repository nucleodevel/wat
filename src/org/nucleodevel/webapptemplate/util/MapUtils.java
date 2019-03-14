package org.nucleodevel.webapptemplate.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>
 *   Implements several useful manipulation functionalities for Java maps.
 * </p>
 * @author Dallan Augusto Toledo Reis
 */
public class MapUtils {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Methods
	 * --------------------------------------------------------------------------------------------
	 */
    
    
	/**
     * <p>
     *   Creates a map with all the keys in a certain period, from begin to end, according to the 
     *   criterion of day, month or year.
     * </p>
     */
    public static Map<Date, Long> createADateLongMapByPeriod(
    	Date first, Date last, int CalendarType 
    ) {
    	Map<Date, Long> map = new TreeMap<Date, Long>();
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	
    	switch (CalendarType) {
    		case Calendar.DATE:
    			sdf = new SimpleDateFormat("yyyy-MM-dd");
    		break;
    		case Calendar.MONTH:
    			sdf = new SimpleDateFormat("yyyy-MM");
    		break;
    		case Calendar.YEAR:
    			sdf = new SimpleDateFormat("yyyy");
    		break;
    	}
    	
		Calendar firstDate = Calendar.getInstance();
		Calendar lastDate = Calendar.getInstance();

		try {
			firstDate.setTime(sdf.parse(sdf.format(first)));
			lastDate.setTime(sdf.parse(sdf.format(last)));
			
			firstDate.set(Calendar.HOUR_OF_DAY, 0);
			firstDate.set(Calendar.MINUTE, 0);
			firstDate.set(Calendar.SECOND, 0);
			
			lastDate.set(Calendar.HOUR_OF_DAY, 23);
			lastDate.set(Calendar.MINUTE, 59);
			lastDate.set(Calendar.SECOND, 59);
			
			for (Calendar d = firstDate; d.compareTo(lastDate) <= 0; d.add(CalendarType, 1))
				map.put(sdf.parse(sdf.format(d.getTime())), 0L);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return map;
    }
	
}