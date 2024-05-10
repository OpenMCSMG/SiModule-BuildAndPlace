# 建与挖混合

## TNT 爆炸执行
cn.cyanbukkit.block.gamehandle下的 TNTBoom


### 模式1
` fun mode1(amount: Int) `
单个爆炸amount 是数量 出10个就是 mode1(10)
测试指令 `/tnttest 1 10`

### 模式2
` fun mode2(amount: Int, group: Int) `
组刷爆炸 amount是 几组  group 一组几个
mode2(10, 2) 10组 每组2个
测试指令 `/tnttest 2 10 2`

### 模式3
`fun mode3(second: Int)`

`second`是秒
天机俯冲爆炸 （不是瓦尔基里！
向上但是TNT会有 10%几率爆炸区域内的方块
测试指令 `/tnttest 3 5`
意思是5秒执行

### 模式4
`fun mode4(second: Int)`
`second` 是秒
天际俯冲爆炸 一秒20个会有 20%几率会炸响区域内的方块
测试指令 `/tnttest 4 5`




## 辅助 cn.cyanbukkit.block.gamehandle 下的Assist

### 模式1
fun mode1(amount: Int)
挖模式下几层下落
建模式下修复几层
测试指令
/assist 1 1
后面的1是多少层

### 模式2
fun mode2(amount: Int)
哇模式下单个方块下落
建模式下单个方块修复
amount 是几个方块
/assist 2 1
后面的1是多少个方块

### 模式3
fun mode3(amount: Int)
生成猪通用
测试指令/assist 3 1

### 模式4
fun clean()
清空区域
测试指令 /assist 4

### 模式5
fun completedChange(amount: Int)
变更现在的回合数
amount 可写 -1 或者1
/assist 5 -5
就是去掉5个回合

### 模式6
fun all()
挖模式下添加全部
建模式下全部修复
/assist 6
有卡崩端的可能


