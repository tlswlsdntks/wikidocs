package kr.wikidocs.config.resolver;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.util.UriUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomPathResourceResolver extends PathResourceResolver implements ResourceResolver {

	@Override
	protected Resource getResource(String resourcePath, Resource location) throws IOException {

		// fixes problems with special chars in url
        resourcePath = UriUtils.decode(resourcePath, "UTF-8");
        resourcePath = UriUtils.decode(resourcePath, "UTF-8");

		// fixes problems with whitespaces in url
		//resourcePath = resourcePath.replaceAll(" ", "%20");
		//resourcePath = resourcePath.replaceAll("%20", " ");

        log.debug("resourcePath: {}", resourcePath);
		return super.getResource(resourcePath, location);
	}
}