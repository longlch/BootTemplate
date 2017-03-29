var markers = [];
var url = window.location.href;

function openNav() {
    document.getElementById("mySidenav").style.width = "30%";
}

function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
}

function initMap() {
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
    var uluru = {
        lat: -25.363
        , lng: 131.044
    };
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
    
    
    $("body").on("click", ".btn-back", function(event) {
    	getBack(url);
    });
    $("body").on("click", ".rowClear", function(event) {
    	  let routeId = $(this).find(".routeId").text();
    	  ajaxGetContent(url,routeId,"go");
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