# TNT 爆炸执行
`TNTBoom` 类位于 `cn.cyanbukkit.block.gamehandle` 包下，提供多种 TNT 爆炸模式。

## TNTBoom 类 - 爆炸模式

### 模式 1
/tnttest 1 10
- **方法**: `mode1(amount: Int)`
- **描述**: 单个爆炸，`amount` 是爆炸的数量。

### 模式 2
```plaintext
/tnttest 2 10 2
```
- **方法**: `mode2(amount: Int, group: Int)`
- **描述**: 分组爆炸，`amount` 是几组，`group` 是一组几个。

### 模式 3
```plaintext
/tnttest 3 5
```
- **方法**: `mode3(second: Int)`
- **描述**: 天机俯冲爆炸，`second` 是秒数，TNT 有 10% 几率爆炸区域内的方块。

### 模式 4
```plaintext
/tnttest 4 5
```
- **方法**: `mode4(second: Int)`
- **描述**: 天际俯冲爆炸，每秒 20 个，有 20% 几率会炸响区域内的方块。

## Assist 类 - 辅助功能

`Assist` 类同样位于 `cn.cyanbukkit.block.gamehandle` 包下，提供多种辅助模式。

### 模式 1
```plaintext
/assist 1 1
```
- **方法**: `mode1(amount: Int)`
- **描述**: 挖模式下几层下落，建模式下修复几层，`amount` 是层数。

### 模式 2
```plaintext
/assist 2 1
```
- **方法**: `mode2(amount: Int)`
- **描述**: 挖模式下单个方块下落，建模式下单个方块修复，`amount` 是方块数量。

### 模式 3
```plaintext
/assist 3 1
```
- **方法**: `mode3(amount: Int)`
- **描述**: 生成猪，通用模式。

### 模式 4
```plaintext
/assist 4
```
- **方法**: `clean()`
- **描述**: 清空区域。

### 模式 5
```plaintext
/assist 5 -5
```
- **方法**: `completedChange(amount: Int)`
- **描述**: 变更当前回合数，`amount` 可以是 `-1` 或 `1`。

### 模式 6
```plaintext
/assist 6
```
- **方法**: `all()`
- **描述**: 挖模式下添加全部，建模式下全部修复。注意有可能导致卡崩。

## 注意事项
- 使用测试指令时，请确保已正确加载 `TNTBoom` 和 `Assist` 类，并且调用的玩家具有相应的权限。
- 某些模式可能需要特定的游戏环境或模组支持，以确保功能正确执行。
- 使用模式 6 时请小心，因为有可能引起游戏不稳定或卡崩。
```
