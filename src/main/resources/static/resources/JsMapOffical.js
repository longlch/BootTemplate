var markers = [];
var url = window.location.href;
var trend;
var currentTrend;

function openNav() {
    document.getElementById("mySidenav").style.width = "30%";
}

function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
}

function getBack(url) {
    $.ajax({
        type: "GET"
        , url: url
        , success: function (data) {
            let html = jQuery('<body>').html(data);
            let content = html.find("#content").html();
            $("#content").html(content);
        }
    });
}

/* redirect to direction page */
function getDirectionContent(url) {
    let routeUrl = url + "/direction";
    $.ajax({
        type: "GET"
        , url: routeUrl
        , success: function (data) {
            let html = jQuery('<body>').html(data);
            let content = html.find("#content").html();
            $("#content").html(content);
        }
    });
}

function initMap() {
    let directionsService = new google.maps.DirectionsService;
    var directionsDisplay=new google.maps.DirectionsRenderer;
    let geocoder = new google.maps.Geocoder;
    let infowindow = new google.maps.InfoWindow;
    let currentRoute;
    var styledMapType = customizeMap();
    
    let directionTab=$("#directions-tab").html();
    let routesTab=$("#routes-tab").html();
// console.log(directionTab.html());
    
    
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
   /*
	 * directionsDisplay.setMap(map); directionsDisplay.setOptions({
	 * suppressMarkers: true });
	 */
    /* init event for app */
    
    
    $("body").on("click", ".btn-back", function (event) {
        if(directionsDisplay){
            directionsDisplay.setMap(null);
        }
        clearMarkers();
        $("#routes-tab").html(routesTab);
    });
    $("#dispatchRoutes").click(function(){
// $("#content").html(routesTab);
    });
    $("#dispatchDirection").click(function(){
// $("#content").html(directionTab);
    });
    $("body").on("click", ".rowClear", function (event) {
        clearMarkers();
        markers = [];
        let routeId = $(this).find(".routeId").text();
        trend = "true";
        currentTrend = "true"
        ajaxGetContent(url, routeId, trend);
        currentRoute = routeId;
        callAjax(url, routeId, trend, directionsService, directionsDisplay, map, geocoder, infowindow);
    });
    $("body").on("click", "#btnBack", function (event) {
        currentTrend = "false";
        checkTrend(currentTrend, currentRoute, url, directionsService, directionsDisplay, map, geocoder, infowindow);
    });
    $("body").on("click", "#btnGo", function (event) {
        currentTrend = "true";
        checkTrend(currentTrend, currentRoute, url, directionsService, directionsDisplay, map, geocoder, infowindow);
    });
    $("body").on("click","#btnSearch",function(event){
        sendAddress();
    });
}

function sendAddress(){
    let startPoint=$("#startPoint").val();
    let endPoint=$("#endPoint").val();
    ajaxDirection(url,startPoint,endPoint);
    sideBarDirection(url,startPoint,endPoint);
}

function checkTrend(currentTrend, currentRoute, url, directionsService, directionsDisplay, map, geocoder, infowindow) {
    if (currentTrend == trend) {}
    else {
        clearMarkers();
        markers = [];
        trend = currentTrend;
        ajaxGetContent(url, currentRoute, currentTrend);
        callAjax(url, currentRoute, currentTrend, directionsService, directionsDisplay, map, geocoder, infowindow);
    }
}

function ajaxGetContent(url, routeId, trend) {
    var getUrl = url + "/route/" + routeId;
    $.ajax({
        type: "GET"
        , url: getUrl
        , data: {
            trend: trend
        }
        , success: function (data) {
            var html = jQuery('<body>').html(data);
            var content = html.find("#content").html();
            $("#routes-tab").html(content);
             showMarkerDetail(markers);
        }
    });
}

function ajaxDirection(url,startPoint,endPoint) {
    var getUrl = url + "/detail" ;
    $.ajax({
        type: "GET"
        , url: getUrl
        , data: {
            startPoint:startPoint,
            endPoint:endPoint
        }
        , success: function (data) {
            console.log("datat ne "+data[0].name);
        }
    });
}
function sideBarDirection(url,startPoint,endPoint) {
	var getUrl = url + "/direction" ;
	$.ajax({
		type: "GET"
			, url: getUrl
			, data: {
				startPoint:startPoint,
				endPoint:endPoint
			}
	, success: function (data) {
		let html = jQuery('<body>').html(data);
        let content = html.find("#contentDirection").html();
        $("#direction-content").html(content);
         showMarkerDetail(markers);
	}
	});
}

