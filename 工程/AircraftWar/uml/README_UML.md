# DAO模式UML类图说明

## 如何查看UML类图

### 方法1：使用PlantUML查看
1. 安装PlantUML插件（如果使用VS Code或IntelliJ IDEA）
2. 打开文件：`uml/dao-pattern.puml`
3. 预览UML图

### 方法2：在线查看
1. 访问 http://www.plantuml.com/plantuml/uml/
2. 复制 `uml/dao-pattern.puml` 文件内容
3. 粘贴到在线编辑器查看

### 方法3：查看文本描述
打开 `EXPERIMENT4_REPORT.md` 文件中的ASCII艺术图

---

## UML类图结构说明

### 涉及的类

#### 1. ScoreRecord（值对象 VO）
**包**: edu.hitsz.dao

**职责**: 封装单局游戏的得分记录信息

**属性**:
- `- playerName: String`: 玩家名称
- `- score: int`: 得分
- `- recordTime: String`: 记录时间（格式：MM-dd HH:mm）

**方法**:
- `+ ScoreRecord(playerName: String, score: int)`: 构造函数（自动使用当前时间）
- `+ getPlayerName(): String`: 获取玩家名称
- `+ getScore(): int`: 获取得分
- `+ getRecordTime(): String`: 获取记录时间
- `+ toString(): String`: 返回字符串表示

---

#### 2. ScoreDao（DAO接口）
**包**: edu.hitsz.dao

**职责**: 定义数据访问操作的抽象接口

**方法**:
- `+ addScore(record: ScoreRecord): void`: 添加新的得分记录
- `+ getAllScores(): List<ScoreRecord>`: 获取所有得分记录（按分数降序排序）
- `+ deleteScore(index: int): void`: 删除指定索引的得分记录
- `+ displayLeaderboard(): void`: 在控制台打印排行榜

---

#### 3. ScoreDaoImpl（DAO实现类）
**包**: edu.hitsz.dao

**职责**: 实现数据访问逻辑，包括文件读写和排序

**属性**:
- `- scoreRecords: List<ScoreRecord>`: 内存中的得分记录列表
- `- filePath: String`: 文件存储路径（根据难度命名：scores_easy.txt等）

**方法**:
- `+ ScoreDaoImpl(difficulty: String)`: 构造函数，根据难度初始化文件路径
- `- loadScores(): void`: 从文件加载得分记录（私有方法）
- `- saveScores(): void`: 保存得分记录到文件（私有方法）
- `+ addScore(record: ScoreRecord): void`: 添加记录并保存到文件
- `+ getAllScores(): List<ScoreRecord>`: 获取记录并按分数降序排序
- `+ deleteScore(index: int): void`: 删除记录并保存
- `+ displayLeaderboard(): void`: 格式化打印排行榜

---

#### 4. Game（游戏类 - 使用DAO）
**包**: edu.hitsz.application

**职责**: 游戏主逻辑，在游戏结束时使用DAO保存和显示分数

**新增属性**:
- `- scoreDao: ScoreDao`: DAO实例，用于管理得分记录

**修改的方法**:
- `+ Game()`: 构造函数中初始化DAO实例（默认难度normal）
- `- checkResultAction(): void`: 游戏结束时保存分数并显示排行榜

---

## 类之间的关系

### 1. 实现关系（Realization）
```
ScoreDao <|-- ScoreDaoImpl
```
- ScoreDaoImpl 实现 ScoreDao 接口
- 用虚线空心箭头表示

### 2. 关联关系（Association）
```
Game --> ScoreDao
```
- Game 使用 ScoreDao 进行数据访问
- 用实线箭头表示

### 3. 依赖关系（Dependency）
```
ScoreDaoImpl --> ScoreRecord
```
- ScoreDaoImpl 管理和操作 ScoreRecord 对象
- 用虚线箭头表示

---

## 设计模式应用

### DAO模式的三层结构

```
┌─────────────────────────────────────────┐
│          业务逻辑层 (Game)              │
│   - 游戏逻辑                            │
│   - 调用DAO保存分数                     │
│   - 不关心数据如何存储                  │
└────────────────┬────────────────────────┘
                 │ 使用
┌────────────────▼────────────────────────┐
│          DAO接口层 (ScoreDao)           │
│   - 定义数据访问操作的抽象接口          │
│   - 业务逻辑与数据访问解耦              │
└────────────────┬────────────────────────┘
                 │ 实现
┌────────────────▼────────────────────────┐
│        DAO实现层 (ScoreDaoImpl)         │
│   - 实现文件读写                        │
│   - 数据排序                            │
│   - 持久化存储                        │
└────────────────┬────────────────────────┘
                 │ 管理
┌────────────────▼────────────────────────┐
│          值对象层 (ScoreRecord)         │
│   - 封装数据                            │
│   - 在层间传输                          │
└─────────────────────────────────────────┘
```

---

## 文件存储说明

### 存储位置
项目根目录下的文本文件：
- `scores_easy.txt`: 简单难度得分记录
- `scores_normal.txt`: 普通难度得分记录（当前使用）
- `scores_hard.txt`: 困难难度得分记录

### 文件格式
每行一条记录，逗号分隔：
```
玩家名,分数,时间
```

示例：
```
testUserName,370,04-15 17:15
testUserName,330,03-08 21:08
testUserName,190,01-20 17:15
```

---

## 排行榜输出格式

游戏结束时，控制台输出格式：

```
Game Over!

========== 得分排行榜 ==========
第1名: testUserName, 370, 04-15 17:15
第2名: testUserName, 330, 03-08 21:08
第3名: testUserName, 190, 01-20 17:15
...
================================
```

---

## 单元测试说明

### 测试文件位置
```
test/edu/hitsz/aircraft/HeroAircraftTest.java
```

### 测试覆盖
- **测试方法数**: 12个
- **覆盖来源**:
  - HeroAircraft自身方法: 6个
  - 父类AbstractAircraft方法: 2个
  - 祖父类AbstractFlyingObject方法: 4个

### 运行测试
1. 在IntelliJ IDEA中打开测试文件
2. 右键点击文件 → Run 'HeroAircraftTest'
3. 查看测试结果

---

## 符合实验要求

✅ 1. UML类图绘制：按照规范绘制DAO模式的UML结构类图
✅ 2. 设计模式应用：完成代码重构，采用DAO模式实现排行榜数据持久化
✅ 3. 文件读写：得分数据存储在文件中，每个难度对应单独文件
✅ 4. 排行榜：游戏结束时插入记录并展示排行榜（名次、玩家名、得分、时间）
✅ 5. 单元测试：对HeroAircraft类进行单元测试，测试12个方法（含父类方法）

---

**提示**: 如需生成UML图片，建议使用PlantUML插件或在线工具打开 `uml/dao-pattern.puml` 文件
