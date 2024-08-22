;
(function($){

	/* ----------------------------------------------------------------------------------
	 * Empty Check
	 * ----------------------------------------------------------------------------------
	 */
	$.isEmpty = function(value){
		if(value == "" || value == "null" || value == "undefined" || value == null || value == undefined || (value != null && typeof value == "object" && !Object.keys(value).length)){
			return true;
		}
		else{
			return false;
		}
	};
	$.isNotEmpty = function(value){
		return !$.isEmpty(value);
	};
	$.nvl = function(value, defaultValue){
		if(typeof defaultValue === "undefined"){
			defaultValue = "";
		}
		if($.isEmpty(value)){
			value = defaultValue;
		}
		return value;
	};

	/* ----------------------------------------------------------------------------------
	 * Form Validate
	 * ----------------------------------------------------------------------------------
	 */
	$.validator.setDefaults({
		debug: false,
		onclick: false,
		onkeyup: false,
		onfocusout: false,
		errorClass: 'error',
		errorElement: 'span',
		errorPlacement: function(error, element){
			error.addClass('invalid-feedback');
			element.closest('.form-group').append(error);
		},
		highlight: function(element, errorClass, validClass){
			$(element).addClass('is-invalid');
			$(element).closest('.error').remove();
		},
		unhighlight: function(element, errorClass, validClass){
			$(element).removeClass('is-invalid');
			$(element).closest('.error').remove();
		},
		success: function(element){
			$(element).hide().closest('.form-group').removeClass('is-invalid');
			$(element).closest('.error').remove();
		},
		submitHandler: function(form){
		},
		showErrors: function(errorMap, errorList){
			//console.log(errorMap, errorList);
			/*
			if(errorList.length > 0){
				let message = '(<i class="fa fa-exclamation-circle"></i> ' + errorList[0].message + ')';
				$('[role=valid-form]').find('#valid-message').html(message).show();
			} else {
				$('[role=valid-form]').find('#valid-message').html(null);
			}*/

			if(errorList.length > 0){
				$.toasts({type: 'valid', text: errorList[0].message});
			}
			this.defaultShowErrors();
		},
		rules: {},
		messages: {}
	});

	/* ----------------------------------------------------------------------------------
	 * Form Data
	 * ----------------------------------------------------------------------------------
	 */
	let originalVal = $.fn.val;
	$.fn.val = function(value){
		let res = originalVal.apply(this, arguments);
		if(this.is("input:text") && arguments.length >= 1){
			// this is input type=text setter
			this.trigger("input");
			// formatter: money setter
			this.filter("[class*=fmt-money]").trigger('blur');
		}
		else if(this.is("select") && arguments.length >= 1){
			this.filter("[class*=select-segment]").trigger('change');
		}
		return res;
	};

	$.fn.serializeObject = function(){
		/*
		var obj = {};
		$.each(this.serializeArray(), function(i, o) {
			var n = o.name, v = o.value;

			obj[n] = obj[n] === undefined ? v
					: $.isArray(obj[n]) ? obj[n].concat(v)
					: [ obj[n], v ];
		});
		return obj;
		*/

		// disabled 항목도 넘어가도록 변경.
		var obj = {};
		$(this).each(function(i){
			var name = $(this).attr('name');
			if(name){
				name = name.replace(/\]/i, '');
				switch($(this).attr('type')){
					case 'radio':
						if($(this).is(':checked')){
							obj[name] = $(this).val();
						}
						break;
					case 'checkbox':
						/*
						if ($(this).attr('checked')) {
							if(obj[name] != undefined) {
								obj[name] = $.makeArray(obj[name]);
								obj[name] = obj[name].concat($(this).val());
							} else {
								obj[name] = $(this).val();
							}
						}*/
						if($(this).prop('checked')){
							obj[name] = 'Y';
						}
						else{
							obj[name] = 'N';
						}
						break;
					default:
						if(obj[name] != undefined){
							obj[name] = $.makeArray(obj[name]);
							obj[name] = obj[name].concat($(this).val());
						}
						else{
							obj[name] = $(this).val();
							if($(this).is("[class*='js-datepicker']")){
								//obj[name] = $(this).data().rawMaskFn();
								obj[name] = $(this).val().replace(/[^0-9]/g, '');
							}
						}
						break;
				}
			}
		});
		return obj;
	};

	// Form 데이터 가져오기
	$.fn.getFormData = function(isEmptyData){

		let rtnData = new Object();
		let createWarp = false;

		try{
			let formId = this.attr("id") || this.attr("name");
			if(this.length == 0){
				return rtnData;
			}

			let warpId = formId;
			if(this.prop("tagName") != 'FORM'){
				if(this.find("form").length){
					warpId = this.find("form").attr("id");
				}
				else {
					warpId = formId + '-wrapper';
					if($('#' + warpId).length == 0){
						this.wrap("<form id=\"" + warpId + "\"></form>");
						createWarp = true;
					}
				}
			}

			let disabledObjArr = [];
			$.each($("[disabled]"), function(idx, data){
				let obj = $(this);
				disabledObjArr.push(obj);
				obj.prop("disabled", false);
			});

			let checkBoxObjArr = [];
			$.each($("[type=checkbox][class*=fmt-yn]"), function(idx, data){
				let obj = $(this);
				checkBoxObjArr.push(obj);
				obj.prop("type", "hidden");
			});

			let escapeHtml = function(val){
				var map = {
					'&': '&amp;',
					'<': '&lt;',
					'>': '&gt;',
					'"': '&quot;',
					"'": '&#039;'
				};
				return val.replace(/[&<>"']/g, function(m){
					return map[m];
				});
			}

			let serialize = $('#' + warpId).serializeArray();
			$.each(serialize, function(){
				let key = this.name;
				let val = this.value;
				//val = escapeHtml(val);

				if($.isEmpty(key)){
					return true;
				}

				// formatter:
				if($.isNotEmpty(val)){
					// money 포맷제거
					if($("[name=" + key + "][class*=fmt-money]").length){
						val = val.replace(/\,/g, '');
					}
					// datepicker 포맷제거
					else if($("[name=" + key + "][data-format*=date]").length){
						val = val.replace(/[-:]/g, '');
					}
				}

				if(typeof rtnData[key] === "undefined"){
					if(isEmptyData === true){
						rtnData[key] = val;
					}
					else{
						if(val != ""){
							rtnData[key] = val;
						}
						else{
							if(key.startsWith("arr") || key.startsWith("list")){
								rtnData[key] = val;
							}
						}
					}
				}
				else{
					if(typeof rtnData[key] === "string"){
						let initData = rtnData[key];
						rtnData[key] = new Array();
						rtnData[key].push(initData);
						if(isEmptyData === true){
							rtnData[key].push(val);
						}
						else{
							if(val != ""){
								rtnData[key].push(val);
							}
							else{
								if(key.startsWith("arr") || key.startsWith("list")){
									rtnData[key].push(val);
								}
							}
						}
					}
					else{
						if(isEmptyData === true){
							rtnData[key].push(val);
						}
						else{
							if(val != ""){
								rtnData[key].push(val);
							}
							else{
								if(key.startsWith("arr") || key.startsWith("list")){
									rtnData[key].push(val);
								}
							}
						}
					}
				}
			});

			$.each(disabledObjArr, function(idx, obj){
				obj.prop("disabled", true);
			});
			$.each(checkBoxObjArr, function(idx, obj){
				obj.prop("type", "checkbox");
			});

			if(createWarp){
				this.unwrap();
			}
		}
		catch(e){
			console.error(e);
			throw e;
		}
		return rtnData;
	};

	// Form 데이터 설정
	$.fn.setFormData = function(data, prefixKey){

		try{
			let formId = this.attr("id") || this.attr("name");
			if(!(formId == "document" || formId == "body")){
				formId = "#" + formId;
			}
			if($(formId).length == 0){
				return;
			}

			// 데이터 초기화
			$(formId).resetFormData();

			// 데이터 설정
			var key, val;
			for(key in data){
				val = data[key], vtp = typeof (val);
				if(data.hasOwnProperty(key)){
					if(prefixKey != undefined){
						key = prefixKey + key;
					}

					// ID검색 후 없으면 Name검색
					var inputObj = $(formId).find("#" + key);
					if(inputObj.length == 0){
						inputObj = $(formId).find("[name=" + key + "]");
					}

					if(inputObj.length != 0){
						var tagName = inputObj.prop("tagName");
						if(tagName != undefined){

							tagName = tagName.toLowerCase();
							var type = inputObj.prop("type");

							// input 처리
							if(tagName == "input"){
								if(type == "text"){
									inputObj.val(val);
								}
								else if(type == "email"){
									inputObj.val(val);
								}
								else if(type == "hidden"){
									inputObj.val(val);
								}
								else if(type == "radio"){
									inputObj.filter("[value='" + val + "']").prop("checked", true).trigger("change");
								}
								else if(type == "checkbox"){
									// 단건체크
									if(inputObj.length == 1){
										if(val == "Y"){
											inputObj.prop("checked", true);
										}
										else{
											inputObj.prop("checked", false);
										}
									}
									// 멀티체크
									else if(inputObj.length > 1){
										inputObj.prop("checked", false);
										if($.isNotEmpty(val)){
											var values = val.split(",");
											$.each(inputObj, function(idx, el){
												if(values.includes(el.value)){
													$(el).prop("checked", true);
												}
											});
											// 전체체크
											if(inputObj.length == values.length){
												$("#" + key + "0").prop("checked", true);
											}
										}
									}
									else{
										inputObj.prop("checked", false);
									}
								}
							}
							// textarea 처리
							else if(tagName == "textarea"){
								inputObj.val(val);
							}
							// select box 처리
							else if(tagName == "select"){
								inputObj.val(val);
							}
							// td 처리
							else{
								inputObj.html(val);
							}
						}
					}
				}
			}
		}
		catch(e){
			console.error(e);
			throw e;
		}
	};

	// Form 데이터 초기화
	$.fn.resetFormData = function(){

		let formId = this.attr("id") || this.attr("name");
		if(!(formId == "document" || formId == "body")){
			formId = "#" + formId;
		}
		if($(formId).length == 0){
			return;
		}

		let includeHidden = true;
		let isReset = $(formId).clearForm(includeHidden);

		// 유효성체크 초기화
		$(formId).find('#valid-message').html(null);
		$(formId).find('.is-valid, .is-invalid').removeClass('is-valid').removeClass('is-invalid');

		// 첫번째값으로 설정
		$(formId).find('input,select').each(function(index, el){
			if(el.type == 'radio'){
				$(formId).find('[name='+el.name+']').eq(0).prop('checked', true);
			}
			else if(el.type == 'select' || el.type == 'select-one'){
				$(formId).find('[name='+el.name+']').find('option:first').prop('selected', true);
			}
		});
		return isReset;
	};

	// Checkbox Y/N 설정
	$.fn.setYN = function(){
		let _this = this;
		if(_this.is("[type=checkbox][class*=fmt-yn]")){
			if(_this.is(":checked")){
				_this.prop("value", "Y");
			}
			else{
				_this.prop("value", "N");
			}
		}
	};

	/* ----------------------------------------------------------------------------------
	 * Toasts message
	 * ----------------------------------------------------------------------------------
	 */
	$.toasts = function(options){

		if($.type(options) == 'string'){
			options = {
				class: 'bg-info',
				text: options
			}
		}
		else if($.type(options) == 'object'){
			// bg-success, bg-info, bg-warning, bg-danger, bg-maroon
			if(options.type == 'valid'){
				options = $.extend(true, {}, {
					class: 'bg-warning',
					icon: 'fa fa-exclamation-circle',
					title: 'Validation'
				}, options)
			}
			else {
				options.class = 'bg-'+options.type;
			}
		}

		if($.isNotEmpty(options.title)){
			options.close = true;
		} else {
			options.close = false;
		}

		$.toast({
			heading: 'Information',
			text: 'Loaders are enabled by default. Use `loader`, `loaderBg` to change the default behavior',
			icon: 'info',
			loader: true,        // Change it to false to disable loader
			loaderBg: '#9EC600'  // To change the background
		});


		// $(document).toasts('create', {
		// 	autoHide: true,
		// 	delay: 4000,
		// 	class: options.class,
		// 	close: options.close,
		// 	icon: options.icon,
		// 	title: options.title,
		// 	body: options.text
		// });
	}

	/* ----------------------------------------------------------------------------------
	 * jconfirm default
	 * ----------------------------------------------------------------------------------
	 */
	jconfirm.defaults = {
		title: '', //Hello
		titleClass: '',
		type: 'default',
		typeAnimated: true,
		draggable: true,
		dragWindowGap: 15,
		dragWindowBorder: false, // true - vertical-align: middle
		alignMiddle: true, //deprecated
		smoothContent: true,
		content: '',
		buttons: {},
		defaultButtons: {
			ok: {
				action: function(){
				}
			},
			close: {
				action: function(){
				}
			},
		},
		contentLoaded: function(data, status, xhr){
		},
		icon: '',
		lazyOpen: false,
		bgOpacity: null,
		theme: 'material', //light,dark,modern,supervan,material,bootstrap
		animation: 'scale', //right, left, bottom, top, rotate, none, opacity, scale, zoom, scaleY, scaleX, rotateY, rotateYR (reverse), rotateX, rotateXR
		closeAnimation: 'scale',
		animationSpeed: 400,
		animationBounce: 1,
		animateFromElement: false,
		escapeKey: false,
		rtl: false,
		container: 'body',
		containerFluid: true, //false
		backgroundDismiss: false,
		backgroundDismissAnimation: '', //shake, glow
		autoClose: false,
		closeIcon: true, //null
		closeIconClass: false,
		watchInterval: 100,
		columnClass: 'col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3 col-xs-10 col-xs-offset-1',
		boxWidth: '50%',
		scrollToPreviousElement: true,
		scrollToPreviousElementAnimate: true,
		useBootstrap: true,
		offsetTop: null,
		offsetBottom: null,
		bootstrapClasses: {
			container: 'container',
			containerFluid: 'container-fluid',
			row: 'row',
		},
		onContentReady: function(){
		},
		onOpenBefore: function(){
		},
		onOpen: function(){
		},
		onClose: function(){
		},
		onDestroy: function(){
		},
		onAction: function(){
		}
	};

	/* ----------------------------------------------------------------------------------
	 * DateTimePicker default
	 * ----------------------------------------------------------------------------------
	 */
	if($.fn.datetimepicker){
		$.fn.datetimepicker.Constructor.Default = $.extend(true, {}, $.fn.datetimepicker.Constructor.Default, {
			icons: {
				time: 'far fa-clock'
			},
			format: 'YYYY-MM-DD HH:mm:ss',
			locale: 'ko',
			debug: false,
			collapse: true,
			readonly: false,
			defaultDate: false,
			focusOnShow: true,
			allowInputToggle: false,
			buttons: {
				showToday: true,
				showClear: true,
				showClose: true
			}
		});
	}


	/* ----------------------------------------------------------------------------------
	 * Modal default
	 * ----------------------------------------------------------------------------------
	 */
	if($.fn.modal){
		$.fn.modal.Constructor.Default = $.extend(true, {}, $.fn.modal.Constructor.Default, {
			show: false,
			focus: true,
			keyboard: true,
			backdrop: true	//'static'
		});
	}

})(jQuery);

