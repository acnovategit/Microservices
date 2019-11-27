
//var option = [[${messageJSON}]]; 
	var option = document.getElementById("messageText").value;
function showDowloadButton()
{
	alert(option);
	if(option.includes("watermark"))
		{
		document.getElementById("showOnWatermark").style.visibility="inherit";
		}
	}