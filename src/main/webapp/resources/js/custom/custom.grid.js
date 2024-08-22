/**
 * ----------------------------------------------------------------------------------
 * Grid 공통스크립트
 * ----------------------------------------------------------------------------------
 */
custom.grid = {
	/*
	 * Grid 인스턴스
	 */
	inst: {
	},
	/*
	 * Grid 사이즈(페이지)
	 */
	size: 10,
	/*
	 * Grid 초기화
	 */
	init : function(options, param){

		const targetId = options.targetId.replace(/#/gi, '');
		const $grid = $('#'+targetId);

		if($grid.length == 0){
			console.error("Grid [" + targetId + "] TARGET ID가 없습니다.");
			return false;
		}else if($grid.length > 1){
			console.error("Grid [" + targetId + "] TARGET ID가 중복됩니다.");
			return false;
		}

		// COLUMNDEFS
		let codeList = custom.storage.session.get('codeList');
		options.columnDefs.forEach((col, idx) => {

			// 공통코드 적용
			if($.isNotEmpty(col.code)){
				let valueFormatter = function(params){
					let value = params.value;
					if(value){
						value = value.trim();
					}
					if(codeList[col.code]){
						value = $.nvl(codeList[col.code][value], value);
					}
					return value;
				}
				col.valueFormatter = valueFormatter;
			}
			// Align
			if($.isNotEmpty(col.cellStyle)){
				if(col.cellStyle['textAlign'] == 'center'){
					col.headerClass = 'ag-center-header';
				}
				else if(col.cellStyle['textAlign'] == 'right'){
					col.headerClass = 'ag-right-header';
				}
			}
		});

		// Default PageSize
		if(options.paging){
			options.pagination = true;
		}
		if(options.pagination === true){
			if($.isEmpty(options.paginationPageSize)){
				options.paginationPageSize = this.size;
			}
		}

		// Paging: SERVER-SIDE
		if(options.paging && $.isNotEmpty(options.paging.url)){

			options.rowModelType = 'infinite';
			options.cacheBlockSize = options.paginationPageSize;
			options.cacheOverflowSize = 0;
			//options.customBlocksInCache = 10;
			options.infiniteInitialRowCount = -1;
			options.serverSideInfiniteScroll = false;

			options.paging = $.extend(true, {
				url: null,
				param: {},
				dataSource: {
					rowCount: null,
					getRows: function(params) {

						//console.log('paging.getRows.param:', params);
						let grid = custom.grid.inst[targetId];
						let pageSize = grid.api.paginationGetPageSize();
						let pageNumber = Math.round(params.endRow / pageSize);
						grid.paging.param.pageSize = pageSize;
						grid.paging.param.pageNumber = pageNumber;

						custom.util.ajax({
							targetId: targetId,
							url: grid.paging.url,
							param: grid.paging.param,
							callback: function(result){
								if(result.rstCd == 0){
									var data = result.rstData;
									if(data && data.length > 0){
										grid.api.hideOverlay();
										params.successCallback(data, Number(data[0].cnt));
									} else {
										grid.api.showNoRowsOverlay();
										params.successCallback([], 0);
									}
									//params.success({ rowData: data, rowCount: -1 });

									if($.isFunction(grid.paging.callback)){
										grid.paging.callback(params, data);
									}
								}
								else {
									params.fail();
								}
							}
						});

					}
				},
				clear: function(){

					let grid = custom.grid.inst[targetId];
					grid.api.setDatasource({
						rowCount: null,
						getRows: function(params) {
							grid.api.showNoRowsOverlay();
							params.successCallback([], 0);
						}
					});
				},
				search: function(param){

					let grid = custom.grid.inst[targetId];
					grid.paging.param = param||{};
					grid.api.setDatasource(this.dataSource);
					//grid.api.setServerSideDatasource(this.dataSource);
				},
				goto: function(pageNumber){

					let grid = custom.grid.inst[targetId];
					grid.api.paginationGoToPage(Number(pageNumber)-1);
				},
				callback: null
			}, options.paging);
		}

		// EVENTS
		let onGridReady = function(event){
			// 전체열 자동조절
			//event.columnApi.columnModel.autoSizeAllColumns();
			//event.api.sizeColumnsToFit();

			if($.isPlainObject(options.paging)){
				let grid = custom.grid.inst[targetId];
				grid.paging.clear()
			}
			if($.isFunction(options.onGridReady)){
				options.onGridReady(event);
			}
		};
		let onGridSizeChanged = function(event){
			// 전체열 자동조절
			//event.columnApi.columnModel.autoSizeAllColumns();
			//event.api.sizeColumnsToFit();

			if($.isFunction(options.onGridSizeChanged)){
				options.onGridSizeChanged(event);
			}
		};
		let onRowClicked = function(event){
			if($.isFunction(options.onRowClicked)){
				options.onRowClicked(event);
			}
		};
		let onRowSelected = function(event){
			if($.isFunction(options.onRowSelected)){
				options.onRowSelected(event);
			}
		};

		// 스타일 적용
		$grid.addClass('ag-theme-alpine').css({ width: '100%', height: 536});
		if(options.width){
			$grid.css({ width: options.width });
		}
		if(options.height){
			$grid.css({ height: options.height });
		}

		if(!options.rowData) options.rowData = [];
		const gridOptions = $.extend(true, {}, {
			// OPTIONS
			// -----------------------------------------
			debug: false,
			columnDefs: options.columnDefs,
			defaultColDef: {
				flex: 0,
				sortable: false,
				resizable: true,
				lockPosition: false,
				//enableValue: true,
				//enablePivot: true,
				//enableRowGroup: true,
			},
			rowData: options.rowData,
			animateRows: false,
			enableFilter: false,
			enableSorting: false,
			enableColResize: true,
			pagination: options.pagination||true,
			paginationPageSize: options.paginationPageSize||this.size,
			paginationAutoPageSize: false,
			rowModelType: 'clientSide', // [clientSide, serverSide, infinite, viewport]
			rowSelection: 'single', // [single, multiple]
			rowMultiSelectWithClick: true,
			suppressRowClickSelection: false,
			suppressMovableColumns: false,
			overlayNoRowsTemplate: '조회할 내용이 없습니다',
			//domLayout: 'autoHeight',
			// OPTIONS(Enterprise features)
			// -----------------------------------------
			sideBar: false,
			masterDetail: false,
			enableRangeSelection: true,
			suppressMultiRangeSelection: true,
			// EVENTS
			// -----------------------------------------
			onGridReady: event => null,
			onGridSizeChanged: event => null,
			onRowClicked: event => null,
			onRowSelected: event => null,
			onColumnResized: event => null,
			// CALLBACKS
			// -----------------------------------------
			onCellClicked: params => null,
			getRowHeight: params => {
				return null;
			},
		}, options);

		// EVENTS
		gridOptions.onGridReady = onGridReady;
		gridOptions.onGridSizeChanged = onGridSizeChanged;
		gridOptions.onRowClicked = onRowClicked;
		gridOptions.onRowSelected = onRowSelected;

		const gridDiv = document.querySelector('#'+targetId);
	    const gridInst = new agGrid.Grid(gridDiv, gridOptions);
	    this.inst[targetId] = gridInst.gridOptions;
	    return this.inst[targetId];
	},
	search : function(param, callback){
	},
};
