# kafka 生产者
## 食用方式
1. maven打包
2. 修改配置文件生产者初始化参数，配置文件地址conf/kafka.json
3. 创建一个或者多个文件夹，文件夹以TOPIC命名，如BOOK_TOPIC
4. 创建一个或者多个json、txt文件，一个文件为一条数据
5. 将上面创建的一个或者多个json、txt文件丢入上面创建的TOPIC文件夹
6. 将TOPIC文件夹丢到kafka-producer-build\conf\topic
7. 运行启动脚本