/**
 * ----------------------------------------------------------------------------------
 * Custom prototype
 * ----------------------------------------------------------------------------------
 */
$.extend(String.prototype, {
	comma: function(){
		let obj;
		if(this.toString() === ""){
			obj = "0";
		}
		else{
			obj = this.toString();
		}
		return obj.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	},
	phone: function(){
		let obj;
		if(this.toString() === ""){
			obj = "";
		}
		else{
			obj = this.toString();
		}
		return obj.replace(/(^02.{0}|^01.{1}|^[0-9]{3})([0-9]*)([0-9]{4})/, "$1-$2-$3");
	},
	lpad: function(padLen, padStr){
		let str = this + "";
		padStr += "";
		if(padStr.length > padLen) {
			return str;
		}
		while(str.length < padLen) {
			str = padStr + str;
		}
		str = str.length >= padLen ? str.substring(0, padLen) : str;
		return str;
	},
	rpad: function(padLen, padStr){
		let str = this + "";
		padStr += "";
		if(padStr.length > padLen) {
			return str;
		}
		while(str.length < padLen) {
			str += padStr;
		}
		str = str.length >= padLen ? str.substring(0, padLen) : str;
		return str;
	},
	split2: function(arg){
		if(this.toString() === ""){
			return [];
		}
		else{
			return this.toString().split(arg);
		}
	},
	settleSplit: function(arg){

		if(this.toString() === ""){
			return "";
		}
		else{
			return this.toString().split(arg);
		}
	},
	/**
	 * 전체 문자치환
	 */
	replaceAll: function(strAfter, strNext){

		if(this.toString() === ""){
			return "";
		}
		else{
			let changeStr = this.toString();
			while(changeStr.indexOf(strAfter) !== -1){
				changeStr = changeStr.replace(strAfter, strNext);
			}
			return changeStr;
		}
	}
});

