# 飞机大战（Windows 基础工程）

迁移基线：黄梓洋（2024311608）的软件构造期末提交。
本工程用于后续 Android 迁移，目前仍是 Java Swing 桌面工程。

## 运行

使用 IntelliJ IDEA 打开本目录，导入 AircraftWar-base.iml，配置本机 JDK（语言级别 11）。
入口：src/edu/hitsz/application/Main.java。
运行工作目录必须设为本工程根目录，图片与音频通过 src/images、src/videos 的相对路径加载。

也可在工程根目录通过 PowerShell 执行：

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem src -Recurse -Filter *.java).FullName
java -cp out edu.hitsz.application.Main
```

## 文件

- src：游戏源码及图片、音频资源。
- test：原有 JUnit 5 测试，依赖存放在 lib。
- uml：原有 UML 文档，可能与实际实现存在差异。
- lib：JUnit 5 及其运行依赖，保留各 JAR 自带的许可证信息。

## 本次整理

- 英雄机初始血量从 10000 改为 100，最大血量同步为 100。
- 原生命值测试改为先扣 20、再扣 30、最后扣至 0。
- 移除未使用且原目录缺失的 commons-lang3 依赖配置。
- 排除旧编译产物、IDE 配置、聊天配置和历史成绩；运行后成绩文件会自动生成。
- 原始期末提交未修改。
