package fun.enhui.design.adapter.springmvc;

///定义一个Adapter接口 
public interface HandlerAdapter {
	public boolean supports(Object handler);

	public void handle(Object handler);
}

// 多种适配器类

class SimpleHandlerAdapter implements HandlerAdapter {

	@Override
	public void handle(Object handler) {
		((SimpleController) handler).doSimplerHandler();
	}

	@Override
	public boolean supports(Object handler) {
		return (handler instanceof SimpleController);
	}

}

class HttpHandlerAdapter implements HandlerAdapter {

	@Override
	public void handle(Object handler) {
		((HttpController) handler).doHttpHandler();
	}

	@Override
	public boolean supports(Object handler) {
		return (handler instanceof HttpController);
	}

}

class AnnotationHandlerAdapter implements HandlerAdapter {

	@Override
	public void handle(Object handler) {
		((AnnotationController) handler).doAnnotationHandler();
	}

	@Override
	public boolean supports(Object handler) {

		return (handler instanceof AnnotationController);
	}

}