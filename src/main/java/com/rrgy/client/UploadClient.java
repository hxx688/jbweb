package com.rrgy.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.base.controller.BladeController;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.file.BladeFile;
import com.rrgy.core.toolbox.file.BladeFileKit;
import com.rrgy.core.toolbox.kit.JsonKit;

@Controller
@RequestMapping("/")
public class UploadClient extends BladeController{

	@ResponseBody
	@RequestMapping("uploadImage")
	public String uploadImage(@RequestParam("imgFile") CommonsMultipartFile[] files){
		ResultVo rv = new ResultVo();
		if (null == files) {
			rv.setReturnCode("1");
			rv.setReturnMsg("请选择要上传的图片");
			return JsonKit.toJson(rv);
		}
		String url = "";
		for(int i=0;i<files.length;i++){
			MultipartFile file = files[i]; 
			String originalFileName = file.getOriginalFilename();
			// 测试后缀
			boolean ok = BladeFileKit.testExt("image", originalFileName);
			if (!ok) {
				rv.setReturnCode("1");
				rv.setReturnMsg("上传文件的类型不允许");
				return JsonKit.toJson(rv);
			}
			BladeFile bf = getFile(file);
			bf.transfer();
			url += ","+ bf.getUploadVirtualPath();
		}
		if(!Func.isEmpty(url)){
			url = url.substring(1);
		}
		return url;	
	}
}