function getBack(url) {
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
    for (let i = 0; i < markers.length; i++) {
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

function callAjax(url, routeId, trend, directionsService, directionsDisplay, map, geocoder, infowindow) {
// let busRoute = "route" + routeId;
    $.ajax({
        type: "GET"
        , contentType: "application/json"
        , url: url + "/ajax"
        , data: {
            busRoute: routeId
            , trend: trend
        }
        , dataType: 'json'
        , timeout: 100000
        , success: function (jsonResponse) {
            calculateAndDisplayRoute1(directionsService, directionsDisplay, jsonResponse, map, geocoder, infowindow);
        }
    });
}

function calculateAndDisplayRoute1(directionsService, directionsDisplay, jsonResponse, map, geocoder, infowindow) {
    // load waypoint from server
    let totalDataLength = jsonResponse.length;
    let waypts = [];
    let marker;
    let startIcon = "https://chart.googleapis.com/chart?chst=d_map_xpin_icon&chld=pin_star|car-dealer|ADDE63|FF0000";
    let endIcon = "https://chart.googleapis.com/chart?chst=d_map_pin_icon&chld=flag|ADDE63";
    let wayPointsIcon;
    let lat;
    let lng;
    for (let i = 0; i < jsonResponse.length; i++) {
        lat =jsonResponse[i].lat;
        lng = jsonResponse[i].lng;
        wayPointsIcon = "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=" + (i + 1) + "|FF0000|000000";
        if (i == 0) {
        	
            marker = createMarker(lat, lng, startIcon, map);
            markers.push(marker);
        }
        else if (i > 0 && i < jsonResponse.length - 1) {
            marker = createMarker(lat, lng, wayPointsIcon, map);
            markers.push(marker);
            waypts.push({
                location: new google.maps.LatLng(lat, lng)
                , stopover: true
            });
        }
        else {
            marker = createMarker(lat, lng, endIcon, map);
            markers.push(marker);
        }
        markers[i].addListener('click', function () {
        	map.setCenter(markers[i].getPosition());
           geocodeLatLng(geocoder, map, infowindow,	 lat,lng, jsonResponse[i].name);
            infowindow.open(map, markers[i]);
        });
    }
    drawDirection(jsonResponse,map,directionsService,directionsDisplay);
}

function createMarker(lat, lng, icon, map) {
    let marker = new google.maps.Marker({
        position: new google.maps.LatLng(lat, lng)
        , icon: icon
        , map: map
    });	
    return marker;
}
function showMarkerDetail(markers){
	let stationsName = $(".stationsName");
	for(let j=0;j<stationsName.length;j++){
		google.maps.event.addDomListener(stationsName[j],"click",function(){
			google.maps.event.trigger(markers[j],"click");
		});
	}
}

/* backup if markers = 0 */
function showMarkerDetail1(markers){
    let markersLength=markers.length;
    let stationsName=$(".stationsName");
    if(markersLength>0 && ( typeof(stationsName) != "underfined") ){
        for(let j=0;j<stationsName.length;j++){
        console.log("stations name object "+stationsName[j]);
            
        google.maps.event.addDomListener(stationsName[j], "click", function () {
                google.maps.event.trigger(markers[j], "click");
        });
    }
    }else{
        alert("ahhihi");
    }
}
function geocodeLatLng(geocoder, map, infowindow, lat1,lng1, nameStation) {
    let latlng = {
        lat: lat1
        , lng:lng1
    };
    geocoder.geocode({
        'location': latlng
    }, function (results, status) {
        if (status === 'OK') {
            if (results[0]) {
                let contentString = '<div id="infoContent" style="height:85px;width:355px">' + '<div id="siteNotice">' + '</div>' + '<div id="bodyContent" >' + '<p><b>Tên trạm dừng:    </b>' + nameStation + '</p>' + '<p><b>Địa chỉ:    </b>' + results[0].formatted_address + '</p>' + '<input style="height:20px;width:341px" type = "button" value = "Thời gian chờ"/>' + '</div>' + '</div>';
                infowindow.setContent(contentString);
            }
            else {
                window.alert('No results found');
            }
        }
        else {
            window.alert('Geocoder failed due to: ' + status);
        }
    });
}
function drawDirection(stations,map,service,directionsDisplay){

    var lngs = stations.map(function (station) {
        return station.lng;
    });
    var lats = stations.map(function (station) {
        return station.lat;
    });
    map.fitBounds({
        west: Math.min.apply(null, lngs)
        , east: Math.max.apply(null, lngs)
        , north: Math.min.apply(null, lats)
        , south: Math.max.apply(null, lats)
    , });
    // Divide route to several parts because max stations limit is 25 (23
	// waypoints + 1 origin + 1 destination)
    for (var i = 0, parts = [], max = 25-1; i < stations.length; i = i + max) parts.push(stations.slice(i, i + max + 1));
    // Callback function to process service results
    var service_callback = function (response, status) {
        if (status != 'OK') {
            console.log('Directions request failed due to ' + status);
            return;
        }
        
// directionsDisplay = new google.maps.DirectionsRenderer;
        directionsDisplay.setMap(map);
        directionsDisplay.setOptions({
            suppressMarkers: true
            , preserveViewport: true
        });
        directionsDisplay.setDirections(response);
    };
    // Send requests to service to get route (for stations count <= 25 only one
	// request will be sent)
    for (var i = 0; i < parts.length; i++) {
        // Waypoints does not include first station (origin) and last station
		// (destination)
        var waypoints = [];
        for (var j = 1; j < parts[i].length - 1; j++) waypoints.push({
            location: parts[i][j]
            , stopover: false
        });
        // Service options
        var service_options = {
            origin: parts[i][0]
            , destination: parts[i][parts[i].length - 1]
            , waypoints: waypoints
            , travelMode: 'DRIVING'
        };
        // Send request
        service.route(service_options, service_callback);
    }
}
function customizeMap() {
    let styledMapType = new google.maps.StyledMapType(
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
    return styledMapType;
}