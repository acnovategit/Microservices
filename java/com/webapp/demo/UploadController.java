package com.webapp.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.webapp.demo.FileStorageService;
import com.webapp.demo.MediaTypeUtils;
import com.webapp.demo.UploadFileResponse;
import com.webapp.utility.AsposeCADUtility;
import com.webapp.utility.AsposeDocUtility;
import com.webapp.utility.AsposeExcelUtility;
import com.webapp.utility.AsposePDFUtility;

@Controller
public class UploadController {
	
	private final Logger logger = LoggerFactory.getLogger(UploadController.class);
	 @Autowired
	    private FileStorageService fileStorageService;
	 
	 @Autowired
	    private ServletContext servletContext;
		
	 private static final String UPLOAD_DIRECTORY = "C:\\Users\\MY PC\\Desktop\\Uploads\\";
//	 private static final String DOWNLOAD_DIRECTORY = "C:\\Users\\MY PC\\Desktop\\Downloads\\";
	/*
	 * @RequestMapping(value= {"/","/login"}) public ModelAndView login() {
	 * ModelAndView model = new ModelAndView(); System.out.println("inside login");
	 * model.setViewName("user/login"); return model;
	 * 
	 * }
	 */
	 
	@RequestMapping(value= {"/","/home"})
	public ModelAndView getHomePage(HttpServletRequest req)
	
