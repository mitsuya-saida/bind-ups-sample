# CloudFoundry × SpringBoot環境でDBの鍵をUserProvidedServiceで管理


## この資料について

CloudFoundryにデプロイしているSpringBootアプリケーションの鍵を管理する際に直接書いたりしたくないので、CloudFoundryが用意しているUserProviededService(UPS)を使っていい感じにやる方法を記述する

## 導入までの大筋

1. UPSインスタンスを立ち上げて鍵を登録する
2. アプリケーションからUPSで登録した鍵を引けるように記述する
3. manifest.ymlにUPSをバインドする設定を記述する
4. アプリケーションをCFにpushする

## 導入するアプリケーション
MySQLに接続してデータを返却するAPIとする。詳細なコードは巻末のソースを参照すること

## 導入する

#### 1. UPSインスタンスを立ち上げて鍵を登録する

###### 下記コマンドを実行して鍵を対話的に登録する

```bash
$ cf cups my-user-provided-service -p "url, user, password"

url> jdbc:mysql://your.mysql.server.domain:3306/demo

user> demo

password> secret
```

###### 補足. 登録すると下記のようなJSONがバインドしたインスタンスの環境変数に登録される

```
"VCAP_SERVICES": {
  "user-provided": [
   {
    "credentials": {
     "password": "secret",
     "url": "jdbc:mysql://your.mysql.server.domain:3306/demo",
     "user": "demo"
    },
    "label": "user-provided",
    "name": "my-user-provided-service",
    "syslog_drain_url": "",
    "tags": [],
    "volume_mounts": []
  }
 ]
}
```

#### 2. アプリケーションからUPSで登録した鍵を引けるように記述する

###### application.ymlにMySQLの接続情報を下記のように記述する

```
spring:
  datasource:
    url: ${vcap.services.my-user-provided-service.credentials.url}
    username: ${vcap.services.my-user-provided-service.credentials.user}
    password: ${vcap.services.my-user-provided-service.credentials.password}
```

###### 補足. 配信されるVCAP_SERVICEの情報は下記で取得できる

SpringBootでは、内部的にVCAP_*の情報をパースして下記の指定方法で使用できるようしているため、独自でJSONのパーサ等を作る必要はない

```
${vcap.services.UPSのnameにあたるもの.user-provided以下のkey名.前で指定したオブジェクトのkey名}
もしくは、
${cloud.services.UPSのnameにあたるもの.user-provided以下のkey名.前で指定したオブジェクトのkey名}
```

#### 3. manifest.ymlにUPSをバインドする設定を記述する

```
applications:
- name: bind-ups-sample
  buildpack: https://github.com/cloudfoundry/java-buildpack.git
  memory: 1G
  path: build/libs/bind-ups-sample-0.0.1-SNAPSHOT.jar
  services:
  	- my-user-provided-service # これがバインドする設定
```

#### 4. アプリケーションをCFにpushして起動できれば完了

```
$ cf push

```

## ソース
あとで書く

## 参考にした資料

* [User-Provided Service Instances | Cloud Foundry Docs](https://docs.cloudfoundry.org/devguide/services/user-provided.html)
* [Deploying with Application Manifests | Cloud Foundry Docs 
](https://docs.cloudfoundry.org/devguide/deploy-apps/manifest.html#services-block)
* [cloudfoundry - can not access environment variable specified by user provided service with java - Stack Overflow](https://stackoverflow.com/questions/45293954/can-not-access-environment-variable-specified-by-user-provided-service-with-java)
* [Binding to Data Services with Spring Boot in Cloud Foundry](https://spring.io/blog/2015/04/27/binding-to-data-services-with-spring-boot-in-cloud-foundry#a-purely-declarative-approach)
