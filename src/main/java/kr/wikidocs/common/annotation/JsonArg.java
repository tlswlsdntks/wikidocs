package kr.wikidocs.common.annotation;

public @interface JsonArg {

	/**
	 * @return string
	 */
	public String value() default "";
}