package com.webapp.utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.HeaderStories;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class DocumentUtility {
	static boolean bUpdated = false;
	/**
	 * This method returns the content as a string in the header of .docx files
	 *@param filePath The path of file for which the header is to be read
	 *@return sHeaderData which is a string of data containing the entire header text
	 *@throws FileNotFoundException
	 *@throws InvalidFormatException
	 *@throws IOException
	 */
		
	public static String sGetHeaderDataForDocX(String filePath) throws FileNotFoundException,InvalidFormatException,IOException {
		
		String sHeaderData = null;

		FileInputStream fis = new FileInputStream(filePath);
		XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
		XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(xdoc);
		XWPFHeader header = policy.getDefaultHeader();
		sHeaderData = header.getText().toString();
		xdoc.close();
		return sHeaderData;
	}
	/**
	 * @param filePath
	 * @return sFooterData which is a string of data containing the entire footer text
	 * @throws FileNotFoundException
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static String sGetFooterDataForDocX(String filePath) throws FileNotFoundException,InvalidFormatException,IOException {
			
			String sFooterData = null;

			FileInputStream fis = new FileInputStream(filePath);
			XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
			XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(xdoc);
			XWPFFooter header = policy.getDefaultFooter();
			sFooterData = header.getText().toString();
			return sFooterData;
		}

		/**
		 * This method replaces a string in the header of .docx file with the given string
		 * @param filePath
		 * @param token
		 * @param textToReplace
		 *@throws FileNotFoundException
		 *@throws InvalidFormatException
		 *@throws IOException
		 */
		public static XWPFDocument setHeader(String filePath, String token,String textToReplace) throws FileNotFoundException,InvalidFormatException,IOException {
			
			FileInputStream fis = new FileInputStream(filePath);
			XWPFDocument document= new XWPFDocument(OPCPackage.open(fis));
			XWPFHeaderFooterPolicy policy = document.getHeaderFooterPolicy();
			XWPFHeader header = policy.getDefaultHeader();
			bUpdated = replaceInParagraphs(header.getParagraphs(), token,
					textToReplace);
			if (bUpdated) {
			} else {
				bUpdated = replaceInParagraphsMod(header.getParagraphs(), token,
						textToReplace);
				if (bUpdated) {
				}
			}

			
			return document;
		}
		/**
		 * @param paragraphs
		 * @param placeHolder
		 * @param replaceText
		 * @return
		 */
		private static boolean replaceInParagraphs(List<XWPFParagraph> paragraphs,
				String placeHolder, String replaceText) {
			boolean bFlag = false;
			Iterator<XWPFParagraph> var5 = paragraphs.iterator();

			while (var5.hasNext()) {
				XWPFParagraph xwpfParagraph = (XWPFParagraph) var5.next();
				List<XWPFRun> runs = xwpfParagraph.getRuns();
				Iterator<XWPFRun> var8 = runs.iterator();

				while (var8.hasNext()) {
					XWPFRun run = (XWPFRun) var8.next();

					String runText;
					try {
						runText = run.getText(run.getTextPosition());
					} catch (Exception var11) {
						continue;
					}

					
					if (placeHolder != ""
							&& !placeHolder.isEmpty()
							&& placeHolder != null
							&& runText != null
							&& Pattern.compile(placeHolder, 2).matcher(runText)
									.find()) {
						if (runText.contains("Effectiv")) {
							runText = "Effective Date: " + replaceText;
						} else {
							runText = replaceText;
						}

						bFlag = true;
					}

					run.setText(runText, 0);
				}
			}
			return bFlag;
		}

		/**
		 * @param paragraphs
		 * @param placeHolder
		 * @param replaceText
		 * @return
		 */
		private static boolean replaceInParagraphsMod(
				List<XWPFParagraph> paragraphs, String placeHolder,
				String replaceText) {
			boolean bFlag = false;
			Iterator var5 = paragraphs.iterator();

			while (true) {
				XWPFParagraph xwpfParagraph;
				do {
					if (!var5.hasNext()) {
						return bFlag;
					}

					xwpfParagraph = (XWPFParagraph) var5.next();
				} while (!xwpfParagraph.getText().contains(placeHolder));

				String paratext = xwpfParagraph.getText();
				String replacedPara = paratext.replace(placeHolder, replaceText);
				int size = xwpfParagraph.getRuns().size();

				for (int i = 0; i < size; ++i) {
					xwpfParagraph.removeRun(0);
				}

				String[] replacementTextSplitOnCarriageReturn = replacedPara
						.split("\n");

				for (int j = 0; j < replacementTextSplitOnCarriageReturn.length; ++j) {
					String part = replacementTextSplitOnCarriageReturn[j];
					XWPFRun newRun = xwpfParagraph.insertNewRun(j);
					newRun.setText(part);
					if (j + 1 < replacementTextSplitOnCarriageReturn.length) {
						newRun.addCarriageReturn();
					}
				}

				bFlag = true;
			}
			
		}
		
		
		
		/**
		 * Method to read the header data of .doc file
		 * @param filePath The path of file for which the header is to be read
		 * @return sHeader which is a string of data containing the entire header text
		 * @throws IOException
		 */
		public static String sGetHeaderDataForDoc(String filePath, int pageNumber) throws IOException
		{
			POIFSFileSystem pfs = new POIFSFileSystem(new FileInputStream("C:\\Users\\MY PC\\Desktop\\TestDocs\\TestFile.doc"));
			HWPFDocument document = new HWPFDocument(pfs);

			
			HeaderStories headerStore = new HeaderStories(document);
	        String sHeader = headerStore.getHeader(pageNumber); 
	        return sHeader;
		}
		/**
		 * @param filePath
		 * @param pageNumber
		 * @return sFooter which is a string of data containing the entire footer text
		 * @throws IOException
		 */
		public static String sGetFooterDataForDoc(String filePath, int pageNumber) throws IOException
		{
			POIFSFileSystem pfs = new POIFSFileSystem(new FileInputStream("C:\\Users\\MY PC\\Desktop\\TestDocs\\TestFile.doc"));
			HWPFDocument document = new HWPFDocument(pfs);

			
			HeaderStories headerStore = new HeaderStories(document);
	        String sFooter = headerStore.getFooter(pageNumber); 
	        return sFooter;
		}
		/**
		 * Method to replace Text in the header of a .doc file with a given Text
		 * @param filePath
		 * @throws IOException
		 */
		public static void setHeaderforDoc(String filePath,String textToBeReplaced,String replaceText) throws IOException
		{
			POIFSFileSystem pfs = new POIFSFileSystem(new FileInputStream(filePath));
			HWPFDocument document = new HWPFDocument(pfs);
			
			
			HeaderStories headerStore = new HeaderStories(document);
			Range r1 = headerStore.getRange();
	        r1.replaceText(textToBeReplaced, replaceText);

			
			FileOutputStream fos = new FileOutputStream(filePath);
		    document.write(fos);
		    fos.close();
		}
		/**
		 * @param filePath
		 * @return docData list of all the paragraphs of text read from the document.
		 * @throws IOException
		 */
		public static List<String> readTextDataDocx(String filePath) throws IOException{
		    List<String> docData = new ArrayList<String>();
		    try {
		       
		        FileInputStream fis = new FileInputStream(filePath);
				XWPFDocument document = new XWPFDocument(fis);

		        List<XWPFParagraph> paragraphs = document.getParagraphs();
		       
		        for (XWPFParagraph para : paragraphs) {
		           docData.add(para.getText());
		        }
		       
		        
		        fis.close();
		        document.close();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return docData;
		}
		public static List<String> readTextDataDoc(String filePath) throws IOException
		{
			FileInputStream fis=new FileInputStream(filePath);
			List<String> docData = new ArrayList<String>();
			HWPFDocument document=new HWPFDocument(fis);
			WordExtractor extractor = new WordExtractor(document);
			String [] fileData = extractor.getParagraphText();
			for (String currentPara : fileData) 
			{
				docData.add(currentPara);
			}
			extractor.close();
			document.close();
			return docData;
		}
		/**
		 * @param filePath
		 * @return allTabledata a map with key as TABLE#1 and list contains all the data of all the rows
		 * @throws InvalidFormatException
		 * @throws IOException
		 */
		public static Map<String,List<String>> getTableDataFromDocx(String filePath) throws InvalidFormatException, IOException
		{
			FileInputStream fis = new FileInputStream(filePath);
				XWPFDocument doc = new XWPFDocument(OPCPackage.open(fis));
				List<XWPFTable> table = doc.getTables();        
	            String key="TABLE#"; int counter=1;
	            
	            
	            Map<String,List<String>> allTabledata = new HashMap<String,List<String>>();
	            for (XWPFTable xwpfTable : table) {
	            	List<String> tableData = new ArrayList<String>();
						List<XWPFTableRow> row = xwpfTable.getRows();
						key+=counter;
						for (XWPFTableRow xwpfTableRow : row) {
							String rowData="";
							List<XWPFTableCell> cell = xwpfTableRow.getTableCells();
							for (XWPFTableCell xwpfTableCell : cell) {
								if(xwpfTableCell!=null)
								{
									//System.out.println(xwpfTableCell.getText());
									rowData+=xwpfTableCell.getText();
									List<XWPFTable> itable = xwpfTableCell.getTables();
									if(itable.size()!=0)
									{
										for (XWPFTable xwpfiTable : itable) {
											List<XWPFTableRow> irow = xwpfiTable.getRows();
											for (XWPFTableRow xwpfiTableRow : irow) {
												
												List<XWPFTableCell> icell = xwpfiTableRow.getTableCells();
												for (XWPFTableCell xwpfiTableCell : icell) {
													if(xwpfiTableCell!=null)
													{  
														//System.out.println(xwpfiTableCell.getText());
														rowData+=xwpfiTableCell.getText();
													}
												}
											}
										}
									}
								}
							}
							tableData.add(rowData);
						}
						allTabledata.put(key, tableData);
						counter++;
			} 
	            doc.close();
	            return allTabledata;
		}
	 
}
