package adris.rinforzando.util.serialization;

public interface IFailableConfigFile {
    void onFailLoad();

    boolean failedToLoad();
}
