/**
 * 
 */

$('#categorie').change(function(){
	var selected= "";
	selected = $("#categorie option:selected").text();
	path=window.location.pathname;
	path=path+"/sottocategorie/"+selected;
	
    $.ajax({type:"GET",url: path,contentType:"application/json"}).done(function( data ) {
    	var select = $("#sottoCategorie");
    	var options;
    	if(select.prop){
    		options = select.prop('options');
    	}else{
    		options = select.attr('options');
    	}
    	$('option',select).remove();
    	
    	$.each(data,function(val,text){
    		options[options.length] = new Option(text, text);
    	});
    	
    });
});

$("#descrizione").autocomplete({
	source: window.location.pathname+"/getSuggerimenti/prodotti",
});

$("#supermercato").autocomplete({
	source: window.location.pathname+"/getSuggerimenti/supermercati",
	select:function(event,ui){
		var selected = ui.item.label;
		var strs = selected.split(/\s-\s/);
		$('#supermercato').val(strs[0]);
		$('#indirizzo').val(strs[1]);
	}
});

var map;
var infowindow = new google.maps.InfoWindow();
var geocoder = new google.maps.Geocoder();
(function inizialize(){
	var currentLocation;
	geocoder.geocode({'address':"Italia"},function(results,status){
		if(status == google.maps.GeocoderStatus.OK){
			currentLocation = results[0].geometry.location;
			map = new google.maps.Map(document.getElementById('map-canvas'),{
				center: currentLocation,
				zoom: 15,
			});
			
		}else{
			alert("geocoder non funziona");
		};
	});
	
	if(navigator.geolocation){
		navigator.geolocation.getCurrentPosition(function(position){
			currentLocation = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
			map.setCenter(currentLocation);
			
		}, function(){
			alert('consenti di sapere la tua posizione se vuoi essere localizzato error: ');			
		},null);
		
	}
})();

function callback(results, status) {
	  if (status == google.maps.places.PlacesServiceStatus.OK) {
	    for (var i = 0; i < results.length; i++) {
	      createMarker(results[i]);
	    };
	  };
	}

function createMarker(place) {
  var marker = new google.maps.Marker({
    map: map,
    position: place.geometry.location,
  });

  google.maps.event.addListener(marker, 'click', function() {
    infowindow.setContent(place.name);
    infowindow.open(map, this);
  });
};

var typingTimer;
var doneTypingInterval = 5000;

$('#indirizzo').keyup(function(){
	typingTimer = setTimeout(doneTyping, doneTypingInterval);
});

$('#indirizzo').keydown(function(){
	clearTimeout(typingTimer);
});
var markers = [];
function doneTyping(){
	var geocoder = new google.maps.Geocoder();
	geocoder.geocode({'address':$("#indirizzo").val()},function(results,status){
		if(status == google.maps.GeocoderStatus.OK){
			for(i=0;i<markers.length;i++){
				markers[i].setMap(null);
			}
			var marker = new google.maps.Marker({
			    map: map,
			    position: results[0].geometry.location,
			  });
			map.setCenter(marker.getPosition());
			infowindow.setContent($('#supermercato').val()+" "+$('#indirizzo').val());
		    infowindow.open(map, marker);
			markers.push(marker);
		}else{
			alert("errore nell'indirizzo");
		}
	});
}

$('#submit').click(function(){
	var supermercato = $('#supermercato').val();
	supermercato = supermercato.split(/\s*-\s*/);
	$('#supermercato').val(supermercato[0]);
	$('#supermercato').val($('#supermercato').val()+" - "+$('#indirizzo').val());
	var geocoder = new google.maps.Geocoder();
	geocoder.geocode({'address':$("#indirizzo").val()},function(results,status){
		if(status == google.maps.GeocoderStatus.OK){
			$.ajax({
				type:'POST',
				url:window.location.pathname,
				data: $("form").serialize() +"&lat="+results[0].geometry.location.lat()+"&lng"+results[0].geometry.location.lng(),
			});
		}else{
			alert("errore nel submit");
		}
	});
});