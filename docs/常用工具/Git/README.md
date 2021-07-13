# Git版本控制工具

## 系统及配置命令

1.查看Git版本
```git
git --version
```
2.设置签名：用户名
```git 
git config --global user.name "胡恩会"
```
3.设置签名：邮箱
```git 
git config --global user.email "enhuihu@qq.com"
```
## 本地仓库常用命令

> HEAD 可以看作当前版本索引

1.初始化本地仓库
```git 
git init
```

2.将文件添加至暂存区
```git 
# 将文件【a.txt】添加至暂存区
git add a.txt

# 添加全部
git add .
```

3.将暂存区文件提交至本地仓库
```git 
# 将文件【a.txt】提交至本地仓库
git commit -m "本次提交注释-本次提交a.txt" a.txt

# 提交全部
git commit -m "本次提交注释-提交全部"
```

4.查看当前工作状态
```git 
git status
```

5.查看日志-按时间倒序（如果历史日志太多会分页。下一页用【空格】，上一页用【b】.用【q】退出）
```git 
# 方式1.【展示内容：索引、提交人、提交时间、注释】展示4行
git log

# 方式2.【展示内容：索引、注释】展示1行
git log --pretty=oneline

# 方式3.【展示内容：索引(截取8位)、注释】展示1行
git log --oneline

# 方式4.【展示内容：索引(截取8位)、HEAD@{数字}、注释】展示1行
git reflog
```

6.前进或回退版本【基本只使用hard参数】
```git 
# hard参数：本地仓库、暂存区、工作区内容同步回退
git reset --hard 【索引】

# mixed参数：本地仓库、暂存区内容同步回退，工作区不变【不常用】
git reset --mixed 【索引】

# soft参数：本地仓库内容回退，暂存区、工作区不变【不常用】
git reset --soft 【索引】
```

7.删除
```git 
# 删除文件【a.text】，删除后add、commit
rm a.text

# 恢复删除的文件，本质就是回退到删除前的版本
git reset --hard 【要回退的索引】
```

8.文件对比
```git 
# 对比工作区和暂存区的文件,不加文件名称则对比全部文件
git diff
git diff 【文件】

# 对比暂存区和本地仓库的文件（HEAD可以看作当前版本索引）
git diff 【历史版本索引或HEAD】 【文件】
```


## 分支相关命令
1.查看分支
```git
git branch -v
```

2.创建分支
```git
git branch 【新分支名称】
```

3.切换分支
```git 
git checkout 【分支名称】
```
4.合并分支：将【分支1】合并到【master】。在【master】分支中操作
```git
git merge 【分支1】
```

## 远程仓库相关命令
1.查看远程仓库
```git
git remote -v
```
2.关联远程仓库，并起别名【origin】（起别名的意义：远程库地址太长）
```git
git remote add origin 【远程仓库地址-使用https形式】
```
3.推送
```git
# git push 【远程仓库别名】 【分支】
git push origin master

# 简单写法
git push
```
4.克隆
```git
git clone 【远程仓库下载地址】
```
5.拉取
```git
# 拉取(pull) git pull 【远程仓库别名】 【分支】
git pull origin master

# pull相当于 fetch(抓取) + merge(合并)
# 抓取(fetch) git fetch【远程仓库别名】 【分支】
# 抓取操作只会将远程仓库中的内容下载到本地origin/master分支，不会更新工作区内容
git fetch origin master
# 合并(merge) git merge 【远程仓库】
# 将origin/master的内容合并到当前分支
git merge origin/master
```