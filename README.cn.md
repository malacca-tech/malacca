![height 160px](./logo.png)

# 简介  
Malacca是一款云原生的声明式企业服务总线引擎。

### 为什么要做云原生的服务总线
 
> 在这个云计算时代，互联网行业的技术栈走在前沿，云原生、容器化、ServiceMesh、微服务大行其道。  
> 但是传统行业软件依然在逐步转型的过程中。  
> 那么在SOA架构下的宠儿：Enterprise Service Bus就失去了存在的价值吗？  
> 固然，在整体自研程度非常高，中台化微服务已经成型的互联网行业中，
> ESB已经失去了他的价值。  
> 但是可以遇见，在未来的2B、行业软件中，依然会面临非自研的异构系统集成，协议转换等场景。
> 这就是Malacca存在的意义，他既能将SOA架构中ESB的优势（统一服务入口、异构系统集成、协议转换与路由），
> 也使用了最新的云原生技术，基于容器可以轻松的部署在kubernetes之上，便于大规模运维。  
> 最后，声明式、简单的配置语法，让你的服务声明越发简单，可以支持你的服务基于Git Ops来管理，大大的提高了管理便利性。

# 优势  
 * 云原生  
   * 支持大规模运维与弹性扩所容等云计算带来的各种红利。
   * 基于kubernetes安装部署简单。
   * 脚踏现在，面向未来，能够支撑企业未来至少十年的架构演进。
 
 * 声明式的服务配置  
   * 比传统的xml配置、纯页面配置更加简单明了  
   * 支持DAG配置
   * 支持Git Ops
   * Service As Code，管理员可以一目了然的查看企业下的集成服务  
 
 * 高扩展性  
   * 基于java开发，传统的一些服务代码可以简单的转换为malacca插件。  
   * 官方提供丰富的集成插件，方便你快速的上手，配置出各种场景需要的服务。  

# Latest Release
### 核心Jar
```xml
<dependency>
   <groupId>org.malacca</groupId>
   <artifactId>malacca-core</artifactId>
   <version>1.0-SNAPSHOT</version>
</dependency>
```    

### 与Spring集成
```xml
<dependency>
   <groupId>org.malacca</groupId>
   <artifactId>malacca-spring</artifactId>
   <version>1.0-SNAPSHOT</version>
</dependency>
```    

```xml
<dependency>
   <groupId>org.malacca</groupId>
   <artifactId>malacca-boot-starter</artifactId>
   <version>1.0-SNAPSHOT</version>
</dependency>
```    

### helm chart
- todo

# 示例  
## 示例代码库  
 * 接受http请求，存入数据库    
 * 从数据库轮训，写入txt文件   
 * 从数据库轮训，使用groovy脚本转换数据    
 * 从文本读取数据，使用javascript转换数据结构  


# 许可证  
Malacca 服务总线使用 MulanPubL - 2.0 许可证。您可以免费复制及使用源代码。当您修改或分发源代码时，请遵守木兰协议。
