var refreshTime = 1000;

//Refresh all elements
window.setInterval("reload();", refreshTime);

//Reload each element
function reload() {
	reloadElement("element/console");
}

//Reload the specified element
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
