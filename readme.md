笔记本
====
1.基础功能
---
	1.1显示时间戳
	
![](https://raw.githubusercontent.com/DerrickChanJL/DerrickChanJL.github.io/master/images/normal.jpg)

<br>
修改数据库数据信息，添加一个修改时间的long字段
添加了一个TextView显示时间戳
	
	1.2搜索功能
![](https://raw.githubusercontent.com/DerrickChanJL/DerrickChanJL.github.io/master/images/search3.png)
<br>


可根据title搜索
使用了toolbar + searchview + recyclerview 实现搜索功能
	
	
2.附加功能
----
	2.1美化UI
	
	2.2数据备份
![](https://raw.githubusercontent.com/DerrickChanJL/DerrickChanJL.github.io/master/images/beifen.png)	
	点击数据备份  数据库db文件会保存到手机内存中
	删除所有数据后可点击恢复数据 从保存到手机内存中的db文件恢复数据。

	2.3可根据时间、标题、颜色排序


![](https://raw.githubusercontent.com/DerrickChanJL/DerrickChanJL.github.io/master/images/time_paixu.png)
![](https://raw.githubusercontent.com/DerrickChanJL/DerrickChanJL.github.io/master/images/color.png)
![](https://raw.githubusercontent.com/DerrickChanJL/DerrickChanJL.github.io/master/images/title.png)
使用了spinner + 自定义spinnerAdapter