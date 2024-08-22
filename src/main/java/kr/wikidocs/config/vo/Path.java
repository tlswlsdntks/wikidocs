package kr.wikidocs.config.vo;

public class Path {
    private final String basePath;
    private final String cssPath;
    private final String imgPath;
    private final String jsPath;
    private final String pluginPath;
    private final String scssPath;
    private final String vendorPath;

    public Path(String basePath) {
        this.basePath = basePath;
        this.cssPath = basePath + "/resources/css";
        this.imgPath = basePath + "/resources/img";
        this.jsPath = basePath + "/resources/js";
        this.pluginPath = basePath + "/resources/plugin";
        this.scssPath = basePath + "/resources/scss";
        this.vendorPath = basePath + "/resources/vendor";
    }

    public String getBasePath() {
        return basePath;
    }

    public String getCssPath() {
        return cssPath;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getJsPath() {
        return jsPath;
    }

    public String getPluginPath() {
        return pluginPath;
    }

    public String getScssPath() {
        return scssPath;
    }

    public String getVendorPath() {
        return vendorPath;
    }

}
