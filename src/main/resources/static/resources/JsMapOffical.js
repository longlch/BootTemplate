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
function getDirectionContent(url) {
	let routeUrl=url+"/direction1";
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
    let directionsDisplay = new google.maps.DirectionsRenderer;
    let geocoder = new google.maps.Geocoder;
    let infowindow = new google.maps.InfoWindow;
    let dispatchRoutes=$("#dispatchRoutes");
    let dispatchDirection=$("#dispatchDirection");
    let currentRoute;
    
    
    var styledMapType = customizeMap();
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
    directionsDisplay.setOptions({
        suppressMarkers: true
    });
    
    /* init event for app */
    dispatchDirection.click(function(){
        dispatchRoutes.removeClass("active");
        dispatchDirection.addClass("active");
        //call ajax to change content
        getDirectionContent(url);
        
    });
    dispatchRoutes.click(function(){
        dispatchDirection.removeClass("active");
        dispatchRoutes.addClass("active");
        //call ajax
        getBack(url);
    });
    $("body").on("click", ".btn-back", function (event) {
        getBack(url);
    });
    $("body").on("click", ".rowClear", function (event) {
        clearMarkers();
        markers = [];
        let routeId = $(this).find(".routeId").text();
        trend="go";
        currentTrend="go"
        ajaxGetContent(url, routeId, trend);
        currentRoute = routeId;
        callAjax(url, routeId, trend, directionsService, directionsDisplay, map,geocoder,infowindow);
    });
    $("body").on("click", "#btnBack", function (event) {
        currentTrend="back";
        checkTrend(currentTrend,currentRoute,url,directionsService, directionsDisplay, map,geocoder,infowindow);
    });
    $("body").on("click", "#btnGo", function (event) {
        currentTrend="go";
        checkTrend(currentTrend,currentRoute,url,directionsService, directionsDisplay, map,geocoder,infowindow);
    });
}
function checkTrend(currentTrend,currentRoute,url,directionsService, directionsDisplay, map,geocoder,infowindow){
    if(currentTrend==trend){
       
    }else{
        clearMarkers();
        markers = [];
        trend=currentTrend;
        ajaxGetContent(url,currentRoute,currentTrend);
        callAjax(url, currentRoute, currentTrend, directionsService, directionsDisplay, map,geocoder,infowindow);
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
            $("#content").html(content);
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

function callAjax(url, routeId, trend, directionsService, directionsDisplay, map,geocoder,infowindow) {
    let busRoute="route" + routeId;
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
            calculateAndDisplayRoute1(directionsService, directionsDisplay, jsonResponse, map,geocoder,infowindow);
        }
    });
}

function calculateAndDisplayRoute1(directionsService, directionsDisplay, jsonResponse, map,geocoder,infowindow) {
    // load waypoint from server
    var totalDataLength = jsonResponse.length;
    var waypts = [];
    var totalDataLength = jsonResponse.length;
    for (let i = 0; i < jsonResponse.length; i++) {
        let lat = parseLat(jsonResponse[i].latLng);
        let lng = parseLng(jsonResponse[i].latLng);
        if (i == 0) {
            markers.push(marker = new google.maps.Marker({
                position: new google.maps.LatLng(lat, lng)
                , icon: "https://chart.googleapis.com/chart?chst=d_map_xpin_icon&chld=pin_star|car-dealer|ADDE63|FF0000"
                , map: map
            }));
        }
        else if (i > 0 && i < jsonResponse.length - 1) {
            markers.push(marker = new google.maps.Marker({
                position: new google.maps.LatLng(lat, lng)
                , icon: "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=" + (i + 1) + "|FF0000|000000"
                , map: map
            }));
            waypts.push({
                location: new google.maps.LatLng(lat, lng)
                , stopover: true
            });
        }
        else {
            markers.push(marker = new google.maps.Marker({
                position: new google.maps.LatLng(lat, lng)
                , icon: "https://chart.googleapis.com/chart?chst=d_map_pin_icon&chld=flag|ADDE63"
                , map: map
            }));
        }
        markers[i].addListener('click', function () {
            geocodeLatLng(geocoder, map, infowindow, jsonResponse[i].latLng, jsonResponse[i].name);
            infowindow.open(map, markers[i]);
        });
    }
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

function geocodeLatLng(geocoder, map, infowindow, latLngObj, nameStation) {
    let latlngStr = latLngObj.split(',', 2);
    let latlng = {
        lat: parseFloat(latlngStr[0])
        , lng: parseFloat(latlngStr[1])
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
 function customizeMap(){
	 let styledMapType=new google.maps.StyledMapType(
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

