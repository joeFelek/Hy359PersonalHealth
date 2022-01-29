$(document).ready(function() {

	const attribution = new ol.control.Attribution({
		collapsible: false
	});

	// create map
	const map = new ol.Map({
		controls: ol.control.defaults({
			attribution: false
		}).extend([attribution]),
		layers: [
			new ol.layer.Tile({
				source: new ol.source.OSM()
			})
		],
		target: 'Map',
		view: new ol.View({
			center: [0, 0],
			maxZoom: 18,
			zoom: 1
		})
	});

	// 'Show map' button handler
	$('#show-map').click(function() {

		const data = null;

		const xhr = new XMLHttpRequest();
		xhr.withCredentials = true;

		xhr.addEventListener("readystatechange", function() {
			if (this.readyState === this.DONE) {
				console.log('geocoding search response:')
				console.log(this.responseText)

				if (this.responseText === '{}') { // empty
					$('#show-map-error').text("Couldn't find your location");
					$('#show-map-error').show()
				} else if (!this.responseText.includes("Crete")) {
					$('#show-map-error').text("We are sorry the service is only available in Crete");
					$('#show-map-error').show()
				} else {
					init_map(this.responseText)
					// disable button until address changed or map closed
					$('#show-map').attr('disabled', true);
				}
			}
		});

		let address = format_address();
		if (address) {
			$('#show-map-error').hide()
			xhr.open("GET", "https://forward-reverse-geocoding.p.rapidapi.com/v1/search?q=" + address + "&accept-language=en&polygon_threshold=0.0");
			xhr.setRequestHeader("x-rapidapi-host", "forward-reverse-geocoding.p.rapidapi.com");
			xhr.setRequestHeader("x-rapidapi-key", "905067515fmsh7514e7890c1e788p1d7da2jsnaa1e9a0f4cbb");
			xhr.send(data);
		} else {
			$('#show-map-error').text("Please fill all address details to show map");
			$('#show-map-error').show()
		}
	});

	// return address in a format for geocoding 
	function format_address() {
		let country = $('#country').val();
		let city = $('#city').val();
		let areaCode = $('#areaCode').val();
		let address = $('#address').val();

		if (!address || !areaCode || !city || !country)
			return null

		return (address + " " + areaCode + " " + city + " " + country).replaceAll(" ", "%20")
	}

	function init_map(responseText) {

		$('#Map').show();

		const response = JSON.parse(responseText.substring(1, responseText.length - 1));
		const coordinates = ol.proj.fromLonLat([response.lon, response.lat]);

		create_marker(coordinates)
		map.getView().setCenter(coordinates);

		map.getView().setZoom(16)

		create_popup(response.display_name)
	}

	// create style for marker
	const iconStyle = new ol.style.Style({
		image: new ol.style.Icon({
			anchor: [0.5, 46],
			anchorXUnits: 'fraction',
			anchorYUnits: 'pixels',
			src: 'https://img.icons8.com/color/48/000000/marker--v2.png'
		})
	});

	function create_marker(coordinates) {

		// create marker
		const iconFeature = new ol.Feature({
			geometry: new ol.geom.Point(coordinates),
		});

		// add marker to map
		var layer = new ol.layer.Vector({
			source: new ol.source.Vector({
				features: [iconFeature]
			}),
			style: [iconStyle],
			name: 'Marker'
		})
		map.addLayer(layer);
	}

	function create_popup(message) {
		$('#popup').show();

		var container = document.getElementById('popup');
		var content = document.getElementById('popup-content');
		var closer = document.getElementById('popup-closer');

		var overlay = new ol.Overlay({
			element: container,
			name: 'Popup'
		})
		map.addOverlay(overlay);

		closer.onclick = function() {
			overlay.setPosition(undefined);
			closer.blur();
			return false;
		};

		map.on('singleclick', function(event) {
			if (map.hasFeatureAtPixel(event.pixel) === true) {
				var coordinate = event.coordinate;
				overlay.setPosition(coordinate)
				content.innerHTML = message
			} else {
				overlay.setPosition(undefined);
				closer.blur();
			}
		});
	}

	// hide map and remove markers
	function close_map() {
		$('#Map').hide()
		const layers = [...map.getLayers().getArray()]

		layers.forEach((layer) => {
			if (layer.get('name') !== undefined && layer.get('name') === 'Marker')
				map.removeLayer(layer)
		})

		$('#show-map').attr('disabled', false);
	}
	$('#close-map').click(() => close_map());
	$('#address, #city, #country, #areaCode').change(() => close_map());


	/* autofill address based on location 
	   THIS IS ONLY SUPPORTED FOR HTTPS, it can't be tested in my http site,
	   this is why I am using just the reverse_geocoding of a static location to show how it works
	*/

	// autofill button handler
	// if https uncomment geolocation and remove reverse_geocoding example 
	$('#search-location').click(function() {
		get_geolocation_coordinates()
		//reverse_geocoding(35.3373838, 25.1267562)
	});

	// get coordinates from geolocation 
	function get_geolocation_coordinates() {
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(showPosition)
		} else {
			$('#show-map-error').show();
			$('#show-map-error').text('Geolocation is not supported by this browser.');
			$('#search-location').attr('disabled', true);
		}
	}

	function showPosition(position) {
		reverse_geocoding(position.coords.latitude, position.coords.longitude)
	}

	// send reverse geocoding request 
	function reverse_geocoding(lat, lon) {
		const data = null;

		const xhr = new XMLHttpRequest();
		xhr.withCredentials = true;

		xhr.addEventListener("readystatechange", function() {
			if (this.readyState === this.DONE) {
				console.log('reverse geocoding response:')
				console.log(this.responseText);
				fill_address_fields(this.responseText)
			}
		});

		xhr.open("GET", "https://forward-reverse-geocoding.p.rapidapi.com/v1/reverse?lat=" + lat + "&lon=" + lon + "&accept-language=en&polygon_threshold=0.0");
		xhr.setRequestHeader("x-rapidapi-host", "forward-reverse-geocoding.p.rapidapi.com");
		xhr.setRequestHeader("x-rapidapi-key", "905067515fmsh7514e7890c1e788p1d7da2jsnaa1e9a0f4cbb");

		xhr.send(data);
	}

	function fill_address_fields(responseText) {
		if (responseText.includes("error")) {
			$('#show-map-error').text('Could not find location');
			$('#show-map-error').show();
			return
		}
		if(responseText.charAt(0) === '[' || responseText.charAt(0) === '{')
			fill_address_fields_json(responseText)
		else
			fill_address_fields_xml(responseText)

		$('#show-map-error').hide();
	}

	function fill_address_fields_json(responseText) {
		const response = JSON.parse(responseText);
		$('#address').val(response.address.road)
		$('#city').val(response.address.city)
		$('#country').val(response.address.country)
		$('#areaCode').val(response.address.postcode.replace(" ", ''))


	}

	function fill_address_fields_xml(response) {
		var xml = $.parseXML(response),
			$xml = $(xml),
			$road = $xml.find('road'),
			$city =  $xml.find('city'),
			$country = $xml.find('country')
			$postcode = $xml.find('postcode');

		if($road.text())
			$('#address').val($road.text());
		if($city.text())
			$('#city').val($city.text());
		if($postcode.text())
			$('#areaCode').val(parseInt($postcode.text()));
		if($country.text())
			$('#country').val($country.text());
	}

});