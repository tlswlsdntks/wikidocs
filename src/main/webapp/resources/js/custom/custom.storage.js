/**
 * ----------------------------------------------------------------------------------
 * Storage 공통스크립트
 *
 * - 같은 오리진 내에서만 공유된다. (프로토콜/서브도메인/포트 등이 다르면 데이터에 접근불가)
 * - 쿠키와 다르게 웹 스토리지 객체는 네트워크 요청 시 서버로 전송되지 않는다.
 * ----------------------------------------------------------------------------------
 */
custom.storage = {

	/*
	 * 로컬스토리지
	 * - 데이터는 모든 탭과 창에서 공유된다.
	 * - 브라우저나 OS가 재시작하더라도 데이터가 파기되지 않는다.
	 */
	local : {
		set : function(key, value){

			try{

				let storage = window.localStorage;

				if(typeof value === "object"){
					storage.setItem(key, JSON.stringify(value));
				}else if(typeof value === "string"){
					storage.setItem(key, value);
				}else{
					storage.setItem(key, value);
				}
			}catch(e){
				console.error("custom.storage.local set Error ->", e);
			}
		},
		get : function(key){

			try{

				let storageItem = window.localStorage.getItem(key);
				if(storageItem != null && storageItem.indexOf("{") == 0){
					return JSON.parse(storageItem);
				}else{
					return storageItem;
				}
			}catch(e){
				console.error("custom.storage.local get Error ->", e);
			}
		},
		remove : function(key){
			try{
				if(typeof key === "undefined"){
					window.localStorage.clear();
				}else{
					window.localStorage.removeItem(key);
				}
			}catch(e){
				console.error("custom.storage.local remove Error ->", e);
			}
		}
	},

	/*
	 * 세션스토리지
	 * - 같은 페이지라도 다른 탭에 있으면 공유되지 않는다.
	 * - 페이지를 새로 고침할 때 저장된 데이터는 사라지지 않지만, 탭을 닫고 새로 열 때는 사라진다.
	 */
	session : {
		set : function(key, value){

			try{

				let storage = window.sessionStorage;

				if(location.hostname === "localhost"){
					storage = window.localStorage;
				}

				if(typeof value === "object"){
					storage.setItem(key, JSON.stringify(value));
				}else if(typeof value === "string"){
					storage.setItem(key, value);
				}else{
					storage.setItem(key, value);
				}
			}catch(e){
				console.error("custom.storage.session set Error ->", e);
			}
		},
		get : function(key){

			try{

				let storageItem = window.sessionStorage.getItem(key);

				if(location.hostname === "localhost"){
					storageItem = window.localStorage.getItem(key);
				}

				if(storageItem != null && storageItem.indexOf("{") == 0){
					return JSON.parse(storageItem);
				}else{
					return storageItem;
				}
			}catch(e){
				console.error("custom.storage.session get Error ->", e);
			}
		},
		remove : function(key){

			try{
				let storage = window.sessionStorage;

				if(location.hostname === "localhost"){
					storage = window.localStorage;
				}

				if(typeof key === "undefined"){
					storage.clear();
				}else{
					storage.removeItem(key);
				}
			}catch(e){
				console.error("custom.storage.session remove Error ->", e);
			}
		}
	}
};