package org.nucleodevel.webapptemplate.named.mb.jsf;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;

import org.nucleodevel.webapptemplate.dao.AbstractDao;
import org.nucleodevel.webapptemplate.entity.AbstractEntity;
import org.nucleodevel.webapptemplate.named.mb.AbstractMb;
import org.nucleodevel.webapptemplate.session.dao.jsf.AbstractSessionJsfDao;
import org.nucleodevel.webapptemplate.util.JsfUrlUtils;
import org.nucleodevel.webapptemplate.util.ParameterizedClassUtils;
import org.nucleodevel.webapptemplate.util.RandomUtils;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 * <p>
 *   Abstract subclass of AbstractMb that implements the default behavior of a managed bean for 
 *   statistics, queries, and reports, in order not to pollute the subclasses of AbstractJsfCrudMb.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <E> Subclass of AbstractEntity that maps an entity of a datasource.
 * @param <DAO> DAO class that provides access to the datasource.
 * @param <SDAO> DAO class that maps the current HTTP session of the application that owns this 
 *   managed bean.
 */
public abstract class AbstractJsfStatsMb
	<E extends AbstractEntity<?>, DAO extends AbstractDao<E>, SDAO extends AbstractSessionJsfDao>
	extends AbstractMb<SDAO> implements Serializable {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Attributes
	 * --------------------------------------------------------------------------------------------
	 */

	
    private static final long serialVersionUID = 1L;
	
    /**
     * <p>
     *   DAO provides access to the datasource used by the managed bean.
     * </p>
     */
    @Inject
	protected DAO dao;
    
    /**
     * <p>
     *   Attribute that stores the class adopted by E, which is the target entity of the DAO. It is
     *   often used to get the name of this class.
     * </p>
     */
    private Class<E> entityClass;
	
	/**
	 * <p>
	 *   Used as the beggining date for searches. No operations are defined here, but begin can be 
	 *   used in subclasses in their searches. Begin must be started by an HTTP GET parameter with 
	 *   the same name.
	 * </p>
	 */
	private Date begin;
	
	/**
	 * <p>
	 *   Used as the end date for searches. No operation is defined here, but end can be used in 
	 *   subclasses in their searches. End must be started by an HTTP GET parameter with the same 
	 *   name.
	 * </p>
	 */
	private Date end;
	
	/**
	 * <p>
	 *   Stores a model for a chart that shows the daily total of the entities E in a given 
	 *   period.
	 * </p>
	 */
	private LineChartModel totalByDayChartModel;
	
	/**
	 * <p>
	 *   Stores a model for a chart that shows the monthly total of the entities E in a given 
	 *   period.
	 * </p>
	 */
	private LineChartModel totalByMonthChartModel;
	
	/**
	 * <p>
	 *   Stores a model for a chart that shows the yearly total of the entities E in a given 
	 *   period.
	 * </p>
	 */
	private LineChartModel totalByYearChartModel;
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Getters and setters
	 * --------------------------------------------------------------------------------------------
	 */
	
    
	/**
     * <p>
     *   Returns the class<?> of E via ParameterizedClassUtils
     * </p>
     */
    @SuppressWarnings("unchecked")
	protected Class<E> getEntityClass() {
    	if (entityClass == null)
    		entityClass = 
    			(Class<E>) ParameterizedClassUtils 
    				.getParameterClassFromParameterizedClass(getClass(), 0);
    	return entityClass;
    }
 
	/**
     * <p>
     *   Attempts to get the attribute begin by the HTTP GET parameter with the same name or define 
     *   null.
     * </p>
     */
    public Date getBegin() {
    	if (begin == null)
    		begin = JsfUrlUtils.getUrlDateParam("begin");
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	/**
     * <p>
     *   Attempts to get the attribute end by the HTTP GET parameter with the same name or define 
     *   null.
     * </p>
     */
    public Date getEnd() {
		if (end == null)
			end = JsfUrlUtils.getUrlDateParam("end");
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Chart getters
	 * --------------------------------------------------------------------------------------------
	 */

	/**
     * <p>
     *   Method that supports the getters that stores the time unit charts.
     * </p>
     */
	private LineChartModel getTotalByTimeUnitChartModel(
		Map<Date, Long> totalByTimeUnit, String xTickFormat 
	) throws ParseException {
		
		String seriesColor = RandomUtils.getRandomStringColorForChartSeries();
		
		LineChartModel totalByTimeUnitChartModel = new LineChartModel();
        totalByTimeUnitChartModel.setZoom(true);
        totalByTimeUnitChartModel.setShowPointLabels(true);
        totalByTimeUnitChartModel.setSeriesColors(seriesColor);
        totalByTimeUnitChartModel.setShadow(false);

		LineChartSeries series = new LineChartSeries();
		series.setSmoothLine(true);
		
		DateAxis xAxis = new DateAxis();
	    xAxis.setTickAngle(-70);
	    xAxis.setTickFormat(xTickFormat);
	    totalByTimeUnitChartModel.getAxes().put(AxisType.X, xAxis);
        Axis yAxis = totalByTimeUnitChartModel.getAxis(AxisType.Y);
        yAxis.setTickFormat("%d");
        yAxis.setMin(0);
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        
        long max = 0;
        for (Map.Entry<Date, Long> entry : totalByTimeUnit.entrySet()) {
        	String key = dateFormat.format(entry.getKey());
        	Long value = entry.getValue();
        	if (value > max)
        		max = value;
            series.set(key, value);
        }
        
        totalByTimeUnitChartModel.addSeries(series);
		
        return totalByTimeUnitChartModel;
	}
    
	/**
	 * <p>
	 *   Stores a model for a chart that shows the daily total of the entities E in a given 
	 *   period.
	 * </p>
	 */
	public LineChartModel getTotalByDayChartModel(Map<Date, Long> totalByDay) 
		throws ParseException {
		
		if (totalByDayChartModel == null)
			totalByDayChartModel = getTotalByTimeUnitChartModel(totalByDay, "%d/%m/%Y");
		return totalByDayChartModel;
	}

	/**
	 * <p>
	 *   Stores a model for a chart that shows the monthly total of the entities E in a given 
	 *   period.
	 * </p>
	 */
	public LineChartModel getTotalByMonthChartModel(Map<Date, Long> totalByMonth) 
		throws ParseException {
		
		if (totalByMonthChartModel == null)
			totalByMonthChartModel = getTotalByTimeUnitChartModel(totalByMonth, "%m/%Y");
		return totalByMonthChartModel;
	}

	/**
	 * <p>
	 *   Stores a model for a chart that shows the yearly total of the entities E in a given 
	 *   period.
	 * </p>
	 */
	public LineChartModel getTotalByYearChartModel(Map<Date, Long> totalByYear) 
		throws ParseException {
		
		if (totalByYearChartModel == null)
			totalByYearChartModel = getTotalByTimeUnitChartModel(totalByYear, "%Y");
		return totalByYearChartModel;
	}
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   JSF View Filters
	 * --------------------------------------------------------------------------------------------
	 */
    
	/**
     * <p>
     *   Filter that enables the managed bean to allow or prohibit the search of entities. 
     *   Intended to be used primarily in search.jsf, but can be used in other JSF views.
     * </p>
     */
    public abstract boolean canSearch();
    
    /**
     * <p>
     *   Filter that enables the managed bean to allow or prohibit the report of entities. 
     *   Intended to be used primarily in report.jsf, but can be used in other JSF views.
     * </p>
     */
    public abstract boolean canReport();
    
}