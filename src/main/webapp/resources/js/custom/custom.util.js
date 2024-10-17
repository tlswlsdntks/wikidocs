/**
 * ----------------------------------------------------------------------------------
 * 유틸 공통스크립트
 * ----------------------------------------------------------------------------------
 */
custom.util = {

	getParam: function(targetId){

		var isForm = false;
		var tagName = $("#" + targetId).prop("tagName");
		if(typeof tagName === "undefined"){
			return "";
		}
		tagName = tagName.toLowerCase();
		var targetForm = targetId

		if(tagName == "form"){
			isForm = true;
		}

		if(!isForm){
			targetForm = targetId + "Form";
			if($("#" + targetForm).length == 0){
				$("#" + targetId).wrap("<form id=\"" + targetForm + "\"></form>");
			}
		}

		var param = decodeURIComponent($("#" + targetForm).serialize().replace(/\+/g, "%20"));
		param = param.replace(/[^&]+=\.?(?:&|$)/g, '');

		if(!isForm){
			$("#" + targetForm).unwrap();
		}

		return param;
	},
	getJsonParam: function(targetId){

		var tid = targetId.replace(/#/gi, '');
		var $target = $('*[id=' + tid + ']');
		var $inputs = $target.find(':input:not(button)');
		//var $inputs = $target.find(':input:not([id^=ui-multiselect], button)');
		//$inputs = $('*[id='+masterId+']').find(':input:not([id^=ui-multiselect], button)');
		//$inputs = target.filter(':not(select:hidden)');
		var param = $inputs.serializeObject();
		return param;
	},
	toJson: function(data){

		if(typeof data === "object"){
			return data;
		}
		else{
			return JSON.parse(data);
		}
	},
	loading: function(isLoading, targetId){

		// 타겟이 그리드인 경우 그리드 api로 호출
		let grid = null;
		if(custom.grid && custom.grid.inst[targetId]){
			grid = custom.grid.inst[targetId];
		}

		if(isLoading){
			if(grid){
				grid.api.showLoadingOverlay();
			}
			else {
				$('.loading').show();
			}
		}
		else{
			if(grid){
				grid.api.hideOverlay();
			} else {
				$('.loading').hide();
			}
		}
	},
	/**
	 * AJAX
	 */
	ajax: function(options){

		if(typeof options.targetId == "undefined"){
			options.targetId = document;
		} else {
			options.targetId = options.targetId.replace(/#/gi, '');
		}
		if(typeof options.isLoading === "undefined"){
			options.isLoading = true;
		}
		if(typeof options.param === "undefined"){
			options.param = new Object();
		}
		$.each(options.param, function(idx, data){
			if($.type(data) === "array"){
				options.param[idx] = JSON.stringify(data);
			}
		});
		let param = (typeof options.param === "object") ? $.param(options.param) : options.param;

		var option = {
			url: options.url,
			data: param,
			cache: false,
			traditional: true,
			type: "post",
			dataType: "json",
			//contentType: "application/json; charset=UTF-8;",
			contentType: "application/x-www-form-urlencoded; charset=UTF-8;",
			success: function(result, status){
				if(result.rstCd == 0){
					if(typeof options.callback === "function"){
						if(options.targetId){
							custom.util.loading(false, options.targetId);
						}
						options.callback(result);
					}
				}
				else{
					$.alert({
						type: 'red',
						title: 'Error',
						titleClass: 'text-danger',
						icon: 'fas fa-exclamation-triangle',
						escapeKey: 'ok',
						content: result.rstMsg
					});
				}
			},
			error: function(req, status, error){
				if(req.status === 500){
					console.error("500: Internal Server Error");
				}
			},
			beforeSend: function(xhr){
				xhr.setRequestHeader("ajax", true);
				if(options.isLoading){
					custom.util.loading(true, options.targetId);
				}
			},
			complete: function(){
				if(options.isLoading){
					custom.util.loading(false);
				}
			}
		};
		$.ajax(option);
	},
	ajaxJson: function(options){

		if(typeof options.isLoading === "undefined"){
			options.isLoading = true;
		}
		if(typeof options.param === "undefined"){
			options.param = new Object();
		}
		let param = JSON.stringify(options.param);

		var option = {
			url: options.url,
			data: param,
			cache: false,
			traditional: true,
			type: "post",
			dataType: "json",
			contentType: "application/json; charset=UTF-8;",
			success: function(result){
				if(result.rstCd == 0){
					if(typeof options.callback === "function"){
						options.callback(result);
					}
				}
				else{
					$.alert({
						type: 'red',
						title: 'Error',
						titleClass: 'text-danger',
						icon: 'fas fa-exclamation-triangle',
						escapeKey: 'ok',
						content: result.rstMsg
					});
				}
			},
			error: function(req, status, error){
				if(req.status === 500){
					console.error("500: Internal Server Error");
				}
			},
			beforeSend: function(xhr){
				xhr.setRequestHeader("ajax", true);
				if(options.isLoading){
					//custom.util.loading(true);
				}
			},
			complete: function(){
				if(options.isLoading){
					//custom.util.loading(false);
				}
			}
		};
		$.ajax(option);
	},
	ajaxForm: function(options){

		let formId = options.formId.replace(/#/gi, '');
		let $form = $('#' + formId);
		if($form.length == 0) return;

		let warpId = formId;
		let createWarp = false;
		if($form.prop("tagName") != 'FORM'){
			if($form.find("form").length){
				warpId = $form.find("form").attr("id");
			}
			else {
				warpId = formId + '-wrapper';
				if($('#' + warpId).length == 0){
					$form.wrap("<form id=\"" + warpId + "\" onsubmit=\"return false;\"></form>");
					createWarp = true;
				}
			}
		}

		let formData = new FormData(document.getElementById(warpId));
		if(createWarp){
			$form.unwrap();
		}

		var option = {
			url: options.url,
			data: formData,
			type: "post",
			dataType: "json",
			processData: false,
			contentType: false,
			//contentType: "multipart/form-data",
			success: function(result, status){
				if(result.rstCd == 0){
					if(typeof options.callback === "function"){
						options.callback(result);
					}
				}
				else{
					$.alert({
						type: 'red',
						title: 'Error',
						titleClass: 'text-danger',
						icon: 'fas fa-exclamation-triangle',
						escapeKey: 'ok',
						content: result.rstMsg
					});
				}
			},
			error: function(req, status, error){
				if(req.status === 500){
					console.error("500: Internal Server Error");
				}
			},
			beforeSend: function(xhr){
				xhr.setRequestHeader("ajax", true);
				if(options.isLoading){
					//custom.util.loading(true);
				}
			},
			complete: function(){
				if(options.isLoading){
					//custom.util.loading(false);
				}
			}
		};
		$.ajax(option);
	},
	/**
	 * AXIOS
	 */
	axios: function(options){

		if(typeof options.isLoading === "undefined"){
			options.isLoading = true;
		}
		if(typeof options.param === "undefined"){
			options.param = new Object();
		}

		var option = {
			url: options.url,
			method: options.method||'post',
			baseURL: '',
			transformRequest: [function(data, headers){
				// Do whatever you want to transform the data
				return data;
			}],
			transformResponse: [function(data){
				// Do whatever you want to transform the data
				return JSON.parse(data);
			}],
			headers: {
				'X-Requested-With': 'XMLHttpRequest'
			},
			params: options.param,
			paramsSerializer: null,
			data: options.data,
			timeout: 0,
			withCredentials: false,
			adapter: null,
			auth: {},
			// options are: 'arraybuffer', 'document', 'json', 'text', 'stream'
			responseType: 'json',
			responseEncoding: 'utf8',
			xsrfCookieName: 'XSRF-TOKEN',
			xsrfHeaderName: 'X-XSRF-TOKEN',
			onUploadProgress: function(progressEvent){
				// Do whatever you want with the native progress event
			},
			onDownloadProgress: function(progressEvent){
				// Do whatever you want with the native progress event
			},
			customContentLength: 2000,
			customBodyLength: 2000,
			validateStatus: function(status){
				return status >= 200 && status < 300; // default
			},
			customRedirects: 5,
			socketPath: null,
			/*
			httpAgent: new http.Agent({
				keepAlive: true
			}),
			httpsAgent: new https.Agent({
				keepAlive: true
			}),
			proxy: {
				protocol: 'http',
				host: '127.0.0.1',
				port: 8080,
				auth: {
					username: '',
					password: ''
				}
			},
			cancelToken: new CancelToken(function(cancel){
			}),
			*/
			decompress: true
		};

		axios(option)
		.then(function(response){
			// handle success
			if(typeof response.data === 'object'){
				let data = response.data;
				if(data.rstCd == 0){
					if(typeof options.callback === "function"){
						options.callback(data);
					}
				}
				else{
					$.alert({
						type: 'red',
						title: 'Error',
						titleClass: 'text-danger',
						icon: 'fas fa-exclamation-triangle',
						escapeKey: 'ok',
						content: result.rstMsg
					});
				}
			}
		})
		.catch(function(error){
			// handle error
			console.error(error);
		})
		.then(function(){
			// always executed
		});
	},
	movePage: function(options){

		if(typeof options === "string"){
			let url = options;
			options = {
				url: url,
				params: {}
			};
		}

		if(options.url == ""){
			alert("이동할 URL이 설정되지 않았습니다.");
			return;
		}

		$("#nextForm").unwrap();
		var strHtml = "<form id=\"nextForm\" name=\"nextForm\" method=\"get\" action=\"" + options.url + "\"></form>";
		$("body").append(strHtml); // 화면에 form 등 생성
		if(typeof options.params != "undefined"){
			var paramStr = "";
			$.each(options.params, function(index, item){
				type = typeof (item);
				if(type === "string"){
					paramStr += "<input type=\"hidden\" id=\"" + index + "\" name=\"" + index + "\" value=\"" + item + "\"/>";
				}
				else{
					$("#nextForm").append("<input type=\"hidden\" id=\"" + index + "\" name=\"" + index + "\"/>");
					$("#" + index).val(JSON.stringify(item));

				}
			});
			$("#nextForm").append(paramStr);
		}
		$("#nextForm").submit();
	},
	enter: function(options){

		let $trigger = $('#' + options.trigger.replace(/#/gi, ''));
		if($trigger.length == 0) return;

		$.each(options.target, function(idx, item){
			let $target = $('#' + item.replace(/#/gi, ''));
			$target.unbind().bind("keydown", function(event){
				if(event.keyCode == 13){
					$trigger.click();
				}
			});
		});
	},
	downloadFile: function(options){

		let url = "";
		if($.isEmpty(options.url)){
			url = "/file/download";
		}
		let html = [];
		html.push('<form id="downForm" name="downForm" method="post" action="' + url + '">');
		$.map(options.param, function(val, key){
			html.push('  <input type="hidden" id="' + key + '" name="' + key + '" value="' + val + '" />');
    	});
		html.push('</form>');

		// 화면에 동적 form 생성
		$('#downForm').remove();
		$('body').append(html.join(''));
		$('#downForm').submit();
		$('#downForm').remove();
	}
};


/* ----------------------------------------------------------------------------------
 * Axios Interceptor
 * ----------------------------------------------------------------------------------
 */
// axios 전역설정
axios.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
axios.defaults.headers.common['Access-Control-Allow-Credentials'] = true;
axios.defaults.withCredentials = true;

// 요청 인터셉터
axios.interceptors.request.use(
	function(config){
		return config;
	},
	function(error){
		return Promise.reject(error);
	}
);

// 응답 인터셉터
axios.interceptors.response.use(
	function(response){
		return response;
	},
	function(error){
		return Promise.reject(error);
	}
);
