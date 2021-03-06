package com.lfgj.product.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lfgj.product.model.Product;
import com.lfgj.product.service.ProductService;
import com.lfgj.product.util.ProductListCacheUtil;
import com.lfgj.util.CommKit;
import com.lfgj.util.LfConstant;
import com.rrgy.common.base.BaseController;
import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.ajax.AjaxResult;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.JsonKit;

/**
 * Generated by Blade.
 * 2017-09-09 11:53:09
 */
@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {
	private static String CODE = "product";
	private static String PREFIX = "dt_product";
	private static String LIST_SOURCE = "product.list";
	private static String BASE_PATH = "/product/";
	
	@Autowired
	ProductService service;
	
	@RequestMapping(KEY_MAIN)
	public String index(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "product.html";
	}
	
	@RequestMapping("/lookupList")
	public String lookupList(ModelMap mm){
		mm.put("code", CODE);
		mm.put("method", "lookup");
		return BASE_PATH + "product_lookup.html";
	}

	@RequestMapping(KEY_ADD)
	public String add(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "product_add.html";
	}

	@RequestMapping(KEY_EDIT + "/{id}")
	public String edit(@PathVariable String id, ModelMap mm) {
		Product product = service.findById(id);
		mm.put("model", JsonKit.toJson(product));
		mm.put("id", id);
		mm.put("code", CODE);
		return BASE_PATH + "product_edit.html";
	}

	@RequestMapping(KEY_VIEW + "/{id}")
	public String view(@PathVariable String id, ModelMap mm) {
		Product product = service.findById(id);
		mm.put("model", JsonKit.toJson(product));
		mm.put("id", id);
		mm.put("code", CODE);
		return BASE_PATH + "product_view.html";
	}

	@ResponseBody
	@RequestMapping(KEY_LIST)
	public Object list() {
		Object grid = paginate(LIST_SOURCE);
		return grid;
	}

	@ResponseBody
	@RequestMapping(KEY_SAVE)
	public AjaxResult save() {
		ShiroUser user = ShiroKit.getUser();
		Product product = mapping(PREFIX, Product.class);
		product.setCreate_time(new Date());
		product.setCreator(Integer.valueOf(user.getId().toString()));
		product.setStatus(1);
		if(product.getAdjust_bili().floatValue()==0){
			return error("调整比例不能为零");
		}
		
		int i = Blade.create(Product.class).saveRtId(product);
		boolean temp = false;
		if(i>0){
			temp = true;
		}
		if (temp) {
			product.setId(i);
			ProductListCacheUtil.init().put(product);
			return success(SAVE_SUCCESS_MSG);
		} else {
			return error(SAVE_FAIL_MSG);
		}
	}

	@ResponseBody
	@RequestMapping(KEY_UPDATE)
	public AjaxResult update() {
		Product product = mapping(PREFIX, Product.class);
		Product old = Blade.create(Product.class).findById(product.getId());
		boolean temp = service.update(product);
		if (temp) {
			product = Blade.create(Product.class).findById(product.getId());
			if(product.getAdjust().floatValue()!=old.getAdjust().floatValue()
					||product.getBei().floatValue()!=old.getBei().floatValue()){
				service.saveAdjust(old);
			}
			
			if(product.getStatus()==1){
				ProductListCacheUtil.init().put(product);
			}else{
				ProductListCacheUtil.init().remove(product.getCode());
			}
			
			return success(UPDATE_SUCCESS_MSG);
		} else {
			return error(UPDATE_FAIL_MSG);
		}
	}

	@ResponseBody
	@RequestMapping(KEY_REMOVE+ "/{status}")
	public AjaxResult remove(@PathVariable String status,@RequestParam String ids) {
		boolean rs = service.updateStatus(ids, status);
		if (rs) {
			if("0".equals(status)){
				List<Product> products = service.findBy(" id in("+ids+")", Product.class);
				ProductListCacheUtil.init().removeList(products);
			}else{
				List<Product> products = service.findBy(" id in("+ids+")", Product.class);
				ProductListCacheUtil.init().putList(products);
			}
			return success("1".equals(status)?"启用成功":"停用成功");
		} else {
			return error("1".equals(status)?"启用失败":"停用失败");
		}
	}
	
	@ResponseBody
	@RequestMapping(KEY_DEL+ "/{status}")
	public AjaxResult delete(@PathVariable String status,@RequestParam String ids) {
		boolean rs = service.updateStatus(ids, status);
		if (rs) {
			List<Product> products = service.findBy(" id in("+ids+")", Product.class);
			ProductListCacheUtil.init().removeList(products);
			return success("删除成功");
		} else {
			return error("删除失败");
		}
	}
	
	@ResponseBody
	@RequestMapping("/reflashp")
	public AjaxResult reflashp() {
		Product p = new Product();
		p.setStatus(1);
		List<Product> products = Blade.create(Product.class).findByTemplate(p);
		ProductListCacheUtil.init().clear();
		ProductListCacheUtil.init().putList(products);
		return success("刷新成功");
	}
	
	@ResponseBody
	@RequestMapping("/reflashk")
	public AjaxResult reflashk(ModelMap mm) {
		Product p = new Product();
		p.setStatus(1);
		Object ids = this.getRequest().getParameter("ids");
		if(!Func.isEmpty(ids)){
			p.setId(Integer.valueOf(ids.toString()));
		}
		List<Product> products = Blade.create(Product.class).findByTemplate(p);
		
		for(final Product ps:products){
			System.out.println(DateKit.getTime()+":"+ps.getCode()+"日线图");
			CommKit.fillList(ps.getCode(),"0","",0);
        	try {  
                Thread.sleep(LfConstant.time);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }
        	
        	System.out.println(DateKit.getTime()+":"+ps.getCode()+"5分钟线图");
			CommKit.fillList(ps.getCode(),"3","5",0);
        	try {  
                Thread.sleep(LfConstant.time);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }
        	
        	System.out.println(DateKit.getTime()+":"+ps.getCode()+"30分钟线图");
			CommKit.fillList(ps.getCode(),"3","30",0);
        	try {  
                Thread.sleep(LfConstant.time);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }
        	
        	System.out.println(DateKit.getTime()+":"+ps.getCode()+"60分钟线图");
            CommKit.fillList(ps.getCode(),"3","60",0);
            try {
                Thread.sleep(LfConstant.time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(DateKit.getTime()+":"+ps.getCode()+"日线图");
            CommKit.fillList(ps.getCode(),"0",null,0);
            try {
                Thread.sleep(LfConstant.time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
		return success("刷新成功");
	}
}