$.extend(Number.prototype, {
	comma: function(){
		return this.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	},
	lpad: function(padLen, padStr){
		return this.toString().lpad(padLen, padStr);
	},
	rpad: function(padLen, padStr){
		return this.toString().rpad(padLen, padStr);
	},
	toFixedNumber: function(x, base){
		let pow = Math.pow(base||10, x);
		return Math.round(this*pow) / pow;
	},
	toFileSize: function(){
		if(this && this > 0) {
			var s = ['bytes', 'kB', 'MB', 'GB', 'TB', 'PB'];
			var e = Math.floor(Math.log(this) / Math.log(1024));
			return (this / Math.pow(1024, e)).toFixed(2) + " " + s[e];
		}
		else {
			return "0 bytes";
		}
	},
	/**
	 * 전체 문자치환
	 */
	replaceAll: function(strAfter, strNext){

		if(this.toString() === ""){
			return "";
		}
		else{
			let changeStr = this.toString();
			while(changeStr.indexOf(strAfter) !== -1){
				changeStr = changeStr.replace(strAfter, strNext);
			}
			return changeStr;
		}
	}
});

$.extend(Array.prototype, {
	contain: function(value){
		for(var i = 0; i < this.length; i++){
			if(this[i] == value){
				return true;
			}
		}
		return false;
	},
	contains: function(value){
		for(var i = 0; i < this.length; i++){
			if(this[i].includes(value) == true){
				return true;
			}
		}
		return false;
	}
});
