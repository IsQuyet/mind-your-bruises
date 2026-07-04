# Mind Your Bruises

[English](README.md) | [简体中文](README.zh-CN.md)

Mind Your Bruises（注意你的淤青）是一个 Minecraft 1.21.11 Fabric 客户端模组，会根据伤害类型修改实体模型受击时的原版受伤覆盖颜色。

模组保留原版红色作为兜底颜色，并为火焰和爆炸、冰冻和溺水、植物性、魔法/凋零/虚空类、雷电、饥饿和末影珍珠等大类伤害使用不同的受击覆盖颜色。

## 功能

- 在客户端重新着色原版实体受击覆盖效果。
- 为火焰和爆炸、冰冻和溺水、植物性、魔法/凋零/虚空类、雷电、饥饿和末影珍珠等大类伤害使用不同颜色。
- 对未匹配的伤害类型保留原版红色作为兜底颜色。
- 支持通过 JSON 配置颜色和伤害类型映射。
- 可通过 `damageTypeOverrides` 支持来自模组或数据包的自定义伤害类型。

## 配置

Mind Your Bruises 会在游戏启动后创建这个配置文件：

```text
.minecraft/config/mind-your-bruises.json
```

在开发环境中，配置文件会创建在：

```text
run/config/mind-your-bruises.json
```

配置使用一个直接位于 `config/` 下的 JSON 文件。

配置示例：

```json
{
  "enabled": true,
  "fireColor": "#ff7014",
  "frostColor": "#4bd2ff",
  "plantColor": "#50dc3c",
  "fallbackColor": "#ff0000",
  "arcaneColor": "#b950ff",
  "shockColor": "#d8f6ff",
  "starvationColor": "#9a7a32",
  "enderColor": "#2bd6b3",
  "damageTypeOverrides": {
    "minecraft:lava": "fire",
    "minecraft:freeze": "frost",
    "minecraft:wither": "arcane",
    "minecraft:wither_skull": "arcane",
    "minecraft:lightning_bolt": "shock",
    "minecraft:starve": "starvation",
    "minecraft:ender_pearl": "ender"
  }
}
```

手动编辑配置文件后，请重启游戏。

配置项说明：

- `enabled`：是否启用重新着色后的受击覆盖。设置为 `false` 时会回到原版红色。
- `fireColor`、`frostColor`、`plantColor`、`fallbackColor`、`arcaneColor`、`shockColor`、`starvationColor` 和 `enderColor`：颜色配置，使用 `#rrggbb` 格式。漏写 `#` 也可以，模组保存配置时会自动补齐并规范化。
- `damageTypeOverrides`：把某个伤害类型 id 映射到一个颜色组。可用颜色组为 `fire`、`frost`、`plant`、`fallback`、`arcane`、`shock`、`starvation` 和 `ender`。爆炸伤害默认使用 `fire` 颜色组；溺水伤害默认使用 `frost` 颜色组；虚空、世界边界和强制击杀默认使用 `arcane` 颜色组。

默认颜色组：

| 颜色组 | 默认颜色 | 内置伤害类型 |
| --- | --- | --- |
| `fire` | `#ff7014` | 火焰、熔岩、岩浆块、火球、爆炸、烟花 |
| `frost` | `#4bd2ff` | 冰冻、溺水伤害 |
| `plant` | `#50dc3c` | 仙人掌、甜浆果丛 |
| `arcane` | `#b950ff` | 魔法、间接魔法、龙息、音爆、荆棘、凋零、凋零头、虚空、世界边界、强制击杀 |
| `shock` | `#d8f6ff` | 雷电伤害 |
| `starvation` | `#9a7a32` | 饥饿伤害 |
| `ender` | `#2bd6b3` | 末影珍珠伤害 |
| `fallback` | `#ff0000` | 未匹配伤害、原版物理伤害、蜜蜂蜇刺、窒息、实体挤压 |

例如，让某个自定义带刺植物伤害使用植物性颜色：

```json
"damageTypeOverrides": {
  "examplemod:thorny_vine": "plant"
}
```

如果某个伤害类型没有写在 `damageTypeOverrides` 里，Mind Your Bruises 会先使用内置的大类匹配规则，最后再回退到 `fallbackColor`。

## 运行要求

- Minecraft 1.21.11
- Fabric Loader 0.19.3 或更新版本
- Java 21 或更新版本

## 兼容性

Mind Your Bruises 是纯客户端模组。它只改变你客户端上受击实体的渲染效果，不会改变伤害规则、生命值、无敌帧或服务端行为。

## 构建

运行 Gradle wrapper：

```powershell
.\gradlew.bat build
```

Gradle 会将构建出的 jar 写入：

```text
build/libs/
```

## 许可证

本项目使用 CC0-1.0 许可证。
