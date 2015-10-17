var refreshTime = 1000;

//Refresh all elements
window.setInterval("reload();", refreshTime);

//Reload each element
function reload() {
	reloadConsole();
}

function reloadConsole() {
	var messages = "";
	$.getJSON("element/console", null, function(data, status, xhr) {
		if (status === "success") {
			for (var message in data) {
				//TODO
			}
		}
	})
}

//Reload the specified element, uses vanilla js
function reloadElement(element) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			document.getElementById(element).innerHTML = xhttp.responseText;
		}
	}
	xhttp.open("GET", element, true);
	xhttp.send();
}
