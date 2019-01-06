# Release note for Mononov v0.4.5 #
I'm pleased to announce that I've released the version 0.4.5 of the following my tool to find slow memory leak of java applications - called Mononov.

[Tool to find slow memory leak of java applications](http://quitada.hatenablog.jp/entry/2013/02/08/012154)

You can download the jar file of version 0.4.5 from [here](http://code.google.com/p/mononov/downloads/detail?name=JavaObjDiff-v0.4.5.jar)([src](http://code.google.com/p/mononov/downloads/detail?name=JavaObjDiff-v0.4.5-src.zip))([Javadoc](http://hokkaidou.me/quitada/Mononov/Javadoc/045/)). You can just execute this as a Java appliction with adding this jar file to CLASSPATH. The usage is similar to UNIX diff command like the following.
```
java quitada.JavaObjectDiff <java histogram file before memory leak> <java histogram file after memory leak>
```

And also, this release is built as a runnable jar. So you can execute this tool with the following command line.
```
java -jar JavaObjDiff-v0.4.5.jar <java histogram file before memory leak> <java histogram file after memory leak>
```

I added new API with this release - unfortunately, I modified existing API without any notification. It provides factory class to create Mononov object returning a result as a arry of ObjectInfo. The usage is like the following and please check Javadoc for details.
```
import quitada.Mononov;
import quitada.MononovFactory;
import quitada.ObjectInfo;
    :
Mononov mnv = new MononovFactory()
    .setSortType("instance")
    .setSortOrder("asc")
    .setPathBefore("before.txt")
    .setPathAfter("after.txt")
    .create();

ObjectInfo[] oi = mnv.runMerge();
```

You can see the sample source code to use new API [here](http://code.google.com/p/mononov/source/browse/branches/mononov_v0.4.5/testSrc/quitada/test/MononovFactoryTest.java).

To review release notes for older version, please visit the following web sites. <br>
<a href='http://quitada.hatenablog.jp/entry/2013/02/08/012154'>version 0.1</a> <br>
<a href='http://quitada.hatenablog.jp/entry/2013/05/05/152010'>version 0.2</a> <br>
<a href='http://quitada.hatenablog.jp/entry/2013/05/09/224443'>version 0.3</a> <br>
<a href='http://quitada.hatenablog.jp/entry/2013/05/14/234155'>version 0.3.5</a> <br>
<a href='http://quitada.hatenablog.jp/entry/2013/05/16/005957'>version 0.4</a>

