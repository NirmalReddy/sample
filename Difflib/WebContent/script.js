function ajaxFunc() {
	var input1 = document.getElementById("file1.div").value;
	var splitInput1 = input1.split("\n");
	var jsonInput1 = JSON.stringify(splitInput1);

	var input2 = document.getElementById("file2.div").value;
	var splitInput2 = input2.split("\n");
	var jsonInput2 = JSON.stringify(splitInput2);

	createXMLHTTP(jsonInput1, jsonInput2);
}

function createXMLHTTP(jsonInput1, jsonInput2) {
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4) {
			document.getElementById("output").innerHTML = xmlhttp.responseText;
		}
	};
	xmlhttp.open("POST", "http://localhost:7501/embed/DiffJetty", true);
	xmlhttp.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	xmlhttp.send("input1=" + jsonInput1 + "&input2=" + jsonInput2);
}

function blocking(nr) {
	displayNew = (document.getElementById(nr).style.display == 'none') ? 'block'
			: 'none';
	document.getElementById(nr).style.display = displayNew;
}

function changeText(id) {
	var symbol = document.getElementById(id).innerHTML;
	document.getElementById(id).innerHTML = (symbol == '+') ? '-' : '+';
}

function readSingleFile1(evt) {
	var f = evt.target.files[0];
	var ext = f.name.substring(f.name.lastIndexOf('.') + 1);
	if (!f) {
		alert("Failed to load file");
	} else if (ext != "java" && ext!="txt") {
		alert(f.name + " is not a valid java file.");
	} else {
		var r = new FileReader();
		r.onload = function(e) {
			var contents = e.target.result;
			document.getElementById("file1.div").innerHTML = contents;
		};
		r.readAsText(f);
	}
}

function readSingleFile2(evt) {
	var f = evt.target.files[0];
	var ext = f.name.substring(f.name.lastIndexOf('.') + 1);
	if (!f) {
		alert("Failed to load file");
	} else if (ext != "java" && ext!="txt") {
		alert(f.name + " is not a valid java file.");
	} else {
		var r = new FileReader();
		r.onload = function(e) {
			var contents = e.target.result;
			document.getElementById("file2.div").innerHTML = contents;
		};
		r.readAsText(f);
	}
}
