package com.xoado.authcenter.service.user.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoado.authcenter.bean.TblUser;
import com.xoado.authcenter.bean.TblUserExample;
import com.xoado.authcenter.bean.TblWeixinUser;
import com.xoado.authcenter.bean.TblWeixinUserExample;
import com.xoado.authcenter.bean.TblWeixinUserExample.Criteria;
import com.xoado.authcenter.jedis.XoadoSession;
import com.xoado.authcenter.mapper.TblUserMapper;
import com.xoado.authcenter.mapper.TblWeixinUserMapper;
import com.xoado.authcenter.service.Iuser.IUserWxLogin;
import com.xoado.common.MD5;
import com.xoado.common.XoadoResult;




@Service
public class UserWxLogin implements IUserWxLogin{
	
	@Autowired
	private TblWeixinUserMapper tblWeixinUserMapper;
	@Autowired
	private TblUserMapper tblUserMapper;
	@Autowired
	private XoadoSession xoadosession;

	@Override
	public List<TblWeixinUser> WXuser_login_select(String openId) {
		TblWeixinUserExample example = new TblWeixinUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andOpenidEqualTo(openId);
		List<TblWeixinUser> list = tblWeixinUserMapper.selectByExample(example);
		return list;
	}

	@Override
	public XoadoResult register_Wxuser(TblWeixinUser user) {
		tblWeixinUserMapper.insert(user);
		return XoadoResult.ok();
	}
	/**
	 * 微信绑定手机号
	 */
	public XoadoResult Wx_register_phone(String openId,String phoneNumber,String Verification_code,HttpServletRequest request){
		/**
		 * 判断手机号是否存在
		 */
		String phone_code = xoadosession.get(phoneNumber);
		System.out.println("--"+phone_code);
		boolean c= phone_code.equals(Verification_code);
		String userid = MD5.MD5Encode(UUID.randomUUID().toString());
		if(c!=false){
			
		TblUserExample example = new TblUserExample();	
		com.xoado.authcenter.bean.TblUserExample.Criteria criteria = example.createCriteria();
		criteria.andPhoneNumberEqualTo(phoneNumber);
		List<TblUser> list = tblUserMapper.selectByExample(example);
		
		if(list.size()==0 || list==null){
			TblUser tblUser = new TblUser();
			tblUser.setPhoneNumber(phoneNumber);
			tblUser.setUserPassword("123456");
			tblUser.setUserid(userid); 
			tblUser.setUnionid(null);
			tblUser.setName(null);
			tblUser.setNickName(null);
			tblUser.setHeadImgUrl(null);
			tblUser.setRegisterTime(new Date());
			tblUser.setAccountstatus("CONTROLLED");
			tblUser.setAccounttype(null);
			tblUser.setSex(null);
			tblUser.setCity(null);
			tblUser.setCountry(null);
			tblUser.setProvince(null);
			tblUser.setIdcard(null);
			tblUser.setPositiveImgUrl(null);
			tblUser.setReverseImgUrl(null);
			tblUser.setMessage(null);
			tblUser.setAuditTime(null);
			tblUserMapper.insert(tblUser);	
			
			TblWeixinUser user = new TblWeixinUser();
			TblWeixinUser weixinUser = tblWeixinUserMapper.selectByPrimaryKey(openId);
			user.setOpenid(openId);
			user.setWxappid(weixinUser.getWxappid());
			user.setXdappid(weixinUser.getXdappid());
			user.setNickName(weixinUser.getNickName());
			user.setSex(weixinUser.getSex());
			user.setCity(weixinUser.getCity());
			user.setCountry(weixinUser.getCountry());
			user.setProvince(weixinUser.getProvince());
			user.setUserid(userid);
			user.setBindingTime(new Date());
			user.setCreateTime(weixinUser.getCreateTime());
			
			tblWeixinUserMapper.updateByPrimaryKey(user);
			return XoadoResult.build(400, "添加完成");	
		}
		
//		手机号已经注册，但未绑定微信小程序
		for (TblUser tblUser : list) {
			userid = tblUser.getUserid();
		}
		TblWeixinUser user = new TblWeixinUser();
		TblWeixinUser weixinUser = tblWeixinUserMapper.selectByPrimaryKey(openId);
		user.setOpenid(openId);
		user.setWxappid(weixinUser.getWxappid());
		user.setXdappid(weixinUser.getXdappid());
		user.setNickName(weixinUser.getNickName());
		user.setSex(weixinUser.getSex());
		user.setCity(weixinUser.getCity());
		user.setCountry(weixinUser.getCountry());
		user.setProvince(weixinUser.getProvince());
		user.setUserid(userid);
		user.setBindingTime(new Date());
		user.setCreateTime(weixinUser.getCreateTime());
		tblWeixinUserMapper.updateByPrimaryKey(user);
		return XoadoResult.build(0, "更新完成");
		}
		return null;
		
	
	}
}
