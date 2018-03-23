function choose(){
		var index = window.top.layer.open({
		    type: 2,
		    title: "人员选择",
		    area: ['1000px', '520px'],
		    fix: false, //不固定
		    maxmin: true,
		    content: BladeApp.ctxPath + "/member/lookupList",
		    btn: ['确定', '取消', '清空']
		  	,yes: function(index, layero){
				var iframeWin = window.top[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
				var rowData = iframeWin.select();
				if(rowData){
					$("#_parent_id").val(rowData.id);
					$("#_parent_name").val(rowData.real_name);
				}
				window.top.layer.close(index);
		  	}	
		 	,btn2: function(index, layero){
		    //按钮【按钮二】的回调
		    
		    //return false 开启该代码可禁止点击该按钮关闭
		  	}
		 	,btn3: function(index, layero){
		 		$("#_parent_id").val('');
				$("#_parent_name").val('');
				window.top.layer.close(index);
			  }
		  	,cancel: function(){ 
		    //右上角关闭回调
		    
		    //return false 开启该代码可禁止点击该按钮关闭
		  	}
		});
	}