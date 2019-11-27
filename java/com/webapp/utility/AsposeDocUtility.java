package com.webapp.utility;

import java.awt.Color;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspose.words.DocSaveOptions;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FindReplaceOptions;
import com.aspose.words.HeaderFooter;
import com.aspose.words.HeaderFooterCollection;
import com.aspose.words.HeaderFooterType;
import com.aspose.words.HorizontalAlignment;
import com.aspose.words.NodeType;
import com.aspose.words.PageSetup;
import com.aspose.words.Paragraph;
import com.aspose.words.RelativeHorizontalPosition;
import com.aspose.words.RelativeVerticalPosition;
import com.aspose.words.SaveFormat;
import com.aspose.words.Section;
import com.aspose.words.Shape;
import com.aspose.words.ShapeType;
import com.aspose.words.VerticalAlignment;
import com.aspose.words.WrapType;




/**
 * @author Ayush
 *
 */

public class AsposeDocUtility {
	private final static Logger logger = LoggerFactory.getLogger(AsposeDocUtility.class);
	/**
	 * @param filePath
	 * @param headerType
	 * @return
	 * @throws Exception
	 */
	public static String readHeader(String filePath,String headerType) throws Exception
	{
		logger.info("********  Inside readHeader method of AsposeDocUtility ********");
		String headerData=null;
		HeaderFooter header=null;
		Document doc = new Document(filePath);
		logger.info("********  Document with filePath"+filePath+"is accessed ********");
		HeaderFooterCollection hfc = doc.getFirstSection().getHeadersFooters();
		if(headerType.equalsIgnoreCase("HEADER_PRIMARY"))
		{
			 header=hfc.getByHeaderFooterType(HeaderFooterType.HEADER_PRIMARY);
			 logger.info("********  reading primary Header from the document ********");
		}
	    else if(headerType.equalsIgnoreCase("HEADER_FIRST"))
			 {
			header= hfc.getByHeaderFooterType(HeaderFooterType.HEADER_FIRST);
			logger.info("********  reading First Header from the document ********");
			 }
			else
			{
			header= hfc.getByHeaderFooterType(HeaderFooterType.HEADER_EVEN);
			logger.info("********  reading even Header from the document ********");
			}
			headerData= header.getText();
			logger.info("********  Header Text is "+headerData+" ********");
		logger.info("********  END of readHeader method of AsposeDocUtility  ********");
		return headerData;
	}
	/**
	 * @param filePath
	 * @param headerType
	 * @return
	 * @throws Exception
	 */
	public static String readFooter(String filePath,String headerType) throws Exception
	{
		logger.info("********  Inside readFooter method of AsposeDocUtility ********");
		String footerData=null;
		HeaderFooter header=null;
		if(filePath.endsWith(".docx")||filePath.endsWith(".doc")){
		Document doc = new Document(filePath);
		logger.info("********  Document with filePath"+filePath+"is accessed ********");
		HeaderFooterCollection hfc = doc.getFirstSection().getHeadersFooters();
		if(headerType.equalsIgnoreCase("FOOTER_PRIMARY"))
		{
			 header=hfc.getByHeaderFooterType(HeaderFooterType.FOOTER_PRIMARY);
			 logger.info("********  reading primary Footer from the document ********");
		}
	    else if(headerType.equalsIgnoreCase("FOOTER_FIRST"))
	    {	
	    	header= hfc.getByHeaderFooterType(HeaderFooterType.FOOTER_FIRST);
	    	logger.info("********  reading First Footer from the document ********");
	    }
	    else
	    {
			header= hfc.getByHeaderFooterType(HeaderFooterType.FOOTER_EVEN);
			logger.info("********  reading even Footer from the document ********");
	    }
			footerData= header.getText();
		}
		else
			footerData="file format is incorrect!";
		logger.info("********  Footer Text is "+footerData+" ********");
		logger.info("********  END of readFooter method of AsposeDocUtility  ********");
		return footerData;
	}
	/**
	 * @param filePath
	 * @param watermarkText
	 * @throws Exception
	 */
	public  static void insertWatermarkText(String filePath, String watermarkText) throws Exception
	{
		logger.info("********  Inside insertWatermarkText method of AsposeDocUtility ********");
		Document doc = new Document(filePath);
		logger.info("********  Document with filePath"+filePath+"is accessed ********");
	    Shape watermark = new Shape(doc, ShapeType.TEXT_PLAIN_TEXT);

	    // Set up the text of the watermark.
	    watermark.getTextPath().setText(watermarkText);
	    watermark.getTextPath().setFontFamily("Arial");
	    watermark.setWidth(500);
	    watermark.setHeight(100);
	    logger.info("********  Setting up text of the watermark  ********");
	    // Text will be directed from the bottom-left to the top-right corner.
	    watermark.setRotation(-40);
	    // Remove the following two lines if you need a solid black text.
	    watermark.getFill().setColor(Color.GRAY); // Try LightGray to get more Word-style watermark
	    watermark.setStrokeColor(Color.GRAY); // Try LightGray to get more Word-style watermark

	    // Place the watermark in the page center.
	    watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
	    watermark.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
	    watermark.setWrapType(WrapType.NONE);
	    watermark.setVerticalAlignment(VerticalAlignment.CENTER);
	    watermark.setHorizontalAlignment(HorizontalAlignment.CENTER);
	    logger.info("********  placing the watermark  ********");
	    // Create a new paragraph and append the watermark to this paragraph.
	    Paragraph watermarkPara = new Paragraph(doc);
	    watermarkPara.appendChild(watermark);

	    // Insert the watermark into all headers of each document section.
	    for (Section sect : doc.getSections())
	    {
	        // There could be up to three different headers in each section, since we want
	        // the watermark to appear on all pages, insert into all headers.
	    	logger.info("******** Calling  insertWatermarkIntoHeader for primary header ********");
	        insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_PRIMARY);
	        logger.info("******** Calling  insertWatermarkIntoHeader for first header ********");
	        insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_FIRST);
	        logger.info("******** Calling  insertWatermarkIntoHeader for even header ********");
	       insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_EVEN);
	    }
	    logger.info("********  END of insertWatermarkText method of AsposeDocUtility  ********");
	}
	/**
	 * @param watermarkPara
	 * @param sect
	 * @param headerType
	 * @throws Exception
	 */
	private static void insertWatermarkIntoHeader(Paragraph watermarkPara, Section sect, int headerType) throws Exception
	{
		logger.info("********  Inside insertWatermarkIntoHeader method of AsposeDocUtility ********");
		HeaderFooter header = sect.getHeadersFooters().getByHeaderFooterType(headerType);
	 
	    if (header == null)
	    {
	        // There is no header of the specified type in the current section, create it.
	    	logger.info("********  header is null  ********");
	        header = new HeaderFooter(sect.getDocument(), headerType);
	        sect.getHeadersFooters().add(header);
	    }
	 
	    // Insert a clone of the watermark into the header.
	    logger.info("********  Insert a clone of the watermark into the header  ********");
	    header.appendChild(watermarkPara.deepClone(true));
	    logger.info("********  END of insertWatermarkIntoHeader method of AsposeDocUtility  ********");
	}
	/**
	 * @param filePath
	 * @throws Exception
	 */
	public static void acceptRevisions(String filePath) throws Exception
	{
		logger.info("********  Inside acceptRevisions method of AsposeDocUtility ********");
		Document doc = new Document(filePath);
		logger.info("********  Document with filePath"+filePath+"is accessed ********");
		doc.acceptAllRevisions();
		doc.save(filePath);
		logger.info("********  END of acceptRevisions method of AsposeDocUtility  ********");
	}
	public static String saveAsDoc(String filePath) throws Exception {
 
		logger.info("********  Inside saveAsDoc method of AsposeDocUtility ********");
		Document doc = new Document(filePath);
		logger.info("********  Document with filePath"+filePath+"is accessed ********");
        DocSaveOptions options = new DocSaveOptions(SaveFormat.DOC);

      

        doc.save(filePath + ".doc", options);
        logger.info("********  END of saveAsDoc method of AsposeDocUtility  ********");
        String fileName=filePath+".doc";
        return fileName;
    }
	
	/**
	 * @param filePath
	 * @param toReplace
	 * @param replaceText
	 * @param headerType
	 * @throws Exception
	 */
	public static void replaceTextInHeader(String filePath, String toReplace,String replaceText,String headerType) throws Exception
	{
		logger.info("********  Inside replaceTextInHeader method of AsposeDocUtility ********");
		Document doc = new Document(filePath);
		logger.info("********  Document with filePath"+filePath+"is accessed ********");
        HeaderFooterCollection headersFooters = doc.getFirstSection().getHeadersFooters();
        HeaderFooter header=null;
        if(headerType.equalsIgnoreCase("HEADER_PRIMARY")) {
        	header = headersFooters.get(HeaderFooterType.HEADER_PRIMARY);
        	logger.info("********  Retrieving Primary Header  ********");
        }
        else if(headerType.equalsIgnoreCase("HEADER_FIRST"))
    	{
    		header = headersFooters.get(HeaderFooterType.HEADER_FIRST);
    		logger.info("********  Retrieving First Header  ********");
    	}
        else
        {
        	header = headersFooters.get(HeaderFooterType.HEADER_EVEN);
        	logger.info("********  Retrieving Even Header  ********");
        
        }
        FindReplaceOptions options = new FindReplaceOptions();
        options.setMatchCase(false);
        options.setFindWholeWordsOnly(false);

        header.getRange().replace(toReplace, replaceText, options);
        logger.info("********  Replacement of text performed  ********");
        doc.save(filePath);
        logger.info("********  END of replaceTextInHeader method of AsposeDocUtility  ********");
	}
	/**
	 * @param filePath
	 * @param toReplace
	 * @param replaceText
	 * @param footerType
	 * @throws Exception
	 */
	public static void replaceTextInFooter(String filePath, String toReplace,String replaceText,String footerType) throws Exception
	{
		logger.info("********  Inside replaceTextInFooter method of AsposeDocUtility ********");
		Document doc = new Document(filePath);
		logger.info("********  Document with filePath"+filePath+"is accessed ********");
        HeaderFooterCollection headersFooters = doc.getFirstSection().getHeadersFooters();
        HeaderFooter footer=null;
        if(footerType.equalsIgnoreCase("FOOTER_PRIMARY"))
        {
        	footer = headersFooters.get(HeaderFooterType.FOOTER_PRIMARY);
        	logger.info("********  Retrieving Primary Footer  ********");
        }
        else if(footerType.equalsIgnoreCase("FOOTER_FIRST"))
        	{
        	footer = headersFooters.get(HeaderFooterType.FOOTER_FIRST);
        	logger.info("********  Retrieving First Footer  ********");
        	}
        else
        	{
        	footer = headersFooters.get(HeaderFooterType.FOOTER_EVEN);
        	logger.info("********  Retrieving Even Footer  ********");
        	}
        FindReplaceOptions options = new FindReplaceOptions();
        options.setMatchCase(false);
        options.setFindWholeWordsOnly(false);

        footer.getRange().replace(toReplace, replaceText, options);
        logger.info("********  Replacement of text performed  ********");
        doc.save(filePath);
        logger.info("********  END of replaceTextInFooter method of AsposeDocUtility  ********");
	}
	public static void removeWaterMark(String filePath) throws Exception
	{
		logger.info("********  Inside removeWaterMark method of AsposeDocUtility ********");
		Document doc = new Document(filePath);
		logger.info("********  Document with filePath"+filePath+"is accessed ********");
		logger.info("********  Calling method removeWatermarkText ********");
		removeWatermarkText(doc);
		logger.info("********  Watermark removal preformed ********");
        doc.save(filePath);
        logger.info("********  END of removeWaterMark method of AsposeDocUtility  ********");
	}
	 @SuppressWarnings("unchecked")
	private static void removeWatermarkText(Document doc) throws Exception {
		 logger.info("********  Inside removeWatermarkText method of AsposeDocUtility ********");
	        for (HeaderFooter hf : (Iterable<HeaderFooter>) doc.getChildNodes(NodeType.HEADER_FOOTER, true)) {
	            for (Shape shape : (Iterable<Shape>) hf.getChildNodes(NodeType.SHAPE, true)) {
	                if (shape.getName().contains("WaterMark"))
	                    shape.remove();
	                logger.info("********  Shape.remove() executed  ********");
	            }
	        }
	        logger.info("********  END of removeWatermarkText method of AsposeDocUtility  ********");
	    }
	 
	 
	 /**
	 * @param filePath
	 * @throws Exception
	 */
	public static String docToPDF(String filePath) throws Exception
	 {	  logger.info("********  Inside docToPDF method of AsposeDocUtility ********");
		 Document doc = new Document(filePath);
		 logger.info("********  Document with filePath"+filePath+"is accessed ********");
		 doc.save(filePath + ".pdf",SaveFormat.PDF);
		 logger.info("********  END of docToPDF method of AsposeDocUtility  ********");
		 String fileName=filePath+".pdf";
		 return fileName;
	 }
	
	 /**
	 * @param filePath
	 * @param imagePath
	 * @throws Exception
	 */
	public static void insertImageIntoHeader(String filePath, String imagePath) throws Exception
	 {
		logger.info("********  Inside insertImageIntoHeader method of AsposeDocUtility ********"); 
		Document doc = new Document(filePath);
		logger.info("********  Document with filePath"+filePath+"is accessed ********");
		 DocumentBuilder builder = new DocumentBuilder(doc);

		 Section currentSection = builder.getCurrentSection();

		 PageSetup pageSetup = currentSection.getPageSetup();
		

		 builder.moveToHeaderFooter(HeaderFooterType.HEADER_PRIMARY);


		 // Insert absolutely positioned image into the top/left corner of the header.

		 // Distance from the top/left edges of the page is set to 10 points.
		 File file = new File(imagePath);
		 if (file.exists())

		 {

		 builder.insertImage(imagePath, RelativeHorizontalPosition.PAGE, 10, RelativeVerticalPosition.PAGE, 10, 50, 50, WrapType.THROUGH);
		 logger.info("********  Inserting the Image completed ********");
		 }
		 doc.save(filePath);
		 logger.info("********  END of insertImageIntoHeader method of AsposeDocUtility  ********"); 
	 }
}
