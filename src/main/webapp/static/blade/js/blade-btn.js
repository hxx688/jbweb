var btnjsons;
var toolbar;
var exwhere;
(function () {
    var blade_btn = function (btnjson) {
        this.id = btnjson.CODE;
        this.name = btnjson.NAME;
        this.pname = btnjson.PNAME;
        this.alias = btnjson.ALIAS;
        this.url = this.isEmpty(BladeApp.ctxPath) + btnjson.URL;
        this.div = this.isEmpty(btnjson.ICON.split('|')[0]);
        this.li = this.isEmpty(btnjson.ICON.split('|')[1]);
        this.area=this.isEmpty(btnjson.TIPS);
        this.isopen=this.isEmpty(btnjson.ISOPEN);
    };

    //用于额外加载一些控件
    blade_btn.prototype.initOther = function(){
        if( typeof initZtree === 'function' ){
        	initZtree();
        }
    }
    
    blade_btn.prototype.isEmpty = function (str) {
        if (str == "" || str == null || str == "null" || str == "undefined" || str == undefined) {
            return "";
        }
        return str;
    };

    blade_btn.prototype.open = function(blade_url, blade_id, blade_open){
    	var width = (this.area != "") ? (this.area.split('*')[0]) : "800";
    	var height = (this.area != "") ? (this.area.split('*')[1]) : "520";
    	var iframe_width = window.top.$(window).width();
    	var iframe_height = window.top.$(window).height();
    	var flag = (parseInt(width) >= iframe_width || parseInt(height) >= iframe_height) ? true : false; width += "px";height += "px";
    	if(this.isopen == "1" || this.isEmpty(blade_open) == "1"){
    		blade_id = "btn_" + this.id + ((this.isEmpty(blade_id) == "") ? "" : ("_" + blade_id));
    		window.top.addTabs({
                id: blade_id,
                title: this.pname + "→" + this.name,
                url: blade_url,
                icon: this.li,
                close: true
            });
    	}
    	else{
        	var index = layer.open({
        	    type: 2,
        	    title: this.pname + "→" + this.name,
        	    area: [width, height],
        	    fix: false, //不固定
        	    maxmin: true,
        	    content: blade_url
        	});
        	if(flag){
        		layer.full(index);
        	}
    	}
    };
    
    //一级按钮点击事件
    blade_btn.prototype.itemClick = function () {
        var ids = getGridXls().join(",");
        var rows = getGridXls().length;
        var rowData=getRowData();
        var split = "/";
    	if (this.alias == "add") {
        	this.open(this.url);
            return;
        }
        if (this.alias == "addex") {
            if (rows > 1) {
                layer_alert('只能选择一条数据作为父!', "warn");
                return;
            }
            
            var url = this.url;
            this.open(url + split + ids, ids);
            return;
        } 
        if (this.alias == "edit") {
            if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            if (rows > 1) {
                layer_alert('只能选择一条数据!', "warn");
                return;
            }
            
            var url = this.url;
            this.open(url + split + ids, ids);
            return;
        }
        if (this.alias == "recharge" 
        	|| this.alias == "sub") {
            if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            if (rows > 1) {
                layer_alert('只能选择一条数据!', "warn");
                return;
            }
            
            var url = this.url;
            this.open(url + split + ids, ids);
            return;
        }
        if (this.alias == "edit_check") {
            if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            if (rows > 1) {
                layer_alert('只能选择一条数据!', "warn");
                return;
            }
            var url = this.url;
            var  o = this;
            $.post(url + "_check", { ids: ids }, function (data) {
                if (data.code === 0) {
                    o.open(url + split + ids, ids);
                }else {
                	layer_post(data);
                }
            }, "json");
            return;
        }
        if (this.alias == "remove" || this.alias == "del") {
            if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            var _this = this;
            var url = _this.url;
            var name = _this.name;
            layer.confirm('是否确定'+name+'？', {
                icon: 3,
                btn: ['确定', '取消'] //按钮
            }, function () {
                $.post(url, { ids: ids }, function (data) {
                    if (data.code === 0) {
                        layer_alert(data.message, "success");
                        searchGrid();
                        _this.initOther();
                    }
                    else {
                    	layer_post(data);
                    }
                }, "json");

            }, function () {
                //layer.msg('已取消');
            });
            return;
        }
        if (this.alias == "view") {
            if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            if (rows > 1) {
                layer_alert('只能选择一条数据!', "warn");
                return;
            }
            
            var url = this.url;
            this.open(url + split + ids, ids);
            return;
        }
        if (this.alias == "authority") {
            if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            if (rows > 1) {
                layer_alert('只能选择一条数据!', "warn");
                return;
            }
            var _this = this;
            $.post(BladeApp.ctxPath + "/role/getPowerById", { id: ids }, function (data) {
                if (data.code === 0) {
                    var roleName=rowData.NAME;
                    _this.open(_this.url + "?roleId=" + ids + "&roleName=" + encodeURI(encodeURI(roleName)));
                }
                else{
                	layer_alert('请先给上级角色分配权限!', "warn");
                }
            }, "json");
            return;
        }
        if (this.alias == "assign") {
            if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            var name = encodeURI(encodeURI(rowData.NAME));
            var roleID = (rowData.ROLEID == "") ? "0" : rowData.ROLEID;
        	this.open(this.url + split + ids + split + name + split + roleID);
            return;
        }
        if (this.alias == "agent") {
            if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            if (rows > 1) {
                layer_alert('只能选择一条数据!', "warn");
                return;
            }
            var roleID=rowData.ROLEID;
            if(roleID==""){
                layer_alert('请先分配角色!', "warn");
                return;
            }
            var Name = encodeURI(encodeURI(rowData.NAME));
        	this.open(this.url + split + ids + split + Name,"","1");
            return;
        }
        if (this.alias == "download") {
            if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            if (rows > 1) {
                layer_alert('只能选择一条数据!', "warn");
                return;
            }
            var url = rowData.ATTACHURL;
            window.open(url, "附件下载");
            return;
        }
        if (this.alias == "reset") {
            if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            var url = this.url;
            layer.confirm('是否将密码重置为 111111？', {
                icon: 3,
                btn: ['确定', '取消'] //按钮
            }, function () {
                $.post(url, { ids: ids }, function (data) {
                    if (data.code === 0) {
                        layer_alert(data.message, "success");
                    }
                    else {
                    	layer_post(data);
                    }
                }, "json");

            }, function () {
                //layer.msg('已取消');
            });
            return;
        }
        if (this.alias == "ajaxpost") {
        	if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
	    	 if (rows > 1) {
	             layer_alert('只能选择一条数据!', "warn");
	             return;
	         }
           var url = this.url;
           layer.confirm('确定'+this.name+'吗？', {
               icon: 3,
               btn: ['确定', '取消'] //按钮
           }, function () {
               $.post(url, { ids: ids }, function (data) {
                   if (data.code === 0) {
                       layer_alert(data.message, "success");
                       searchGrid();
                   }
                   else {
                   	layer_post(data);
                   }
               }, "json");

           }, function () {
               //layer.msg('已取消');
           });
           return;
       }
        if (this.alias == "reflash") {
	    	 if (rows > 1) {
	             layer_alert('只能选择一条数据!', "warn");
	             return;
	         }
            var url = this.url;
            layer.confirm('确定'+this.name+'吗？', {
                icon: 3,
                btn: ['确定', '取消'] //按钮
            }, function () {
                $.post(url, { ids: ids }, function (data) {
                    if (data.code === 0) {
                        layer_alert(data.message, "success");
                        searchGrid();
                    }
                    else {
                    	layer_post(data);
                    }
                }, "json");

            }, function () {
                //layer.msg('已取消');
            });
            return;
        }
        if (this.alias == "frozen") {
            if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            var url = this.url;
            layer.confirm('是否将选中账号冻结/解冻？', {
                icon: 3,
                btn: ['确定', '取消'] //按钮
            }, function () {
                $.post(url, { ids: ids }, function (data) {
                    if (data.code === 0) {
                        layer_alert(data.message, "success");
                        searchGrid();
                    }
                    else {
                    	layer_post(data);
                    }
                }, "json");

            }, function () {
                //layer.msg('已取消');
            });
            return;
        }
        
        if (this.alias == "audit_check") {
            if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            if (rows > 1) {
                layer_alert('只能选择一条数据!', "warn");
                return;
            }
            var url = this.url;
            layer.confirm('是否将选中项通过审核？', {
                icon: 3,
                btn: ['确定', '取消'] //按钮
            }, function () {
            	var layerIndex = layer.load(1, {
          		  shade: [0.1,'#fff'] //0.1透明度的白色背景
          		});
                $.post(url, { ids: ids }, function (data) {
                    if (data.code === 0) {
                        layer_alert(data.message, "success");
                        searchGrid();
                        layer.close(layerIndex);
                    }
                    else {
                    	layer_post(data);
                    	layer.close(layerIndex);
                    }
                }, "json");

            }, function () {
                //layer.msg('已取消');
            });
            return;
        }
        
        if (this.alias == "export") {
        	
        	 var url = this.url;
             layer.confirm('是否导出数据？', {
                 icon: 3,
                 btn: ['确定', '取消'] //按钮
             }, function () {
            	 //url += "?" + $('#_search_form').serialize();
            	 url += "?";
            	 $.each($(".form-control"), function(i, n) {
     				var key = $(this).attr("name");
     				var value = $(this).val();
     				if (BladeTool.isNotEmpty(key) && BladeTool.isNotEmpty(value)) {
     					url += key + "=" + encodeURI(encodeURI(value)) + "&";
     				}
     			 });
                 window.open(url, "导出EXCEL");
                 //window.location.href=url;
                 layer.closeAll("dialog");
             }, function () {
                 //layer.msg('已取消');
             });
             return;
        }


        //带子按钮区域
        if (this.alias == "audit" || this.alias == "recycle") {
        	var code = this.id;
            if (stage.all[code] == undefined) {
                $.post(BladeApp.ctxPath + "/cache/getChildBtn", { code: code }, function (data) {
                    if (data.code === 0) {
                        btnjsons = data.data;
                        var _btn_child_stage = new btn_child_stage();
                        for (var i = 0; i < btnjsons.length; i++) {
                            if (btnjsons[i].name == '') continue;
                            var btn_child = new blade_btn(btnjsons[i]);
                            _btn_child_stage.btn.register(btn_child);
                        }
                        stage.register(code, _btn_child_stage);
                        stage.all[code].btn.bind(toolbar);
                    }
                }, "json");
            }
            else {
            	stage.all[code].btn.bind(toolbar);
            }
            exwhere = this.url.replace(BladeApp.ctxPath, "");//修复未发布在tomcat根目录下带有项目路径导致不能搜索的问题 
            isAutoPage = false;//自动跳转到第一页
            searchGrid();
            return;

        }
        
        if (this.alias == "filter") {
        	if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            if (rows > 1) {
                layer_alert('只能选择一条数据!', "warn");
                return;
            }
            var urls = this.url+"/"+ids;
            var id = this.id+"_"+ids;
            var obj = {close:true,icon:this.icon,id:id,title:rowData.real_name,url:urls};
            parent.addTabs(obj);
            return;
        }
        
        if (this.alias == "ok") {
            if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            var url = this.url;
            layer.confirm('是否通过？', {
                icon: 3,
                btn: ['确定', '取消'] //按钮
            }, function () {
                $.post(url, { ids: ids }, function (data) {
                    if (data.code === 0) {
                        layer_alert(data.message, "success");
                        searchGrid();
                    }
                    else {
                  	  layer_post(data);
                    }
                }, "json");

            }, function () {
                //layer.msg('已取消');
            });
            return;
          }
          if (this.alias == "refuse") {
              if (rows == 0) {
                  layer_alert('请选择一条数据!', "warn");
                  return;
              }
              var url = this.url;
              layer.confirm('是否拒绝？', {
                  icon: 3,
                  btn: ['确定', '取消'] //按钮
              }, function () {
                  $.post(url, { ids: ids }, function (data) {
                      if (data.code === 0) {
                          layer_alert(data.message, "success");
                          searchGrid();
                      }
                      else {
                      	layer_post(data);
                      }
                  }, "json");

              }, function () {
                  //layer.msg('已取消');
              });
              return;
          }
          
    };

    //二级按钮点击事件
    blade_btn.prototype.childItemClick = function () {
        var ids = getGridXls().join(",");
        var rows = getGridXls().length;
        var split = "/";
        if (this.alias == "back") {
            btn_stage.bind(toolbar);
            reloadGrid();
            return;
        }
        if (this.alias == "view") {
            if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            if (rows > 1) {
                layer_alert('只能选择一条数据!', "warn");
                return;
            }
            this.open(this.url + split + ids);
            return;
        }
        if (this.alias == "ok") {
          if (rows == 0) {
              layer_alert('请选择一条数据!', "warn");
              return;
          }
          var url = this.url;
          layer.confirm('是否通过？', {
              icon: 3,
              btn: ['确定', '取消'] //按钮
          }, function () {
              $.post(url, { ids: ids }, function (data) {
                  if (data.code === 0) {
                      layer_alert(data.message, "success");
                      searchGrid();
                  }
                  else {
                	  layer_post(data);
                  }
              }, "json");

          }, function () {
              //layer.msg('已取消');
          });
          return;
        }
        if (this.alias == "refuse") {
            if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            var url = this.url;
            layer.confirm('是否拒绝？', {
                icon: 3,
                btn: ['确定', '取消'] //按钮
            }, function () {
                $.post(url, { ids: ids }, function (data) {
                    if (data.code === 0) {
                        layer_alert(data.message, "success");
                        searchGrid();
                    }
                    else {
                    	layer_post(data);
                    }
                }, "json");

            }, function () {
                //layer.msg('已取消');
            });
            return;
        }
        if (this.alias == "restore") {
            if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            var _this = this;
            var url = this.url;
            layer.confirm('是否还原', {
                icon: 3,
                btn: ['确定', '取消'] //按钮
            }, function () {
                $.post(url, { ids: ids }, function (data) {
                    if (data.code === 0) {
                        layer_alert(data.message, "success");
                        searchGrid();
                        _this.initOther();
                    }
                    else {
                    	layer_post(data);
                    }
                }, "json");

            }, function () {
                //layer.msg('已取消');
            });
            return;
        }
        if (this.alias == "remove") {
            if (rows == 0) {
                layer_alert('请选择一条数据!', "warn");
                return;
            }
            var _this = this;
            var url = this.url;
            layer.confirm('是否删除？删除后不可恢复', {
                icon: 3,
                btn: ['确定', '取消'] //按钮
            }, function () {
                $.post(url, { ids: ids }, function (data) {
                    if (data.code === 0) {
                        layer_alert(data.message, "success");
                        searchGrid();
                        _this.initOther();
                    }
                    else {
                    	layer_post(data);
                    }
                }, "json");

            }, function () {
                //layer.msg('已取消');
            });
            return;
        }
    };
    
    //一级按钮缓存区
    var btn_stage = {
        all: {},
        count: 0,
        register: function (btn) {
            this.all[btn.id] = btn;
        },
        bind: function (toolbar) {
            var $toolbar = $("#" + toolbar);
            $toolbar.html("");
            this.count = 0;
            for (var btn_id in this.all) {
                this.count++;
                var _this = this;
                var btn = _this.all[btn_id];
                var _btn = "<button class='" + btn.div + "' id='" + btn.id + "'>&nbsp;" + "<li class='" + btn.li + "'></li>&nbsp;" + btn.name + "&nbsp;</button>&nbsp;";
                $toolbar.append(_btn);
                var $btn = $("#" + btn.id);
                $btn.bind("click", function () {
                    _this.all[this.id].itemClick();
                	$(this).blur();
                	if (typeof btnCallBack === 'function') {
						btnCallBack(_this.all[this.id]);
					}
                });

            }
        }
    };

    //二级按钮缓存区
    var stage = {
        all: {},
        register: function (id, _btn_child_stage) {
            this.all[id] = _btn_child_stage;
        }
    };

    //二级按钮实例
	var btn_child_stage = function(){
	    this.btn = {
	            all: {},
	            count: 0,
	            register: function (btn) {
	                this.all[btn.id] = btn;
	            },
	            bind: function (toolbar) {
	                var $toolbar = $("#" + toolbar);
	                $toolbar.html("");
	                this.count = 0;
	                for (var btn_id in this.all) {
	                    this.count++;
	                    var _this = this;
	                    var btn = _this.all[btn_id];
	                    var _btn = "<button class='" + btn.div + "' id='" + btn.id + "'>&nbsp;" + "<li class='" + btn.li + "'></li>&nbsp;" + btn.name + "&nbsp;</button>&nbsp;";
	                    $toolbar.append(_btn);
	                    var $btn = $("#" + btn.id);
	                    $btn.bind("click", function () {
	                        _this.all[this.id].childItemClick();
	                    	$(this).blur();
	                    	if (typeof btnCallBack === 'function') {
	    						btnCallBack(_this.all[this.id]);
	    					}
	                    });
	                }
	            }
	        };
	};
    
    window.blade_btn = blade_btn;
    window.stage = stage;
    window.btn_stage = btn_stage;
    window.btn_child_stage = btn_child_stage;
} ());


// 根据模块code生成每个工具条上面的 btn
function initMenuBtn(obj, code) {
    $.post(BladeApp.ctxPath + "/cache/getBtn", { code: code }, function (data) {
        if (data.code === 0) {
            toolbar = obj;
            btnjsons = data.data;
            for (var i = 0; i < btnjsons.length; i++) {
                if (btnjsons[i].name == '') continue;
                var btn_parent = new blade_btn(btnjsons[i]);
                btn_stage.register(btn_parent);
            }
            btn_stage.bind(toolbar);
        }
    	if( typeof doOther === 'function' ){
    		doOther();
        }
    }, "json");
}
