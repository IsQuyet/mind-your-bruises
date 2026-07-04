# Mind Your Bruises

[English](README.md) | [简体中文](README.zh-CN.md)

Mind Your Bruises（注意你的淤青）是一个 Minecraft 1.21.11 Fabric 客户端模组，会根据伤害类型修改实体模型受击时的原版受伤覆盖颜色。

模组保留原版红色作为兜底颜色，并为火焰、冰冻、毒性、魔法、爆炸、水和虚空等大类伤害使用不同的受击覆盖颜色。

## 功能

- 在客户端重新着色原版实体受击覆盖效果。
- 为火焰、冰冻、毒性、魔法、爆炸、水和虚空等大类伤害使用不同颜色。
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
  "toxicColor": "#50dc3c",
  "fallbackColor": "#ff0000",
  "arcaneColor": "#b950ff",
  "blastColor": "#ffd540",
  "waterColor": "#3c78ff",
  "voidColor": "#2d005a",
  "damageTypeOverrides": {
    "minecraft:lava": "fire",
    "minecraft:freeze": "frost"
  }
}
```

手动编辑配置文件后，请重启游戏。

配置项说明：

- `enabled`：是否启用重新着色后的受击覆盖。设置为 `false` 时会回到原版红色。
- `fireColor`、`frostColor`、`toxicColor`、`fallbackColor`、`arcaneColor`、`blastColor`、`waterColor` 和 `voidColor`：颜色配置，使用 `#rrggbb` 格式。漏写 `#` 也可以，模组保存配置时会自动补齐并规范化。
- `damageTypeOverrides`：把某个伤害类型 id 映射到一个颜色组。可用颜色组为 `fire`、`frost`、`toxic`、`fallback`、`arcane`、`blast`、`water` 和 `void`。

例如，让某个自定义酸液伤害使用毒性颜色：

```json
"damageTypeOverrides": {
  "examplemod:acid": "toxic"
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
