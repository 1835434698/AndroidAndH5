# AndroidAndH5
1、创建数据库，这里我用的是mysql数据库，数据库名称mymvcdb。
2、手机端登录用户名和密码分别是123456789、123456
3、其中twoPage是一个混合框架部分：页面中使用GridView作为基础，从服务器获取view来填充GridView，其中每一个gridView对应一个h5模块，因为是demo，所有只做了前三个。
第一次点击下载对应的h5，第二次点击打开对应的h5页面。
4、说明：由于时间紧迫做的比较简洁，实现功能为目的，网络层使用okhttp，并且没有做加密处理，h5页使用明文，没有加密。服务端说明：由于本人不是座服务端的所以服务端很多功能没有实现。
5、框架介绍：本框架是讲H5模块化，H5之间互不影响，并且可以热更新、热修复。将h5分模块打包，上传到服务端（完善的应该是可以动态上传的，由于本人做客户端所以服务端没有做成动态的）。客户端通过服务端返回的参数加载H5的logo列表，通过点击对应的logo加载对应的h5模块。
