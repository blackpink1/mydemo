package com.example.mydemo.pojo;

import com.example.mydemo.common.pojo.DataEntity;

import java.util.Date;


public class User extends DataEntity  {

    /**
    * 用户名
    */
    private String userName;
    /**
    * 密码
    */
    private String password;
    /**
    * 昵称
    */
    private String nickName;
    /**
    * 状态
    */
    private Integer status;
    /**
    * 创建时间
    */
    private Date creatTime;
    /**
    * 修改时间
    */
    private Date updateTime;
    /**
    * 创建人
    */
    private Integer creatBy;
    /**
    * 修改人
    */
    private Integer updateBy;

    public  void  setUserName(String userName){
    this.userName = userName;
    }

    public  String getUserName(){
    return userName;
    }
    public  void  setPassword(String password){
    this.password = password;
    }

    public  String getPassword(){
    return password;
    }
    public  void  setNickName(String nickName){
    this.nickName = nickName;
    }

    public  String getNickName(){
    return nickName;
    }
    public  void  setStatus(Integer status){
    this.status = status;
    }

    public  Integer getStatus(){
    return status;
    }
    public  void  setCreatTime(Date creatTime){
    this.creatTime = creatTime;
    }

    public  Date getCreatTime(){
    return creatTime;
    }
    public  void  setUpdateTime(Date updateTime){
    this.updateTime = updateTime;
    }

    public  Date getUpdateTime(){
    return updateTime;
    }
    public  void  setCreatBy(Integer creatBy){
    this.creatBy = creatBy;
    }

    public  Integer getCreatBy(){
    return creatBy;
    }
    public  void  setUpdateBy(Integer updateBy){
    this.updateBy = updateBy;
    }

    public  Integer getUpdateBy(){
    return updateBy;
    }

}