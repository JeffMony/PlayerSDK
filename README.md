# PlayerSDK
提供一个高效的播放器库，播放相关的优化和功能都会在这个项目中完成

#### 目前支持的功能
> * 支持ijkplayer 和 exoplayer 两种播放器
> * 支持进度条和seek功能
> * 支持二维码扫描网址功能
> * 支持循环播放的功能

#### 版本更新日志
##### 1.0.0
```
repositories {
    maven { url 'https://jitpack.io' }
}

implementation 'com.github.JeffMony:PlayerSDK:1.0.0'
```

#### PlayerSDK接入文档
```
CommonPlayer mPlayer = new CommonPlayer(Context, PlayerType);
mPlayer.setLooping(mIsLooping);
mPlayer.setSurface(mSurface);
mPlayer.setOnPreparedListener(mPrepareListener);
mPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
mPlayer.prepareAsync();
```

#### demo示意图
![](./files/test1.jpg)

欢迎关注我的公众号JeffMony，我会持续为你带来音视频---算法---Android---python 方面的知识分享
![](./files/JeffMony.jpg)
