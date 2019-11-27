var modal1 = document.getElementById("readHeaderForm");
var modal2 = document.getElementById("readFooterForm");
var modal3 = document.getElementById("insertWatermarkForm");
var modal4 = document.getElementById("removeWatermarkForm");
var modal5 = document.getElementById("replaceTextinHeaderForm");
var modal6 = document.getElementById("insertImageinHeaderForm");
var modal7 = document.getElementById("acceptRevisionsForm");
var modal8 = document.getElementById("saveAsDOCForm");
var modal9 = document.getElementById("saveAsPDFForm");
// Get the button that opens the modal
var btn1 = document.getElementById("readHeaderCard");
var btn2 = document.getElementById("readFooterCard");
var btn3 = document.getElementById("insertWatermarkCard");
var btn4 = document.getElementById("removeWatermarkCard");
var btn5 = document.getElementById("insertTextHeaderCard");
var btn6 = document.getElementById("insertImageHeaderCard");
var btn7 = document.getElementById("acceptRevisionsCard");
var btn8 = document.getElementById("saveAsDOCCard");
var btn9 = document.getElementById("saveAsPDFCard");
// Get the <span> element that closes the modal
var span1 = document.getElementsByClassName("closeHeader")[0];
var span2 = document.getElementsByClassName("closeFooter")[0];
var span3 = document.getElementsByClassName("closewaterMark")[0];
var span4 = document.getElementsByClassName("closeRMWatermark")[0];
var span5 = document.getElementsByClassName("closeReplaceText")[0];
var span6 = document.getElementsByClassName("closeInsertImage")[0];
var span7 = document.getElementsByClassName("closeAcceptRev")[0];
var span8 = document.getElementsByClassName("closeSaveAsDOC")[0];
var span9 = document.getElementsByClassName("closeSaveAsPDF")[0];
// When the user clicks on the button, open the modal
btn1.onclick = function() {
  modal1.style.display = "block";
}
btn2.onclick = function() {
	  modal2.style.display = "block";
	}
btn3.onclick = function() {
	  modal3.style.display = "block";
	}
btn4.onclick = function() {
	  modal4.style.display = "block";
	}
btn5.onclick = function() {
	  modal5.style.display = "block";
	}
btn6.onclick = function() {
	  modal6.style.display = "block";
	}
btn7.onclick = function() {
	  modal7.style.display = "block";
	}
btn8.onclick = function() {
	  modal8.style.display = "block";
	}
btn9.onclick = function() {
	  modal9.style.display = "block";
	}
/*function myFunction() {
	  var x = document.getElementById("readHeaderForm");
	  if (x.style.visibility === "hidden") {
	    x.style.visibility = "visible	";
	  } else {
	    x.style.visibility = "hidden";
	  }
	}*/
function setHeaderUrl()
{
	var option = document.getElementById("typeHeader").value;
	
	window.location.href="perform?action=readHeader&option="+option;
	}
function setFooterUrl()
{
	var option = document.getElementById("typeFooter").value;
	
	window.location.href="perform?action=readFooter&option="+option;
	}
function setWMUrl()
{
	var option =document.getElementById("watermarkText").value;
	window.location.href="performWM?action=insertWatermark&option="+option;
	}
function setReplaceTextUrl()
{
	var toBeReplaced = document.getElementById("textToBeReplaced").value;
	var newText = document.getElementById("newText").value;
	var headerType = document.getElementById("typeHeader").value;
	
	window.location.href="performReplaceText?toBeReplaced="+toBeReplaced+"&newText="+newText+"&typeHeader="+headerType;
}
function setInsertImageUrl()
{
	var imagePath = document.getElementById("imageFile").value;
	
	//var newPath=imagePath.replace(///g,"-");
			alert(imagePath);
	window.location.href="perform?action=insertImageHeader&option="+imagePath;
}
function setAcceptRevUrl()
{
	window.location.href="perform?action=acceptRevisions&option=1";
}
function setSaveAsDOCUrl()
{
	window.location.href="perform?action=saveAsDoc&option=1";
}
function setSaveAsPDFUrl()
{
	window.location.href="perform?action=saveAsPdf&option=1";
}
function setRMUrl()
{
	//var option =document.getElementById("watermarkText").value;
	window.location.href="perform?action=removeWatermark";
	}
// When the user clicks on <span> (x), close the modal
span1.onclick = function() {
  modal1.style.display = "none";
}
span2.onclick = function() {
	  modal2.style.display = "none";
	}
span3.onclick = function() {
	  modal3.style.display = "none";
	}
span4.onclick = function() {
	  modal4.style.display = "none";
	}
span5.onclick = function() {
	  modal5.style.display = "none";
	}

span6.onclick = function() {
	  modal6.style.display = "none";
	}

span7.onclick = function() {
	  modal7.style.display = "none";
	}

span8.onclick = function() {
	  modal8.style.display = "none";
	}

span9.onclick = function() {
	  modal9.style.display = "none";
	}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target == modal1) {
    modal1.style.display = "none";
  }
}
window.onclick = function(event) {
	  if (event.target == modal2) {
	    modal2.style.display = "none";
	  }
	}