/*
 * Marko Tallgren
 * a1600545
 */

	$(function() {
		$( ".date, #maksupvm" ).datepicker({
			dateFormat: 'yy-mm-dd',
			onSelect: function(){
				$( ".date, #maksupvm" ).datepicker( "hide" );
				laskeKokonaishinta();
			}
		})
	});
	
	$.datepicker.regional['fi'] = {
		closeText: 'Sulje',
		prevText: 'Edellinen',
		nextText: 'Seuraava',
		currentText: 'T&auml;n&auml;&auml;n',
		monthNames: ['Tammikuu','Helmikuu','Maaliskuu','Huhtikuu','Toukokuu',
			'Kes&auml;kuu','Hein&auml;kuu','Elokuu','Syyskuu','Lokakuu',
			'Marraskuu','Joulukuu'],
		monthNamesShort: ['Tammi','Helmi','Maalis','Huhti','Touko',
			'Kes&auml;','Hein&auml;','Elo','Syys','Loka','Marras','Joulu'],
		dayNamesShort: ['Su','Ma','Ti','Ke','To','Pe','Su'],
		dayNames: ['Sunnuntai','Maanantai','Tiistai','Keskiviikko','Torstai',
			'Perjantai','Lauantai'],
		dayNamesMin: ['Su','Ma','Ti','Ke','To','Pe','La'],
		weekHeader: 'Vk',
		      dateFormat: 'dd.mm.yy',
		firstDay: 1,
		isRTL: false,
		showMonthAfterYear: false,
		yearSuffix: ''};
	$.datepicker.setDefaults($.datepicker.regional['fi']);