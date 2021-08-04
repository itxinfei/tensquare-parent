<template>
    <div class="home-content"> 
    
     <div class="activities account-type"> 
      <h4 class="tit"><span>账户设置</span></h4> 
        <div class="account-main">
        	
   
	      <div class="account-person"> 
	       <h3>个人账号</h3> 
	       <ul class="account-info"> 
	        <li> <span class="gray index">名字</span> 
			 <span class="info" v-show="!name_input">{{user.nickname}}</span> 
	         <form  v-show="name_input"> 
	          <input type="text" placeholder="输入姓名" v-model="user.nickname" /> 
	          <button type='button' class="sui-btn btn-danger save-btn" @click="saveinfo_name()">保存</button> 
			  <button type='button' class="sui-btn btn-danger save-btn" @click="name_input=false" >取消</button> 
	         </form> <span class="gray edit"><a class="fa fa-pencil" aria-hidden="true" @click="name_input=true">编辑</a> </span> </li> 
	        
	        <li> <span class="gray index">Email </span> <span class="info">{{user.email}}</span> 
	         <form  class="sui-form form-inline"> 
	          <input type="email" placeholder="输入邮箱" /> 
	          <button class="sui-btn btn-danger save-btn">保存</button> 
	         </form> <span class="gray edit"><a class="fa fa-pencil" @click="test" aria-hidden="true">编辑</a> </span> </li> 
	        <li> <span class="gray index">手机号码 </span> <span class="info">{{user.mobile}}</span> 
	         <form class="sui-form form-inline"> 
	          <input type="phone" placeholder="输入手机号码" /> 
	          <button class="sui-btn btn-danger save-btn">保存</button> 
	         </form> <span class="gray edit"><a class="fa fa-pencil" aria-hidden="true">编辑</a> </span> </li> 
	        <li> <span class="gray index">密码 </span> <span class="info">点击修改密码</span> 
	         <form class="sui-form form-inline"> 
	          <input type="password" placeholder="输入新的密码" /> 
	          <button class="sui-btn btn-danger save-btn">保存</button> 
	         </form> <span class="gray edit"><a class="fa fa-pencil" aria-hidden="true">编辑</a> </span> </li> 
	       </ul> 
	      </div> 
	      <div class="account-other"> 
	       <h3>第三方账号</h3> 
	       <ul class="account-info"> 
	        <li> <span class="gray index"><img src="~/assets/img/widget-weixin.png" width="20" /> 微信</span> <span class="name">已绑定，解绑</span> <span class="gray check"><i class="fa fa-square-o" aria-hidden="true"></i> 公开显示</span> </li> 
	        <li> <span class="gray index"><img src="~/assets/img/widget-QQ.png" width="20" /> QQ</span> <span class="name bind">绑定账号</span> <span class="gray check"><i class="fa fa-square-o" aria-hidden="true"></i> 公开显示</span> </li> 
	        <li> <span class="gray index"><img src="~/assets/img/widget-weibo.png" width="20" /> 新浪微博 </span> <span class="name">已绑定，解绑</span> <span class="gray check"><i class="fa fa-square-o" aria-hidden="true"></i> 公开显示</span> </li> 
	       </ul> 
	      </div> 
	     </div>
     
     </div> 
    </div> 

</template>
<script>
import '~/assets/css/page-sj-person-account.css'
import userApi from '@/api/user'
import {getUser} from '@/utils/auth'
import axios from 'axios'
export default {	
	/*asyncData(){
		return userApi.info().then(  res=>{
			return {user: res.data.data }
		})
	},*/
	data(){
		return {
			user:{},
			name_input: false
		}
	},
	created(){
		userApi.info().then( res=> {
			this.user=res.data.data
		})
	},
	methods:{
		saveinfo_name(){
			userApi.saveinfo( { 'nickname': this.user.nickname } ).then( res=> {
				this.name_input=false
			})
		},
		test(){
			axios.get('http://127.0.0.1:8888/?code=1213132131').then( res=>{
				alert( JSON.stringify(res.data) )
			})
		}
	}
}
</script>
