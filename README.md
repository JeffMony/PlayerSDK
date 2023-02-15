# PlayerSDK
提供一个高效的播放器库，播放相关的优化和功能都会在这个项目中完成

#### 目前支持的功能
> * 支持ijkplayer 和 exoplayer 两种播放器
> * 支持进度条和seek功能
> * 支持二维码扫描网址功能
> * 支持循环播放的功能
> * 支持倍速的功能
> * 支持针对HLS Master视频的切换清晰度功能
> * 添加Player实例监控方法
> * 支持GLSurfaceView渲染视频
> * 播放时调整声音大小
> * 播放视频加水印

#### 播放器渲染SDK
- 可以支持MediaPlayer、ijkplayer、exoplayer接入外部的渲染工具，使用opengl渲染
- 可以支持SurfaceView和TextureView
- 可以添加贴纸
- 可以调整清晰度
- 可以调整亮度、对比度、色温、饱和度、颗粒度、锐度
- 可以截图
- 可以调整黑白滤镜
- 可以添加滤镜

![](./files/video-render.jpeg)

#### 版本更新日志
##### 2.0.0
```
repositories {
    maven { url 'https://jitpack.io' }
}

implementation 'com.github.JeffMony:PlayerSDK:2.0.0'
```

#### 播放时设置声音音量
这里的调整音量不是让用户操作按键，而是在播放过程中调整声音的振幅。
```
//将声音的振幅调整为原来的3倍
mPlayer.setSonicVolume(3f)
```
调整声音振幅使用Sonic库来实现。


#### Player实例监控
```
PlayerConfig config = new PlayerManager.Builder().setLimitCount(6).buildConfig();
PlayerManager.getInstance().initConfig(config);
PlayerManager.getInstance().addGlobalPlayerInstanceListener(mListener);

private IPlayerInstanceListener mListener = new IPlayerInstanceListener() {
    @Override
    public void onPlayerCount(int count) {
        LogUtils.e("onPlayerCount count=" + count);
    }

    @Override
    public void onExceedLimit() {
        LogUtils.e("onExceedLimit report info");
    }
};
```
设置player实例限制是6个，如果超过6个，那么可以上报信息

#### 切换清晰度的接入
```
VideoInfoParserManager.getInstance().parseVideoInfo(mUrl, mVideoInfoCallback);

public interface IVideoInfoCallback {
  void onVideoType(String contentType, String name);
  void onMutipleVideo(List<M3U8Seg> urlList);
  void onFailed(Exception e);
}
```
在onMutipleVideo(List<M3U8Seg> urlList)回调函数中会出现呈现的几个清晰度的片源

#### PlayerSDK接入文档
```
CommonPlayer mPlayer = new CommonPlayer(Context, PlayerType);
mPlayer.setLooping(mIsLooping);
mPlayer.setSurface(mSurface);
mPlayer.setOnPreparedListener(mPrepareListener);
mPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
mPlayer.prepareAsync();
```

设置接口层是IPlayer.java

#### demo示意图
![](./files/test1.jpg)![](files/test2.jpg)![](files/test3.jpg)

#### 播放视频加水印
![](./files/test4.jpg)

欢迎关注我的公众号JeffMony，我会持续为你带来音视频---算法---Android---python 方面的知识分享<br>
![](./files/JeffMony.jpg)
