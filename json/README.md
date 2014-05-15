此文件夹用于存放前端和后端所传输的JSON数据

格式如下：
1. 文件：每一次发送与接收的过程保存为一个文件，如 login.json
2. 每个文件的内容组成：
   1. 前端发送给后端的URL
   2.前端发送的数据
   3.后端返回的数据
3.例子：

login.json

/**
 * URL:192.168.137.1/doric/login.php
 **/

/**  前端发送的数据  **/
//username:doric
//password:123456


/**  后端返回的数据  **/
// login success
 {
 	"tag": "login",
 	"success": 1,
 	"error": 0,
 	"username": "a",
 	"user_id":"13",
 	"email": "616690602@qq.com",
 	"created_at": "2014-04-13 22:04:07"
 }

 // Incorrect email or password!
 {
 	"tag": "login",
 	"success": 0,
 	"error": 1,
 	"error_msg": "Incorrect email or password!"
 }
