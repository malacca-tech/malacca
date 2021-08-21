![height 160px](./logo.png)

# Intro
**[Malacca](https://www.malacca.tech/)** is a cloud native declarative enterprise service bus.

### Why Cloud Native ESB

> In this era of cloud computing, the technology stack of the Internet industry is at the forefront.Cloud native,container,service mesh,micro service are widely adopted.  
> But industry software is still in the progress of gradual transformation.
> So does the kernel (ESB) under SOA architecture lose its meaning of existence?  
> Of course,In the Internet industry with a very high degree of overall self-research and the formation of platform and microservices,
> ESB is no usage.
> However, it can be seen that the future 2B and industry software will still face non self-developed heterogeneous system integration, protocol conversion and other scenarios.
> This is why Malacca.  
> Malacca can integrate the advantages of ESB in SOA Architecture (unified service entry, heterogeneous system integration, protocol transformation and routing)，
> but also uses the latest cloud native technology. Container based can be easily deployed on kubernetes for large-scale ops.
> At last, the simple and declarative service configuration can let your service easy to write and manage.  
> Because of this, you can also manage your service by git ops to improve the convenience of management.

# Advantage
* Cloud Native
    * Support various bonuses brought by cloud computing such as large-scale operation and maintenance and flexible expansion.
    * Simple installation and deployment based on kubernetes.
    * Stepping on the present, facing the future, can support the enterprise's architecture evolution for at least ten years in the future.

* Declarative service configuration
    * Simpler and clearer than traditional xml configuration and pure web configuration.
    * Support Git Ops.
    * Service As Code.Administrators can view the integrated services under the enterprise at a glance.

* High scalability
    * Based on java development, some traditional service codes can be simply converted to malacca plug-ins.
    * The official provides a wealth of integrated plug-ins, which is convenient for you to quickly get started and configure the services required by various scenarios.

# Latest Release
### kernel jar
```xml
<dependency>
   <groupId>org.malacca</groupId>
   <artifactId>malacca-core</artifactId>
   <version>1.0-SNAPSHOT</version>
</dependency>
```    

### Integration With Spring
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

# Examples

## Example Repo
* Request by http and save to database.
* Polling read from database and write to file.
* Polling read from database and transfer data by groovy script.
* Read from file and transfer by javascript.


# Licencing
Malacca ESB is under MulanPubL - 2.0 License.You can freely copy and use the source code. When you modify or distribute the source code, please obey the MulanPubL - 2.0 license.

