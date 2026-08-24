package com.mobilprogramlar.FizikFormullerim;

public class Uygulama_MyItem {
    public static final int TYPE_APP_NAME = 0;
    public static final int TYPE_APP_IMAGE = 1;
    public static final int TYPE_IMAGE_DESC = 2;
    public static final int TYPE_TWO_IMAGES_DESC = 3;
    public static final int TYPE_FULL_DESC = 4;
    public static final int TYPE_IMAGE_RIGHT_DESC = 5;

    private final String appName;
    private final int appImage;
    private final int imageRes;
    private final String description;
    private final int topImageRes;
    private final String topDescription;
    private final int bottomImageRes;
    private final String bottomDescription;
    private final String fullDescription;
    private final String link;
    private final int type;

    // Constructor for app name
    public Uygulama_MyItem(String appName, int type) {
        this(appName, 0, 0, null, 0, null, 0, null, null, null, type);
    }

    // Constructor for app image
    public Uygulama_MyItem(int appImage, int type) {
        this(null, appImage, 0, null, 0, null, 0, null, null, null, type);
    }

    // Constructor for image and description
    public Uygulama_MyItem(int imageRes, String description, int type) {
        this(null, 0, imageRes, description, 0, null, 0, null, null, null, type);
    }

    // Constructor for two images and descriptions
    public Uygulama_MyItem(int topImageRes, String topDescription, int bottomImageRes, String bottomDescription, int type) {
        this(null, 0, 0, null, topImageRes, topDescription, bottomImageRes, bottomDescription, null, null, type);
    }

    // Constructor for full description
    public Uygulama_MyItem(String fullDescription, int type, boolean isFullDesc) {
        this(null, 0, 0, null, 0, null, 0, null, fullDescription, null, type);
    }

    // Constructor for app name, description, image, and link
    public Uygulama_MyItem(String appName, String description, int imageRes, String link, int type) {
        this(appName, 0, imageRes, description, 0, null, 0, null, null, link, type);
    }

    // General constructor to initialize all fields
    private Uygulama_MyItem(String appName, int appImage, int imageRes, String description, int topImageRes, String topDescription, int bottomImageRes, String bottomDescription, String fullDescription, String link, int type) {
        this.appName = appName;
        this.appImage = appImage;
        this.imageRes = imageRes;
        this.description = description;
        this.topImageRes = topImageRes;
        this.topDescription = topDescription;
        this.bottomImageRes = bottomImageRes;
        this.bottomDescription = bottomDescription;
        this.fullDescription = fullDescription;
        this.link = link;
        this.type = type;
    }

    // Getter methods
    public String getAppName() {
        return appName;
    }

    public int getAppImage() {
        return appImage;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getDescription() {
        return description;
    }

    public int getTopImageRes() {
        return topImageRes;
    }

    public String getTopDescription() {
        return topDescription;
    }

    public int getBottomImageRes() {
        return bottomImageRes;
    }

    public String getBottomDescription() {
        return bottomDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public int getType() {
        return type;
    }

    public String getLink() {
        return link;
    }
}
