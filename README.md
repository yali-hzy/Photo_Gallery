# 快搜相册 📸  
智能图片管理和搜索应用，支持命名、搜索、图片管理等功能，为用户提供高效的相册体验。


## 功能简介 🎯

### 主要功能
- **导入图片**：将本地图片加入应用图库。
- **重命名图片**：为图片设置自定义名称。
- **删除图片**：移除不需要的图片。
- **查看图片大图**：点击图片放大查看。
- **智能分类命名**：基于图像分类模型自动为图片生成名称。
- **搜索功能**：通过搜索框快速定位所需图片。


## 使用技术 🛠️

- **编程语言**：Kotlin
- **UI 框架**：Jetpack Compose + Material 3
- **依赖库**：
  - [Coil](https://coil-kt.github.io/coil/)：高效的图片加载库
  - [Room](https://developer.android.com/jetpack/androidx/releases/room)：本地数据库管理
  - [Paging](https://developer.android.com/jetpack/androidx/releases/paging)：分页加载实现懒加载
  - [TensorFlow Lite](https://www.tensorflow.org/lite)：图片分类与模型推理
  - [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)：高效异步操作
  - ViewModel：跨组件生命周期管理数据

 
## 软件架构 🏗️

 - **设计模式**：采用 [MVVM](https://developer.android.com/topic/architecture) 模式，解耦视图、逻辑和数据层。
 - **UI 优化**：
      - Material 3 设计风格。
      - 点击非输入区域自动隐藏键盘，提升用户体验。
      - 支持分页懒加载图片，优化性能。
 - **图片命名**： 使用轻量级 [MobileNet V3](https://arxiv.org/abs/1905.02244) 模型对图片进行分类，通过分类结果智能命名。
