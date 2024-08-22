"use strict";

/**
 * custom 공통스크립트
 */
let custom = {};
let my_modal = null;

/* ----------------------------------------------------------------------------------
 * UI 공통스크립트
 * ----------------------------------------------------------------------------------
 */
custom.ui = {
	init: function(){

		let _this = this;
		if($(".main-header").length > 0){
			_this.headInit();
		}
		if($(".main-sidebar").length > 0){
			_this.menuInit();
		}

		// 화면초기화 함수 호출전 동기화가 필요한 부분은 Promise 함수로 작성하여 추가
		Promise.all([this.setCodeList()]).then(function(){

			// Input 초기화
			_this.inputInit();

			if(typeof initFunction === "function"){
				if(typeof result === "object" && result != null){
					initFunction(result);
				}
				else{
					initFunction();
				}
			}
		});
	},
	headInit: function(){

		// 모달: 계정정보
		my_modal = {
			elem: null,
			init: function(){

				// 운영자관리 화면인경우 기존모달 사용
				if(location.pathname === "/system/operrMngList"){
					$("#my-info").remove();
					return;
				}

				this.elem = $("#my-info").modal({
					show: false
				})
				.on("show.bs.modal", function(e){
				})
				.on("shown.bs.modal", function(e){
				})
				.on("hidden.bs.modal", function(e){
				})
				.draggable({
					handle: ".draggable-handler"
				});

				// 유효성
				$(my_modal.elem).find("form").validate({
					rules: {
						lginPwd: {
							required: false
						},
						lginPwd2: {
							required: false,
							equalTo: "#lginPwd"
						},
						email: {
							email: true
						}
					},
					messages: {
						lginPwd: {
							required: "비밀번호를 입력해주세요."
						},
						lginPwd2: {
							required: "비밀번호확인을 입력해주세요.",
							equalTo: "비밀번호가 일치하지 않습니다."
						},
						email: {
							email: "Email 형식으로 입력해주세요."
						}
					}
				});

				// 버튼: 모달저장
				$("#btnMySave").unbind().bind("click", function(){
					my_modal.save();
				});
			},
			// 모달열기
			show: function(id){
				if($.isNotEmpty(id)){
					custom.util.ajax({
						url: "/system/data/selectOperrView",
						param: {
							operrId: id
						},
						callback: function(result){
							var data = result.rstData;
							$(my_modal.elem).setFormData(data);
						}
					});
				}
				else {
					$(my_modal.elem).resetFormData();
				}
				my_modal.elem.modal("show");
			},
			// 모달닫기
			hide: function(){
				my_modal.elem.modal("hide");
			},
			// 모달저장
			save: function(){

				var formValid = $(my_modal.elem).find("form").valid();
				if( formValid === true ){
					var p = $(my_modal.elem).getFormData();
					custom.util.ajax({
						url: "/system/data/updateOperrMng",
						param: p,
						callback: function(result){
							if(result.rstCd == 0){
								my_modal.hide();
							}
						}
					});
				}
			}
		};
		my_modal.init();

		// 버튼: 계정정보
		$("#btnMyInfo").unbind().bind("click", function(){
			var id = $(this).data("id");
			// 운영자관리 화면인경우 기존모달 사용
			if(location.pathname === "/system/operrMngList"){
				modal.show(id);
			} else {
				my_modal.show(id);
			}
		});
	},
	menuInit: function(){

		// Initialize nav
		$('[role=menu]').find('.active').removeClass('active');

		// active nav
		let arrMenu = [], actMenu1, actMenu2;
		let locPath = location.pathname;
		let actLink = $('a[href="'+locPath+'"]').addClass('active');
		arrMenu.unshift(actLink.text().trim());

		let parents = actLink.parents('.nav-item');
		if (parents.length == 2) {
			actMenu1 = parents.eq(1).addClass('menu-open').find('a:first').addClass('active');
			arrMenu.unshift(actMenu1.text().trim());
		} else if(parents.length == 3) {
			actMenu1 = parents.eq(1).addClass('menu-open').find('a:first');
			arrMenu.unshift(actMenu1.text().trim());
			actMenu2 = parents.eq(2).addClass('menu-open').find('a:first').addClass('active');
			arrMenu.unshift(actMenu2.text().trim());
		}

		// breadcrumb nav
		let $breadcrumb = $('.main-header .breadcrumb');
		$breadcrumb.find('li:gt(0)').remove();
		if(!(locPath == '/' || locPath == '/main' || locPath == '/home')){
			$.each(arrMenu, function(idx, menu){
				$breadcrumb.append('<li class="breadcrumb-item">'+menu+'</li>');
			});
		}
	},
	inputInit: function(target){

		try{
			if(target === undefined){
				target = document;
			}

			// 타입별 플러그인 공통적용
			this.inputBasic(target);
			this.inputCodeData(target);
			this.inputCheckbox(target);
			this.inputSelectbox(target);
			this.inputFormatter(target);
			this.inputDateTime(target);
		}
		catch(e){
			console.error(e);
		}
	},
	inputBasic: function(target){

		// Initialize autocomplete
		$.each($(target).find("input:text,input[type=search],input[type=email]"), function(){
			$(this).prop("autocomplete", "off");
		});
	},
	inputCodeData: function(target){

		// Initialize data-code
		$.each($(target).find('[data-code]'), function(){
			let data = $(this).data();
			let codeList = custom.storage.session.get("codeList");
			if (codeList != null) {

				$(this).empty();
				let codes = codeList[data.code];
				//console.log('codes:', codes);

				let filters = [];
				if(data.filter){
					filters = String(data.filter).split(',');
				}

				let tabindex = null;
				if(data.tabindex){
					tabindex = Number(data.tabindex);
				}

				switch(data.type) {
					case 'radio':
						let html = [], index = 1;
						if(data.all) {
							index = 0;
							html.push('<div class="custom-control custom-radio">');
							html.push('  <input class="custom-control-input" type="radio" id="'+data.name+'-'+index+'" name="'+data.name+'" value="" tabindex="'+tabindex+'">');
							html.push('  <label for="'+data.name+'-'+index+'" class="custom-control-label">'+data.all+'</label>');
							html.push('</div>');
							index++;
						}
						$.each(codes, function(key, val){
							if($.isArray(filters) && filters.length > 0){
								if(!filters.contain(key)){
									return true;
								}
							}
							html.push('<div class="custom-control custom-radio">');
							html.push('  <input class="custom-control-input" type="radio" id="'+data.name+'-'+index+'" name="'+data.name+'" value="'+key+'" tabindex="'+tabindex+'">');
							html.push('  <label for="'+data.name+'-'+index+'" class="custom-control-label">'+val+'</label>');
							html.push('</div>');
							index++;
						});
						$(this).append(html.join('')).find('[name='+data.name+']').eq(0).prop('checked', true);
						break;

					case 'select':
						let $select = $('<select>').appendTo(this).wrap('<div class="input-select">').attr({
							id: data.name,
							name: data.name,
							'class': 'form-control form-control-sm',
							tabindex: tabindex
						});
						if(data.all) {
							$select.append($('<option>').val('').text(data.all));
						}
						$.each(codes, function(key, val){
							if($.isArray(filters) && filters.length > 0){
								if(!filters.contain(key)){
									return true;
								}
							}
							$select.append($('<option>').val(key).text(val));
						});
						break;

					case 'checkbox':
						console.log('checkbox');
						break;

					default:
						break;
				}
			}
		});
	},
	inputCheckbox: function(target){

		$.each($(target).find("[type=checkbox][class*=fmt-yn]"), function(){
			let checkObj = $(this);
			checkObj.setYN();
			checkObj.unbind().bind({
				click: function(e){
					$(this).setYN();
				}
			});
		});
	},
	inputSelectbox: function(target){
	},
	inputFormatter: function(target){

		/*
		$.each($(target).find("[type=text][class*=fmt]"), function(){
			let fmtClass = $(this).attr("class").split(" ").filter(function(clazz){
				return (clazz.indexOf("fmt") != -1);
			})[0];
			let fmtType = fmtClass.replace("fmt-", "");

			if(fmtType == "date"){
			}
			else if(fmtType == "time"){
			}
			else if(fmtType == "number"){
			}
			else if(fmtType == "float"){
			}
			else if(fmtType == "money"){
			}
			else if(fmtType == "money(float)"){
			}
			//$(this).attr("data-ax5formatter", fmtType).ax5formatter();
		});*/

		$.each($(target).find("[type=text]").filter('[data-format]'), function(){

			let $input = $(this);
			let format = $input.data('format');

			if(format == "integer"){
				$input.inputmask('integer', {
					rightAlign: false,
					allowMinus: false
				});
			}
			else if(format == "decimal"){
				$input.inputmask('decimal', {
					rightAlign: false,
					allowMinus: false
				});
			}
			else if(format == "money"){
				$input.inputmask('integer', {
					rightAlign: false,
					allowMinus: false,
					groupSeparator: ','
				});
			}
			else if(format == "percent"){
				$input.inputmask('integer', {
					rightAlign: false,
					allowMinus: false,
					digits: 1,
	                digitsOptional: !1,
	                radixPoint: ".",
	                placeholder: "0",
	                autoGroup: !1,
	                min: 0,
	                custom: 100,
	                suffix: " %"
				});
			}
			else if(format == 'regex'){
				//$input.inputmask('Regex', { regex: "^[1-9][0-9]?$|^100$" });
			}
		});
	},
	inputDateTime: function(target){

		// datetimepicker
		$.each($(target).find("[type=text]").filter('[data-format*=date],[data-format*=time]'), function(){

			let $input = $(this);
			if($input.data('datetimepicker')) return;

			let options = {
				width: 0,
				format: $input.data('format')
			}
			if(options.format == 'date'){
				options = {
					width: 130,
					format: 'YYYY-MM-DD',
					buttonClass: 'fa fa-calendar'
				}
				$input.inputmask({
					alias: 'datetime',
					placeholder: '_',
					inputFormat: 'yyyy-mm-dd',
					clearIncomplete: true,
					clearMaskOnLostFocus: false
				});
			}
			else if(options.format == 'time'){
				options = {
					width: 120,
					format: 'HH:mm:ss',
					buttonClass: 'fa fa-clock'
				}
				$input.inputmask({
					alias: 'datetime',
					placeholder: '_',
					inputFormat: 'HH:MM:ss',
					clearIncomplete: true,
					clearMaskOnLostFocus: false
				});
			}
			else if(options.format == 'datetime'){
				options = {
					width: 180,
					format: 'YYYY-MM-DD HH:mm:ss',
					buttonClass: 'fa fa-calendar'
				}
				$input.inputmask({
					alias: 'datetime',
					placeholder: '_',
					inputFormat: 'yyyy-mm-dd HH:MM:ss',
					clearIncomplete: true,
					clearMaskOnLostFocus: false
				});
			}
			let $group = $input.wrap('<div class="input-group"></div>').parent();
			$group.css({
				width: options.width
			});

			let button = [];
			button.push('<div class="input-group-append" data-target="' + '#' + $input.attr('id') + '" data-toggle="datetimepicker">');
			button.push('  <div class="input-group-text"><i class="'+ options.buttonClass +'"></i></div>');
			button.push('</div>');
			$input.after(button.join('')).addClass('datetimepicker-input');
			$input.datetimepicker(options);
		});
	},
	setDateTimePicker: function(options){

		/*
		let inputObj = $(options.target).prev("input");
		//inputObj.prop("autocomplete", "off");
		//inputObj.prop("readonly", true);
		inputObj.datetimepicker(options);

		$(options.target).unbind().bind("click", function(){
			inputObj.focus();
		});
		*/
	},
	setCodeList: function(){
		return new Promise(function(resolve, reject) {

			resolve();

			// 공통코드 목록
			// let codeList = custom.storage.session.get("codeList");
			// if (codeList == null) {
			//
			// 	custom.util.ajax({
			// 		url: "/system/data/selectCodeList",
			// 		param: {
			// 			useYn: 'Y'
			// 		},
			// 		callback: function(result){
			// 			if(result.rstCd == 0){
			// 				codeList = {};
			// 				$.each(result.rstData, function(idx, data){
			// 					if(!codeList[data.cdClsId]){
			// 						codeList[data.cdClsId] = {};
			// 					}
			// 					codeList[data.cdClsId][data.cdId] = data.cdNm;
			// 				});
			// 				custom.storage.session.set("codeList", codeList);
			// 				resolve();
			// 			}
			// 			else {
			// 				console.error(result.rstMsg);
			// 				reject();
			// 			}
			// 		}
			// 	});
			// }
			// else {
			// 	resolve();
			// }
		});
	}
}

