# Viewsysystem
自定义View ，及View事件体系原理

事件的传递规则:
     
     所谓点击事件的分发，其实就是对MotionEvent事件的分发过程，即当一个MotionEvent产生以后，系统需要把这个事件传递给一个具体的view，而这个传递过程就是分发过程。
    
    点击事件的分发过程由三个很重要的方法来共同完成：dispatchTouchEvent、onInterceptTouchEvent和onTouchEvent;
    
   dispatchTouchEvent（MotionEvent ev）：
      用来进行事件的分发。如果事件能够传递给当前的view，那么此方法一定会被调用，返回结构受当前view的onTouchEvent和下级view的dispatchTouchEvent方法的影响，表示是否消耗当前事件；

  onIntercetTouchEvent(MotionEvent ev):
       用来判断是否拦截某个事件，如果当前view拦截了某个事件，那么在同一个事件序列当中，此方法不会被再次调用，返回结果表示是否拦截当前事件；

  onTouchEvent(MotionEvent ev):
      在dispatchTouchEvent 方法中调用，用来处理点击事件，返回结果表示是否消耗当前事件，如果不消耗，则在同一事件序列中，当前view无法再次接受到事件；


  三者关系可用如下伪代码表示:
  

public boolean dispatchTouchEvent(MotionEvent event){
   boolean consume = false;
   if(intercetTouchEvent(event)) {
        consume = onTouchEvent(event);  
   } else {
        consume = child.dispatchTouchEvent(event);
   }
   return consume; 
}


大致的了解一下事件的传递规则：
对于一个根ViewGroup，点击事件产生后，首先会传递给它，这时dispatchTouchEvent被调用，如果这个ViewGroup 的onIntercetTouchEvent返回true表示它要拦截当前事件，接着事件会交给ViewGroup处理，即它的onTouchEvent方法就会调用；如果这个ViewGroup的onIntercetTouchEvent返回false表示它不拦截当前事件，这时事件会继续传递给它的子元素，接着子元素的dispatchTouchEvent被调用，如此反复直到事件被最终处理。

