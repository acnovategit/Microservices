package com.webapp.utility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspose.cad.Color;
import com.aspose.cad.Image;
import com.aspose.cad.ImageOptionsBase;
import com.aspose.cad.fileformats.cad.CadImage;
import com.aspose.cad.fileformats.cad.cadconsts.CadEntityTypeName;
import com.aspose.cad.fileformats.cad.cadobjects.Cad3DPoint;
import com.aspose.cad.fileformats.cad.cadobjects.CadBaseEntity;
import com.aspose.cad.fileformats.cad.cadobjects.CadBlockEntity;
import com.aspose.cad.fileformats.cad.cadobjects.CadInsertObject;
import com.aspose.cad.fileformats.cad.cadobjects.CadMText;
import com.aspose.cad.fileformats.cad.cadobjects.CadText;
import com.aspose.cad.fileformats.cad.cadobjects.attentities.CadAttDef;
import com.aspose.cad.fileformats.cad.cadobjects.attentities.CadAttrib;
import com.aspose.cad.imageoptions.CadRasterizationOptions;
import com.aspose.cad.imageoptions.JpegOptions;
import com.aspose.cad.imageoptions.PdfOptions;
import com.aspose.cad.imageoptions.PngOptions;

/**
 * @author Ayush
 *
 */
public class AsposeCADUtility {
	private final static Logger logger = LoggerFactory.getLogger(AsposeCADUtility.class);

