    var socket = io.connect('https://dnbus-rt.herokuapp.com/');

     $("body").on("click", ".btnGet", function (event) {
         socket.emit('subscribe',routeId);
     });
    $("body").on("click", ".btnStop", function (event) {
         socket.emit('unsubscribe', routeId);
        clearBusMarker();
     });
    var arrayMarker = [];

    var markers = [];
    var url = window.location.href;
    var trend;
    var currentTrend;   
    var renderList=[];
    var routeId=null;
    var walkLine;
    var polySpecial1;

    //this variable for real time
    var realLat;
    var realLng;
    var realRoute;


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
        let maxRoute;
        let routesTab=$("#routes-tab").html();
        let startPoint1= document.getElementById("startPoint");
        let autocomplete = new google.maps.places.Autocomplete(startPoint1);
        let endPoint1= document.getElementById("endPoint");
        let autocomplete1 = new google.maps.places.Autocomplete(endPoint1);
        let flag= true;

        walkLine = '#FF0000';
        var busLine=' #00e600';
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
        socket.on('message', function(data) {
            var now = new Date(Date.now());
            var formatted = now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds();
            for (let i = 0, n = data.length; i < n; i++) {
                let newLatLng = new google.maps.LatLng(data[i].lat, data[i].lng);
                let curMarker = new google.maps.Marker({
                    map: map,
                    icon: {
                        url:"http://maps.google.com/mapfiles/ms/micons/bus.png"
                        , scaledSize: new google.maps.Size(30, 30)
                    },
                    position: newLatLng,
                    visible: false
                });
                if (arrayMarker[i]) arrayMarker[i].setVisible(false);
                arrayMarker[i] = curMarker;
                arrayMarker[i].setVisible(true);
            }
            console.log(data, formatted);
        });
        // back button from content in side bar
        $("body").on("click", ".btn-back", function (event) {
            socket.emit('unsubscribe', routeId);
            clearMarkers();
            clearPolyline(renderList);
            renderList=[];
            $("#routes-tab").html(routesTab);
        });

        $("body").on("click", ".rowClear", function (event) {
            clearMarkers();
            markers = [];
            clearPolyline(renderList);
            routeId = $(this).find(".routeId").text();
            trend = "true";
            currentTrend = "true";
            currentRoute = routeId;
            flag=false;

            // Poly Special
            polySpecial(url, routeId, trend);

            //   get content and push it in side bar
            ajaxGetContent(url, routeId, trend);
            //  create maker 
            callAjax(url, routeId, trend, directionsService, directionsDisplay, map, geocoder, infowindow,busLine,flag,polySpecial1);
            // draw bus line base on polyspecial
            drawPoly(url, routeId, trend, directionsService, directionsDisplay, map, geocoder, infowindow,busLine);



        });
        $("body").on("click", "#btnBack", function (event) {
            flag=false;
            currentTrend = "false";
            checkTrend(currentTrend, currentRoute, url, directionsService, directionsDisplay, map, geocoder, infowindow,busLine,flag,polySpecial1);
        });
        $("body").on("click", "#btnGo", function (event) {
            flag=false;
            currentTrend = "true";
            checkTrend(currentTrend, currentRoute,url,directionsService,directionsDisplay,map,geocoder,infowindow,busLine,flag,polySpecial1);
        });
        $("#btnSearch").click(function(){
            flag=true;
            if (typeof maxRoute=== "undefined") {
               clearMarkers();
                markers = [];
                clearPolyline(renderList);
                sendAddress(2,map,directionsService,geocoder,infowindow,busLine,flag);
            }else if(maxRoute >3){
                alert("There are no direction was be found");
                maxRoute=2;
            }else{
                clearMarkers();
                markers = [];
                clearPolyline(renderList);
                sendAddress(maxRoute,map,directionsService,geocoder,infowindow,busLine,flag);
                maxRoute=2;
            }
        });
        $(".max-route").click(function() {
            maxRoute=$(this).val();
        });
        $("body").on("click", "#btnDetail", function (event) {
            // It will head to detail page and get conttent from there
            detailContent(url, routeId);
        });
        $("body").on("click", ".realTime", function (event) {
    //        alert(realLng+" "+realLat+" "+realRoute);
    //        realTime(realLat,realLng,realRoute);
            $.post("https://dnbus-rt.herokuapp.com/api/get-bus-time/",
            {
             lat: realLat
            , lng: realLng
            ,turn:true
            ,route:realRoute
            ,data:realRoute
            },
            function(data,status){
                if(typeof data.data[0] === "undefined"){
                    alert("logan bug");    
                }else{
    //                request a json object from logan response
                    realTime(url,data);
                }
                alert("Data: " + data + "\nStatus: " + status);
                console.log("data in screen "+data);
            });
        });

    }
    function polySpecial(url, routeId, trend){
        $.ajax({
            async: false,
            type: "GET"
            , contentType: "application/json"
            , url: url + "/special"
            , data: {
                busRoute: routeId
                , trend: trend
            }
            , dataType: 'json'
            , timeout: 100000
            , success: function (jsonResponse) {
    //            drawDirection(jsonResponse,map,directionsService,busLine);
                polySpecial1=jsonResponse;
    //            calBack(jsonResponse);
    //            var cloVar=jsonResponse;
    //            for(let j=0;j<jsonResponse.length;j++){
    //                console.log("lat is "+jsonResponse[j].lat);
    //                console.log("lng is "+jsonResponse[j].lng);
    //            }
            }
        });
    }
    function realTime(url,text){
         $.ajax({
                type: 'post',
                url: url + "/real",
                data: JSON.stringify(text),
                contentType: "application/json; charset=utf-8",
                traditional: true,
                success: function (data) {
                     var html = jQuery('<body>').html(data);
                    var content = html.find("#content").html();
                    $("#routes-tab").html(content);
                }
            });
    }
    function drawPoly(url, routeId, trend, directionsService, directionsDisplay, map, geocoder, infowindow,busLine){
         $.ajax({
            type: "GET"
            , contentType: "application/json"
            , url: url + "/draw"
            , data: {
                busRoute: routeId
                , trend: trend
            }
            , dataType: 'json'
            , timeout: 100000
            , success: function (jsonResponse) {
                drawDirection(jsonResponse,map,directionsService,busLine);
            }
        });
    }
    function clearPolyline(renderList){
        for(let i=0;i<renderList.length;i++){
            renderList[i].setMap(null);
        }
    }

    function sendAddress(maxRoute,map,service,geocoder,infowindow,busLine,flag){
        let startPoint=$("#startPoint").val();
        let endPoint=$("#endPoint").val();
        ajaxDirection(url,startPoint,endPoint,maxRoute,map,service,geocoder,infowindow,busLine,flag);
         sideBarDirection(url,startPoint,endPoint,maxRoute);
    }
    function ajaxDirection(url,startPoint,endPoint,maxRoute,map,service, geocoder,infowindow,busLine,flag) {
        var getUrl = url + "/detail" ;
        $.ajax({
            type: "GET"
            , url: getUrl
            , data: {
                startPoint:startPoint,
                endPoint:endPoint,
                maxRoute:maxRoute
            }
            , success: function (data) {
                // bug here
    //            alert(data[0].busList[0].turn);
    //        	console.log("console in ajaxDirection "+data.length);
    //        	for(let j=0;j<data.length;j++){
    //        		console.log("data is "+data[j].id);
    //        		console.log("data is "+data[j]);
    //        	}
                let html = jQuery('<body>').html(data);
                let checkData= html.find("#contentDirection").val();
                if( typeof checkData === "undefined"){
                     if(data.length != 1){
                         calculateAndDisplayRoute1(service, null, data, map, geocoder, infowindow,busLine,flag);   
                     }
                }else{
                    console.log("No direction was found");
                }

            }
        });
    }

    /*
     * function drawDirectionLine(data){ for(let i=0;i<data.length;i++){ if(data ){
     *  } } }
     */
    function sideBarDirection(url,startPoint,endPoint,maxRoute) {
        var getUrl = url + "/direction" ;
        $.ajax({
            type: "GET"
                , url: getUrl
                , data: {
                    startPoint:startPoint,
                    endPoint:endPoint,
                    maxRoute:maxRoute
                }
        , success: function (data) {
            let html = jQuery('<body>').html(data);
            let content = html.find("#contentDirection").html();
            $("#direction-content").html(content);
             showMarkerDetail(markers);
        }
        });
    }

    function checkTrend(currentTrend, currentRoute, url, directionsService, directionsDisplay, map, geocoder, infowindow,busLine,flag,special) {
        if (currentTrend == trend) {}
        else {
            clearMarkers();
            markers = [];
            clearPolyline(renderList);
            renderList=[];
            trend = currentTrend;
            ajaxGetContent(url, currentRoute, currentTrend);
            callAjax(url, currentRoute, currentTrend, directionsService, directionsDisplay, map, geocoder, infowindow,busLine,flag,special);
            drawPoly(url, routeId, trend, directionsService, directionsDisplay, map, geocoder, infowindow,busLine);
        }
    }

    function ajaxGetContent(url, routeId, trend) {
        var getUrl = url + "/route/" + routeId;
        $.ajax({
             async: false,
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
        for (let i = 0; i < arrayMarker.length; i++) {
            arrayMarker[i].setMap(map);
        }
    }
    
    function setMapOnAllBusMarker(map) {
        for (let i = 0; i < arrayMarker.length; i++) {
            arrayMarker[i].setMap(map);
        }
    }
    function clearMarkers() {
        setMapOnAll(null);
    }
    
    function clearBusMarker(){
        setMapOnAllBusMarker(null);
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

    function callAjax(url, routeId, trend, directionsService, directionsDisplay, map, geocoder, infowindow,busLine,flag,special) {
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
    //            alert(jsonResponse[0].busList[0].turn);

                calculateAndDisplayRoute1(directionsService, directionsDisplay, jsonResponse, map, geocoder, infowindow,busLine,flag,special);
            }
        });
    }

    function calculateAndDisplayRoute1(directionsService, directionsDisplay, jsonResponse, map, geocoder, infowindow,busLine,flag,special) {
        // load waypoint from server
        let totalDataLength = jsonResponse.length;
        let waypts = [];
        let marker;
        let startIcon = "https://chart.googleapis.com/chart?chst=d_map_xpin_icon&chld=pin_star|car-dealer|FFFF00|FF0000";
        let endIcon = "https://chart.googleapis.com/chart?chst=d_map_pin_icon&chld=flag|FFFF00";
        let wayPointsIcon;
        let lat;
        let lng;


        alert("list polyspecial "+special);
        if(typeof(special) === "underfined"){
            console.log("special is underfinded");
        }else{
            console.log("come here if have special ");

        }
        for (let index = 0; index < jsonResponse.length; index++) {
            lat =jsonResponse[index].lat;
            lng = jsonResponse[index].lng;
            wayPointsIcon = "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=" +jsonResponse[index].id + "|109d59|ffffff";
            if (index == 0) {

                marker = createMarker(lat, lng, startIcon, map);
                markers.push(marker);
            }
            else if (index > 0 && index < jsonResponse.length - 1) {
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
            markers[index].addListener('click', function () {
                map.setCenter(markers[index].getPosition());

                if(flag){
                    console.log("special is underfinded");
                    realLat=jsonResponse[index].lat;
                    realLng=jsonResponse[index].lng;
                }else{
                    console.log("come here if have special ");
                    realLng=special[index].lng;
                    realLat=special[index].lat;    
                }
                if(jsonResponse[index].id != -1 && jsonResponse[index] != 9999){
                    realRoute=jsonResponse[index].busList[0].id;
                }
                
                console.log("real lat "+realLat);
                console.log("real lng"+realLng);
                alert("current route is "+realRoute);
               geocodeLatLng(geocoder, map, infowindow,jsonResponse[index].lat,jsonResponse[index].lng, jsonResponse[index].name);
                infowindow.open(map, markers[index]);
            });
        }
        if(flag){
            drawDirection(jsonResponse,map,directionsService,busLine);
        }

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

            google.maps.event.addDomListener(stationsName[j], "click", function () {
                    google.maps.event.trigger(markers[j], "click");
            });
        }
        }else{
            alert("content with marker get error ");
        }
    }
    function geocodeLatLng(geocoder, map, infowindow, lat1,lng1, nameStation) {
        let latlng = {
            lat: lat1
            , lng:lng1
        };
        console.log("lat "+lat1);
        console.log("lng "+lng1);
        geocoder.geocode({
            'location': latlng
        }, function (results, status) {
            if (status === 'OK') {
                if (results[0]) {
                    let contentString = '<div id="infoContent" style="height:85px;width:355px">' 
                        + '<div id="siteNotice">' + '</div>' + '<div id="bodyContent" >' 
                        + '<p><b>Tên trạm dừng:    </b>'
                        + nameStation + '</p>' + '<p><b>Địa chỉ:  </b>' 
                        + results[0].formatted_address + '</p>' 
                        + '<input class="realTime" style="height:20px;width:341px" type = "button" value = "Thời gian chờ"/>' 
                        + '</div>' + '</div>';
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
    function drawDirection(stations,map,service,busLine){
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
        for (var i = 0, parts = [], max = 25 - 1; i < stations.length; i = i + max)
            parts.push(stations.slice(i, i + max + 1));
        // Callback function to process service results
        var service_callback = function (response, status) {
                    if (status != 'OK') {
                        console.log('Directions request failed due to ' + status);
                        return;
                    }
                    var renderer = new google.maps.DirectionsRenderer;
                    renderer.setMap(map);
                    renderer.setOptions({
                        suppressMarkers: true
                        , preserveViewport: true, 
                        polylineOptions: { strokeColor: busLine } 
                    });
                    renderList.push(renderer);
                    renderer.setDirections(response);
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
                        "featureType": "poi",
                        "elementType": "labels.text",
                        "stylers": [
                          {
                            "visibility": "off"
                          }
                        ]
                      },
                      {
                        "featureType": "poi.business",
                        "stylers": [
                          {
                            "visibility": "off"
                          }
                        ]
                      },
                      {
                        "featureType": "road",
                        "elementType": "labels.icon",
                        "stylers": [
                          {
                            "visibility": "off"
                          }
                        ]
                      },
                      {
                        "featureType": "transit",
                        "stylers": [
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

    function detailContent(url, routeId) {
         var getUrl = url + "/directionDetail/" + routeId;
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