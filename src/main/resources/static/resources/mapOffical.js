var markers=[];

        function initMap() {
        	var styledMapType = new google.maps.StyledMapType(
        		    [
        		  {
        		    "featureType": "poi",
        		    "stylers": [
        		      {
        		        "color": "#ff3b88"
        		      },
        		      {
        		        "visibility": "off"
        		      }
        		    ]
        		  },
        		  {
        		    "featureType": "poi.attraction",
        		    "stylers": [
        		      {
        		        "visibility": "off"
        		      }
        		    ]
        		  },
        		  {
        		    "featureType": "poi.government",
        		    "stylers": [
        		      {
        		        "visibility": "off"
        		      }
        		    ]
        		  },
        		  {
        		    "featureType": "poi.medical",
        		    "stylers": [
        		      {
        		        "visibility": "off"
        		      }
        		    ]
        		  },
        		  {
        		    "featureType": "poi.school",
        		    "stylers": [
        		      {
        		        "visibility": "off"
        		      }
        		    ]
        		  },
        		  {
        		    "featureType": "poi.sports_complex",
        		    "stylers": [
        		      {
        		        "visibility": "off"
        		      }
        		    ]
        		  }
        		],{name: 'Styled Map'}
        		 );
            let directionsService = new google.maps.DirectionsService;
            let directionsDisplay = new google.maps.DirectionsRenderer;
            // init map
            var map = new google.maps.Map(document.getElementById('map'), {
                zoom: 12
                , center: {
                    lat: 16.054856
                    , lng: 108.200403
                },
                mapTypeControlOptions: {
                    mapTypeIds: ['roadmap','terrain',
                    'styled_map']
                 }
            });
            map.mapTypes.set('styled_map', styledMapType);
            map.setMapTypeId('styled_map');
            directionsDisplay.setMap(map);
// hide way points
            directionsDisplay.setOptions( { suppressMarkers: true } );
            var currentRoute;
            var url = window.location.href;
            $(".route").click(function () {
            	clearMarkers();
            	 markers = [];
                let busRoute = $(this).val();
                currentRoute = $(this).attr("name");
                callAjax(url, busRoute, "go", directionsService, directionsDisplay,map);
            });
            $("#btnBack").click(function () {
                if (typeof (currentRoute) == "undefined") {
                    alert("please choose the route");
                }
                else {
                	clearMarkers();
               	 markers = [];
                    callAjax(url, currentRoute, "back", directionsService, directionsDisplay,map);
                }
            });
            $("#btnGo").click(function () {
                if (typeof (currentRoute) == "undefined") {
                	alert("please choose the route");
                }
                else {
                	clearMarkers();
               	 markers = [];
                    callAjax(url, currentRoute, "go", directionsService, directionsDisplay,map);
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
    					icon:"https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=" + i + "|FF0000|000000",
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
        function openNav() {
            document.getElementById("mySidenav").style.width = "30%";
        }

        function closeNav() {
            document.getElementById("mySidenav").style.width = "0";
        }
 