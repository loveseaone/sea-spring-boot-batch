**批量写入单表数据-10000条**
方案1 for循环 保存数据   --34391s--

方案2  集合并行流 保存数据   --8384s--
 
方案3 springbatch 保存数据  --1035s--
 
方案4  JDBCAppendTableSink 保存数据   --4310s--

参考文献  
https://blog.51cto.com/13501268/2298081  
https://www.xncoding.com/2017/08/01/spring/sb-batch.html