	public static String ConvertCADDrawingToRasterPNGFormat(String filePath)
	{
		
		logger.info("********  Inside ConvertCADDrawingToRasterPNGFormat method of AsposeCADUtility ********");
			Image image = Image.load(filePath); 
			logger.info("********  CAD Document with filePath"+filePath+"is accessed ********");
			// Create an instance of CadRasterizationOptions
		    CadRasterizationOptions rasterizationOptions = new CadRasterizationOptions();
		    // Set page width & height
		    rasterizationOptions.setPageWidth(1200);
		    rasterizationOptions.setPageHeight(1200);

		    // Create an instance of PngOptions for the resultant image
		    ImageOptionsBase options = new PngOptions();
		    //Set rasterization options
		    options.setVectorRasterizationOptions(rasterizationOptions);

		    // Save resultant image
		    image.save(filePath+".png", options);
		    image.close();
		    String fileName=filePath+".png";
		    logger.info("********  END of ConvertCADDrawingToRasterPNGFormat method of AsposeCADUtility  ********");
		    return fileName;
	}
	public static String ConvertCADDrawingToRasterJPGFormat(String filePath)
	{
		logger.info("********  Inside ConvertCADDrawingToRasterJPGFormat method of AsposeCADUtility ********");
		Image image = Image.load(filePath);
		logger.info("********  CAD Document with filePath"+filePath+"is accessed ********");
		// Create an instance of CadRasterizationOptions
		CadRasterizationOptions rasterizationOptions = new CadRasterizationOptions();
		// Set image width & height
		rasterizationOptions.setPageWidth(500);
		rasterizationOptions.setPageHeight(500);

		// Set the drawing to render at the center of image
//		rasterizationOptions.setCenterDrawing(true);

                List<String> stringList = new ArrayList<>(Arrays.asList("0"));
		// Add the layer name to the CadRasterizationOptions's layer list
	        rasterizationOptions.setLayers(stringList);
 
		// Create an instance of JpegOptions (or any ImageOptions for raster formats)
		JpegOptions options = new JpegOptions();
		// Set VectorRasterizationOptions property to the instance of CadRasterizationOptions
		options.setVectorRasterizationOptions(rasterizationOptions);
		// Export each layer to JPEG format
		image.save(filePath + ".jpg", options);
		image.close();
		String fileName=filePath+".jpg";
		logger.info("********  END of ConvertCADDrawingToRasterJPGFormat method of AsposeCADUtility  ********");
		return fileName;
	}
	public static String ConvertCADDrawingToRasterPDF(String filePath)
	{
		
		logger.info("********  Inside ConvertCADDrawingToRasterPDF method of AsposeCADUtility ********");
			Image image = Image.load(filePath); 
			logger.info("********  CAD Document with filePath"+filePath+"is accessed ********");
			// Create an instance of CadRasterizationOptions
			 CadRasterizationOptions rasterizationOptions = new CadRasterizationOptions();
			    rasterizationOptions.setBackgroundColor(Color.getWhite());
			    rasterizationOptions.setPageWidth(1600);
			    rasterizationOptions.setPageHeight(1600);

			    // Create an instance of PdfOptions
			    PdfOptions pdfOptions = new PdfOptions();
			    // Set the VectorRasterizationOptions property
			    pdfOptions.setVectorRasterizationOptions(rasterizationOptions);

			    // Export the DXF to PDF
			    image.save(filePath + ".pdf", pdfOptions);    
			    image.close();
			    String fileName=filePath+".pdf";
			    logger.info("********  END of ConvertCADDrawingToRasterPDF method of AsposeCADUtility  ********");
			    return fileName;
	}
	public static void AddWatermark (String filePath)
	{
		logger.info("********  Inside AddWatermark method of AsposeCADUtility ********");
		CadImage cadImage = (CadImage) Image.load(filePath);
		logger.info("********  CAD Document with filePath"+filePath+"is accessed ********");
        //add new MTEXT
        CadMText watermark = new CadMText();
        watermark.setText("Watermark message");
        watermark.setInitialTextHeight(40);
        watermark.setInsertionPoint(new Cad3DPoint(300, 40));
        watermark.setLayerName("0");
        cadImage.getBlockEntities().get_Item("*Model_Space").addEntity(watermark);
        
        
        
        // or add more simple entity like Text
        CadText text = new CadText();
        text.setDefaultValue("Watermark text");
        text.setTextHeight(40);
        text.setFirstAlignment(new Cad3DPoint(300, 40));
        text.setLayerName("0") ;
        cadImage.getBlockEntities().get_Item("*Model_Space").addEntity(text);
        
        
        
        // export to pdf
        CadRasterizationOptions rasterizationOptions = new CadRasterizationOptions();
        rasterizationOptions.setPageWidth(1600);
        rasterizationOptions.setPageHeight(1600);
        rasterizationOptions.setLayouts(new String[]{"Model"});
        PdfOptions pdfOptions = new PdfOptions();
        pdfOptions.setVectorRasterizationOptions(rasterizationOptions);
        cadImage.save(filePath + "AddWatermark_out.pdf", pdfOptions);
        cadImage.close();
        logger.info("********  END of AddWatermark method of AsposeCADUtility  ********");
	}
	public static void ConvertAllLayersToImages(String filePath)
	{
		logger.info("********  Inside ConvertAllLayersToImages method of AsposeCADUtility ********");
		// Load a CAD drawing in an instance of CadImage
		CadImage image = (CadImage) Image.load(filePath);
		logger.info("********  CAD Document with filePath"+filePath+"is accessed ********");
		// Create an instance of CadRasterizationOptions
		CadRasterizationOptions rasterizationOptions = new CadRasterizationOptions();

		// Set image width & height
		rasterizationOptions.setPageWidth(500);
		rasterizationOptions.setPageHeight(500);

		// Get the layers in an instance of CadLayersDictionary.
		// Iterate over the layers
		for (String layer : image.getLayers().getLayersNames()) {
			// Display layer name for tracking
			System.out.println("Start with " + layer);

			List<String> stringList = Arrays.asList(layer);
			// Add the layer name to the CadRasterizationOptions's layer list
			rasterizationOptions.setLayers(stringList);

			// Create an instance of JpegOptions (or any ImageOptions for raster formats)
			JpegOptions options = new JpegOptions();
			// Set VectorRasterizationOptions property to the instance of
			// CadRasterizationOptions
			options.setVectorRasterizationOptions(rasterizationOptions);
			// Export each layer to JPEG format
			image.save(filePath + layer + "_out_.jpg", options);

		}
		 image.close();
		 logger.info("********  END of ConvertAllLayersToImages method of AsposeCADUtility  ********");
	}
	
	 
	public static void searchTextInDWGAutoCADFile(String filePath) 
    { 
 //String  dataDir="Test_Apsose.CAD\\";
  // Load an existing DWG file as CadImage.
		logger.info("********  Inside searchTextInDWGAutoCADFile method of AsposeCADUtility ********");
		CadImage cadImage = (CadImage) CadImage.load(filePath);
		for (CadBaseEntity entity : cadImage.getEntities()) {
			IterateCADNodeEntities(entity);
		}

		// Search for text in the block section
		for (CadBlockEntity blockEntity : cadImage.getBlockEntities().getValues()) {
			for (CadBaseEntity entity : blockEntity.getEntities()) {
				IterateCADNodeEntities(entity);
			}
		}
		 logger.info("********  END of searchTextInDWGAutoCADFile method of AsposeCADUtility  ********");
} 
//Recursive function to iterate nodes inside nodes
private static void IterateCADNodeEntities(CadBaseEntity obj) 
{ 
 switch (obj.getTypeName())
 { 
     case CadEntityTypeName.TEXT: 
         CadText childObjectText = (CadText) obj; 

        logger.info(childObjectText.getDefaultValue()); 

         break; 

     case CadEntityTypeName.MTEXT: 
         CadMText childObjectMText = (CadMText) obj; 

         logger.info(childObjectMText.getText()); 

         break; 

     case CadEntityTypeName.INSERT: 
         CadInsertObject childInsertObject = (CadInsertObject) obj; 

         for (CadBaseEntity tempobj : childInsertObject.getChildObjects())
         { 
             IterateCADNodeEntities(tempobj); 
         } 
         break; 

     case CadEntityTypeName.ATTDEF: 
         CadAttDef attDef = (CadAttDef) obj; 

         System.out.println(attDef.getDefaultString()); 
         break; 

     case CadEntityTypeName.ATTRIB: 
         CadAttrib attAttrib = (CadAttrib) obj; 

         System.out.println(attAttrib.getDefaultText()); 
         break; 
 } 
}
	 
}
