function openNav() {
	document.getElementById("mySidenav").style.width = "30%";
}

function closeNav() {
	document.getElementById("mySidenav").style.width = "0";
}

var markers = [];
function initMap() {
	var styledMapType = new google.maps.StyledMapType([ {
		"featureType" : "poi",
		"stylers" : [ {
			"color" : "#ff3b88"
		}, {
			"visibility" : "off"
		} ]
	}, {
		"featureType" : "poi.attraction",
		"stylers" : [ {
			"visibility" : "off"
		} ]
	}, {
		"featureType" : "poi.government",
		"stylers" : [ {
			"visibility" : "off"
		} ]
	}, {
		"featureType" : "poi.medical",
		"stylers" : [ {
			"visibility" : "off"
		} ]
	}, {
		"featureType" : "poi.school",
		"stylers" : [ {
			"visibility" : "off"
		} ]
	}, {
		"featureType" : "poi.sports_complex",
		"stylers" : [ {
			"visibility" : "off"
		} ]
	} ], {
		name : 'Styled Map'
	});
	var uluru = {
		lat : -25.363,
		lng : 131.044
	};
	var map = new google.maps.Map(document.getElementById('map'), {
		zoom : 12,
		center : {
			lat : 16.054856,
			lng : 108.200403
		},
		mapTypeControlOptions : {
			mapTypeIds : [ 'roadmap', 'terrain', 'styled_map' ]
		}
	});
	map.mapTypes.set('styled_map', styledMapType);
	map.setMapTypeId('styled_map');

	$(".rowClear").click(function() {
		var routeId = $(this).find(".routeId").text();
		alert(routeId);
	});
}