//-----------------------------------------------------------------------------------
// custom window native alert & confirm
//-----------------------------------------------------------------------------------
/*
try{
	window.winAlert = alert;
	window.winConfirm = confirm;
}catch(e){
	console.error(e);
};

window.alert = function(options, callback){
};

window.confirm = function(options, callback){
};
*/

$(document).ready(function(){

	// UI 초기화
	custom.ui.init();
});

//--------------------------------------------------------------------------------------------
// Notification
//--------------------------------------------------------------------------------------------
let notify = {
	info: function(text, callback){
		var option = {
			type: 'info',
			text: text,
			callback: callback
		};
		this.common(option);
	},
	warn: function(text, callback){
		var option = {
			type: 'warning',
			text: text,
			callback: callback
		};
		this.common(option);
	},
	alert: function(text, callback){
		var option = {
			type: 'success',
			text: text,
			callback: callback
		};
		this.common(option);
	},
	error: function(text, callback){
		var option = {
			type: 'danger',
			text: text,
			callback: callback
		};
		this.common(option);
	},
	common: function(option){
	}
};

//--------------------------------------------------------------------------------------------
// System
//--------------------------------------------------------------------------------------------
let system = {

	// 시스템정보
	info: function(){
	}
}

//--------------------------------------------------------------------------------------------
// ComboBox
//--------------------------------------------------------------------------------------------
let combo = {

	ajax: function(option){
	}
};

//--------------------------------------------------------------------------------------------
// RadioBox
//--------------------------------------------------------------------------------------------
let radio = {

	ajax: function(option){
	}
};
