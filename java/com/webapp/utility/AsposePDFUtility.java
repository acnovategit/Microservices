package com.webapp.utility;


import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspose.pdf.Artifact;
import com.aspose.pdf.ArtifactCollection;
import com.aspose.pdf.Document;

public class AsposePDFUtility {

	private final static Logger logger = LoggerFactory.getLogger(AsposePDFUtility.class);
	public static String readHeaderPDF(String filePath) {
		logger.info("********  Inside readHeaderPDF method of AsposePDFUtility ********");
		Document doc = new Document(filePath);

		// get artifacts collection of frist page

		ArtifactCollection artifact = doc.getPages().get_Item(1).getArtifacts();

		Iterator fi = artifact.iterator();
		String headerText="";
		while(fi.hasNext())

		{

		if (((Artifact)fi.next()).getSubtype() == Artifact.ArtifactSubtype.Header) 

		// print text of artifact

		headerText+=((Artifact)fi.next()).getText();
		logger.info("********  header text - "+headerText+" ********");

		}
		logger.info("******** END of readHeaderPDF method of AsposePDFUtility ********");
		return headerText;
	}
	public static String readFooterPDF(String filePath) {
		Document doc = new Document(filePath);

		// get artifacts collection of frist page

		ArtifactCollection artifact = doc.getPages().get_Item(1).getArtifacts();

		Iterator fi = artifact.iterator();
		String footerText="";
		while(fi.hasNext())

		{

		if (((Artifact)fi.next()).getSubtype() == Artifact.ArtifactSubtype.Footer) 

		// print text of artifact

			footerText+=((Artifact)fi.next()).getText();

		}
		return footerText;
	}
}
