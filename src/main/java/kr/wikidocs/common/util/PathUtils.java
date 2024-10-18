package kr.wikidocs.common.util;

import jakarta.servlet.http.HttpServletRequest;
import kr.wikidocs.config.vo.Path;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

/**
 * 공통 경로 추가
 */
public class PathUtils {
    public static void addCommonPath(HttpServletRequest req, Object obj) {
        Path path = new Path(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort());
        if (obj instanceof ModelAndView modelAndView) {
            modelAndView.addObject("basePath", path.getBasePath());
            modelAndView.addObject("cssPath", path.getCssPath());
            modelAndView.addObject("imgPath", path.getImgPath());
            modelAndView.addObject("jsPath", path.getJsPath());
            modelAndView.addObject("pluginPath", path.getPluginPath());
            modelAndView.addObject("scssPath", path.getScssPath());
            modelAndView.addObject("vendorPath", path.getVendorPath());
        } else if (obj instanceof Model model) {
            model.addAttribute("basePath", path.getBasePath());
            model.addAttribute("cssPath", path.getCssPath());
            model.addAttribute("imgPath", path.getImgPath());
            model.addAttribute("jsPath", path.getJsPath());
            model.addAttribute("pluginPath", path.getPluginPath());
            model.addAttribute("scssPath", path.getScssPath());
            model.addAttribute("vendorPath", path.getVendorPath());
        }
    }
}
