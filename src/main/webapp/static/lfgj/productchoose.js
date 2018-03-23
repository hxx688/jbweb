function choose(){
		var index = window.top.layer.open({
		    type: 2,
		    title: "产品选择",
		    area: ['1000px', '520px'],
		    fix: false, //不固定
		    maxmin: true,
		    content: BladeApp.ctxPath + "/product/lookupList",
		    btn: ['确定', '取消', '清空']
		  	,yes: function(index, layero){
				var iframeWin = window.top[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
				var rowData = iframeWin.select();
				if(rowData){
					$("#_product_id").val(rowData.id);
					$("#_product_name").val(rowData.product_name);
					$("#_diancs").val(rowData.diancs);
					$("#_code_name").val(rowData.code);
				}
				window.top.layer.close(index);
		  	}	
		 	,btn2: function(index, layero){
		    //按钮【按钮二】的回调
		    
		    //return false 开启该代码可禁止点击该按钮关闭
		  	}
		 	,btn3: function(index, layero){
		 		$("#_product_id").val('');
				$("#_product_name").val('');
//				$("#_adjust").val('');
				$("#_code_name").val('');
				window.top.layer.close(index);
			  }
		  	,cancel: function(){ 
		    //右上角关闭回调
		    
		    //return false 开启该代码可禁止点击该按钮关闭
		  	}
		});
	}