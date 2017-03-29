var markers = [];
var url = window.location.href;

function openNav() {
    document.getElementById("mySidenav").style.width = "30%";
}

function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
}

function initMap() {
	let directionsService = new google.maps.DirectionsService;
    let directionsDisplay = new google.maps.DirectionsRenderer;
    
    var styledMapType = new google.maps.StyledMapType(
    [
            {
                "featureType": "poi"
                , "stylers": [
                    {
                        "color": "#ff3b88"
      }
                      , {
                        "visibility": "off"
      }
    ]
  }
              , {
                "featureType": "poi.attraction"
                , "stylers": [
                    {
                        "visibility": "off"
      }
    ]
  }
              , {
                "featureType": "poi.government"
                , "stylers": [
                    {
                        "visibility": "off"
      }
    ]
  }
              , {
                "featureType": "poi.medical"
                , "stylers": [
                    {
                        "visibility": "off"
      }
    ]
  }
              , {
                "featureType": "poi.school"
                , "stylers": [
                    {
                        "visibility": "off"
      }
    ]
  }
              , {
                "featureType": "poi.sports_complex"
                , "stylers": [
                    {
                        "visibility": "off"
      }
    ]
  }
], {
            name: 'Styled Map'
        });
    
    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 12
        , center: {
            lat: 16.054856
            , lng: 108.200403
        }
        , mapTypeControlOptions: {
            mapTypeIds: ['roadmap', 'terrain'
                    , 'styled_map']
        }
    });
    map.mapTypes.set('styled_map', styledMapType);
    map.setMapTypeId('styled_map');
    
    directionsDisplay.setMap(map);
    directionsDisplay.setOptions( { suppressMarkers: true } );
    
    let currentRoute;
    
    /* init event for app */
    $("body").on("click", ".btn-back", function(event) {
    	
    	getBack(url);
    });
    $("body").on("click", ".rowClear", function(event) {
    		clearMarkers();
    	  let routeId = $(this).find(".routeId").text();
    	  ajaxGetContent(url,routeId,"go");
    	  routeId="route"+routeId;
    	  currentRoute=routeId;
    	  callAjax(url, routeId, "go", directionsService, directionsDisplay,map);
    });
    $("body").on("click","#btnBack",function(event){
        	clearMarkers();
       	 	markers = [];
            callAjax(url, currentRoute, "back", directionsService, directionsDisplay,map);
    });
    $("body").on("click","#btnGo",function(event){
    	clearMarkers();
   	 	markers = [];
        callAjax(url, currentRoute, "go", directionsService, directionsDisplay,map);
});
    
}

function ajaxGetContent(url, routeId ,trend) {
	var getUrl = url + "/route/"+routeId;
    $.ajax({
        type: "GET"
        , url: getUrl
        ,data:{
            trend:trend
        }
        , success: function (data) {
            var html = jQuery('<body>').html(data);
            var content = html.find("#content").html();
            $("#content").html(content);
        }
    });
}
function getBack(url){
    $.ajax({
        type: "GET"
        , url: url 
        , success: function (data) {
            var html = jQuery('<body>').html(data);
            var content = html.find("#content").html();
            $("#content").html(content);
        }
    });
}
function setMapOnAll(map) {
    for (var i = 0; i < markers.length; i++) {
      markers[i].setMap(map);
    }
  }
function clearMarkers() {
    setMapOnAll(null);
 }
function parseLat(str) {
    let temp = [];
    temp = str.split(',');
    let number = parseFloat(temp[0]);
    return number;
}
function parseLng(str) {
    let temp = [];
    temp = str.split(',');
    let number = parseFloat(temp[1]);
    return number;
}
function calculateAndDisplayRoute1(directionsService, directionsDisplay, jsonResponse,map) {
    // load waypoint from server
    var totalDataLength = jsonResponse.length;
    var waypts = [];
    var totalDataLength = jsonResponse.length;
    var latLng = jsonResponse[0].latLng;
    for (let i = 0; i < jsonResponse.length; i++) {
    	let lat = parseLat(jsonResponse[i].latLng);
        let lng = parseLng(jsonResponse[i].latLng);
    	if(i==0){
    		markers.push(marker = new google.maps.Marker({
				position: new google.maps.LatLng(lat,lng),
				icon:"https://chart.googleapis.com/chart?chst=d_map_xpin_icon&chld=pin_star|car-dealer|ADDE63|FF0000",
				map: map
		    }));
    	}
    	else if (i > 0 && i < jsonResponse.length - 1) {
            markers.push(marker = new google.maps.Marker({
				position: new google.maps.LatLng(lat,lng),
				icon:"https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=" + (i+1) + "|FF0000|000000",
				map: map
		    }));
            waypts.push({
                location: new google.maps.LatLng(lat, lng)
                , stopover: true
            });
        }
    	else{
    		markers.push(marker = new google.maps.Marker({
				position: new google.maps.LatLng(lat,lng),
				icon:"https://chart.googleapis.com/chart?chst=d_map_pin_icon&chld=flag|ADDE63",
				map: map
		    }));
    	}
    }
    console.log(waypts[0].location);
    // cusomize waypoints
// icon:"https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=" + i +
// "|FF0000|000000",
       for (let i = 0; i < waypts.length; i++) {
		
	}  
    // /////////
    directionsService.route({
        origin: new google.maps.LatLng(parseLat(jsonResponse[0].latLng), parseLng(jsonResponse[0].latLng))
        , destination: new google.maps.LatLng(parseLat(jsonResponse[totalDataLength - 1].latLng), parseLng(jsonResponse[totalDataLength - 1].latLng))
        , waypoints: waypts
        , optimizeWaypoints: true
        , travelMode: 'DRIVING'
    }, function (response, status) {
        if (status === 'OK') {
            directionsDisplay.setDirections(response);
        }
        else {
            window.alert('Directions request failed due to ' + status);
        }
    });
}

function callAjax(url, busRoute, trend, directionsService, directionsDisplay,map) {
    $.ajax({
        type: "GET"
        , contentType: "application/json"
        , url: url + "/ajax"
        , data: {
            busRoute: busRoute
            , trend: trend
        }
        , dataType: 'json'
        , timeout: 100000
        , success: function (jsonResponse) {
            calculateAndDisplayRoute1(directionsService, directionsDisplay, jsonResponse,map);
        }
    });
}