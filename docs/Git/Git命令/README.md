# Git常用命令

## 系统级命令

```shell script
# 查看Git版本
git --version

# 设置签名：用户名和邮箱
git config --global user.name "胡恩会"
git config --global user.email "717253212@qq.com"
```

## 常用命令

> HEAD 可以看作当前版本索引

```shell script
# 初始化本地仓库
git init

# 添加
# 将文件【a.txt】添加至暂存区
git add a.txt
# 添加全部
git add .

# 提交
# 将文件【a.txt】提交至本地仓库
git commit -m "本次提交注释-本次提交a.txt" a.txt
# 提交全部


# 查看当前工作状态s
git status

# 查看日志-按时间倒序【如果历史日志太多会分页。下一页用【空格】，上一页用【b】.用【q】退出】
# 方式1.【展示内容：索引、提交人、提交时间、注释】展示4行
git log
# 方式2.【展示内容：索引、注释】展示1行
git log --pretty=oneline
# 方式3.【展示内容：索引(截取8位)、注释】展示1行
git log --oneline
# 方式4.【展示内容：索引(截取8位)、HEAD@{数字}、注释】展示1行
git reflog

# 前进或回退版本【基本只使用hard参数】
# hard参数：本地仓库、暂存区、工作区内容同步回退
git reset --hard 【索引】
# mixed参数：本地仓库、暂存区内容同步回退，工作区不变【不常用】
git reset --mixed 【索引】
# soft参数：本地仓库内容回退，暂存区、工作区不变【不常用】
git reset --soft 【索引】

# 删除
# 删除文件【a.text】，删除后add、commit
rm a.text
# 恢复删除的文件，本质就是回退到删除前的版本
git reset --hard 【要回退的索引】

# 文件对比
# 对比工作区和暂存区的文件,不加文件名称则对比全部文件
git diff
git diff 【文件】
# 对比暂存区和本地仓库的文件（HEAD可以看作当前版本索引）
git diff 【历史版本索引或HEAD】 【文件】

```

## 分支相关命令

```shell script
# 查看分支
git branch -v

# 创建分支
git branch 【新分支名称】

# 切换分支
git checkout 【分支名称】

# 合并分支：将【分支1】合并到【master】。在【master】分支中操作
git merge 【分支1】



```
