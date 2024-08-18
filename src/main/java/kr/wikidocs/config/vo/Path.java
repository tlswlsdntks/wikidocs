package kr.wikidocs.config.vo;

public class Path {
    private final String basePath;
    private final String cssPath;
    private final String imgPath;
    private final String jsPath;
    private final String scssPath;
    private final String venderPath;

    public Path(String basePath) {
        this.basePath = basePath;
        this.cssPath = basePath + "/css";
        this.imgPath = basePath + "/img";
        this.jsPath = basePath + "/js";
        this.scssPath = basePath + "/scss";
        this.venderPath = basePath + "/vender";
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

    public String getScssPath() {
        return scssPath;
    }

    public String getVenderPath() {
        return venderPath;
    }

    @Override
    public String toString() {
        return "Path{" +
                "basePath='" + basePath + '\'' +
                ", cssPath='" + cssPath + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", jsPath='" + jsPath + '\'' +
                ", scssPath='" + scssPath + '\'' +
                ", venderPath='" + venderPath + '\'' +
                '}';
    }
}
