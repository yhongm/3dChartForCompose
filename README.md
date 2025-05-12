# compose实现的3d效果的柱状图以及饼状图组件

## 预览效果

![compose 预览效果](previewComposeChart.png)

## 使用方法

1 .柱状图

```kotlin
  ThreeDBarChart(
    modifier = Modifier.width(400.dp).height(400.dp),
    bars = bars,

    maxHeightRatio = 1f,
)

```

2.饼状图

```kotlin
  ThreeDPieChart(
    modifier = Modifier.width(400.dp).height(400.dp),
    pies = bars,
)
```