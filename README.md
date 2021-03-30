[![](https://jitpack.io/v/GitSmark/MultiStatePage.svg)](https://jitpack.io/#GitSmark/MultiStatePage)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# MultiStatePage
无缝为Activity、Fragment、任何View设置等待(loading)、失败重试(error)、无数据(empty)页面。

使用说明
-----
  
1. 首先，在项目的build.gradle文件中添加以下配置
  ```
  repositories {
      maven {
          url "https://jitpack.io"
      }
  }
  ```
2. 然后在app的build.gradle文件中添加以下依赖
  ```
  implementation 'com.github.GitSmark:MultiStatePage:1.2.0'
  ```

用法介绍
-----
  
1. 可以在Application中设置全局配置，如果不想每次回调都要手动进行的话，可以加入转换器，根据接口返回的响应状态码，自动适配对应的状态页，更多使用方法详见 `MultiStatePageConfig`
  ```java
  @Override
  public void onCreate() {
      super.onCreate();

      MultiStatePageManager.Config()
          //.addConverterFactory(this)
          .loadingView(R.layout.layout_loading)
          .emptyView(R.layout.layout_empty)
          .errorView(R.layout.layout_error)
          .showLoading(true); //设置初始状态是否显示加载状态（默认false）
  }
  ```
2. 在Activity中使用
  ```java
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(layoutResID);

      initView(); setListener(); //Must be before injection.

      //pageManager = MultiStatePageManager.inject(view);

      pageManager = MultiStatePageManager.inject(this);
  }
  ```
3. 在Fragment中使用
  ```java
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);
      View root = inflater.inflate(layoutResID, container, false);

      initView(); setListener(); //Must be before injection.

      //pageManager = MultiStatePageManager.inject(view);
      //return root;

      pageManager = MultiStatePageManager.inject(root);
      return pageManager;
  }
  ```

  注意：因为实现原理的问题，如果要注入RelativeLayout或ConstraintLayout的子View，若该子View与父Layout或其它子View存在约束关系，则需要在该View外层再包一层布局（或者直接使用xml注入，支持直接嵌套使用），避免注入后可能出现布局错乱等情况。

4. 常见用法，更多使用方法详见 `MultiStatePageManager`
  ```java
  //pageManager = MultiStatePageManager.inject(this, MultiStatePageConfig);
  //pageManager = MultiStatePageManager.inject(this, OnRetryEventListener);
  pageManager.setListener(new MultiStatePageManager.OnRetryEventListener() {
      @Override
      public void onRetryEvent() {
          //失败重试
      }
  });

  pageManager.show(200, "请求成功"); //状态转换器，自动适配

  pageManager.showView(view); //其它状态，任意View

  pageManager.loading(); //加载中...
  pageManager.success(); //加载成功
  pageManager.error(); //加载失败
  pageManager.empty(); //无数据
  
  ```
5. 弹出框，更多使用方法详见 `MultiStatePageDialog`
 - `showLoadingDialog(prompt);` //加载状态框，需要手动调用hideDialog()
 - `showSuccessDialog(prompt);` //成功提示框，默认自动关闭
 - `showFailDialog(prompt);` //失败提示框，默认自动关闭
 - `hideDialog();` //关闭提示框

 支持xml中引用，详见示例代码

Sample
------
![Get it on Google Play](http://www.android.com/images/brand/get_it_on_play_logo_small.png)

Contact
--------
  Have problem? Just [tweet me](https://twitter.com/huangxy) or [send me an email](mailto:huangxy8023@foxmail.com).

License
----------

    Copyright 2016 huangxy@GitSmark

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


