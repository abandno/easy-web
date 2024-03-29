# Easy Web

为简化 web 开发而生!

虽然java开发工具、脚⼿架丰富、开源组件众多。但针对组内具体开发场景，依然有部分是它们触及不到的或⽆法满⾜的。
这时，需要⼀些更贴近业务开发、定制化的⼯具类库，来简 化⽇常开发。

[Easyweb 参考文档](https://nisus-liu.gitee.io/easyweb-document/#/)

文档协作
[Easyweb gitee 文档仓库](https://gitee.com/Nisus-Liu/easyweb-document)

## 项目结构

| 模块                    | 功能描述                                                                                                                                               |
| ----------------------- |----------------------------------------------------------------------------------------------------------------------------------------------------|
| core                    | 基础包, 不依赖任何本项目模块                                                                                                                                    |
| util                    | 工具, 轻量                                                                                                                                             |
| web-util | web 开发相关的工具, 依赖比 util 稍微重                                                                                                                          |
| easy-web-boot-starter | web 启动器, 一键您启用需要的功能                                                                                                                                |
| experiment | 实验性模块, 好用和稳定后, 会迁移到上面对应模块中                                                                                                                         |
| test-service            | 用于测试本项目代码, 不 deploy, maven.deploy.skip=true . <br />所有其他模块里不要方测试类, 统一放这里, 推荐使用 Spock framework , 表达力更好. <br />推荐安装 TestMe IDEA 插件, 自动生成非常完善的测试类模板. |


![模块架构](./doc/easy%20web%20模块依赖图.png)