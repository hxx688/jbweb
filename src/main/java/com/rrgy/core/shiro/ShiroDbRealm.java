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
package com.rrgy.core.shiro;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.lfgj.member.model.Member;
import com.lfgj.util.MD5;
import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.interfaces.IShiro;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.person.model.Person;
import com.rrgy.system.model.User;

public class ShiroDbRealm extends AuthorizingRealm {
	private static Logger log = LogManager.getLogger(ShiroDbRealm.class);
	
	/**
	 * 登录认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		log.info("Shiro登录认证启动");
		
		IShiro shiroFactory = ShiroManager.me().getDefaultShiroFactory();
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;	
		SimpleAuthenticationInfo info = null;
		String group_id = token.getHost();
		String username = token.getUsername();
		String password = String.valueOf(token.getPassword());
		if(Func.isEmpty(group_id)){
			User user = shiroFactory.user(token.getUsername());
			ShiroUser shiroUser = shiroFactory.shiroUser(user);
			info = shiroFactory.info(shiroUser, user, getName());
		}else{
			Member person = Blade.create(Member.class).findFirstBy("mobile = #{mobile}",
					Paras.create().set("mobile", username));
			// 账号不存在
			if (null == person) {
				throw new UnknownAccountException();
			}
			// 账号未审核
			if (person.getStatus() == 1 || person.getStatus() == 2) {
				throw new DisabledAccountException();
			}
			// 账号被锁定
			if (person.getStatus() == 3) {
				throw new LockedAccountException();
			}
	
			MD5 md5 = new MD5();
			String mds_password = md5.enCodeByMD5(password,person.getMobile());// 加密后的密码
			if (!person.getPassword().equals(mds_password)) {// 密码不相同
				throw new IncorrectCredentialsException();
			}
			info = new SimpleAuthenticationInfo(person, mds_password, getName());
		}
		log.info("Shiro登录认证完毕");
		return info;
	}

	/**
	 * 权限认证
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		IShiro shiroFactory = ShiroManager.me().getDefaultShiroFactory();
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		Object userId = shiroUser.getId();
		List<String> roleList = shiroUser.getRoleList();
		Set<String> urlSet = new HashSet<>();
		Set<String> roleNameSet = new HashSet<>();
		for (String roleId : roleList) {
			List<Map> permissions = shiroFactory.findPermissionsByRoleId(userId, roleId);
			if (null != permissions) {
				for (Map map : permissions) {
					if (!Func.isEmpty(map.get("URL"))) {
						urlSet.add(Func.toStr(map.get("URL")));
					}
				}
			}
			String roleName = shiroFactory.findRoleNameByRoleId(roleId);
			roleNameSet.add(roleName);
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermissions(urlSet);
		info.addRoles(roleNameSet);
		return info;
	}

	/**
	 * 设置认证加密方式
	 */
	@PostConstruct
	public void setCredentialMatcher() {
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		credentialsMatcher.setHashAlgorithmName(ShiroKit.hashAlgorithmName);
		credentialsMatcher.setHashIterations(ShiroKit.hashIterations);
		setCredentialsMatcher(credentialsMatcher);
	}

}
