package com.webapp.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspose.cells.PageSetup;
import com.aspose.cells.Workbook;


/**
 * @author Ayush
 *
 */
public class AsposeExcelUtility {
	private final static Logger logger = LoggerFactory.getLogger(AsposeExcelUtility.class);
	
	/**
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static String readHeader(String filePath) throws Exception
	{
		logger.info("********  Inside readHeader method of AsposeExcelUtility ********");
		Workbook workbook = new Workbook(filePath);
		logger.info("********  Excel with filePath"+filePath+"is accessed ********");
		PageSetup pageSetup = workbook.getWorksheets().get(0).getPageSetup();
		 logger.info("********  reading Header from the excel ********");
		String header=pageSetup.getHeader(0);
		logger.info("********  END of readHeader method of AsposeExcelUtility  ********");
		return header;
	}
	public static String readFooter(String filePath) throws Exception
	{
		logger.info("********  Inside readFooter method of AsposeExcelUtility ********");
		Workbook workbook = new Workbook(filePath);
		logger.info("********  Excel with filePath"+filePath+"is accessed ********");
		PageSetup pageSetup = workbook.getWorksheets().get(0).getPageSetup();
		 logger.info("********  reading Footer from the excel ********");
		String header=pageSetup.getFooter(0);
		logger.info("********  END of readFooter method of AsposeExcelUtility  ********");
		return header;
	}
	public static void removeHeaderFooter(String filePath) throws Exception
	{
		logger.info("********  Inside removeHeaderFooter method of AsposeExcelUtility ********");
		Workbook workbook = new Workbook(filePath);
		logger.info("********  Excel with filePath"+filePath+"is accessed ********");
		PageSetup pageSetup = workbook.getWorksheets().get(0).getPageSetup();
		logger.info("********  Clearing Header Footer from the excel ********");
		pageSetup.clearHeaderFooter();
		logger.info("********  END of removeHeaderFooter method of AsposeExcelUtility  ********");
		
	}
	
	
}
