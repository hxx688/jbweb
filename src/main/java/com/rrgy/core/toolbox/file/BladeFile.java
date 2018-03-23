/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (cbjr@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rrgy.core.toolbox.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.rrgy.core.constant.ConstConfig;
import com.rrgy.core.constant.Cst;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.StrKit;

public class BladeFile {
	/**
	 * 上传文件在附件表中的id
	 */
	private Object fileId;
	
	/**
	 * 上传文件
	 */
	private MultipartFile file;
	
	/**
	 * 上传分类文件夹
	 */
	private String dir;
	
	/**
	 * 上传物理路径
	 */
	private String uploadPath;
	
	/**
	 * 上传虚拟路径
	 */
	private String uploadVirtualPath;
	
	/**
	 * 文件名
	 */
	private String fileName;
	
	/**
	 * 真实文件名
	 */
	private String originalFileName;

	public BladeFile() {
		
	}

	private BladeFile(MultipartFile file, String dir) {
		this.dir = dir;
		this.file = file;
		this.fileName = file.getName();
		this.originalFileName = file.getOriginalFilename();
		this.uploadPath = BladeFileKit.formatUrl(File.separator + Cst.me().getUploadRealPath() + File.separator + dir + File.separator + DateKit.getDays() + File.separator + this.originalFileName);
		this.uploadVirtualPath = BladeFileKit.formatUrl(Cst.me().getUploadCtxPath().replace(Cst.me().getContextPath(), "") + File.separator + dir + File.separator + DateKit.getDays() + File.separator + this.originalFileName);
	}

	private BladeFile(MultipartFile file, String dir, String uploadPath, String uploadVirtualPath) {
		this(file, dir);
		if (null != uploadPath){
			this.uploadPath = BladeFileKit.formatUrl(uploadPath);
			this.uploadVirtualPath = BladeFileKit.formatUrl(uploadVirtualPath);
		}
	}

	/**   
	 * 图片上传
	*/
	public void transfer() {
		transfer(false);
	}

	/**   
	 * 图片上传
	 * @param compress 是否压缩
	*/
	public void transfer(boolean compress) {
		IFileProxy fileFactory = FileProxyManager.me().getDefaultFileProxyFactory();
		this.transfer(fileFactory, compress);
	}
	
	/**   
	 * 图片上传
	 * @param fileFactory 文件上传工厂类
	 * @param compress 是否压缩
	*/
	public void transfer(IFileProxy fileFactory, boolean compress) {
		try {
			File file = new File(uploadPath);
			
			if(null != fileFactory){
				String [] path = fileFactory.path(file, dir);
				this.uploadPath = path[0];
				this.uploadVirtualPath = path[1].replace(Cst.me().getContextPath(), "");
				file = fileFactory.rename(file, path[0]);
			}
			
			File pfile = file.getParentFile();
			if (!pfile.exists()) {
				pfile.mkdirs();
			}
			
			this.file.transferTo(file);
			
			if (compress) {
				fileFactory.compress(this.uploadPath);				
			}
			
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
	}

	public Object getFileId() {
		if(null == this.fileId) {
			IFileProxy fileFactory = FileProxyManager.me().getDefaultFileProxyFactory();
			this.fileId = fileFactory.getFileId(this);
		}
		return fileId;
	}
	
	public void setFileId(Object fileId) {
		this.fileId = fileId;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public String getUploadVirtualPath() {
		return uploadVirtualPath;
	}

	public void setUploadVirtualPath(String uploadVirtualPath) {
		this.uploadVirtualPath = uploadVirtualPath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	
	/**
	 * 获取BladeFile封装类
	 * @param file
	 * @return
	 */
	public static BladeFile getFile(MultipartFile file){
		return getFile(file, "image", null, null);
	}
	
	/**
	 * 获取BladeFile封装类
	 * @param file
	 * @param dir
	 * @return
	 */
	public static BladeFile getFile(MultipartFile file, String dir){
		return getFile(file, dir, null, null);
	}
	
	/**
	 * 获取BladeFile封装类
	 * @param file
	 * @param dir
	 * @param path
	 * @param virtualPath
	 * @return
	 */
	public static BladeFile getFile(MultipartFile file, String dir, String path, String virtualPath){
		return new BladeFile(file, dir, path, virtualPath);
	}
	
	/**
	 * 获取BladeFile封装类
	 * @param files
	 * @return
	 */
	public static List<BladeFile> getFiles(List<MultipartFile> files){
		return getFiles(files, "image", null, null);
	}
	
	/**
	 * 获取BladeFile封装类
	 * @param files
	 * @param dir
	 * @return
	 */
	public static List<BladeFile> getFiles(List<MultipartFile> files, String dir){
		return getFiles(files, dir, null, null);
	}
	
	/**
	 * 获取BladeFile封装类
	 * @param files
	 * @param path
	 * @param virtualPath
	 * @return
	 */
	public static List<BladeFile> getFiles(List<MultipartFile> files, String dir, String path, String virtualPath){
		List<BladeFile> list = new ArrayList<>();
		for (MultipartFile file : files){
			list.add(new BladeFile(file, dir, path, virtualPath));
		}
		return list;
	}
	
	/**   
	 * 为虚拟路径添加配置的域名前缀
	 * @param map  对象
	 * @param name 路径字段名
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addDomain(Map map, String name) {
		if (null == map) {
			return;
		}
		String url = Func.toStr(map.get(name));
		// map为引用传递, 防止每次附加到缓存
		if (url.indexOf(ConstConfig.DOMAIN) >= 0 || StrKit.isBlank(url)) {
			return;
		} else {
			url = ConstConfig.DOMAIN + url;
			map.put(name, url);
		}
	}

	/**   
	 * 为虚拟路径添加配置的域名前缀
	 * @param list 对象
	 * @param name 路径字段名
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addDomain(List<Map> list, String name) {
		for (Map m : list) {
			if (null == m) {
				continue;
			}
			String url = Func.toStr(m.get(name));
			// map为引用传递, 防止每次附加到缓存
			if (url.indexOf(ConstConfig.DOMAIN) >= 0 || StrKit.isBlank(url)) {
				break;
			} else {
				url = ConstConfig.DOMAIN + url;
				m.put(name, url);
			}
		}
	}
}
