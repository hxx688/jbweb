function shangchuan(inputId,imgId){
	layer.open({
//		  id :imgUrl,
		  type: 2,
		  area: ['700px', '530px'],
		  fixed: false, //不固定
		  maxmin: true,
		  content: BladeApp.ctxPath+'/attach/add',
		  success: function(layero, index){
		    var imgUrlBackId = layer.getChildFrame('#imgUrlBackId', index);
		    var imgUrlBackImg = layer.getChildFrame('#imgUrlBackImg', index);
//		    console.log(body.html()) //得到iframe页的body内容
//		    var imgUrlBackId = $("#imgUrlBackId");
		    imgUrlBackId.val(inputId)
		    imgUrlBackImg.val(imgId)
		  }
	});
	
}