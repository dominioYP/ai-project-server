/**
 * 
 */

var risposta;
var i = 0;

var supermercati_markers = [];

(function getSottocategorie(){
	var selected=$('#categorie option:selected').text();
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
})();


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
var codici_prodotti= [];
var prodotto_selected="";
$("#descrizione").autocomplete({
	
	source: function(request,response){
		var risp = [];
		$.ajax({type:"GET",
			url: window.location.pathname+"/getSuggerimenti/prodotti",
			contentType:"application/json",
			data:{"term":request.term},
			success:function(data){
				codici_prodotti=data;
			}				
		});
		$.each(codici_prodotti,function(index,value){
			
			$.each(value,function(index,value){
				risp.push(value);
			});
		});
		response(risp);	
	},
	select:function(event,ui){
		$.each(codici_prodotti,function(index,value){
			
			if(value==ui.item.label){
				$('#codiceBarre').val(index);
				return false;
			}
		});
	}
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
			google.maps.event.addListener(map, 'tilesloaded', function(){
				var latLng = map.getCenter();
				$.ajax({type:"GET",
					url: window.location.pathname+"/getSupermercati",
					contentType:"application/json",
					data:{"lat":latLng.lat(),"lng":latLng.lng()},
					success:function(data){
						for(var i=0;i<supermercati_markers.length;i++){
							supermercati_markers[i].setMap(null);
						}
						$.each(data,function(index,value){
							var latLng = new google.maps.LatLng(value.lat,value.lng);
							var marker = new google.maps.Marker({
							    map: map,
							    position: latLng,
							});	
							
							google.maps.event.addListener(marker, 'click', function() {
								var string = "<a class ='infowindow' id='Si'>Si</a> o <a class ='infowindow' id='No'>No</a>";
								infowindow.setContent("<div class='infowindow 'id='nome'>"+value.nome+"</div>\n<div class='infowindow' id='domanda'> E' questo?\n"+string+"</div>");
								infowindow.open(map,this);
								$('a.infowindow').click(function(){
									if($(this).attr('id')=="Si"){
										var strs = $('#nome.infowindow').text().split(/\s-\s/);
										$('#supermercato').val(strs[0]);
										$('#indirizzo').val(strs[1]);
										$('#domanda.infowindow').empty();
										$('#nome.infowindow').after("risposta ricevuta");
									}else{
										$('#domanda.infowindow').empty();
										$('#nome.infowindow').after("risposta ricevuta");
									}
								}).css({'cursor':'pointer',
					    			'background-color':'skyblue',
					    			'color':'blue',
					    		});
							});
							supermercati_markers.push(marker);
						});
					}				
				});
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
var descrizioneTimer;
var descrizioneInterval= 5000;

$('#descrizione').keyup(function(){
	descrizioneTimer = setTimeout(searchImage,descrizioneInterval);
});

$('#descrizione').keydown(function(){
	clearTimeout(descrizioneTimer);
});

var results;

function searchImage(){
	/*var iURL = "http://ajax.googleapis.com/ajax/services/search/images";
	$.ajax({
	    url: iURL,
	    type: 'GET',
	    dataType: 'jsonp',
	    data: {
	        v:  '1.0',
	        q:  $("#descrizione").val(),
	        format: 'json',
	        jsoncallback:  '?'
	    },
	    success: function asd(json){
	    	if(json.responseData.results[0])
	    		$('img').attr("src",json.responseData.results[0].unescapedUrl);
	    }
	});*/
	
	var imageSearch = new google.search.ImageSearch();

   
   imageSearch.setRestriction(
		   google.search.ImageSearch.RESTRICT_IMAGESIZE,
		   google.search.ImageSearch.IMAGESIZE_LARGE);

    
    imageSearch.setSearchCompleteCallback(this, function(){
    	var searcher = imageSearch;
    	if (searcher.results && searcher.results.length > 0) {
    		results = searcher.results;
    		$('#preview').attr("src",searcher.results[0].tbUrl);
    		$('.domanda').empty();
    		$('#preview').parent().parent().after(
    		"<tr class='domanda'><td class='domanda'></td><td class='domanda'>E' questo? <a class='domanda' id='si'>Si</a> <a class='domanda' id='no'>No</a></td></tr>"		
    		);
    		$('a.domanda').css({'cursor':'pointer',
    			'background-color':'skyblue',
    			'color':'blue'
    		});
    		$('a.domanda').click(function(){
    			
    			risposta=$(this).text();
    			elaboraRisposta(risposta);
    			
    		});    		
    		
    		 		
    		
    	}
    	
    }, 
    		null);

    
    imageSearch.execute($("#descrizione").val());
}

function elaboraRisposta(risp){
	if(risposta=="No"){
		i++;
		if(results.length>i){
			$('td.domanda').next().html('Risposta ricevuta - <a class="domanda"> Next?</a>');
			$('a.domanda').css({'cursor':'pointer',
				'background-color':'skyblue',
				'color':'blue'
			});
			$('a.domanda').click(function(){
					
					$('#preview').attr("src",results[i].tbUrl);
					$('td.domanda').next().html("<tr class='domanda'><td class='domanda'></td><td class='domanda'>E' questo? <a class='domanda' id='si'>Si</a> <a class='domanda' id='no'>No</a></td></tr>");
					$('a.domanda').css({'cursor':'pointer',
		    			'background-color':'skyblue',
		    			'color':'blue'
		    		});
					$('a.domanda').click(function(){
		    			
		    			risposta=$(this).text();
		    			elaboraRisposta(risposta);
		    			
		    		});   
			});
		}else{
			$('td.domanda').next().html('Risposta ricevuta');
		}
	}else{
		$('td.domanda').next().html('Risposta ricevuta');
	}
}


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
			for(var i=0;i<markers.length;i++){
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

$('#insertionForm').submit(function(event){
	if(risposta=="No"){
		$('#preview').attr("src","");
	}
	var supermercato = $('#supermercato').val();
	supermercato = supermercato.split(/\s*-\s*/);
	$('#supermercato').val(supermercato[0]);
	$('#supermercato').val($('#supermercato').val()+" - "+$('#indirizzo').val());
	var geocoder = new google.maps.Geocoder();
	geocoder.geocode({'address':$("#indirizzo").val()},function(results,status){
		if(status == google.maps.GeocoderStatus.OK){			
			var form = new FormData();
			
			$.each($("form").serializeArray(),function(index,value){				
				form.append(value.name,value.value);
			});
			form.append("lat",results[0].geometry.location.lat());
			form.append("lng",results[0].geometry.location.lng());
			form.append("foto",$('#preview').attr("src"));
		
			form.append("file",$("#file")[0].files[0]);
			$.ajax({
				type:'POST',
				url:window.location.pathname,
				cache:false,
				contentType:false,
				processData:false,
				data: form,
				success:function(response){
					alert("successo");
					$(":root").html(response);
				}
			});
		}else{
			alert("errore nel submit");
		}
	});
	event.preventDefault();
});