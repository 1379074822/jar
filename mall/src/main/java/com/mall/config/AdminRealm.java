package com.mall.config;

import com.mall.pojo.Admin;
import com.mall.service.AdminService;
import com.mall.service.DataDictionaryService;
import com.mall.tools.MD5Utils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;
/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 管理员身份验证和权限控制的配置类
 */
public class AdminRealm extends AuthorizingRealm {
	/**
	 * 管理员接口
	 */
	@Autowired
	AdminService adminService;

	@Autowired
	private DataDictionaryService dataDictionaryService;

	Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 认证 参数：AuthenticationToken是从表单穿过来封装好的对象
	 * @param token 表单穿过来封装好的对象
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		logger.debug("doGetAuthenticationInfo:" + token);

		String uname = (String) token.getPrincipal();
		Map<String, Object> map = new HashMap<>(16);
		map.put("uname", uname);
		String password = new String((char[]) token.getCredentials());
		// 查询用户信息
		Admin user = adminService.getAdminByAccount(uname);
		logger.info( uname + " ,password: " + password + ",adminRealm consult userinfo : " + user.toString() );
		// 账号不存在
		if (user == null) {
			throw new UnknownAccountException("账号不存在!");
		}
		String pwdAfterMD5=MD5Utils.encrypt(user.getAccount(),user.getPassword());
		// 密码错误
		if (!password.equals(pwdAfterMD5)) {
			logger.info("验证密码： pwd " + password + " ,加密后数据库密码: " + pwdAfterMD5 + " check result" + password.equals(pwdAfterMD5));
			throw new IncorrectCredentialsException("密码不正确!");
		}
		// 用户密码的比对是Shiro帮我们完成的
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
		return info;
	}

	/**
	 * 用于授权
	 * @param principals
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Admin admin = (Admin)SecurityUtils.getSubject().getPrincipal();
		Integer authorityId = admin.getAuthorityId();
		String roleName=dataDictionaryService.getAdminRoleName(authorityId);
		Collection<String> rolesCollection = new HashSet<>();
		//Set<String> perms = menuService.listPerms((long)admin.getRoleId());
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		//info.setStringPermissions(perms);//添加权限
		info.addRole(roleName);//添加角色权限
		logger.info("MyShiroRealm的doGetAuthorizationInfo授权方法执行");
		logger.info( admin.getAdminName() + " 拥有的权限角色：" + roleName);
		return info;
	}

}