	{
		logger.info("********  Inside getHomePage method of Upload Controller ********");
		ModelAndView model = new ModelAndView();
		HttpSession session = req.getSession();
		session.setAttribute("userName", "Administrator");
		model.addObject("userName", "Administrator");
//		System.out.println("inside getHomePage");
		logger.info("********  Rendering the home page view ********");
		model.setViewName( "home/home");
		return model;
	}
	@RequestMapping(value="/upload")
	public ModelAndView getUpload(@RequestParam("file") MultipartFile file,HttpServletRequest req)
	{
		logger.info("********  Inside the getUpload method of Upload Controller ********");
		ModelAndView model = new ModelAndView();
		String fileName = fileStorageService.storeFile(file);
		logger.info("********  The File uploaded is "+fileName+" ********");
		HttpSession session = req.getSession();
		
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();
        logger.info("********  fileDownloadURI - "+fileDownloadUri+" ********");
        session.setAttribute("fileName", fileName);
        UploadFileResponse res=new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
		String fileType = file.getContentType();
		model.addObject("fileType", fileType);
		model.addObject("fileName", fileName);
		model.addObject("userName",session.getAttribute("userName"));
		if(fileName.endsWith(".docx")||fileName.endsWith(".doc"))
		{
//			System.out.println("document uploaded");
			logger.info("********  .docx or .doc file is Uploaded  ********");
			model.setViewName("/result/resultDocument");
		}
		else if(fileName.endsWith(".xlsx")||fileName.endsWith(".xls"))
		{
//			System.out.println("excel uploaded");
			logger.info("********  .xlsx or .xls file is Uploaded  ********");
			model.setViewName("/result/resultExcel");
		}
		else if(fileName.endsWith(".pdf"))
		{
//			System.out.println("PDF uploaded");
			logger.info("********  .pdf file is Uploaded  ********");
			model.setViewName("/result/resultPDF");
			}
		else if(fileName.endsWith(".dxf")||fileName.endsWith(".dwg"))
		{
//			System.out.println("PDF uploaded");
			logger.info("********  CAD file is Uploaded  ********");
			model.setViewName("/result/resultCAD");
			}
			else
			{
				logger.info("********  file of unknown type is Uploaded  ********");
			model.setViewName("/result/error");
			}
		return model;
			
	}
	@RequestMapping("/perform")
	public ModelAndView performDocAction(HttpServletRequest req) throws Exception
	{
		logger.info("********  Inside method performDocAction  ********");
		HttpSession session = req.getSession();
		ModelAndView model = new ModelAndView();
		String action = req.getParameter("action");
		String option = req.getParameter("option");
//		String fileName=req.getParameter("fileName");
		logger.info("********  Action = "+action+"with option = "+option+"  ********");
		String fileName=(String) session.getAttribute("fileName");
//		System.out.println("Action = "+action+" Option = "+option+" FileName = "+fileName);
		logger.info("********  FileName = "+fileName+"  ********");
		String Data=null;
		String newFileName=null;
		if(action.equalsIgnoreCase("readHeader"))
		{
			
		// Data=DocumentUtility.sGetHeaderDataForDocX("C:\\\\Users\\\\MY PC\\\\Desktop\\\\Uploads\\"+fileName);
			logger.info("********  Calling Aspose Read Header Method  ********");
		 Data = AsposeDocUtility.readHeader(UPLOAD_DIRECTORY+fileName, option);
		 
		}
		else if(action.equalsIgnoreCase("readFooter"))
		{
			 //Data=DocumentUtility.sGetFooterDataForDocX("C:\\\\Users\\\\MY PC\\\\Desktop\\\\Uploads\\"+fileName);
			logger.info("********  Calling Aspose Read Footer Method  ********"); 
			Data =AsposeDocUtility.readFooter(UPLOAD_DIRECTORY+fileName, option);
			 
		}
		else if(action.equalsIgnoreCase("insertWatermark"))
		{
			logger.info("********  Calling Aspose Insert Watermark Method  ********");
			AsposeDocUtility.insertWatermarkText(UPLOAD_DIRECTORY+fileName, option);
			Data="File"+fileName+"is updated with watermark"+option+"\n"+"Please Download the file here..."+"\n";
		}
		/*
		 * else if(action.equalsIgnoreCase("insertTextHeader")) {
		 * AsposeDocUtility.replaceTextInHeader("C:\\Users\\MY PC\\Desktop\\Uploads\\"
		 * +fileName, toReplace, replaceText, headerType); }
		 */
		/*
		 * else if(action.equalsIgnoreCase("insertImageHeader")) { logger.
		 * info("********  Calling Aspose Insert Image into Header Method  ********");
		 * String imageFileName = fileStorageService.storeFile(file);
		 * AsposeDocUtility.insertImageIntoHeader(UPLOAD_DIRECTORY+fileName,
		 * imageFileName); Data="Image Inserted into the Document Header"
		 * +"\n"+"Please Download the file here..."+"\n"; }
		 */
		else if(action.equalsIgnoreCase("removeWatermark"))
		{
			logger.info("********  Calling Aspose remove Watermark Method  ********");
			AsposeDocUtility.removeWaterMark(UPLOAD_DIRECTORY+fileName);
			Data="Watermark is removed from the document"+"\n"+"Please Download the file here..."+"\n";
		}
		else if(action.equalsIgnoreCase("acceptRevisions"))
		{
			AsposeDocUtility.acceptRevisions(UPLOAD_DIRECTORY+fileName);
			Data="Revisions are accepted for the Document"+"\n"+"Please Download the file here..."+"\n";
		}
		else if(action.equalsIgnoreCase("saveAsDoc"))
		{
			newFileName=AsposeDocUtility.saveAsDoc(UPLOAD_DIRECTORY+fileName);
			Data="Document is saved as .doc format"+"\n"+"Please Download the file here..."+"\n";
			session.setAttribute("fileName", newFileName);
		}
		else if(action.equalsIgnoreCase("saveAsPdf"))
		{
			 newFileName=AsposeDocUtility.docToPDF(UPLOAD_DIRECTORY+fileName);
			Data="Document is saved as .pdf format"+"\n"+"Please Download the file here..."+"\n";
			session.setAttribute("fileName", newFileName);
		}
		model.addObject("message", Data);
		model.addObject("fileName",fileName);
		model.addObject("userName",session.getAttribute("userName"));
//		System.out.println("action ="+action);
//		System.out.println("message"+Data);
		logger.info("********  Message to be Printed : "+Data+"  ********");
		if(action.equalsIgnoreCase("readHeader")||action.equalsIgnoreCase("readFooter"))
		model.setViewName("/result/output");
		else
			model.setViewName("/result/outputWM");
		logger.info("********  Rendering the output view  ********");
		return model;
			
	}
	@RequestMapping("/performInsertImage")
	public ModelAndView performInsertImage(@RequestParam("file") MultipartFile file,HttpServletRequest req) throws Exception
	{
		logger.info("********  Inside performInsertImage method of Uplaod Controller  ********");
		HttpSession session = req.getSession();
		ModelAndView model = new ModelAndView();
		String fileName=(String) session.getAttribute("fileName");
		logger.info("********  FileName = "+fileName+"  ********");
		String imageFileName = fileStorageService.storeFile(file);
		logger.info("********  Image File Name = "+imageFileName+"  ********");
		logger.info("********  Calling Aspose insertImageIntoHeader Method  ********");
		AsposeDocUtility.insertImageIntoHeader(UPLOAD_DIRECTORY+fileName, UPLOAD_DIRECTORY+imageFileName);
		String Data="Image Inserted into the Document Header"+"\n"+"Please Download the file here..."+"\n";
		model.addObject("message", Data);
		model.addObject("fileName",fileName);
		model.addObject("userName",session.getAttribute("userName"));
		logger.info("********  Rendering the output view  ********");
		return model;
	}
	@RequestMapping("/performWM")
	public ModelAndView performDocWatermark(HttpServletRequest req) throws Exception {
		logger.info("********  Inside performDocWatermark method of Uplaod Controller  ********");
		HttpSession session = req.getSession();
		ModelAndView model = new ModelAndView();
		String action = req.getParameter("action");
		String option = req.getParameter("option");
		logger.info("********  Action = "+action+"option = "+option+"  ********");
//		String fileName=req.getParameter("fileName");
		String fileName=(String) session.getAttribute("fileName");
//		System.out.println("Action = "+action+" Option = "+option+" FileName = "+fileName);
		logger.info("********  FileName = "+fileName+"  ********");
		String Data=null;
		if(action.equalsIgnoreCase("insertWatermark"))
		{
			logger.info("********  Calling Aspose insertWatermarkText method  ********");
			AsposeDocUtility.insertWatermarkText(UPLOAD_DIRECTORY+fileName, option);
			Data="File"+fileName+"is updated with watermark"+option+"\n"+"Please Download the file here..."+"\n";
		}
		model.addObject("message", Data);
		model.addObject("fileName",fileName);
		model.addObject("userName",session.getAttribute("userName"));
		model.setViewName("/result/outputWM");
		logger.info("********  Rendering the output view  ********");
		return model;
	}
	@RequestMapping("/performReplaceText")
	public ModelAndView performDocReplaceText(HttpServletRequest req) throws Exception
	{
		logger.info("********  Inside performDocReplaceText method of Uplaod Controller  ********");
		HttpSession session = req.getSession();
		ModelAndView model = new ModelAndView();
		String toBeReplaced =  req.getParameter("toBeReplaced");
		String newText = req.getParameter("newText");
		String typeHeader = req.getParameter("typeHeader");
		logger.info("********  Text to be replaced = "+toBeReplaced+"New text = "+newText+"type of Header="+typeHeader+"  ********");
		String fileName=(String) session.getAttribute("fileName");
		logger.info("********  FileName = "+fileName+"  ********");
		logger.info("********  Calling Aspose replaceTextInHeader method  ********");
		AsposeDocUtility.replaceTextInHeader(UPLOAD_DIRECTORY+fileName, toBeReplaced, newText, typeHeader);
		String Data="File"+fileName+"is updated with text"+toBeReplaced+"\n"+"Please Download the file here..."+"\n";
		model.addObject("message", Data);
		model.addObject("fileName",fileName);
		model.addObject("userName",session.getAttribute("userName"));
		model.setViewName("/result/outputWM");
		logger.info("********  Rendering the output view  ********");
		return model;
	}
	@RequestMapping("/performExcel")
	public ModelAndView performExcel(HttpServletRequest req) throws Exception
	{
		logger.info("********  Inside performExcel method of Uplaod Controller  ********");
		HttpSession session = req.getSession();
		ModelAndView model = new ModelAndView();
		String action = req.getParameter("action");
		String fileName=(String) session.getAttribute("fileName");
		logger.info("********  FileName = "+fileName+"  ********");
		String Data=null;
		if(action.equalsIgnoreCase("readHeader"))
		{
			logger.info("********  Calling AsposeExcelUtility readHeader method  ********");
			Data=AsposeExcelUtility.readHeader(UPLOAD_DIRECTORY+fileName);
		}
		else if(action.equalsIgnoreCase("readFooter"))
		{
			logger.info("********  Calling AsposeExcelUtility readFooter method  ********");
			Data=AsposeExcelUtility.readFooter(UPLOAD_DIRECTORY+fileName);
		}
		else if(action.equalsIgnoreCase("removeHeaderFooter"))
		{
			logger.info("********  Calling AsposeExcelUtility removeHeaderFooter method  ********");
			AsposeExcelUtility.removeHeaderFooter(UPLOAD_DIRECTORY+fileName);
		}
		model.addObject("message", Data);
		model.addObject("fileName",fileName);
		model.addObject("userName",session.getAttribute("userName"));
		model.setViewName("/result/output");
		logger.info("********  Rendering the output view  ********");
		return model;
		
	}
	@RequestMapping("/performCAD")
	public ModelAndView performCAD(HttpServletRequest req) throws Exception
	{
		logger.info("********  Inside performCAD method of Uplaod Controller  ********");
		HttpSession session = req.getSession();
		ModelAndView model = new ModelAndView();
		String action = req.getParameter("action");
		String fileName=(String) session.getAttribute("fileName");
		String Data=null,newFileName="";
		logger.info("********  FileName = "+fileName+"  ********");
		if(action.equalsIgnoreCase("convertToPNG"))
		{
			newFileName=AsposeCADUtility.ConvertCADDrawingToRasterPNGFormat(UPLOAD_DIRECTORY + fileName);
			Data="CAD File is converted to PNG. Please download here...!";
			session.setAttribute("fileName", newFileName);
		}
		else if(action.equalsIgnoreCase("convertToJPEG")) {
			newFileName=AsposeCADUtility.ConvertCADDrawingToRasterJPGFormat(UPLOAD_DIRECTORY + fileName);
			Data="CAD File is converted to JPEG. Please download here...!";
			session.setAttribute("fileName", newFileName);
		}
		else if(action.equalsIgnoreCase("convertToPDF"))
		{
			newFileName=AsposeCADUtility.ConvertCADDrawingToRasterPDF(UPLOAD_DIRECTORY + fileName);
			Data="CAD File is converted to PDF. Please download here...!";
			session.setAttribute("fileName", newFileName);
		}
		else if(action.equalsIgnoreCase("convertAllLayersToImages"))
		{
			AsposeCADUtility.ConvertAllLayersToImages(UPLOAD_DIRECTORY + fileName);
			Data="All the layers are converted to images";
		}
		else if(action.equalsIgnoreCase("searchText"))
		{
			AsposeCADUtility.searchTextInDWGAutoCADFile(UPLOAD_DIRECTORY + fileName);
		}
		model.addObject("message", Data);
		model.addObject("fileName",fileName);
		model.addObject("userName",session.getAttribute("userName"));
		model.setViewName("/result/outputWM");
		logger.info("********  Rendering the output view  ********");
		return model;
	}
	@RequestMapping("/performPDF")
	public ModelAndView performPDF(HttpServletRequest req) throws Exception
	{
		logger.info("********  Inside performCAD method of Uplaod Controller  ********");
		HttpSession session = req.getSession();
		ModelAndView model = new ModelAndView();
		String action = req.getParameter("action");
		String fileName=(String) session.getAttribute("fileName");
		String Data=null;
		logger.info("********  FileName = "+fileName+"  ********");
		if(action.equalsIgnoreCase("readHeader"))
		{
			Data=AsposePDFUtility.readHeaderPDF(UPLOAD_DIRECTORY + fileName);
			
		}
		else if(action.equalsIgnoreCase("readFooter")) {
			Data=AsposePDFUtility.readFooterPDF(UPLOAD_DIRECTORY + fileName);
			
		}
		
		model.addObject("message", Data);
		model.addObject("fileName",fileName);
		model.addObject("userName",session.getAttribute("userName"));
		model.setViewName("/result/output");
		logger.info("********  Rendering the output view  ********");
		return model;
	}
	@RequestMapping("/download")
	 public ResponseEntity<ByteArrayResource> downloadFile(HttpServletRequest req) throws IOException {
			logger.info("********  Inside downloadFile method of Upload Controller  ********");
			HttpSession session = req.getSession();
			String fileName=(String) session.getAttribute("fileName");
			logger.info("********  FileName = "+fileName+"  ********");
	        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
//	        System.out.println("fileName: " + fileName);
//	        System.out.println("mediaType: " + mediaType);
	        logger.info("********  MediaType = "+mediaType+"  ********");
	        Path path;
	        if(fileName.contains("C:")) {
	        path = Paths.get( fileName);
	        
	        }
	        else
	        {
	        	path = Paths.get(UPLOAD_DIRECTORY +fileName);
	        }
	        
	        byte[] data = Files.readAllBytes(path);
	        ByteArrayResource resource = new ByteArrayResource(data);
	        logger.info("********  File Download starts...  ********");
	        return ResponseEntity.ok()
	                // Content-Disposition
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
	                // Content-Type
	                .contentType(mediaType) //
	                // Content-Lengh
	                .contentLength(data.length) //
	                .body(resource);
	    }
}
