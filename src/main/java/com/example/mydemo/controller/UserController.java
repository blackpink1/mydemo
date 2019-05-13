package com.example.mydemo.controller;

import com.example.mydemo.pojo.User;
import com.example.mydemo.common.util.CopyUtil;
import com.example.mydemo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.github.pagehelper.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/user")
public class UserController{

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
    
    @ResponseBody
	@RequestMapping(value = "getUser")
	public Map<String,Object> getUser(Integer sid) throws Exception{
		logger.debug("sid : {} ",sid);
		Map<String,Object> result = new HashMap<String,Object>();
		User entity =  this.userService.selectById(sid);
		result.put("data",entity);
		result.put("statusCode","000000");
		return result;
	}
    
    @ResponseBody
	@RequestMapping(value = "selectByPage")
	public Map<String,Object> selectByPage(@RequestParam("pageNum") int pageNum,@RequestParam("pageSize") int pageSize,
			HttpServletRequest request) throws Exception{
		Map<String,Object> par = CopyUtil.copyMap(request,true) ;
		logger.debug("par : {} " ,par);
		Map<String,Object> result = new HashMap<String,Object>();
		Page<User> page = userService.selectPage(pageNum,pageSize,par);
		result.put("data",page);
		result.put("statusCode","000000");
		return result;
	}
    

    @ResponseBody
    @RequestMapping(value = "save")
    public Map<String,Object> save(User entity) throws Exception{
	    logger.debug("user : {} ",entity);
		Map<String,Object> result = new HashMap<String,Object>();
	    this.userService.saveSelective(entity);
		result.put("statusCode","000000");
	    return result;
    }
    
    @ResponseBody
	@RequestMapping(value = "delete")
	public Map<String,Object> delete(Integer sid) throws Exception{
		logger.debug("sid : {} ",sid);
		Map<String,Object> result = new HashMap<String,Object>();
		this.userService.deleteById(sid);
		result.put("statusCode","000000");
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "deleteDataBatch")
	public Map<String,Object> deleteDataBatch(@RequestParam("sids[]") Integer[] sids, HttpServletRequest request) throws Exception{
		logger.debug("sids lentgh  : {} ",sids.length);
		Map<String,Object> delMap = new HashMap<String, Object>();
		delMap.put("sids",sids);
		Map<String,Object> result = new HashMap<String,Object>();
		this.userService.deleteByParams(delMap);
		result.put("statusCode","000000");
		return result;
	}
    
 }
