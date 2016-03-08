# CoolWeather(分支Another)
查看全国省市县的天气，可手动更新天气和后台更新天气

## 练手的一个小项目，可以查询全国各省市的未来五天的天气情况，可以根据时间来切换日间和黄昏的背景照片。

### 主界面
> 主界面使用了design包的CoordinatorLayout加上AppBarLayout来包裹天气信息内容，右下角加了浮动按钮FloatingActionButton。
> 除此之外，还实现了左侧侧滑菜单，DrawerLayout。侧滑菜单内使用的是NavigationView导航组件。

### 选择城市界面
> 选择城市界面，省级城市使用的是ListView，然后市级以及县级使用悬浮穿PopupWindow包裹ExpandableListView折叠列表实现的。

### 显示天气信息内容
> 使用RecycleView的横向LinearLayoutManager.HORIZONTAL来实现的。
